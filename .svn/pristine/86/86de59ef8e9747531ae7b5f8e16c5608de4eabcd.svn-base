package mvc.service;

import helper.JacksonUtil;








import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class LoginServer {
	protected JdbcTemplate jdbcTemplate = null;
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	


	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();


	public String login(String username, String password,HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
//		if(!"wx".equals(username.substring(0,2))){
//			map.put("info", "1");
//			return jacksonUtil.toJson(map);
//		}
		List<Map<String, Object>> list = null;
		String sql= "select * from TB_USER u,tb_user_power p where u.note=p.id and user_name = ? and user_pwd = ? and u.sign is null ";
		System.out.println(sql);		
			list = jdbcTemplate.queryForList(sql,username,password);
			map.put("value", "0");
		if(list.size()>0){
			map.put("info", "0");
			request.getSession().setAttribute("username", list.get(0).get("user_name"));
			request.getSession().setAttribute("user_pwd", list.get(0).get("user_pwd"));
			request.getSession().setAttribute("user_id", list.get(0).get("user_id"));
			request.getSession().setAttribute("realname", list.get(0).get("real_name"));
			request.getSession().setAttribute("power", list.get(0).get("power"));
			request.getSession().setAttribute("power_but", list.get(0).get("power_but"));
			request.getSession().setAttribute("power_info", list.get(0).get("power_info"));
			request.getSession().setAttribute("power_name", list.get(0).get("power_name"));
			request.getSession().setAttribute("power_option", String.valueOf(list.get(0).get("POWER_OPTION")));
			request.getSession().setAttribute("option_name", list.get(0).get("option_name"));
		}else{
			map.put("info", "1");
		}
		return jacksonUtil.toJson(map);
	}


	public String index(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("realname", request.getSession().getAttribute("realname"));
		map.put("username", request.getSession().getAttribute("username"));
		map.put("user_pwd", request.getSession().getAttribute("user_pwd"));
		map.put("power_name", request.getSession().getAttribute("power_name"));
		map.put("power_option", request.getSession().getAttribute("power_option"));
		map.put("info", request.getSession().getAttribute("power"));
		map.put("power_but", request.getSession().getAttribute("power_but"));
		map.put("option_name", request.getSession().getAttribute("option_name"));
		return jacksonUtil.toJson(map);
	}
	public String editUser(HttpServletRequest request,String postData) {
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String user_name = String.valueOf(paramMap.get("user_name"));
		String user_pwd = String.valueOf(paramMap.get("user_pwd"));
		String real_name = String.valueOf(paramMap.get("real_name"));
		String user_id=(String) request.getSession().getAttribute("user_id");
		String sql = "update TB_USER set user_name='"+user_name+"',user_pwd='"+user_pwd+
				"',real_name='"+real_name+"' where user_id='"+user_id+"'";
		int count=0;
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
}
