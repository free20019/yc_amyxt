package mvc.service;


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import helper.JacksonUtil;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class jsyServer {
	protected JdbcTemplate jdbcTemplate = null;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	public List<Map<String, Object>> findjsy(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String province = String.valueOf(paramMap.get("province"));
		String city = String.valueOf(paramMap.get("city"));
		String channel = String.valueOf(paramMap.get("channel"));
		String company = String.valueOf(paramMap.get("company"));
		String choose = String.valueOf(paramMap.get("choose"));
		String choosed = String.valueOf(paramMap.get("choosed"));
		String sql = "select dri.*,cow.owner_name,veh.type,cha.channel_name,cha.province,cha.city,com.company_name from TB_CAR_OWNER cow,tb_channel cha,tb_company com,tb_vehicle veh,tb_driver dri "
				+ "where dri.vehicle_id=veh.id and veh.OWNER_ID=cow.ID and cow.company_id=com.id and com.channel_id=cha.id ";
		if(province!=null&&province.length()>0&&!province.equals("null")&&!province.equals("省份")){
			sql += " and cha.province= '"+province+"'";
		}
		if(city!=null&&city.length()>0&&!city.equals("null")&&!city.equals("地级市")){
			sql += " and cha.city= '"+city+"'";
		}
		if(channel!=null&&channel.length()>0&&!channel.equals("null")){
			sql += " and cha.id= '"+channel+"'";
		}
		if(company!=null&&company.length()>0&&!company.equals("null")){
			sql += " and com.id= '"+company+"'";
		}
		if(choosed!=null&&choosed.length()>0&&!choosed.equals("null")){
			if(choose.equals("TB_CAR_OWNER")){
				sql += " and cow.id= '"+choosed+"'";
			}else if(choose.equals("TB_VEHICLE")){
				sql += " and veh.id= '"+choosed+"'";
			}else if(choose.equals("TB_DRIVER")){
				sql += " and dri.id= '"+choosed+"'";
			}		
		}
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}
	public String findjsyb(int pageIndex,int pageSize,String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String province = String.valueOf(paramMap.get("province"));
		String city = String.valueOf(paramMap.get("city"));
		String channel = String.valueOf(paramMap.get("channel"));
		String company = String.valueOf(paramMap.get("company"));
		String choose = String.valueOf(paramMap.get("choose"));
		String choosed = String.valueOf(paramMap.get("choosed"));
		String sql = "select a.* from (select rownum rn ,dri.*,veh.type,cow.owner_name,cha.channel_name,cha.province,cha.city,com.company_name "
				+ "from TB_CAR_OWNER cow,tb_channel cha,tb_company com,tb_vehicle veh,(select * from tb_driver order by create_time desc) dri "
				+ "where dri.vehicle_id=veh.id and veh.OWNER_ID=cow.ID and cow.company_id=com.id and com.channel_id=cha.id and rownum <= "+(pageIndex*pageSize);
		if(province!=null&&province.length()>0&&!province.equals("null")&&!province.equals("省份")){
			sql += " and cha.province= '"+province+"'";
		}
		if(city!=null&&city.length()>0&&!city.equals("null")&&!city.equals("地级市")){
			sql += " and cha.city= '"+city+"'";
		}
		if(channel!=null&&channel.length()>0&&!channel.equals("null")){
			sql += " and cha.id= '"+channel+"'";
		}
		if(company!=null&&company.length()>0&&!company.equals("null")){
			sql += " and com.id= '"+company+"'";
		}
		if(choosed!=null&&choosed.length()>0&&!choosed.equals("null")){
			if(choose.equals("TB_CAR_OWNER")){
				sql += " and cow.id= '"+choosed+"'";
			}else if(choose.equals("TB_VEHICLE")){
				sql += " and veh.id= '"+choosed+"'";
			}else if(choose.equals("TB_DRIVER")){
				sql += " and dri.id= '"+choosed+"'";
			}		
		}
		sql +=" ) a where a.rn >"+((pageIndex-1)*pageSize);
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);	
		return jacksonUtil.toJson(list);
	}
	public String addjsy(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String vehicle = String.valueOf(paramMap.get("vehicle"));
		String vehicle_no = String.valueOf(paramMap.get("vehicle_no"));
		String name = String.valueOf(paramMap.get("name"));
		String idCard = String.valueOf(paramMap.get("idCard"));
		String phoneNum = String.valueOf(paramMap.get("phoneNum"));
		int count = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		String cx= "select * from tb_user where user_name='"+idCard+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(cx);
		if(list.size()>0){
			map.put("info", "存在用户");
			return jacksonUtil.toJson(map);
		}
		int id = findMaxId("TB_DRIVER","ID");
		int user_id = findMaxId("TB_USER","USER_ID");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳 	
		String sql = "insert into TB_DRIVER (VEHICLE_ID,VEHICLE_NO,DRIVER_NAME,DRIVER_IDCARD,PHONE_NUM,CREATE_TIME,id,user_id) values "
				+ " ('"+vehicle+"','"+vehicle_no+"','"+name+"','"+idCard+"','"+phoneNum+"',to_date('"+date+"','yyyy-mm-dd hh24:mi:ss'),'"+id+"','"+user_id+"')";
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
		System.out.println(sql);
		if(count>0){
			map.put("info", "添加成功");
			String insert="insert into TB_USER (user_name,user_pwd,real_name,note,user_id,POWER_OPTION,OPTION_NAME,SIGN) "
					+ " values('"+idCard+"','"+idCard+"','"+name+"',2,'"+user_id+"','4|"+vehicle+"','"+vehicle_no+"',1)";
			jdbcTemplate.update(insert);
			
		}else{
			map.put("info", "添加失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String editjsy(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String vehicle = String.valueOf(paramMap.get("vehicle"));
		String vehicle_no = String.valueOf(paramMap.get("vehicle_no"));
		String name = String.valueOf(paramMap.get("name"));
		String idCard = String.valueOf(paramMap.get("idCard"));
		String phoneNum = String.valueOf(paramMap.get("phoneNum"));
		String id = String.valueOf(paramMap.get("id"));
		String user_id = String.valueOf(paramMap.get("user_id"));
		int count = 0;
		String sql = "update TB_DRIVER set VEHICLE_ID='"+vehicle+"',VEHICLE_NO='"+vehicle_no+
				"',DRIVER_NAME='"+name+"',DRIVER_IDCARD='"+idCard+"',PHONE_NUM='"+phoneNum+"' where ID='"+id+"'";
		Map<String, Object> map = new HashMap<String, Object>();
		String cx= "select * from tb_user where user_name='"+idCard+"'";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(cx);
		if(list.size()>0){
			map.put("info", "存在用户");
			return jacksonUtil.toJson(map);
		}
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
			String update="update TB_USER set user_name='"+idCard+"',user_pwd='"+idCard+"',real_name='"+name+"',"
					+ " POWER_OPTION='4|"+vehicle+"',OPTION_NAME='"+vehicle_no+"' where user_id='"+user_id+"'";
			jdbcTemplate.update(update);
		}else{
			map.put("info", "修改失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String deljsy(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String id = String.valueOf(paramMap.get("id"));
		String[] ids = id.split(",");	
		String is = "";
		for (int i = 0; i < ids.length; i++) {
			is += "'"+ids[i]+"',";
		}
		String userid = String.valueOf(paramMap.get("userid"));
		String[] userids = userid.split(",");	
		String iss = "";
		for (int i = 0; i < userids.length; i++) {
			iss += "'"+userids[i]+"',";
		}
		Map<String, String> map = new HashMap<String, String>();
		int count=0;
		int count1=0;
		String sql ="delete from TB_DRIVER where ID in ("+is.substring(0, is.length()-1)+")";
		count = jdbcTemplate.update(sql);
		String delete ="delete from TB_USER where USER_ID in ("+iss.substring(0, iss.length()-1)+")";
		count1 = jdbcTemplate.update(delete);
		if(count>0){
			if(count1>0){
				map.put("info", "删除成功");
			}
		}else{
			map.put("info", "删除失败");
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
