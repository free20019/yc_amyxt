package mvc.service;


import java.util.Date;
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
public class clServer {
	protected JdbcTemplate jdbcTemplate = null;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	public List<Map<String, Object>> findcl(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String province = String.valueOf(paramMap.get("province"));
		String city = String.valueOf(paramMap.get("city"));
		String company = String.valueOf(paramMap.get("company"));
		String choose = String.valueOf(paramMap.get("choose"));
		String choosed = String.valueOf(paramMap.get("choosed"));
		String sql = "select veh.*,cow.owner_name,com.province,com.city,com.company_name from TB_CAR_OWNER cow,tb_company com,tb_vehicle veh "
				+ "where veh.OWNER_ID=cow.ID and cow.company_id=com.id ";
		if(province!=null&&province.length()>0&&!province.equals("null")&&!province.equals("省份")){
			sql += " and com.province= '"+province+"'";
		}
		if(city!=null&&city.length()>0&&!city.equals("null")&&!city.equals("地级市")){
			sql += " and com.city= '"+city+"'";
		}
		if(company!=null&&company.length()>0&&!company.equals("null")){
			sql += " and com.id= '"+company+"'";
		}
		if(choosed!=null&&choosed.length()>0&&!choosed.equals("null")){
			if(choose.equals("TB_CAR_OWNER")){
				sql += " and cow.id= '"+choosed+"'";
			}else if(choose.equals("TB_VEHICLE")){
				sql += " and veh.id= '"+choosed+"'";
			}	
		}
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}
	public String findclb(int pageIndex,int pageSize,String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String province = String.valueOf(paramMap.get("province"));
		String city = String.valueOf(paramMap.get("city"));
		String company = String.valueOf(paramMap.get("company"));
		String choose = String.valueOf(paramMap.get("choose"));
		String choosed = String.valueOf(paramMap.get("choosed"));
		String sql = "select t.* from (select rownum rn ,a.* from (select veh.*,cow.owner_name,com.province,com.city,com.company_name "
				+ "from TB_CAR_OWNER cow,tb_company com,(select * from tb_vehicle  order by add_time desc) veh "
				+ "where  veh.OWNER_ID=cow.ID and cow.company_id=com.id ";
		if(province!=null&&province.length()>0&&!province.equals("null")&&!province.equals("省份")){
			sql += " and com.province= '"+province+"'";
		}
		if(city!=null&&city.length()>0&&!city.equals("null")&&!city.equals("地级市")){
			sql += " and com.city= '"+city+"'";
		}
		if(company!=null&&company.length()>0&&!company.equals("null")){
			sql += " and com.id= '"+company+"'";
		}
		if(choosed!=null&&choosed.length()>0&&!choosed.equals("null")){
			if(choose.equals("TB_CAR_OWNER")){
				sql += " and cow.id= '"+choosed+"'";
			}else if(choose.equals("TB_VEHICLE")){
				sql += " and veh.id= '"+choosed+"'";
			}	
		}
		sql +="  order by veh.add_time desc) a where rownum <= "+(pageIndex*pageSize)+ ")t where t.rn >"+((pageIndex-1)*pageSize);
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);	
		return jacksonUtil.toJson(list);
	}
	public String addcl(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String ownerName = String.valueOf(paramMap.get("ownerName"));
		String type = String.valueOf(paramMap.get("type"));
		String vehicle = String.valueOf(paramMap.get("vehicle"));
		int user_id = findMaxId("TB_VEHICLE","ID");
		int count = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "insert into TB_VEHICLE (OWNER_ID,type,VEHICLE_NO,ADD_TIME,id) values "
			+ " ('"+ownerName+"','"+type+"','"+vehicle+"',to_date('"+date+"','yyyy-mm-dd hh24:mi:ss'),'"+user_id+"')";
		try {
			count = jdbcTemplate.update(sql);
		} catch(RuntimeException e) {
			if(e.getMessage().contains("ORA-02291")){
				map.put("info", "不存在");
				return jacksonUtil.toJson(map);
			}
			if(e.getMessage().contains("ORA-00001")){
				map.put("info", "重复添加");
				return jacksonUtil.toJson(map);
			}
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
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "update TB_VEHICLE set OWNER_ID='"+ownerName+"',type='"+type+
				"',VEHICLE_NO='"+vehicle+"' where ID='"+id+"'";
		try {
			count = jdbcTemplate.update(sql);
		} catch(RuntimeException e) {
			if(e.getMessage().contains("ORA-02291")){
				map.put("info", "不存在");
				return jacksonUtil.toJson(map);
			}
			if(e.getMessage().contains("ORA-00001")){
				map.put("info", "重复添加");
				return jacksonUtil.toJson(map);
			}
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
		String num = "";
		int count = 0;
		for (int i = 0; i < ids.length; i++) {
			String cx="select ins.VEHICLE_ID,veh.VEHICLE_NO from TB_INSTALLATION ins,tb_vehicle veh where ins.VEHICLE_ID=veh.id and ins.VEHICLE_ID='"+ids[i]+"'";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(cx);	
			if(list.size()>0){
				if(!String.valueOf(list.get(0).get("VEHICLE_NO")).equals("null")){				
					num +=list.get(0).get("VEHICLE_NO").toString()+",";
				}
			}else{				
				is += "'"+ids[i]+"',";
			}
		}
		String powerId="";
		for (int i = 0; i < ids.length; i++) {	
			powerId += "'4|"+ids[i]+"',";	
		}
		String user ="update tb_user set SIGN='1' where POWER_OPTION in ("+powerId.substring(0, powerId.length()-1)+")";
		jdbcTemplate.update(user);
		Map<String, String> map = new HashMap<String, String>();
		if(!is.equals("")){			
			String sql ="delete from tb_vehicle where ID in ("+is.substring(0, is.length()-1)+")";
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
