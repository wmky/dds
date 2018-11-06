package com.bi.dds.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

@Controller
@RequestMapping(value="logoutctrl")
public class LogoutController{
	/**
	 * 日志对象
	 */
	private static final Logger logger = Logger.getLogger(LogoutController.class);

	/**
	 * 系统注销
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value="/logout")
	public ModelAndView logout(HttpServletRequest request,
			HttpServletResponse response) {
		
		String result = "";	//返回结果
		PrintWriter out = null;
		
		try {
			HttpSession session = request.getSession(true);
			String loginId = (String) session.getAttribute("loginId");
			//User user = (User)session.getAttribute("user");
			if(loginId!=null){
				//UserHelper.logout(loginId);
				session.invalidate();//将调用SessionListen.sessionDestroyed
			}
			result = "1&感谢使用系统";
			
		} catch (Exception e) {
			logger.error(e);
			result = "2&未完成操作，请联系管理员";
		}finally{
			try{
				response.setContentType("text/text;charset=UTF-8");// 否则ajax接收中文乱码
				out = response.getWriter();
				out.print(result);
			}catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		return null;
	}
}
