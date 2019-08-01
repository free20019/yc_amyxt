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
public class ycddServer {
	protected JdbcTemplate jdbcTemplate = null;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	public List<Map<String, Object>> finddd(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		int pageIndex = Integer.valueOf(String.valueOf(paramMap.get("page")));
		int pageSize =  Integer.valueOf(String.valueOf(paramMap.get("pageSize")));
		String vehicle = String.valueOf(paramMap.get("vehicle"));
		String status = String.valueOf(paramMap.get("status"));
		String handle = String.valueOf(paramMap.get("handle"));
		String startTime = String.valueOf(paramMap.get("startTime"));
		String endTime = String.valueOf(paramMap.get("endTime"));
		String tj="";
		if(vehicle!=null&&vehicle.length()>0&&!vehicle.equals("null")){
			tj += " and ter.VEHICLE = '"+vehicle+"'";
		}
		if(status!=null&&status.length()>0&&!status.equals("null")){
			if(status.equals("2")){
				tj += " and ord.COMMODITYSTATUS= '"+status+"'";				
			}else{
				tj += " and (ord.COMMODITYSTATUS <> '2' or ord.COMMODITYSTATUS is null)";			
			}
			
		}
		tj += " and ex.order_id is not null";	
		if(handle!=null&&handle.length()>0&&!handle.equals("null")){
			if(handle.equals("1")){
				tj += " and ex.HANDLESTATUS= '"+handle+"'";				
			}else{
				tj += " and (ex.HANDLESTATUS <> '1' or ex.HANDLESTATUS is null)";		
			}
		}
		if(startTime!=null&&startTime.length()>0&&!startTime.equals("null")){
			tj += " and ord.UPTIME > to_date('"+startTime+"', 'yyyy-mm-dd hh24:mi:ss')";
		}
		if(endTime!=null&&endTime.length()>0&&!endTime.equals("null")){
			tj += " and ord.UPTIME <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss')";
		}
		String sql = "select (select count(*) from tb_order ord "
				+ " left join (select ter.TERMINAL_NUM,veh.VEHICLE_NO VEHICLE from tb_vehicle veh,TB_TERMINAL ter,TB_INSTALLATION ins where ter.ID =ins.TERMINAL_ID and veh.ID=ins.VEHICLE_ID) ter on ord.MDT_NO=ter.TERMINAL_NUM"
				+ " left join tb_order_exception ex on ord.commodity_id=ex.order_id where  ord.STATUS='1'";
//		ord.UPTIME<sysdate - 10/(60*24) and
		sql +=tj;
		sql +=") as COUNT,t.* from (select rownum rn ,ord.*,ex.*,ter.VEHICLE from (select * from tb_order order by UPTIME desc) ord"
				+ " left join (select ter.TERMINAL_NUM,veh.VEHICLE_NO VEHICLE from tb_vehicle veh,TB_TERMINAL ter,TB_INSTALLATION ins where ter.ID =ins.TERMINAL_ID and veh.ID=ins.VEHICLE_ID) ter on ord.MDT_NO=ter.TERMINAL_NUM"
				+ " left join tb_order_exception ex on ord.commodity_id=ex.order_id  where rownum <= "+(pageIndex*pageSize)+" and ord.STATUS='1'";
		sql +=tj;	
		sql +=" ) t where t.rn >"+((pageIndex-1)*pageSize);
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}
	public String handle(String id){
		Map<String, Object> map = new HashMap<String, Object>();
		String sql="update tb_order_exception set HANDLESTATUS='1' where order_id='"+id+"'";
		int count =jdbcTemplate.update(sql);
		if(count>0){
			map.put("info", "操作成功");
		}else{
			map.put("info", "操作失败");
		}
		return jacksonUtil.toJson(map);
	}
	public List<Map<String, Object>> dddc(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String vehicle = String.valueOf(paramMap.get("vehicle"));
		String status = String.valueOf(paramMap.get("status"));
		String handle = String.valueOf(paramMap.get("handle"));
		String startTime = String.valueOf(paramMap.get("startTime"));
		String endTime = String.valueOf(paramMap.get("endTime"));
		String tj="";
		if(vehicle!=null&&vehicle.length()>0&&!vehicle.equals("null")){
			tj += " and ter.VEHICLE = '"+vehicle+"'";
		}
		if(status!=null&&status.length()>0&&!status.equals("null")){
			if(status.equals("2")){
				tj += " and ord.COMMODITYSTATUS= '"+status+"'";				
			}else{
				tj += " and (ord.COMMODITYSTATUS <> '2' or ord.COMMODITYSTATUS is null)";			
			}
			
		}
		tj += " and ex.order_id is not null";	
		if(handle!=null&&handle.length()>0&&!handle.equals("null")){
			if(handle.equals("1")){
				tj += " and ex.HANDLESTATUS= '"+handle+"'";				
			}else{
				tj += " and (ex.HANDLESTATUS <> '1' or ex.HANDLESTATUS is null)";		
			}
		}
		if(startTime!=null&&startTime.length()>0&&!startTime.equals("null")){
			tj += " and ord.UPTIME > to_date('"+startTime+"', 'yyyy-mm-dd hh24:mi:ss')";
		}
		if(endTime!=null&&endTime.length()>0&&!endTime.equals("null")){
			tj += " and ord.UPTIME <= to_date('"+endTime+"', 'yyyy-mm-dd hh24:mi:ss')";
		}
		String sql = "select t.* from (select rownum rn ,ord.*,ex.*,ter.VEHICLE from (select * from tb_order order by UPTIME desc) ord"
				+ " left join (select ter.TERMINAL_NUM,veh.VEHICLE_NO VEHICLE from tb_vehicle veh,TB_TERMINAL ter,TB_INSTALLATION ins where ter.ID =ins.TERMINAL_ID and veh.ID=ins.VEHICLE_ID) ter on ord.MDT_NO=ter.TERMINAL_NUM"
				+ " left join tb_order_exception ex on ord.commodity_id=ex.order_id  where ord.STATUS='1'";
		sql +=tj;	
		sql +=" ) t";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				if(list.get(i).get("TOTAL_FEE")!=null){
					double free=Integer.valueOf(list.get(i).get("TOTAL_FEE").toString());
					list.get(i).put("TOTAL_FEE", free/100);
				}
				if( String.valueOf(list.get(i).get("COMMODITYSTATUS")).equals("2")){
					list.get(i).put("COMMODITYSTATUS", "正常订单");
//					list.get(i).put("HANDLESTATUS", "无需处理");
				}else{
					list.get(i).put("COMMODITYSTATUS", "异常订单");
				}
				if(String.valueOf(list.get(i).get("HANDLESTATUS")).equals("1")){
					list.get(i).put("HANDLESTATUS", "已处理");
				}else{
					list.get(i).put("HANDLESTATUS", "未处理");
				}
			}	
		}
		return list;
	}

}
