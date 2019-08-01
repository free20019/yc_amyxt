package mvc.controllers;


import helper.DownloadAct;
import helper.JacksonUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.service.ddServer;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping(value = "/order")
public class ddAction {
	private ddServer ddServer;
	
	public ddServer getDdServer() {
		return ddServer;
	}
	@Autowired
	public void setDdServer(ddServer ddServer) {
		this.ddServer = ddServer;
	}
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	@RequestMapping("/finddd")
	@ResponseBody
	public String finddd(HttpServletRequest request,@RequestParam("postData") String postData) {
		String power_option = String.valueOf(request.getSession().getAttribute("power_option"));
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String page = String.valueOf(paramMap.get("page"));
		 	int pageIndex = Integer.parseInt(page);//当前页
	        int pageSize =10;//设置每页要展示的数据数量(根据项目需求灵活设置)
	        int rowCount = 0 ;
	        List<Map<String, Object>> listExamine=ddServer.finddd(postData,power_option);;//获取总数据量
			float amount=0;
			for(int i=0;i<listExamine.size();i++){
				if(listExamine!=null)
				if(listExamine.get(i)!=null)
				if(!String.valueOf(listExamine.get(i).get("TOTAL_FEE")).equals("null")){	
					amount +=Integer.valueOf(listExamine.get(i).get("TOTAL_FEE").toString()).intValue();			
				}
			}

	        try {
	                rowCount=listExamine.size();//总条数
	               //通过计算，得到分页应该需要分几页，其中不满一页的数据按一页计算
	               if(rowCount%pageSize!=0)
	               {
	                   rowCount = rowCount / pageSize + 1;
	               }
	               else
	               {
	                   rowCount = rowCount / pageSize;
	               }
	           } catch (Exception e) {
	       }
//	        String showList=ddServer.findddb(pageIndex,pageSize,postData,power_option);//根据pageIndex和pageSize获取需要展示的该页数据
	       //转成Json格式
	       String msg = "{\"count\":"+listExamine.size()+",\"amount\":"+amount/100+",\"pageCount\":"+rowCount+",\"CurrentPage\":"+pageIndex+",\"list\":" + jacksonUtil.toJson(listExamine) + "}";
	    return msg;
	}
//	@RequestMapping("/adddd")
//	@ResponseBody
//	public String addcl(HttpServletRequest request,@RequestParam("postData") String postData) {
//		String msg = ddServer.adddd(postData);
//	    return msg;
//	}
//	@RequestMapping("/editcl")
//	@ResponseBody
//	public String editcl(HttpServletRequest request,@RequestParam("postData") String postData) {
//		String msg = ddServer.editcl(postData);
//	    return msg;
//	}
//	@RequestMapping("/delcl")
//	@ResponseBody
//	public String delcl(HttpServletRequest request,@RequestParam("postData") String postData) {
//		String msg = ddServer.delcl(postData);
//	    return msg;
//	}

	@RequestMapping("dddc")
	@ResponseBody
	public String dddc(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("postData") String postData) throws IOException{
		String power_option = String.valueOf(request.getSession().getAttribute("power_option"));
		postData = java.net.URLDecoder.decode(postData, "UTF-8");
		String a[] = {"省","市","渠道","企业","车主","车牌","订单编号","订单时间","订单金额(分)","订单状态"};//导出列明
		String b[] = {"PROVINCE","CITY","CHANNEL_NAME","COMPANY_NAME","OWNER_NAME","VEHICLE_NO","COMMODITY_ID","UPTIME","TOTAL_FEE","COMMODITYSTATUS"};//导出map中的key
		String gzb = "订单管理";//导出sheet名和导出的文件名
		List<Map<String, Object>> list = ddServer.finddd(postData,power_option);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}

}
