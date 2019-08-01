package mvc.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;















import helper.JacksonUtil;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class czServer {
	protected JdbcTemplate jdbcTemplate = null;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	public List<Map<String, Object>> findcz(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String province = String.valueOf(paramMap.get("province"));
		String city = String.valueOf(paramMap.get("city"));
		String company = String.valueOf(paramMap.get("company"));
		String owner = String.valueOf(paramMap.get("owner"));
		String status = String.valueOf(paramMap.get("status"));
		String sql = "select cow.*,com.province,com.city,com.company_name from TB_CAR_OWNER cow,tb_company com "
				+ "where cow.company_id=com.id ";
		if(province!=null&&province.length()>0&&!province.equals("null")&&!province.equals("省份")){
			sql += " and com.province= '"+province+"'";
		}
		if(city!=null&&city.length()>0&&!city.equals("null")&&!city.equals("地级市")){
			sql += " and com.city= '"+city+"'";
		}
		if(company!=null&&company.length()>0&&!company.equals("null")){
			sql += " and com.id= '"+company+"'";
		}
		if(owner!=null&&owner.length()>0&&!owner.equals("null")){
			sql += " and cow.id= '"+owner+"'";
		}
		if(status!=null&&status.length()>0&&!status.equals("null")){
			if(status.equals("1")){				
				sql += " and cow.id in (select owner_id from tb_vehicle)";
			}
			if(status.equals("0")){				
				sql += " and cow.id not in (select owner_id from tb_vehicle)";
			}			
		}
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}
	public String findczb(int pageIndex,int pageSize,String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String province = String.valueOf(paramMap.get("province"));
		String city = String.valueOf(paramMap.get("city"));
		String company = String.valueOf(paramMap.get("company"));
		String owner = String.valueOf(paramMap.get("owner"));
		String status = String.valueOf(paramMap.get("status"));
		String sql = "select a.* from (select rownum rn ,cow.*,com.province,com.city,com.company_name"
				+ " from (select * from TB_CAR_OWNER  order by to_number(ID) desc) cow,tb_company com "
				+ "where cow.company_id=com.id and rownum <= "+(pageIndex*pageSize);
		if(province!=null&&province.length()>0&&!province.equals("null")&&!province.equals("省份")){
			sql += " and com.province= '"+province+"'";
		}
		if(city!=null&&city.length()>0&&!city.equals("null")&&!city.equals("地级市")){
			sql += " and com.city= '"+city+"'";
		}
		if(company!=null&&company.length()>0&&!company.equals("null")){
			sql += " and com.id= '"+company+"'";
		}
		if(owner!=null&&owner.length()>0&&!owner.equals("null")){
			sql += " and cow.id= '"+owner+"'";
		}
		if(status!=null&&status.length()>0&&!status.equals("null")){
			if(status.equals("1")){				
				sql += " and cow.id in (select owner_id from tb_vehicle)";
			}
			if(status.equals("0")){				
				sql += " and cow.id not in (select owner_id from tb_vehicle)";
			}			
		}
		sql+= ") a where a.rn >"+((pageIndex-1)*pageSize);
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);	
		return jacksonUtil.toJson(list);
	}
	public String addcz(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String companyid = String.valueOf(paramMap.get("companyid"));
		String username = String.valueOf(paramMap.get("username"));
		String phonenum = String.valueOf(paramMap.get("phonenum"));
		String idcard = String.valueOf(paramMap.get("idcard"));
		int count = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		int id = findMaxId("TB_CAR_OWNER","ID");
		int user_id = findMaxId("TB_USER","USER_ID");
		String sql = "insert into TB_CAR_OWNER (company_id,owner_name,phone_num,id_card,id,user_id) values "
				+ " ('"+companyid+"','"+username+"','"+phonenum+"','"+idcard+"','"+id+"','"+user_id+"')";
//		String cx= "select * from tb_user where user_name='"+idcard+"'";
//		List<Map<String,Object>> list=jdbcTemplate.queryForList(cx);
//		if(list.size()>0){
//			map.put("info", "存在用户");
//			return jacksonUtil.toJson(map);
//		}
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
//			String insert="insert into TB_USER (user_name,user_pwd,real_name,note,user_id,POWER_OPTION,OPTION_NAME,SIGN) "
//					+ " values('"+idcard+"','"+idcard+"','"+username+"',2,'"+user_id+"','3|"+id+"','"+username+"',1)";
//			jdbcTemplate.update(insert);
		}else{
			map.put("info", "添加失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String editowner(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String companyid = String.valueOf(paramMap.get("companyid"));
		String username = String.valueOf(paramMap.get("username"));
		String phonenum = String.valueOf(paramMap.get("phonenum"));
		String idcard = String.valueOf(paramMap.get("idcard"));
		String id = String.valueOf(paramMap.get("id"));
		String user_id = String.valueOf(paramMap.get("user_id"));
		int count = 0;
		Map<String, Object> map = new HashMap<String, Object>();	
		String sql = "update TB_CAR_OWNER set company_id='"+companyid+"',owner_name='"+username+
				"',phone_num='"+phonenum+"',id_card='"+idcard+"' where ID='"+id+"'";
//		String cx= "select * from tb_user where user_name='"+idcard+"' and user_id!='"+user_id+"'";
//		List<Map<String,Object>> list=jdbcTemplate.queryForList(cx);
//		if(list.size()>0){
//			map.put("info", "存在用户");
//			return jacksonUtil.toJson(map);
//		}
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
//			String update="update TB_USER set user_name='"+idcard+"',user_pwd='"+idcard+"',real_name='"+username+"',"
//					+ " POWER_OPTION='3|"+id+"',OPTION_NAME='"+username+"' where user_id='"+user_id+"'";
//			jdbcTemplate.update(update);
		}else{
			map.put("info", "修改失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String delcz(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String id = String.valueOf(paramMap.get("id"));
		String[] ids = id.split(",");
		String is = "";
		String num = "";
		int count = 0;
		for (int i = 0; i < ids.length; i++) {
			String cx="select veh.owner_id,cow.owner_name from TB_VEHICLE veh,TB_CAR_OWNER cow where veh.owner_id=cow.id and veh.owner_id='"+ids[i]+"'";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(cx);	
			if(list.size()>0){
				if(!String.valueOf(list.get(0).get("owner_name")).equals("null")){				
					num +=list.get(0).get("owner_name").toString()+",";
				}
			}else{				
				is += "'"+ids[i]+"',";
			}
		}
		String powerId="";
		for (int i = 0; i < ids.length; i++) {	
			powerId += "'3|"+ids[i]+"',";	
		}
		String user ="update tb_user set SIGN='1' where POWER_OPTION in ("+powerId.substring(0, powerId.length()-1)+")";
		jdbcTemplate.update(user);
		Map<String, String> map = new HashMap<String, String>();
		if(!is.equals("")){			
			String sql2="select user_id from TB_CAR_OWNER where ID in ("+is.substring(0, is.length()-1)+")";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql2);
			String iss="";
			String sql ="delete from TB_CAR_OWNER where ID in ("+is.substring(0, is.length()-1)+")";
			count = jdbcTemplate.update(sql);
			System.out.println(sql);
			for(int i=0;i<list.size();i++){
				iss += "'"+list.get(i).get("user_id")+"',";
			}
//			String sql3="delete from TB_USER where user_id in ("+iss.substring(0, iss.length()-1)+")";
//			int count1 = jdbcTemplate.update(sql3);
			if(count>0){
//				if(count1>0){
					map.put("info", "删除成功");
//				}
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
