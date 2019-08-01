package mvc.service;


import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
















import helper.JacksonUtil;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;



@Service
public class ddServer {
	protected JdbcTemplate jdbcTemplate = null;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	private String findsql(String power_option){
		if(power_option!=null&&power_option.length()>0&&!power_option.equals("null")){		
			String[] a = power_option.split("\\|");
			String sql = "";
			if(a[0].equals("1")){
				String cx="select * from tb_channel where id="+a[1];
				List<Map<String, Object>> list = jdbcTemplate.queryForList(cx);
				String is = "";
				if(list.size()==1){
					String[] ids=(String.valueOf(list.get(0).get("CHANNEL_REVENUE"))).replace(";", ",").split(",");;
					for (int i = 0; i < ids.length; i=i+2) {
						is += "'"+ids[i]+"',";
					}	
				}
				sql += " and com.id in ("+is.substring(0, is.length()-1)+")";
			}else if(a[0].equals("2")){
				sql += " and com.id= '"+a[1]+"'";
			}else if(a[0].equals("3")){
				sql += " and cow.id= '"+a[1]+"'";
			}else if(a[0].equals("4")){
				sql += " and veh.id= '"+a[1]+"'";
			}
			return sql;
		}
		return "";
	}
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	public List<Map<String, Object>> finddd(String postData,String power_option){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String province = String.valueOf(paramMap.get("province"));
		String city = String.valueOf(paramMap.get("city"));
		String company = String.valueOf(paramMap.get("company"));
		String choose = String.valueOf(paramMap.get("choose"));
		String choosed = String.valueOf(paramMap.get("choosed"));
//		String status = String.valueOf(paramMap.get("status"));
		String ddstatus = String.valueOf(paramMap.get("ddstatus"));
		String startTime = String.valueOf(paramMap.get("startTime"));
		String endTime = String.valueOf(paramMap.get("endTime"));
		String sql = "select ord.TOTAL_FEE,ord.STATUS,ord.COMMODITY_ID,ord.UPTIME,ord.COMMODITYSTATUS,cow.owner_name,com.province,com.city,com.company_name,veh.TYPE,veh.VEHICLE_NO,ter.TERMINAL_TYPE"
				+ " from TB_CAR_OWNER cow,tb_company com,tb_vehicle veh,TB_INSTALLATION ins,TB_TERMINAL ter,TB_ORDER ord "
				+ " where ord.MDT_NO=ter.TERMINAL_NUM and ter.ID =ins.TERMINAL_ID and veh.ID=ins.VEHICLE_ID and veh.OWNER_ID=cow.ID and cow.company_id=com.id ";
		if(province!=null&&province.length()>0&&!province.equals("null")&&!province.equals("省份")){
			sql += " and com.province= '"+province+"'";
		}
		if(city!=null&&city.length()>0&&!city.equals("null")&&!city.equals("地级市")){
			sql += " and com.city= '"+city+"'";
		}
		if(company!=null&&company.length()>0&&!company.equals("null")){
			sql += " and com.id= '"+company+"'";
		}
//		if(status!=null&&status.length()>0&&!status.equals("null")){
//			sql += " and ord.STATUS= '"+status+"'";
//		}
		if(ddstatus!=null&&ddstatus.length()>0&&!ddstatus.equals("null")){
			if(ddstatus.equals("2")){
				sql += " and ord.COMMODITYSTATUS= '"+ddstatus+"'";				
			}else{
				sql += " and (ord.COMMODITYSTATUS <> '2' or ord.COMMODITYSTATUS is null)";			
			}
			
		}
		sql += " and ord.STATUS= '1' and ord.UPTIME<sysdate - 10/(60*24) ";
		if(startTime!=null&&startTime.length()>0&&!startTime.equals("null")){
			sql += " and ord.UPTIME > to_date('"+startTime+"', 'yyyy-mm-dd hh24:mi:ss')";
		}
		if(endTime!=null&&endTime.length()>0&&!endTime.equals("null")){
			sql += " and ord.UPTIME <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss')";
		}
		if(choosed!=null&&choosed.length()>0&&!choosed.equals("null")){
			if(choose.equals("TB_CAR_OWNER")){
				sql += " and cow.id= '"+choosed+"'";
			}else if(choose.equals("TB_VEHICLE")){
				sql += " and veh.id= '"+choosed+"'";
			}else if(choose.equals("TB_TERMINAL")){
				sql += " and ter.id= '"+choosed+"'";
			}		
		}
		sql+=findsql(power_option);
		sql+=" order by ord.UPTIME desc";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
//		if(list.size()>0){
//			for(int i=0;i<list.size();i++){
//				if(!String.valueOf(list.get(i).get("STATUS")).equals("null")){
//					if((String.valueOf((list.get(i).get("STATUS")))).equals("0")){
//						list.get(i).put("RESTATUS", "支付失败");
//					}else if((String.valueOf((list.get(i).get("STATUS")))).equals("1")){
//						list.get(i).put("RESTATUS", "支付成功");
//					}else {
//						list.get(i).put("RESTATUS", "");
//					}
//				}
//			}
//		}
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				if( String.valueOf(list.get(i).get("COMMODITYSTATUS")).equals("2")){
					list.get(i).put("COMMODITYSTATUS", "正常订单");
				}else{
					list.get(i).put("COMMODITYSTATUS", "异常订单");
				}
			}	
		}
		return list;
	}

//	public String findddb(int pageIndex,int pageSize,String postData,String power_option){
//		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
//		String province = String.valueOf(paramMap.get("province"));
//		String city = String.valueOf(paramMap.get("city"));
//		String channel = String.valueOf(paramMap.get("channel"));
//		String company = String.valueOf(paramMap.get("company"));
//		String choose = String.valueOf(paramMap.get("choose"));
//		String choosed = String.valueOf(paramMap.get("choosed"));
////		String status = String.valueOf(paramMap.get("status"));
//		String startTime = String.valueOf(paramMap.get("startTime"));
//		String endTime = String.valueOf(paramMap.get("endTime"));
//		String sql = "select a.* from (select rownum as rn,ord.STATUS,ord.TOTAL_FEE,ord.COMMODITY_ID,ord.UPTIME,cow.owner_name,cha.channel_name,cha.province,cha.city,com.company_name,veh.TYPE,veh.VEHICLE_NO,ter.TERMINAL_TYPE"
//				+ " from TB_CAR_OWNER cow,tb_channel cha,tb_company com,tb_vehicle veh,TB_INSTALLATION ins,TB_TERMINAL ter,(select * from TB_ORDER order by UPTIME desc) ord "
//				+ " where ord.MDT_NO=ter.TERMINAL_NUM and ter.ID =ins.TERMINAL_ID and veh.ID=ins.VEHICLE_ID and veh.OWNER_ID=cow.ID and cow.company_id=com.id and com.channel_id=cha.id ";
//		if(province!=null&&province.length()>0&&!province.equals("null")&&!province.equals("省份")){
//			sql += " and cha.province= '"+province+"'";
//		}
//		if(city!=null&&city.length()>0&&!city.equals("null")&&!city.equals("地级市")){
//			sql += " and cha.city= '"+city+"'";
//		}
//		if(channel!=null&&channel.length()>0&&!channel.equals("null")){
//			sql += " and cha.id= '"+channel+"'";
//		}
//		if(company!=null&&company.length()>0&&!company.equals("null")){
//			sql += " and com.id= '"+company+"'";
//		}
////		if(status!=null&&status.length()>0&&!status.equals("null")){
////			sql += " and ord.STATUS= '"+status+"'";
////		}
//		sql += " and ord.STATUS= '1'";
//		if(startTime!=null&&startTime.length()>0&&!startTime.equals("null")){
//			sql += " and ord.UPTIME > to_date('"+startTime+"', 'yyyy-mm-dd hh24:mi:ss')";
//		}
//		if(endTime!=null&&endTime.length()>0&&!endTime.equals("null")){
//			sql += " and ord.UPTIME <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss')";
//		}
//		if(choosed!=null&&choosed.length()>0&&!choosed.equals("null")){
//			if(choose.equals("TB_CAR_OWNER")){
//				sql += " and cow.id= '"+choosed+"'";
//			}else if(choose.equals("TB_VEHICLE")){
//				sql += " and veh.id= '"+choosed+"'";
//			}else if(choose.equals("TB_TERMINAL")){
//				sql += " and ter.id= '"+choosed+"'";
//			}		
//		}
//		sql +=findsql(power_option);
//		sql +=" and rownum <= "+(pageIndex*pageSize)+") a where a.rn >"+((pageIndex-1)*pageSize)+" order by a.UPTIME desc";
//		System.out.println(sql);
//		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);	
//		return jacksonUtil.toJson(list);
//	}
	/*public String adddd(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String vehicle = String.valueOf(paramMap.get("vehicle"));
		String dd_order = String.valueOf(paramMap.get("dd_order"));
		String dd_time = String.valueOf(paramMap.get("dd_time"));
		String dd_money = String.valueOf(paramMap.get("dd_money"));
		int user_id = findMaxId("TB_ORDER_INFO","ID");
		int count = 0;
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//		String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
		String sql = "insert into TB_ORDER_INFO (VEHICLE_ID,ORDER_NO,ORDER_DATE,ORDER_MONEY,id) values "
			+ " ('"+vehicle+"','"+dd_order+"',to_date('"+dd_time+"','yyyy-mm-dd hh24:mi:ss'),'"+dd_money+"','"+user_id+"')";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			count = jdbcTemplate.update(sql);
		} catch(RuntimeException e) {
			map.put("info", "重复添加");
			return jacksonUtil.toJson(map);
		}
		if(count>0){
			map.put("info", "添加成功");
		}else{
			map.put("info", "添加失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String editcl(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ownerName = String.valueOf(paramMap.get("ownerName"));
		String type = String.valueOf(paramMap.get("type"));
		String vehicle = String.valueOf(paramMap.get("vehicle"));
		String id = String.valueOf(paramMap.get("id"));
		int count = 0;
		String sql = "update TB_VEHICLE set OWNER_ID='"+ownerName+"',type='"+type+
				"',VEHICLE_NO='"+vehicle+"' where ID='"+id+"'";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			count = jdbcTemplate.update(sql);
		} catch(RuntimeException e) {
			map.put("info", "重复添加");
			return jacksonUtil.toJson(map);
		}
		if(count>0){
			map.put("info", "修改成功");
		}else{
			map.put("info", "修改失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String delcl(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String id = String.valueOf(paramMap.get("id"));
		String[] ids = id.split(",");
		String is = "";
		int count = 0;
		for (int i = 0; i < ids.length; i++) {
			is += "'"+ids[i]+"',";
		}
		String sql ="delete from TB_VEHICLE where ID in ("+is.substring(0, is.length()-1)+")";
		count = jdbcTemplate.update(sql);
		System.out.println(sql);
		Map<String, String> map = new HashMap<String, String>();
		if(count>0){
			map.put("info", "删除成功");
		}else{
			map.put("info", "删除失败");
		}
		return jacksonUtil.toJson(map);
	}*/
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
