package mvc.controllers;


import helper.DownloadAct;
import helper.JacksonUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.service.ycddServer;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping(value = "/ycdd")
public class ycddAction {
	private ycddServer ycddServer;
	
	public ycddServer getycddServer() {
		return ycddServer;
	}
	@Autowired
	public void setycddServer(ycddServer ycddServer) {
		this.ycddServer = ycddServer;
	}
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	@RequestMapping("/findycdd")
	@ResponseBody
	public String findycdd(HttpServletRequest request,@RequestParam("postData") String postData) {
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String page = String.valueOf(paramMap.get("page"));
		 	int pageIndex = Integer.parseInt(page);//当前页
	        int pageSize =Integer.parseInt(String.valueOf(paramMap.get("pageSize")));
	        int rowCount = 0 ;
	        int size=0;
	        List<Map<String, Object>> listExamine=ycddServer.finddd(postData);;//获取总数据量

	        try {
		        	if( listExamine!=null && listExamine.size() >0){
		        		size = Integer.parseInt(String.valueOf(listExamine.get(0).get("COUNT")));//总条数
		        		rowCount = Integer.parseInt(String.valueOf(listExamine.get(0).get("COUNT")));//总条数
		    		}
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
	       //转成Json格式
	       String msg = "{\"count\":"+size+",\"pageCount\":"+rowCount+",\"CurrentPage\":"+pageIndex+",\"list\":" + jacksonUtil.toJson(listExamine) + "}";
	       return msg;
	}
	@RequestMapping("/handle")
	@ResponseBody
	public String handle(HttpServletRequest request,@RequestParam("id") String id) {
		String msg = ycddServer.handle(id);
	    return msg;
	}

	@RequestMapping("ycdddc")
	@ResponseBody
	public String ycdddc(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("postData") String postData) throws IOException{
		String power_option = String.valueOf(request.getSession().getAttribute("power_option"));
		postData = java.net.URLDecoder.decode(postData, "UTF-8");
		String a[] = {"商品编号","车牌号码","入库时间","订单金额(元)","订单状态","用户昵称","联系方式","异常说明","订单处理"};//导出列明
		String b[] = {"COMMODITY_ID","VEHICLE","UPTIME","TOTAL_FEE","COMMODITYSTATUS","NICKNAME","LXFS","MEMO","HANDLESTATUS"};//导出map中的key
		String gzb = "异常订单管理";//导出sheet名和导出的文件名
		List<Map<String, Object>> list = ycddServer.dddc(postData);
		DownloadAct.download(request,response,a,b,gzb,list);
		return null;
	}

}
