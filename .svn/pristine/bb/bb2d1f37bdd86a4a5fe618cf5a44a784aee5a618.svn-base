package mvc.controllers;


import helper.DownloadAct;
import helper.JacksonUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.service.zdServer;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping(value = "/terminal")
public class zdAction {
	private zdServer zdServer;
	
	public zdServer getZdServer() {
		return zdServer;
	}
	@Autowired
	public void setZdServer(zdServer zdServer) {
		this.zdServer = zdServer;
	}
	private JacksonUtil jacksonUtil = JacksonUtil.buildNormalBinder();
	@RequestMapping("/findzd")
	@ResponseBody
	public String findzd(HttpServletRequest request,@RequestParam("postData") String postData) {
		Map<String, Object> paramMap = jacksonUtil.toObject(postData,new TypeReference<Map<String, Object>>() {});
		String info = String.valueOf(paramMap.get("info"));
		String page = String.valueOf(paramMap.get("page"));
		 	int pageIndex = Integer.parseInt(page);//当前页
	        int pageSize =15;//设置每页要展示的数据数量(根据项目需求灵活设置)
	        int rowCount = 0 ;
	        List<Map<String, Object>> listExamine=zdServer.findzd(postData);;//获取总数据量
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
	        String showList=zdServer.findzdb(pageIndex,pageSize,postData);//根据pageIndex和pageSize获取需要展示的该页数据
	       //转成Json格式
	       String msg = "{\"pageCount\":"+rowCount+",\"CurrentPage\":"+pageIndex+",\"list\":" + showList + "}";
	    return msg;
	}
	@RequestMapping("/addzd")
	@ResponseBody
	public String addzd(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = zdServer.addzd(postData);
	    return msg;
	}
	@RequestMapping("/editzd")
	@ResponseBody
	public String editzd(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = zdServer.editzd(postData);
	    return msg;
	}
	@RequestMapping("/delzd")
	@ResponseBody
	public String delzd(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = zdServer.delzd(postData);
	    return msg;
	}
	@RequestMapping("/addAll")
	@ResponseBody
	public String addAll(HttpServletRequest request,@RequestParam("postData") String postData) {
		String msg = zdServer.addAll(postData);
	    return msg;
	}
//	@RequestMapping("findpowerexcle")
//	@ResponseBody
//	public String findpowerexcle(HttpServletRequest request,
//			HttpServletResponse response,@RequestParam("info") String info) throws IOException{
//		String a[] = {"权限名","权限"};//导出列明
//		String b[] = {"POWER_NAME","POWER_INFO"};//导出map中的key
//		String gzb = "权限管理";//导出sheet名和导出的文件名
//		String msg = zdServer.findpower(info);
//		List<Map<String, Object>>list = DownloadAct.parseJSON2List2(msg);
//		DownloadAct.download(request,response,a,b,gzb,list);
//		return null;
//	}

}
