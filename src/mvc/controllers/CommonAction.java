package mvc.controllers;


import helper.DownloadAct;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.service.CommonServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 公共基础类，通用方法
 * 公司、分公司、车辆等下拉框
 */
@Controller
@RequestMapping(value = "/common")
public class CommonAction {
	private CommonServer commonService;

	public CommonServer getCommonServer() {
		return commonService;
	}

	@Autowired
	public void setCommonServer(CommonServer commonService) {
		this.commonService = commonService;
	}

	@RequestMapping(value = "/ssjk")
	@ResponseBody
	public String ssjk(HttpServletRequest request,@RequestParam("postData") String postData
			) {
		String msg = commonService.ssjk(postData);
		return msg;
	}
	//渠道
	@RequestMapping("/findqd")
	@ResponseBody
	public String findqd(){
		String msg = commonService.findqd();
		return msg;
	}
	//企业
	@RequestMapping("/findqy")
	@ResponseBody
	public String findqy(){
		String msg = commonService.findqy();
		return msg;
	}
	//车主
	@RequestMapping("/findclcz")
	@ResponseBody
	public String findclcz(){
		String msg = commonService.findclcz();
		return msg;
	}
	//车辆
		@RequestMapping("/findcl")
		@ResponseBody
		public String findcl(){
			String msg = commonService.findcl();
			return msg;
		}
	//类型
		@RequestMapping("/findlx")
		@ResponseBody
		public String findlx(@RequestParam("vhic") String vhic){
			String msg = commonService.findlx(vhic);
			return msg;
		}
	//车主,车牌,终端编号,驾驶员
	@RequestMapping("/findchoose")
	@ResponseBody
	public String findchoose(@RequestParam("choose") String choose){
		String msg = commonService.findchoose(choose);
		return msg;
	}
	//终端
	@RequestMapping("/findzd")
	@ResponseBody
	public String findzd(){
		String msg = commonService.findzd();
		return msg;
	}
	//终端型号和类型
		@RequestMapping("/findmodel")
		@ResponseBody
		public String findmodel(){
			String msg = commonService.findmodel();
			return msg;
		}
	//终端类型
		@RequestMapping("/findtype")
		@ResponseBody
		public String findtype(@RequestParam("id") String id){
			String msg = commonService.findtype(id);
			return msg;
		}
	//车主,渠道,公司,驾驶员
	@RequestMapping("/finduserchoose")
	@ResponseBody
	public String finduserchoose(@RequestParam("choose") String choose){
		String msg = commonService.finduserchoose(choose);
		return msg;
	}
	//车辆类型
		@RequestMapping("/findvehicletype")
		@ResponseBody
		public String findvehicletype(){
			String msg = commonService.findvehicletype();
			return msg;
		}
	@RequestMapping(value = "/query_pic")
	@ResponseBody
	public synchronized String query_pic(HttpServletRequest request,HttpServletResponse response,@RequestParam("type") String type,@RequestParam("rr_id") String rr_id) {
		String msg = "";
		commonService.query_pic(request, response, type, rr_id);
	    return msg;
	}
}
