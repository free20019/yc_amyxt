package mvc.controllers;


import helper.JacksonUtil;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mvc.service.azServer;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping(value = "/install")
public class azAction {
	private azServer azServer;
	
	public azServer getAzServer() {
		return azServer;
	}
	@Autowired
	public void setAzServer(azServer azServer) {
		this.azServer = azServer;
	}
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	@RequestMapping("/findaz")
	@ResponseBody
	public String findaz(HttpServletRequest request,@RequestParam("postData") String postData) {
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String page = String.valueOf(paramMap.get("page"));
		 	int pageIndex = Integer.parseInt(page);//当前页
	        int pageSize =10;//设置每页要展示的数据数量(根据项目需求灵活设置)
	        int rowCount = 0 ;
	        List<Map<String, Object>> listExamine=azServer.findaz(postData);;//获取总数据量
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
	        String showList=azServer.findazb(pageIndex,pageSize,postData);//根据pageIndex和pageSize获取需要展示的该页数据
	       //转成Json格式
	       String msg = "{\"pageCount\":"+rowCount+",\"CurrentPage\":"+pageIndex+",\"list\":" + showList + "}";
	    return msg;
	}
	@RequestMapping("/findazzd")
	@ResponseBody
	public String findazzd(@RequestParam("terminal") String terminal) {
		String msg = azServer.findazzd(terminal);
	    return msg;
	}
	@RequestMapping("/addaz")
	@ResponseBody
	public String addaz(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = azServer.addaz(postData);
	    return msg;
	}
	@RequestMapping("/editaz")
	@ResponseBody
	public String editaz(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = azServer.editaz(postData);
	    return msg;
	}
	@RequestMapping("/delaz")
	@ResponseBody
	public String delaz(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = azServer.delaz(postData);
	    return msg;
	}

//	@RequestMapping("findpowerexcle")
//	@ResponseBody
//	public String findpowerexcle(HttpServletRequest request,
//			HttpServletResponse response,@RequestParam("info") String info) throws IOException{
//		String a[] = {"权限名","权限"};//导出列明
//		String b[] = {"POWER_NAME","POWER_INFO"};//导出map中的key
//		String gzb = "权限管理";//导出sheet名和导出的文件名
//		String msg = azServer.findpower(info);
//		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
//		DownloadAct.download(request,response,a,b,gzb,list);
//		return null;
//	}

}
