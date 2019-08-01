package mvc.controllers;


import helper.DownloadAct;
import helper.JacksonUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;






import mvc.service.czServer;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping(value = "/owner")
public class czAction {
	private czServer czServer;
	
	public czServer getCzServer() {
		return czServer;
	}
	@Autowired
	public void setCzServer(czServer czServer) {
		this.czServer = czServer;
	}
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	@RequestMapping("/findcz")
	@ResponseBody
	public String findcz(HttpServletRequest request,@RequestParam("postData") String postData) {
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String page = String.valueOf(paramMap.get("page"));
		 	int pageIndex = Integer.parseInt(page);//当前页
	        int pageSize =15;//设置每页要展示的数据数量(根据项目需求灵活设置)
	        int rowCount = 0 ;
	        List<Map<String, Object>> listExamine=czServer.findcz(postData);;//获取总数据量
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
	        String showList=czServer.findczb(pageIndex,pageSize,postData);//根据pageIndex和pageSize获取需要展示的该页数据
	       //转成Json格式
	       String msg = "{\"pageCount\":"+rowCount+",\"CurrentPage\":"+pageIndex+",\"list\":" + showList + "}";
	    return msg;
	}
	private int parseInteger(String page2) {
		// TODO Auto-generated method stub
		return 0;
	}
	@RequestMapping("/addcz")
	@ResponseBody
	public String addcz(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = czServer.addcz(postData);
	    return msg;
	}
	@RequestMapping("/editowner")
	@ResponseBody
	public String editowner(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = czServer.editowner(postData);
	    return msg;
	}
	@RequestMapping("/delcz")
	@ResponseBody
	public String delcz(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = czServer.delcz(postData);
	    return msg;
	}

//	@RequestMapping("findpowerexcle")
//	@ResponseBody
//	public String findpowerexcle(HttpServletRequest request,
//			HttpServletResponse response,@RequestParam("info") String info) throws IOException{
//		String a[] = {"权限名","权限"};//导出列明
//		String b[] = {"POWER_NAME","POWER_INFO"};//导出map中的key
//		String gzb = "权限管理";//导出sheet名和导出的文件名
//		String msg = czServer.findpower(info);
//		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
//		DownloadAct.download(request,response,a,b,gzb,list);
//		return null;
//	}

}
