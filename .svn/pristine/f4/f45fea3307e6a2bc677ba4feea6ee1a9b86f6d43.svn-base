package mvc.service;


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
public class userServer {
	protected JdbcTemplate jdbcTemplate = null;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	public String finduser(String info){
		String sql = "select * from TB_USER u,tb_user_power p where u.note=p.id ";	
		if(info!=null&&info.length()>0){
			sql += " and (user_name like '%"+info+"%' or user_pwd like '%"+info+"%' or"
					+ " real_name like '%"+info+"%' or p.power_name like '%"+info+"%')";
		}
		sql += " and u.sign is null order by to_number(user_id) desc";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return jacksonUtil.toJson(list);
	}
	public String adduser(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String user_name = String.valueOf(paramMap.get("user_name"));
		String user_pwd = String.valueOf(paramMap.get("user_pwd"));
		String real_name = String.valueOf(paramMap.get("real_name"));
		String note = String.valueOf(paramMap.get("note"));
		String choose = String.valueOf(paramMap.get("choose"));
		String choosed = String.valueOf(paramMap.get("choosed"));
		String option = String.valueOf(paramMap.get("option"));
		int user_id = findMaxId("TB_USER","user_id");
		String power_option="";
		if(choose!=null&&choose.length()>0&&!choose.equals("null")&&choosed!=null&&choosed.length()>0&&!choosed.equals("null")){
			power_option=choose+"|"+choosed;
		}
		int count = 0;
		String sql = "insert into TB_USER (user_name,user_pwd,real_name,note,user_id,POWER_OPTION,OPTION_NAME) values "
			+ " ('"+user_name+"','"+user_pwd+"','"+real_name+"','"+note+"','"+user_id+"','"+power_option+"','"+option+"')";
		Map<String, Object> map = new HashMap<String, Object>();
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
	public String edituser(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String user_name = String.valueOf(paramMap.get("user_name"));
		String user_pwd = String.valueOf(paramMap.get("user_pwd"));
		String real_name = String.valueOf(paramMap.get("real_name"));
		String note = String.valueOf(paramMap.get("note"));
		String choose = String.valueOf(paramMap.get("choose"));
		String choosed = String.valueOf(paramMap.get("choosed"));
		String option = String.valueOf(paramMap.get("option"));
		String user_id = String.valueOf(paramMap.get("user_id"));
		String power_option="";
		if(choose!=null&&choose.length()>0&&!choose.equals("null")&&choosed!=null&&choosed.length()>0&&!choosed.equals("null")){
			power_option=choose+"|"+choosed;
		}
		int count = 0;
		String sql = "update TB_USER set user_name='"+user_name+"',user_pwd='"+user_pwd+
				"',real_name='"+real_name+"',note='"+note+"',POWER_OPTION='"+power_option+"',OPTION_NAME='"+option+"' where user_id='"+user_id+"'";
		Map<String, Object> map = new HashMap<String, Object>();
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
	public String deluser(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String id = String.valueOf(paramMap.get("id"));
		String[] ids = id.split(",");
		String is = "";
		int count = 0;
		for (int i = 0; i < ids.length; i++) {
			is += "'"+ids[i]+"',";
		}
		String sql ="delete from TB_USER where user_id in ("+is.substring(0, is.length()-1)+")";
		count = jdbcTemplate.update(sql);
		System.out.println(sql);
		Map<String, String> map = new HashMap<String, String>();
		if(count>0){
			map.put("info", "删除成功");
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
	public String findpower(String info){
		List<Map<String, Object>> list = null;
		String sql = "select * from tb_user_power p where 1=1";
		if(info!=null&&info.length()>0){
			sql += " and power_name like '%"+info+"%'";
		}
		list = jdbcTemplate.queryForList(sql);
		System.out.println("权限搜索:sql="+sql);
		return jacksonUtil.toJson(list);
	}
	public String addpower(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String power_name = String.valueOf(paramMap.get("power_name"));
		String power_but = String.valueOf(paramMap.get("power_but"));
		String power_info = String.valueOf(paramMap.get("power_info"));
		String power = qxym(power_info);
		int id = findMaxId("tb_user_power","id");
		String sql = "insert into tb_user_power (id,power_name,power,power_but,power_info) values("
				+ "'"+id+"','"+power_name+"','"+power+"','"+power_but+"','"+power_info+"')";
//		System.out.println(sql);
		int count = 0;
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
	public String editpower(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String power_name = String.valueOf(paramMap.get("power_name"));
		String power_but = String.valueOf(paramMap.get("power_but"));
		String power_info = String.valueOf(paramMap.get("power_info"));
		String id = String.valueOf(paramMap.get("id"));
		String power = qxym(power_info);
		String sql = "update tb_user_power set power_name='"+power_name+"',power='"+power+"'"
				+ ",power_but='"+power_but+"',power_info='"+power_info+"' where  id='"+id+"'";
//		System.out.println(sql);
		int count =0;
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
	public String delpower(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String id = String.valueOf(paramMap.get("id"));
		String[] ids = id.split(",");
		String is = "";
		String num = "";
		int count = 0;
		for (int i = 0; i < ids.length; i++) {
			String cx="select p.id,p.power_name from tb_user u,tb_user_power p where u.note=p.id and u.note='"+ids[i]+"'";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(cx);	
			if(list.size()>0){
				if(!String.valueOf(list.get(0).get("power_name")).equals("null")){				
					num +=list.get(0).get("power_name").toString()+",";
				}
			}else{				
				is += "'"+ids[i]+"',";
			}
		}
		Map<String, String> map = new HashMap<String, String>();
		if(!is.equals("")){			
			String sql ="delete from tb_user_power where ID in ("+is.substring(0, is.length()-1)+")";
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
	public String qxym(String power_info){
		String ym = "[";
		if(power_info.indexOf("渠道管理")>=0||power_info.indexOf("企业管理")>=0||power_info.indexOf("车主管理")>=0||power_info.indexOf("车辆管理")>=0||power_info.indexOf("驾驶员管理")>=0){			
			ym+="{"+
					"id: \"khInfoManagement\", name: \"客户信息管理\", icon: \"icon-whgl\", displayState: true, children: [ ";
			if(power_info.indexOf("渠道管理")>=0){
				ym += "{id: \"qdManagement\", name: \"渠道管理\", icon: \"icon-ddwh\", active: true, src: \"./app/html/qdManagement.html\"},";
			}
			if(power_info.indexOf("企业管理")>=0){
				ym += "{id: \"qyManagement\", name: \"企业管理\", icon: \"icon-ddwh\", src: \"./app/html/qyManagement.html\"},";
			}
			if(power_info.indexOf("车主管理")>=0){
				ym += "{id: \"czManagement\", name: \"车主管理\", icon: \"icon-yhgl\", src: \"./app/html/czManagement.html\"},";
			}
			if(power_info.indexOf("车辆管理")>=0){
				ym += "{id: \"clManagement\", name: \"车辆管理\", icon: \"icon-wxcc\", src: \"./app/html/clManagement.html\"},";
			}
			if(power_info.indexOf("驾驶员管理")>=0){
				ym += "{id: \"jsyManagement\", name: \"驾驶员管理\", icon: \"icon-yhgl\", src: \"./app/html/jsyManagement.html\"},";
			}
			ym = ym.substring(0,ym.length()-1);
			ym += "]},";
		}
		if(power_info.indexOf("终端管理")>=0||power_info.indexOf("安装管理")>=0||power_info.indexOf("车辆在线查询")>=0){			
			ym+="{"+
					"id: \"sbManagement\", name: \"设备管理\", icon: \"icon-whgl\", children: [ ";
			if(power_info.indexOf("终端管理")>=0){
				ym += "{id: \"zdManagement\", name: \"终端管理\", icon: \"icon-whgl\", src: \"./app/html/zdManagement.html\"},";
			}
			if(power_info.indexOf("安装管理")>=0){
				ym += "{id: \"azManagement\", name: \"安装管理\", icon: \"icon-wxlx\", src: \"./app/html/azManagement.html\"},";
			}
			if(power_info.indexOf("车辆在线查询")>=0){
				ym += "{id: \"clzxManagement\", name: \"车辆在线查询\", icon: \"icon-wxcc\", src: \"./app/html/clzxManagement.html\"},";
			}
			ym = ym.substring(0,ym.length()-1);	
			ym += "]},";
		}
			if(power_info.indexOf("订单管理")>=0||power_info.indexOf("清算管理")>=0||power_info.indexOf("异常订单管理")>=0){
				ym+="{"+
						"id: \"whManagement\", name: \"经营管理\", icon: \"icon-whgl\", children: [ ";
						if(power_info.indexOf("订单管理")>=0){
							ym += "{id: \"ddManagement\", name: \"订单管理\", icon: \"icon-whgl\", src: \"./app/html/ddManagement.html\"},";
						}
						if(power_info.indexOf("清算管理")>=0){
							ym += "{id: \"qsManagement\", name: \"清算管理\", icon: \"icon-whgl\", src: \"./app/html/qsManagement.html\"},";
						}
						if(power_info.indexOf("异常订单管理")>=0){
							ym += "{id: \"ycddManagement\", name: \"异常订单管理\", icon: \"icon-whgl\", src: \"./app/html/ycddManagement.html\"},";
						}
					ym = ym.substring(0,ym.length()-1);	
					ym += "]},";
			}
			if(power_info.indexOf("驾驶员审核")>=0){
				ym += "{id: \"jsyExamine\", name: \"驾驶员审核\", icon: \"icon-qxgl\", src: \"./app/html/jsyExamine.html\"},";
			}
			if(power_info.indexOf("用户管理")>=0){
				ym += "{id: \"userInfo\", name: \"用户管理\", icon: \"icon-yhgl\", src: \"./app/html/userInfo.html\"},";
			}
			if(power_info.indexOf("权限管理")>=0){
				ym += "{id: \"competence\", name: \"权限管理\", icon: \"icon-qxgl\", src: \"./app/html/competence.html\"},";
			}
//			if(power_info.indexOf("用户管理")>=0||power_info.indexOf("权限管理")>=0){
				ym = ym.substring(0,ym.length()-1);	
//			}
			ym += "]";
		return ym;
	}
	//公司树结构
//		public String gstree(){
//			String owner = "select * from TB_OWNER";
//			String ba = "select * from TB_BUSI_AREA";
//			String comp = "select * from TB_COMPANY";
//			List<Map<String, Object>> list1 = jdbcTemplate.queryForList(owner);
//			List<Map<String, Object>> list2 = jdbcTemplate.queryForList(ba);
//			List<Map<String, Object>> list3 = jdbcTemplate.queryForList(comp);
//			List<Map<String, Object>> tree = new ArrayList<Map<String,Object>>();
//			for (int i = 0; i < list1.size(); i++) {
//				Map<String, Object> otree = new HashMap<String, Object>();
//				otree.put("id", list1.get(i).get("owner_id"));
//				otree.put("pId", "0");
//				otree.put("name", list1.get(i).get("owner_name"));
//				otree.put("click", "test('"+list1.get(i).get("owner_id")+"','"+list1.get(i).get("owner_name")+"')");
//				tree.add(otree);
//				for (int j = 0; j < list2.size(); j++) {
//					if(String.valueOf(list2.get(j).get("owner_id")).equals(list1.get(i).get("owner_id"))){
//						Map<String, Object> btree = new HashMap<String, Object>();
//						btree.put("id", list2.get(j).get("ba_id"));
//						btree.put("pId",list1.get(i).get("owner_id"));
//						btree.put("name", list2.get(j).get("ba_name"));
//						btree.put("click", "test('"+list2.get(j).get("ba_id")+"','"+list2.get(j).get("ba_name")+"')");
//						tree.add(btree);
//					}
//				}
//			}
//			for (int j = 0; j < list2.size(); j++) {
//				for (int j2 = 0; j2 < list3.size(); j2++) {
//					if(String.valueOf(list3.get(j2).get("ba_id")).equals(list2.get(j).get("ba_id"))){
//						Map<String, Object> ctree = new HashMap<String, Object>();
//						ctree.put("id", list3.get(j2).get("comp_id"));
//						ctree.put("pId", list2.get(j).get("ba_id"));
//						ctree.put("name", list3.get(j2).get("comp_name"));
//						ctree.put("click", "test('"+list3.get(j2).get("comp_id")+"','"+list3.get(j2).get("comp_name")+"')");
//						tree.add(ctree);
//					}
//				}
//			}
//			return jacksonUtil.toJson(tree);
//		}
}
