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
public class qdServer {
	protected JdbcTemplate jdbcTemplate = null;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	public List<Map<String, Object>> findqd(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String province = String.valueOf(paramMap.get("province"));
		String city = String.valueOf(paramMap.get("city"));
		String channel = String.valueOf(paramMap.get("channel"));
		String sql = "select cha.* from tb_channel cha where 1=1" ;
		if(province!=null&&province.length()>0&&!province.equals("null")&&!province.equals("省份")){
			sql += " and cha.province= '"+province+"'";
		}
		if(city!=null&&city.length()>0&&!city.equals("null")&&!city.equals("地级市")){
			sql += " and cha.city= '"+city+"'";
		}
		if(channel!=null&&channel.length()>0&&!channel.equals("null")){
			sql += " and cha.id= '"+channel+"'";
		}
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}
	public String findqdb(int pageIndex,int pageSize,String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String province = String.valueOf(paramMap.get("province"));
		String city = String.valueOf(paramMap.get("city"));
		String channel = String.valueOf(paramMap.get("channel"));
		String sql = "select a.* from (select rownum rn ,cha.* from (select * from tb_channel order by add_time desc) cha where 1=1 and rownum <= "+(pageIndex*pageSize);
		if(province!=null&&province.length()>0&&!province.equals("null")&&!province.equals("省份")){
			sql += " and cha.province= '"+province+"'";
		}
		if(city!=null&&city.length()>0&&!city.equals("null")&&!city.equals("地级市")){
			sql += " and cha.city= '"+city+"'";
		}
		if(channel!=null&&channel.length()>0&&!channel.equals("null")){
			sql += " and cha.id= '"+channel+"'";
		}
		sql +=" ) a where a.rn >"+((pageIndex-1)*pageSize);
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);	
		return jacksonUtil.toJson(list);
	}
	public String addqd(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String province = String.valueOf(paramMap.get("province"));
		String city = String.valueOf(paramMap.get("city"));
		String channelName = String.valueOf(paramMap.get("channelName"));
		String person = String.valueOf(paramMap.get("person"));
		String telNum = String.valueOf(paramMap.get("telNum"));
		String phoneNum = String.valueOf(paramMap.get("phoneNum"));
		String revenue = String.valueOf(paramMap.get("revenue"));
		int user_id = findMaxId("TB_CHANNEL","ID");
		int count = 0;
		if(province.equals("省份")){
			province="";
		}
		if(city.equals("地级市")){
			city="";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
		String sql = "insert into tb_channel (PROVINCE,CITY,CHANNEL_NAME,USER_NAME,TELPHONE_NUM,PHONE_NUM,ADD_TIME,id) values "
			+ " ('"+province+"','"+city+"','"+channelName+"','"+person+"','"+telNum+"','"+phoneNum+"',to_date('"+date+"','yyyy-mm-dd hh24:mi:ss'),'"+user_id+"')";
		System.out.println(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			count = jdbcTemplate.update(sql);
		} catch(RuntimeException e) {
			map.put("info", "重复添加");
			return jacksonUtil.toJson(map);
		}
		if(count>0){
			map.put("info", "添加成功");
			//渠道提成率添加
			if(!revenue.equals("")){				
				String[] a=revenue.split(",");
				for(int i=0;i<a.length;i=i+2){
					String insert="insert into tb_channel_revenue (CHANNEL_ID,CHANNEL_NAME,COMPANY_ID,REVENUE) values ('"+user_id+"','"+channelName+"','"+a[i]+"','"+a[i+1]+"')";
					jdbcTemplate.update(insert);
				}
			}
		}else{
			map.put("info", "添加失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String editqd(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String province = String.valueOf(paramMap.get("province"));
		String city = String.valueOf(paramMap.get("city"));
		String channelName = String.valueOf(paramMap.get("channelName"));
		String person = String.valueOf(paramMap.get("person"));
		String telNum = String.valueOf(paramMap.get("telNum"));
		String phoneNum = String.valueOf(paramMap.get("phoneNum"));
		String revenue = String.valueOf(paramMap.get("revenue"));
		String id = String.valueOf(paramMap.get("id"));
		Map<String, Object> map = new HashMap<String, Object>();
		int count = 0;
		if(province.equals("省份")){
			province="";
		}
		if(city.equals("地级市")){
			city="";
		}
		String sql = "update tb_channel set PROVINCE='"+province+"',CITY='"+city+
				"',CHANNEL_NAME='"+channelName+"',USER_NAME='"+person+"',TELPHONE_NUM='"+telNum+"',PHONE_NUM='"+phoneNum+"' where ID='"+id+"'";
		System.out.println("sql="+sql);
		try {
			count = jdbcTemplate.update(sql);
		} catch(RuntimeException e) {
			map.put("info", "重复添加");
			return jacksonUtil.toJson(map);
		}
		if(count>0){
			map.put("info", "修改成功");
			//渠道提成率添加
			if(!revenue.equals("null")){				
				String delete="delete from tb_channel_revenue where CHANNEL_ID='"+id+"'";
				jdbcTemplate.update(delete);
				if(!revenue.equals("")){						
					String[] a=revenue.split(",");
					for(int i=0;i<a.length;i=i+2){
						String insert="insert into tb_channel_revenue (CHANNEL_ID,CHANNEL_NAME,COMPANY_ID,REVENUE) values ('"+id+"','"+channelName+"','"+a[i]+"','"+a[i+1]+"')";
						jdbcTemplate.update(insert);
					}
				}
			}
		}else{
			map.put("info", "修改失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String delqd(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String id = String.valueOf(paramMap.get("id"));
		Map<String, String> map = new HashMap<String, String>();
		String[] ids = id.split(",");
		String is = "";
		int count = 0;
		for (int i = 0; i < ids.length; i++) {
			is += "'"+ids[i]+"',";
		}		
		String sql ="delete from TB_CHANNEL where ID in ("+is.substring(0, is.length()-1)+")";
		count = jdbcTemplate.update(sql);
		System.out.println(sql);
		if(count>0){
			map.put("info", "删除成功");
			String delete="delete from tb_channel_revenue where CHANNEL_ID in ("+is.substring(0, is.length()-1)+")";
			jdbcTemplate.update(delete);
		}else{
			map.put("info", "删除失败");
		}
		return jacksonUtil.toJson(map);
	}
	public String findcheckcompany(String channel_id){		
		String sql ="";
		if(channel_id.equals("")){
			sql ="select * from tb_channel_revenue  ";			
		}else{
			sql ="select * from tb_channel_revenue ch,tb_company co where ch.COMPANY_ID=co.ID and ch.CHANNEL_ID='"+channel_id+"' order by co.COMPANY_NAME";
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return jacksonUtil.toJson(list);
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
