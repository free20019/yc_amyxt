package mvc.service;


import java.util.Date;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;









import helper.JacksonUtil;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;



@Service
public class zdServer {
	protected JdbcTemplate jdbcTemplate = null;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	public List<Map<String, Object>> findzd(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String info = String.valueOf(paramMap.get("info"));
		String status = String.valueOf(paramMap.get("status"));
		String sql = "select a.*,mdt.SEAT_NO,veh.vehicle_no from (select rownum rn ,ter.* from (select * from TB_TERMINAL order by CREATE_TIME desc) ter where 1=1 " ;
		if(info!=null&&info.length()>0&&!info.equals("null")){
			sql += " and ter.ID ='"+info+"'";
		}
		if(status!=null&&status.length()>0&&!status.equals("null")){
			if(status.equals("1")){				
				sql += " and ter.id in (select terminal_id from tb_installation)";
			}
			if(status.equals("0")){				
				sql += " and ter.id not in (select terminal_id from tb_installation)";
			}			
		}
		sql +=" ) a left join (select MDT_NO,max(SEAT_NO) SEAT_NO from TB_MDT_MANAGE group by MDT_NO) mdt on mdt.MDT_NO=a.TERMINAL_NUM"
				+ " left join (select ins.terminal_id,veh.vehicle_no from tb_installation ins,tb_vehicle veh where ins.vehicle_id=veh.id) veh on veh.terminal_id=a.id ";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}
	public String findzdb(int pageIndex,int pageSize,String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String info = String.valueOf(paramMap.get("info"));
		String status = String.valueOf(paramMap.get("status"));
		String sql = "select a.*,mdt.SEAT_NO,veh.vehicle_no from (select rownum rn ,ter.* from (select * from TB_TERMINAL order by CREATE_TIME desc) ter where 1=1 " ;
		if(info!=null&&info.length()>0&&!info.equals("null")){
			sql += " and ter.ID ='"+info+"'";
		}
		if(status!=null&&status.length()>0&&!status.equals("null")){
			if(status.equals("1")){				
				sql += " and ter.id in (select terminal_id from tb_installation)";
			}
			if(status.equals("0")){				
				sql += " and ter.id not in (select terminal_id from tb_installation)";
			}			
		}
		sql +=" and rownum <= "+(pageIndex*pageSize)+") a "
				+ " left join (select MDT_NO,max(SEAT_NO) SEAT_NO from TB_MDT_MANAGE group by MDT_NO) mdt on mdt.MDT_NO=a.TERMINAL_NUM"
				+ " left join (select ins.terminal_id,veh.vehicle_no from tb_installation ins,tb_vehicle veh where ins.vehicle_id=veh.id) veh on veh.terminal_id=a.id"
				+ " where a.rn >"+((pageIndex-1)*pageSize)+" order by CREATE_TIME desc";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);	
		return jacksonUtil.toJson(list);
	}
	public String addzd(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String model = String.valueOf(paramMap.get("model"));
		String type = String.valueOf(paramMap.get("type"));
		String number = String.valueOf(paramMap.get("number"));
		int seat = Integer.parseInt(String.valueOf(paramMap.get("seat")));
		int user_id = findMaxId("TB_TERMINAL","ID");
		int count = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
		String sql = "insert into TB_TERMINAL (TERMINAL_MODEL,TERMINAL_TYPE,TERMINAL_NUM,CREATE_TIME,id) values "
			+ " ('"+model+"','"+type+"','"+number+"',to_date('"+date+"','yyyy-mm-dd hh24:mi:ss'),'"+user_id+"')";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			count = jdbcTemplate.update(sql);
		} catch(RuntimeException e) {
			map.put("info", "重复添加");
			return jacksonUtil.toJson(map);
		}
		System.out.println(sql);
		if(count>0){
			map.put("info", "添加终端成功");
		}else{
			map.put("info", "添加终端失败");
		}
		String cx="select MDT_NO,max(SEAT_NO) from TB_MDT_MANAGE where MDT_NO ="+number+"group by MDT_NO";
		List<Map<String, Object>> cxjg = jdbcTemplate.queryForList(cx);	
		String str="";
		map.put("str", str);
		if(cxjg.size()>0){
			if(!String.valueOf(cxjg.get(0).get("max(SEAT_NO)")).equals("null")){
				int num=Integer.valueOf(cxjg.get(0).get("max(SEAT_NO)").toString()).intValue();	
				if(num>seat){
					for(int i=num;i>seat;i--){
						String del="delete from TB_MDT_MANAGE where SEAT_NO="+"'"+i+"'";
						int a=jdbcTemplate.update(del);
						if(a>0){
						}else{
							str +=i+",";
						}
						map.put("str", str);
					}
				}else{	
					for(int i=(num+1);i<(seat+1);i++){
						SimpleDateFormat dfSeat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
						String dateSeat = dfSeat.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
						int id= findMaxId("TB_MDT_MANAGE","ID");
						String insert="insert into TB_MDT_MANAGE (MDT_NO,SEAT_NO,UPDATETIME,id) values "
								+ " ('"+number+"','"+i+"',to_date('"+dateSeat+"','yyyy-mm-dd hh24:mi:ss'),'"+id+"')";	
						int a=jdbcTemplate.update(insert);
						if(a>0){
						}else{
							str +=i+",";
						}
						map.put("str", str);	
					}
				}
			}
		}else{
			for(int i=1;i<(seat+1);i++){
				SimpleDateFormat dfSeat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				String dateSeat = dfSeat.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
				int id= findMaxId("TB_MDT_MANAGE","ID");
				String insert="insert into TB_MDT_MANAGE (MDT_NO,SEAT_NO,UPDATETIME,id) values "
						+ " ('"+number+"','"+i+"',to_date('"+dateSeat+"','yyyy-mm-dd hh24:mi:ss'),'"+id+"')";	
				int a=jdbcTemplate.update(insert);
				if(a>0){
				}else{
					str +=i+",";
				}
				map.put("str", str);
			}
		}
		return jacksonUtil.toJson(map);
	}
	public String editzd(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String model = String.valueOf(paramMap.get("model"));
		String type = String.valueOf(paramMap.get("type"));
		String number = String.valueOf(paramMap.get("number"));
		int seat = Integer.parseInt(String.valueOf(paramMap.get("seat")));
		String zd_id = String.valueOf(paramMap.get("id"));
		int count = 0;
		String sql = "update TB_TERMINAL set TERMINAL_MODEL='"+model+"',TERMINAL_TYPE='"+type+
				"',TERMINAL_NUM='"+number+"' where ID='"+zd_id+"'";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			count = jdbcTemplate.update(sql);
		} catch(RuntimeException e) {
			map.put("info", "重复添加");
			return jacksonUtil.toJson(map);
		}
		if(count>0){
			map.put("info", "修改终端成功");
		}else{
			map.put("info", "修改终端失败");
		}
		String cx="select MDT_NO,max(SEAT_NO) from TB_MDT_MANAGE where MDT_NO ="+number+"group by MDT_NO";
		List<Map<String, Object>> cxjg = jdbcTemplate.queryForList(cx);	
		String str="";
		map.put("str", str);
		if(cxjg.size()>0){
			if(!String.valueOf(cxjg.get(0).get("max(SEAT_NO)")).equals("null")){
				int num=Integer.valueOf(cxjg.get(0).get("max(SEAT_NO)").toString()).intValue();	
				if(num>seat){
					for(int i=num;i>seat;i--){
						String del="delete from TB_MDT_MANAGE where SEAT_NO="+"'"+i+"'";
						int a=jdbcTemplate.update(del);
						if(a>0){
						}else{
							str +=i+",";
						}
						map.put("str", str);
					}
				}else{	
					for(int i=(num+1);i<(seat+1);i++){
						SimpleDateFormat dfSeat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
						String dateSeat = dfSeat.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
						int id= findMaxId("TB_MDT_MANAGE","ID");
						String insert="insert into TB_MDT_MANAGE (MDT_NO,SEAT_NO,UPDATETIME,id) values "
								+ " ('"+number+"','"+i+"',to_date('"+dateSeat+"','yyyy-mm-dd hh24:mi:ss'),'"+id+"')";	
						int a=jdbcTemplate.update(insert);
						if(a>0){
						}else{
							str +=i+",";
						}
						map.put("str", str);	
					}
				}
			}
		}else{
			for(int i=1;i<(seat+1);i++){
				SimpleDateFormat dfSeat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				String dateSeat = dfSeat.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
				int id= findMaxId("TB_MDT_MANAGE","ID");
				String insert="insert into TB_MDT_MANAGE (MDT_NO,SEAT_NO,UPDATETIME,id) values "
						+ " ('"+number+"','"+i+"',to_date('"+dateSeat+"','yyyy-mm-dd hh24:mi:ss'),'"+id+"')";	
				int a=jdbcTemplate.update(insert);
				if(a>0){
				}else{
					str +=i+",";
				}
				map.put("str", str);
			}
		}
		return jacksonUtil.toJson(map);
	}
	public String delzd(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String id = String.valueOf(paramMap.get("id"));
		String[] ids = id.split(",");
		String is = "";
		String num = "";
		int count = 0;
		for (int i = 0; i < ids.length; i++) {
			String cx="select ins.TERMINAL_ID,ter.TERMINAL_NUM from TB_INSTALLATION ins,TB_TERMINAL ter where ins.TERMINAL_ID=ter.id and ins.TERMINAL_ID='"+ids[i]+"'";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(cx);	
			if(list.size()>0){
				if(!String.valueOf(list.get(0).get("TERMINAL_NUM")).equals("null")){				
					num +=list.get(0).get("TERMINAL_NUM").toString()+",";
				}
			}else{				
				is += "'"+ids[i]+"',";
			}
		}
		Map<String, String> map = new HashMap<String, String>();
		if(!is.equals("")){			
			String sql ="delete from TB_TERMINAL where ID in ("+is.substring(0, is.length()-1)+")";
			count = jdbcTemplate.update(sql);
			System.out.println(sql);

			if(count>0){
				map.put("info", "删除成功");
			}else{
				map.put("info", "删除失败");
			}
		}
		if(!num.equals("")){
			map.put("info", "部分数据删除失败");
			map.put("num", num);
		}
		return jacksonUtil.toJson(map);
	}
	
	public String addAll(String postData){
		Map<String, String> map = new HashMap<String, String>();

		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String model = String.valueOf(paramMap.get("model"));
		String type = String.valueOf(paramMap.get("type"));
		String number1 = String.valueOf(paramMap.get("number"));
//		int number2 = java.lang.Integer.parseInt(number1.trim());
		BigInteger number2 = new BigInteger(number1); 
		int seat = Integer.parseInt(String.valueOf(paramMap.get("seat")));
		for(int j=1;j<601;j++){
			BigInteger bigj = new BigInteger(String.valueOf(j));
			String number =number2.add(bigj)+"";
			int user_id = findMaxId("TB_TERMINAL","ID");
			int count = 0;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
			String sql = "insert into TB_TERMINAL (TERMINAL_MODEL,TERMINAL_TYPE,TERMINAL_NUM,CREATE_TIME,id) values "
				+ " ('"+model+"','"+type+"','"+number+"',to_date('"+date+"','yyyy-mm-dd hh24:mi:ss'),'"+user_id+"')";
			count = jdbcTemplate.update(sql);
			System.out.println(sql);
			String cx="select MDT_NO,max(SEAT_NO) from TB_MDT_MANAGE where MDT_NO ='"+number+"' group by MDT_NO";
			List<Map<String, Object>> cxjg = jdbcTemplate.queryForList(cx);	
			String str="";
			if(cxjg.size()>0){
				if(!String.valueOf(cxjg.get(0).get("max(SEAT_NO)")).equals("null")){
					int num=Integer.valueOf(cxjg.get(0).get("max(SEAT_NO)").toString()).intValue();	
					if(num>seat){
						for(int i=num;i>seat;i--){
							String del="delete from TB_MDT_MANAGE where SEAT_NO="+"'"+i+"'";
							int a=jdbcTemplate.update(del);
							if(a>0){
							}else{
								str +=i+",";
							}
						}
					}else{	
						for(int i=(num+1);i<(seat+1);i++){
							SimpleDateFormat dfSeat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
							String dateSeat = dfSeat.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
							int id= findMaxId("TB_MDT_MANAGE","ID");
							String insert="insert into TB_MDT_MANAGE (MDT_NO,SEAT_NO,UPDATETIME,id) values "
									+ " ('"+number+"','"+i+"',to_date('"+dateSeat+"','yyyy-mm-dd hh24:mi:ss'),'"+id+"')";	
							int a=jdbcTemplate.update(insert);
							if(a>0){
							}else{
								str +=i+",";
							}
						}
					}
				}
			}else{
				for(int i=1;i<(seat+1);i++){
					SimpleDateFormat dfSeat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
					String dateSeat = dfSeat.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
					int id= findMaxId("TB_MDT_MANAGE","ID");
					String insert="insert into TB_MDT_MANAGE (MDT_NO,SEAT_NO,UPDATETIME,id) values "
							+ " ('"+number+"','"+i+"',to_date('"+dateSeat+"','yyyy-mm-dd hh24:mi:ss'),'"+id+"')";	
					int a=jdbcTemplate.update(insert);
					if(a>0){
					}else{
						str +=i+",";
					}
				}
			}
		}
		map.put("info", "添加完成");
		return jacksonUtil.toJson(map);
	}
	public int findMaxId(String table,String id){
		int id1 = 1;
		String sql = "select "+id+" from "+table+"  order by to_number("+id+") desc";
		System.out.println("findMaxId sql="+sql);
		List<Map<String, Object>> list = null;
		list = jdbcTemplate.queryForList(sql);
		if(list.size()>0){
			id1 = Integer.parseInt(list.get(0).get(id).toString())+1;
		}
		return id1;
	}

}
