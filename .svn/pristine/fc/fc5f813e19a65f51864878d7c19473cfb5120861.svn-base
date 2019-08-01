package mvc.service;


import java.util.Date;
import java.math.BigDecimal;
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
public class qsServer {
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
				sql += " and cha.channel_id= '"+a[1]+"'";
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
	public List<Map<String, Object>> findqs(String postData,String power_option){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String time = String.valueOf(paramMap.get("time"));
		String sql = "select distinct t.*,ord.count,ord.TOTAL_FEE from (select distinct cow.owner_name,cha.channel_name,cha.revenue channel_revenue,com.province,com.city,com.company_name,com.company_revenue,veh.TYPE,veh.VEHICLE_NO"
				+ " from TB_CAR_OWNER cow,tb_channel_revenue cha,tb_company com,tb_vehicle veh,TB_INSTALLATION ins"
				+ " where ins.TERMINAL_NUM in (select MDT_NO from tb_order where status='1'";
		
		if(time!=null&&time.length()>0&&!time.equals("null")){
			sql += " and  To_Char(UPTIME, 'yyyy-mm-dd') like '"+time+"%'";
		}
		sql +=" ) and veh.ID=ins.VEHICLE_ID and veh.OWNER_ID=cow.ID and cow.company_id=com.id and com.id=cha.company_id";
		sql +=findsql(power_option);
		sql +=" ) t left join (select count(VEHICLE_NO) count,VEHICLE_NO,sum(TOTAL_FEE) TOTAL_FEE from (select ord.TOTAL_FEE,veh.VEHICLE_NO from tb_order ord,TB_INSTALLATION ins, tb_vehicle veh,TB_TERMINAL ter where ord.mdt_no=ter.TERMINAL_NUM and ter.ID = ins.TERMINAL_ID and veh.ID = ins.VEHICLE_ID and ord.status = '1') "
				+ " group by VEHICLE_NO) ord on ord.VEHICLE_NO = t.VEHICLE_NO";
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		Map<String, Object> map=new HashMap<String, Object>();
		if(list.size()>0){
			BigDecimal numall=new BigDecimal(0);
			BigDecimal numaall=new BigDecimal(0);
			BigDecimal numball=new BigDecimal(0);
			BigDecimal numcall=new BigDecimal(0);
			BigDecimal ddcount=new BigDecimal(0);
			for(int i=0;i<list.size();i++){
				list.get(i).put("OWNER_REVENUE", "30%");
				if(!String.valueOf(list.get(i).get("TOTAL_FEE")).equals("null")){
					BigDecimal num=new BigDecimal ((list.get(i).get("TOTAL_FEE")).toString());
					BigDecimal d=new BigDecimal (100);					
					if(!String.valueOf(list.get(i).get("channel_revenue")).equals("null")){
						BigDecimal a=new BigDecimal ((list.get(i).get("channel_revenue")).toString().replace("%", ""));
						BigDecimal numa=num.multiply(a).divide(d).divide(d);
						list.get(i).put("numa", numa);
						numaall=numaall.add(numa);
					}
					if(!String.valueOf(list.get(i).get("company_revenue")).equals("null")){
						BigDecimal b=new BigDecimal ((list.get(i).get("company_revenue")).toString().replace("%", ""));	
						BigDecimal numb=num.multiply(b).divide(d).divide(d);
						list.get(i).put("numb", numb);
					}
					if(!String.valueOf(list.get(i).get("owner_revenue")).equals("null")){
						BigDecimal c=new BigDecimal ((list.get(i).get("owner_revenue")).toString().replace("%", ""));	
						BigDecimal numc=num.multiply(c).divide(d).divide(d);
						list.get(i).put("numc", numc);
					}
					num=num.divide(d);		
					list.get(i).put("num",num);
				}
				list.get(i).put("numaall",numaall);
			}
			List<Map<String, Object>> listCount=new ArrayList<Map<String, Object>>();
			for(int i=0;i<list.size();i++){	
				Map<String, Object> mapCount=new HashMap<String, Object>();
				mapCount.put("VEHICLE_NO", list.get(i).get("VEHICLE_NO"));
				mapCount.put("count", list.get(i).get("count"));
				mapCount.put("num", list.get(i).get("num"));
				mapCount.put("numb", list.get(i).get("numb"));
				mapCount.put("numc", list.get(i).get("numc"));
				listCount.add(mapCount);
			}
			listCount=removeDuplicate(listCount);
			for(int i=0;i<listCount.size();i++){	
				if(!String.valueOf(list.get(i).get("count")).equals("null")){
					BigDecimal count=new BigDecimal ((listCount.get(i).get("count")).toString());	
					ddcount=ddcount.add(count);
				}
				if(!String.valueOf(list.get(i).get("num")).equals("null")){
					BigDecimal num=new BigDecimal ((listCount.get(i).get("num")).toString());	
					numall=numall.add(num);
				}
				if(!String.valueOf(list.get(i).get("numb")).equals("null")){
					BigDecimal numb=new BigDecimal ((listCount.get(i).get("numb")).toString());	
					numball=numball.add(numb);
				}
				if(!String.valueOf(list.get(i).get("numc")).equals("null")){
					BigDecimal numc=new BigDecimal ((listCount.get(i).get("numc")).toString());	
					numcall=numcall.add(numc);
				}
			}
			map.put("ddcount", ddcount);
			map.put("count", listCount.size());
			map.put("numall", numall);
			map.put("numaall", list.get(list.size()-1).get("numaall"));
			map.put("numball", numball);
			map.put("numcall", numcall);
		}
		String[] power=power_option.split("\\|");
		if(power[0].equals("2")||power[0].equals("3")){
			for(int i=0;i<list.size();i++){
				list.get(i).put("CHANNEL_NAME", "");
				list.get(i).put("numaall", "");
				list.get(i).put("numa", "");
				list.get(i).put("CHANNEL_REVENUE", "");
			}
			list=removeDuplicate(list);
		}
		list.add(map);
		return list;
	}
	public List<Map<String, Object>> findqs2(String postData,String power_option){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String time = String.valueOf(paramMap.get("time"));
		String sql = "select distinct t.*,ord.count,ord.TOTAL_FEE from (select distinct cow.owner_name,cha.channel_name,cha.revenue channel_revenue,com.province,com.city,com.company_name,com.company_revenue,veh.TYPE,veh.VEHICLE_NO"
				+ " from TB_CAR_OWNER cow,tb_channel_revenue cha,tb_company com,tb_vehicle veh,TB_INSTALLATION ins"
				+ " where ins.TERMINAL_NUM in (select MDT_NO from tb_order where status='1'";
		
		if(time!=null&&time.length()>0&&!time.equals("null")){
			sql += " and  To_Char(UPTIME, 'yyyy-mm-dd') like '"+time+"%'";
		}
		sql +=" ) and veh.ID=ins.VEHICLE_ID and veh.OWNER_ID=cow.ID and cow.company_id=com.id and com.id=cha.company_id";
		sql +=findsql(power_option);
		sql +=" ) t left join (select count(VEHICLE_NO) count,VEHICLE_NO,sum(TOTAL_FEE) TOTAL_FEE from (select ord.TOTAL_FEE,veh.VEHICLE_NO from tb_order ord,TB_INSTALLATION ins, tb_vehicle veh,TB_TERMINAL ter where ord.mdt_no=ter.TERMINAL_NUM and ter.ID = ins.TERMINAL_ID and veh.ID = ins.VEHICLE_ID and ord.status = '1') "
				+ " group by VEHICLE_NO) ord on ord.VEHICLE_NO = t.VEHICLE_NO";		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		Map<String, Object> map=new HashMap<String, Object>();
		if(list.size()>0){
			BigDecimal numall=new BigDecimal(0);
			BigDecimal numaall=new BigDecimal(0);
			BigDecimal numball=new BigDecimal(0);
			BigDecimal numcall=new BigDecimal(0);
			BigDecimal ddcount=new BigDecimal(0);
			for(int i=0;i<list.size();i++){
				list.get(i).put("OWNER_REVENUE", "30%");
				if(!String.valueOf(list.get(i).get("TOTAL_FEE")).equals("null")){
					BigDecimal num=new BigDecimal ((list.get(i).get("TOTAL_FEE")).toString());
					BigDecimal d=new BigDecimal (100);					
					if(!String.valueOf(list.get(i).get("channel_revenue")).equals("null")){
						BigDecimal a=new BigDecimal ((list.get(i).get("channel_revenue")).toString().replace("%", ""));
						BigDecimal numa=num.multiply(a).divide(d).divide(d);
						list.get(i).put("numa", numa);
						numaall=numaall.add(numa);
					}
					if(!String.valueOf(list.get(i).get("company_revenue")).equals("null")){
						BigDecimal b=new BigDecimal ((list.get(i).get("company_revenue")).toString().replace("%", ""));	
						BigDecimal numb=num.multiply(b).divide(d).divide(d);
						list.get(i).put("numb", numb);
					}
					if(!String.valueOf(list.get(i).get("owner_revenue")).equals("null")){
						BigDecimal c=new BigDecimal ((list.get(i).get("owner_revenue")).toString().replace("%", ""));	
						BigDecimal numc=num.multiply(c).divide(d).divide(d);
						list.get(i).put("numc", numc);
					}
					num=num.divide(d);		
					list.get(i).put("num",num);
				}
				list.get(i).put("numaall",numaall);
			}
			List<Map<String, Object>> listCount=new ArrayList<Map<String, Object>>();
			for(int i=0;i<list.size();i++){	
				Map<String, Object> mapCount=new HashMap<String, Object>();
				mapCount.put("VEHICLE_NO", list.get(i).get("VEHICLE_NO"));
				mapCount.put("count", list.get(i).get("count"));
				mapCount.put("num", list.get(i).get("num"));
				mapCount.put("numb", list.get(i).get("numb"));
				mapCount.put("numc", list.get(i).get("numc"));
				listCount.add(mapCount);
			}
			listCount=removeDuplicate(listCount);
			for(int i=0;i<listCount.size();i++){	
				if(!String.valueOf(list.get(i).get("count")).equals("null")){
					BigDecimal count=new BigDecimal ((listCount.get(i).get("count")).toString());	
					ddcount=ddcount.add(count);
				}
				if(!String.valueOf(list.get(i).get("num")).equals("null")){
					BigDecimal num=new BigDecimal ((listCount.get(i).get("num")).toString());	
					numall=numall.add(num);
				}
				if(!String.valueOf(list.get(i).get("numb")).equals("null")){
					BigDecimal numb=new BigDecimal ((listCount.get(i).get("numb")).toString());	
					numball=numball.add(numb);
				}
				if(!String.valueOf(list.get(i).get("numc")).equals("null")){
					BigDecimal numc=new BigDecimal ((listCount.get(i).get("numc")).toString());	
					numcall=numcall.add(numc);
				}
			}
			map.put("PROVINCE", "总营收");
			map.put("COUNT", ddcount);
			map.put("num", numall);
			map.put("numa", list.get(list.size()-1).get("numaall"));
			map.put("numb", numball);
			map.put("numc", numcall);
		}
		String[] power=power_option.split("\\|");
		if(power[0].equals("2")||power[0].equals("3")){
			for(int i=0;i<list.size();i++){
				list.get(i).put("CHANNEL_NAME", "");
				list.get(i).put("numaall", "");
				list.get(i).put("numa", "");
				list.get(i).put("CHANNEL_REVENUE", "");
			}
			list=removeDuplicate(list);
		}
		list.add(map);
		return list;
	}
	private int Integer(String string) {
		// TODO Auto-generated method stub
		return 0;
	}
	public String findqsb(int pageIndex,int pageSize,String postData,String power_option){
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String time = String.valueOf(paramMap.get("time"));
		String sql = "select a.* from ( select distinct t.*,rownum as rn,ord.count,ord.TOTAL_FEE from (select distinct cow.owner_name,cha.channel_name,cha.revenue channel_revenue,com.province,com.city,com.company_name,com.company_revenue,veh.TYPE,veh.VEHICLE_NO"
				+ " from TB_CAR_OWNER cow,tb_channel_revenue cha,tb_company com,tb_vehicle veh,TB_INSTALLATION ins"
				+ " where ins.TERMINAL_NUM in (select MDT_NO from tb_order where status='1'";
		
		if(time!=null&&time.length()>0&&!time.equals("null")){
			sql += " and  To_Char(UPTIME, 'yyyy-mm-dd') like '"+time+"%'";
		}
		sql +=" ) and veh.ID=ins.VEHICLE_ID and veh.OWNER_ID=cow.ID and cow.company_id=com.id and com.id=cha.company_id";
		sql +=findsql(power_option);
		sql +=" ) t left join (select count(VEHICLE_NO) count,VEHICLE_NO,sum(TOTAL_FEE) TOTAL_FEE from (select ord.TOTAL_FEE,veh.VEHICLE_NO from tb_order ord,TB_INSTALLATION ins, tb_vehicle veh,TB_TERMINAL ter where ord.mdt_no=ter.TERMINAL_NUM and ter.ID = ins.TERMINAL_ID and veh.ID = ins.VEHICLE_ID and ord.status = '1') "
				+ " group by VEHICLE_NO) ord on ord.VEHICLE_NO = t.VEHICLE_NO";
		sql +=" where rownum <= "+(pageIndex*pageSize)+") a where a.rn >"+((pageIndex-1)*pageSize);
		System.out.println(sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				list.get(i).put("OWNER_REVENUE", "30%");
				if(!String.valueOf(list.get(i).get("TOTAL_FEE")).equals("null")){
					BigDecimal num=new BigDecimal ((list.get(i).get("TOTAL_FEE")).toString());
					BigDecimal d=new BigDecimal (100);					
					if(!String.valueOf(list.get(i).get("channel_revenue")).equals("null")){
						BigDecimal a=new BigDecimal ((list.get(i).get("channel_revenue")).toString().replace("%", ""));
						BigDecimal numa=num.multiply(a).divide(d).divide(d);
						list.get(i).put("numa", numa);
					}
					if(!String.valueOf(list.get(i).get("company_revenue")).equals("null")){
						BigDecimal b=new BigDecimal ((list.get(i).get("company_revenue")).toString().replace("%", ""));	
						BigDecimal numb=num.multiply(b).divide(d).divide(d);
						list.get(i).put("numb", numb);
					}
					if(!String.valueOf(list.get(i).get("owner_revenue")).equals("null")){
						BigDecimal c=new BigDecimal ((list.get(i).get("owner_revenue")).toString().replace("%", ""));	
						BigDecimal numc=num.multiply(c).divide(d).divide(d);
						list.get(i).put("numc", numc);
					}
					num=num.divide(d);		
					list.get(i).put("num",num);
				}
			}
		}
		String[] power=power_option.split("\\|");
		if(power[0].equals("2")||power[0].equals("3")){
			for(int i=0;i<list.size();i++){
				list.get(i).put("CHANNEL_NAME", "");
				list.get(i).put("numa", "");
				list.get(i).put("rn", "");
				list.get(i).put("CHANNEL_REVENUE", "");
			}
			list=removeDuplicate(list);
		}
		return jacksonUtil.toJson(list);
	}
	//list集合去重
	public   static   List<Map<String, Object>>  removeDuplicate(List list)  {       
		  for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {       
		      for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {       
		           if  (list.get(j).equals(list.get(i)))  {       
		              list.remove(j);       
		            }        
		        }        
		      }        
		    return list;       
		}
}
