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
public class clzxServer {
	protected JdbcTemplate jdbcTemplate = null;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
//	public List<Map<String, Object>> findzx(String postData){
//		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
//		String terminal = String.valueOf(paramMap.get("terminal"));
//		String vehicle = String.valueOf(paramMap.get("vehicle"));
//		String sql = "select t.* from (select x.*,veh.VEHICLE_NO from TB_MDT_MANAGE x,TB_INSTALLATION ins,TB_VEHICLE veh"
//				+ " where MDTUPDATETIME=(select max(MDTUPDATETIME) from TB_MDT_MANAGE y where x.MDT_NO=y.MDT_NO)"
//				+ " and x.MDT_NO=ins.terminal_num and ins.vehicle_id=veh.id";
//		if(terminal!=null&&terminal.length()>0&&!terminal.equals("null")){
//			sql += " and x.MDT_NO= '"+terminal+"'";
//		}
//		if(vehicle!=null&&vehicle.length()>0&&!vehicle.equals("null")){
//			sql += " and veh.VEHICLE_NO= '"+vehicle+"'";
//		}
//		sql += " ) t";
//		System.out.println(sql);
//		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);	
//		return list;
//	}
//	public String findzxb(int pageIndex,int pageSize,String postData){
//		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
//		String terminal = String.valueOf(paramMap.get("terminal"));
//		String vehicle = String.valueOf(paramMap.get("vehicle"));
//		String sql = "select b.* from ( select t.*,a.COUNT,b.time,rownum rn from (select x.*,veh.VEHICLE_NO from TB_MDT_MANAGE x,TB_INSTALLATION ins,TB_VEHICLE veh"
//					+ " where MDTUPDATETIME=(select max(MDTUPDATETIME) from TB_MDT_MANAGE y where x.MDT_NO=y.MDT_NO)"
//					+ " and x.MDT_NO=ins.terminal_num and ins.vehicle_id=veh.id";
//		if(terminal!=null&&terminal.length()>0&&!terminal.equals("null")){
//			sql += " and x.MDT_NO= '"+terminal+"'";
//		}
//		if(vehicle!=null&&vehicle.length()>0&&!vehicle.equals("null")){
//			sql += " and veh.VEHICLE_NO= '"+vehicle+"'";
//		}
//		sql += " ) t left join (select count(SEAT_NO) COUNT,MDT_NO from (select distinct MDT_NO,SEAT_NO from TB_MDT_MANAGE) group by MDT_NO) a on a.MDT_NO=t.MDT_NO"
//				+ " left join (select mdt_no, max(result) time from (select mdt_no, to_char(wm_concat(nvl((MDTUPDATETIME-to_date('1970-01-01 08:00:00','yyyy-mm-dd hh24:mi:ss'))*24*60*60*1000, '0')) over (partition by mdt_no order by seat_no)) result from (select t.mdt_no,t.seat_no,max(MDTUPDATETIME) MDTUPDATETIME from tb_mdt_manage t group by t.mdt_no,t.seat_no)) group by mdt_no ) b on b.MDT_NO=t.MDT_NO ";
//		sql +=" and rownum <= "+(pageIndex*pageSize) +") b where b.rn >"+((pageIndex-1)*pageSize)+" order by b.MDTUPDATETIME";
//		System.out.println(sql);
//		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
//		return jacksonUtil.toJson(list);
//	}
	
	public List<Map<String, Object>> findzx(String postData){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String terminal = String.valueOf(paramMap.get("terminal"));
		String vehicle = String.valueOf(paramMap.get("vehicle"));
		String sql = "select t.* from (select x.*,veh.VEHICLE_NO from TB_DB_LASTTIME_DTU x,TB_INSTALLATION ins,TB_VEHICLE veh"
				+ " where UPTIME=(select max(UPTIME) from TB_DB_LASTTIME_DTU y where x.MDT_NO=y.MDT_NO)"
				+ " and x.MDT_NO=ins.terminal_num and ins.vehicle_id=veh.id";
		if(terminal!=null&&terminal.length()>0&&!terminal.equals("null")){
			sql += " and x.MDT_NO= '"+terminal+"'";
		}
		if(vehicle!=null&&vehicle.length()>0&&!vehicle.equals("null")){
			sql += " and veh.VEHICLE_NO= '"+vehicle+"'";
		}
		sql += ") t order by t.UPTIME desc";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);	
		return list;
	}
//	public String findzxb(int pageIndex,int pageSize,String postData){
//		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
//		String terminal = String.valueOf(paramMap.get("terminal"));
//		String vehicle = String.valueOf(paramMap.get("vehicle"));
//		String sql = "select b.* from ( select t.*,rownum rn from (select x.*,veh.VEHICLE_NO from TB_DB_LASTTIME_DTU x,TB_INSTALLATION ins,TB_VEHICLE veh"
//					+ " where UPTIME=(select max(UPTIME) from TB_DB_LASTTIME_DTU y where x.MDT_NO=y.MDT_NO)"
//					+ " and x.MDT_NO=ins.terminal_num and ins.vehicle_id=veh.id";
//		if(terminal!=null&&terminal.length()>0&&!terminal.equals("null")){
//			sql += " and x.MDT_NO= '"+terminal+"'";
//		}
//		if(vehicle!=null&&vehicle.length()>0&&!vehicle.equals("null")){
//			sql += " and veh.VEHICLE_NO= '"+vehicle+"'";
//		}
//		sql +=" and rownum <= "+(pageIndex*pageSize) +" ) t order by t.UPTIME desc) b where b.rn >"+((pageIndex-1)*pageSize)+"";
//		System.out.println(sql);
//		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
//		return jacksonUtil.toJson(list);
//	}

}
