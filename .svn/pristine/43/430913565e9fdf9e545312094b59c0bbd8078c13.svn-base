package mvc.service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import helper.JacksonUtil;
import helper.SystemConfig;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class CommonServer {    
	protected JdbcTemplate jdbcTemplate = null;
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	

	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();

	public String ssjk(String postData) {
		Map<String,Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String,Object>>() {});
		String gsmc = String.valueOf(paramMap.get("gsmc"));
		Map<String, Object> resultmap = new HashMap<String, Object>();
		return jacksonUtil.toJson(resultmap);
	}
	/**
	 * 公共基础类，通用方法
	 * 公司、车辆等下拉框
	 */
	//渠道下拉框
	public String findqd(){
		String sql = "select channel_name,id from TB_CHANNEL";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		List al = new ArrayList();
		for(int i=0;i<list.size();i++){
			Map map = new HashMap();
			map.put("name", list.get(i).get("channel_name"));
			map.put("id", list.get(i).get("id"));
			al.add(map);
		}
		Map m = new HashMap();
		m.put("datas", al);
		return  jacksonUtil.toJson(m);
	}
	//企业下拉框
	public String findqy(){
		String sql = "select company_name,id from TB_COMPANY order by company_name";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		List al = new ArrayList();
		for(int i=0;i<list.size();i++){
			Map map = new HashMap();
			map.put("name", list.get(i).get("company_name"));
			map.put("id", list.get(i).get("id"));
			al.add(map);
		}
		Map m = new HashMap();
		m.put("datas", al);
		return  jacksonUtil.toJson(m);
	}
	//车主下拉栏
	public String findclcz(){
		String sql = "select ID,OWNER_NAME from TB_CAR_OWNER where 1=1";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		List al = new ArrayList();
		for(int i=0;i<list.size();i++){
			Map map = new HashMap();
			map.put("name", list.get(i).get("OWNER_NAME"));
			map.put("id", list.get(i).get("ID"));
			al.add(map);
		}
		Map m = new HashMap();
		m.put("datas", al);
		return  jacksonUtil.toJson(m);
	}
	//车辆下拉栏
		public String findcl(){
			String sql = "select ID,VEHICLE_NO from TB_VEHICLE where 1=1";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			List al = new ArrayList();
			for(int i=0;i<list.size();i++){
				Map map = new HashMap();
				map.put("name", list.get(i).get("VEHICLE_NO"));
				map.put("id", list.get(i).get("ID"));
				al.add(map);
			}
			Map m = new HashMap();
			m.put("datas", al);
			return  jacksonUtil.toJson(m);
		}
		//类型下拉栏
				public String findlx(String vhic){
					String sql = "select TYPE from TB_VEHICLE where 1=1";
					if(vhic!=null&&vhic.length()>0){
						sql +=" and ID= '"+ vhic+"'";
					}
					List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
					return  jacksonUtil.toJson(list);
				}
		//车主,车牌,终端编号,驾驶员
		public String findchoose(String choose){
			List al =null;
			if(choose.equals("TB_CAR_OWNER")){
				String sql = "select ID,OWNER_NAME from TB_CAR_OWNER where 1=1";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
				al = new ArrayList();
				for(int i=0;i<list.size();i++){
					Map map = new HashMap();
					map.put("name", list.get(i).get("OWNER_NAME"));
					map.put("id", list.get(i).get("ID"));
					al.add(map);
				}
			}else if(choose.equals("TB_VEHICLE")){
				String sql = "select ID,VEHICLE_NO from TB_VEHICLE";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
				al = new ArrayList();
				for(int i=0;i<list.size();i++){
					Map map = new HashMap();
					map.put("name", list.get(i).get("VEHICLE_NO"));
					map.put("id", list.get(i).get("ID"));
					al.add(map);
				}
			}else if(choose.equals("TB_TERMINAL")){
				String sql = "select ID,TERMINAL_NUM from TB_TERMINAL";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
				al = new ArrayList();
				for(int i=0;i<list.size();i++){
					Map map = new HashMap();
					map.put("name", list.get(i).get("TERMINAL_NUM"));
					map.put("id", list.get(i).get("ID"));
					al.add(map);
				}
			}else if(choose.equals("TB_DRIVER")){
				String sql = "select ID,DRIVER_NAME from TB_DRIVER";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
				al = new ArrayList();
				for(int i=0;i<list.size();i++){
					Map map = new HashMap();
					map.put("name", list.get(i).get("DRIVER_NAME"));
					map.put("id", list.get(i).get("ID"));
					al.add(map);
				}
			}			
			Map m = new HashMap();
			m.put("datas", al);
			return  jacksonUtil.toJson(m);
		}
		//终端下拉栏
		public String findzd(){
			String sql = "select  ID,TERMINAL_NUM  from TB_TERMINAL ";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			List al = new ArrayList();
			for(int i=0;i<list.size();i++){
				Map map = new HashMap();
				map.put("name", list.get(i).get("TERMINAL_NUM"));
				map.put("id", list.get(i).get("ID"));
				al.add(map);
			}
			Map m = new HashMap();
			m.put("datas", al);
			return  jacksonUtil.toJson(m);
		}
		//终端型号
		public String findmodel(){
			String sql = "select  ID,MDT_MODEL  from TB_TERMINAL_DETAILS ";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			List al = new ArrayList();
			for(int i=0;i<list.size();i++){
				Map map = new HashMap();
				map.put("name", list.get(i).get("MDT_MODEL"));
				map.put("id", list.get(i).get("ID"));
				al.add(map);
			}
			Map m = new HashMap();
			m.put("datas", al);
			return  jacksonUtil.toJson(m);
		}
	//终端类型
		public String findtype(String id){
			String sql = "select  MDT_TYPE from TB_TERMINAL_DETAILS where 1=1 ";
			if(id!=null&&id.length()>0){
				sql +=" and MDT_MODEL= '"+ id+"'";
			}
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			return  jacksonUtil.toJson(list);
		}
		//车主,车牌,终端编号
				public String finduserchoose(String choose){
					if(choose.equals("1")){
						choose="TB_CHANNEL";
					}else if(choose.equals("2")){
						choose="TB_COMPANY";
					}else if(choose.equals("3")){
						choose="TB_CAR_OWNER";
					}else if(choose.equals("4")){
						choose="TB_VEHICLE";
					}
					List al =null;
					if(choose.equals("TB_CAR_OWNER")){
						String sql = "select ID,OWNER_NAME from TB_CAR_OWNER where 1=1";
						List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
						al = new ArrayList();
						for(int i=0;i<list.size();i++){
							Map map = new HashMap();
							map.put("name", list.get(i).get("OWNER_NAME"));
							map.put("id", list.get(i).get("ID"));
							al.add(map);
						}
					}else if(choose.equals("TB_CHANNEL")){
						String sql = "select ID,CHANNEL_NAME from TB_CHANNEL";
						List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
						al = new ArrayList();
						for(int i=0;i<list.size();i++){
							Map map = new HashMap();
							map.put("name", list.get(i).get("CHANNEL_NAME"));
							map.put("id", list.get(i).get("ID"));
							al.add(map);
						}
					}else if(choose.equals("TB_COMPANY")){
						String sql = "select ID,COMPANY_NAME from TB_COMPANY";
						List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
						al = new ArrayList();
						for(int i=0;i<list.size();i++){
							Map map = new HashMap();
							map.put("name", list.get(i).get("COMPANY_NAME"));
							map.put("id", list.get(i).get("ID"));
							al.add(map);
						}
					}else if(choose.equals("TB_VEHICLE")){
						String sql = "select ID,VEHICLE_NO from TB_VEHICLE";
						List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
						al = new ArrayList();
						for(int i=0;i<list.size();i++){
							Map map = new HashMap();
							map.put("name", list.get(i).get("VEHICLE_NO"));
							map.put("id", list.get(i).get("ID"));
							al.add(map);
						}
					}				
					Map m = new HashMap();
					m.put("datas", al);
					return  jacksonUtil.toJson(m);
				}
	//车辆类型
	public String findvehicletype(){
		String sql = "select  ID,VEHICLE_TYPE  from tb_vehicle_seat ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		List al = new ArrayList();
		for(int i=0;i<list.size();i++){
			Map map = new HashMap();
			map.put("name", list.get(i).get("VEHICLE_TYPE"));
			map.put("id", list.get(i).get("ID"));
			al.add(map);
		}
		Map m = new HashMap();
		m.put("datas", al);
		return  jacksonUtil.toJson(m);
	}
	public void query_pic(HttpServletRequest request, HttpServletResponse response,String type, String rr_id) {
		String sql = "select ID_CARD_PIC,VEHICLE_PIC,LICENSE_PIC from tb_driver_register where id = '"+ rr_id+"'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		String url = "";
		try {
			if(type.equals("0")){
				url = list.get(0).get("ID_CARD_PIC").toString();
			}else if(type.equals("1")){
				url = list.get(0).get("VEHICLE_PIC").toString();
			}else if(type.equals("2")){
				url = list.get(0).get("LICENSE_PIC").toString();
			}
			System.out.println(url);
			File pf = new File(url);
			if (!pf.exists()) {
				return;
			}
			double rate = 0.5;
			int[] results = getImgWidth(pf);
			int widthdist = 0;
			int heightdist = 0;
			if (results == null || results[0] == 0 || results[1] == 0) {
				return;
			} else {
//				if (results[0]>126) {
//					rate = (double)126/(double)results[0];
//				}
				widthdist = (int) (results[0] * rate);
				heightdist = (int) (results[1] * rate);
			}
			Image src = javax.imageio.ImageIO.read(pf);
			BufferedImage tag = new BufferedImage((int) widthdist, (int) heightdist,
					BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0,
					null);
			ServletOutputStream fout = response.getOutputStream();
			ImageIO.write(tag, "jpg", fout);
			fout.close();
		} catch (Exception e) {
		}
	}
	public static int[] getImgWidth(File file) {
		InputStream is = null;
		BufferedImage src = null;
		int result[] = { 0, 0 };
		try {
			is = new FileInputStream(file);
			src = javax.imageio.ImageIO.read(is);
			result[0] = src.getWidth(null); // 得到源图宽
			result[1] = src.getHeight(null); // 得到源图高
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
