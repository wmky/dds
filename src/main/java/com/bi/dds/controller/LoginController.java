package com.bi.dds.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.bi.dds.model.BiUser;
import com.bi.dds.service.SystemItf;
import com.bi.dds.util.ProcessException;


@Controller
@RequestMapping(value="loginctrl")
public class LoginController{

	private static final Logger logger = Logger.getLogger(LoginController.class);

	//系统服务接口
	@Autowired
	private SystemItf systemitf;
	
	
	/**
	 * 系统初始化登录页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/init")
	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response){
		logger.debug("初始化登录页面");
		return new ModelAndView("main/login");
	}
	
	/**
	 * 获取验证码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="getImage")
	public ModelAndView getImage(HttpServletRequest request,
			HttpServletResponse response){
			logger.debug("开始获取验证码图片");
			return new ModelAndView("main/image");
	}
	
	/**
	 * 登录控制类
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 */
	@RequestMapping(value="/login")
	public ModelAndView login(HttpServletRequest request,
									HttpServletResponse response)throws ProcessException{
		logger.debug("用户开始登录");
		
		//返回结果
		String result = "";
		
		try{
			String userTag = request.getParameter("userTag");	//用户账号
			String passWord = request.getParameter("passWord");	//用户密码
			String rand = request.getParameter("rand");			//图片验证码
			String screenw = request.getParameter("screenw");	//屏幕宽
			String screenh = request.getParameter("screenh");	//屏幕高
			String ip = request.getRemoteAddr();				//获取ip地址
			
			logger.debug("userTag:"+userTag);
			logger.debug("passWord:"+passWord);
			logger.debug("ip:"+ip);
			logger.debug("rand:"+rand);
			logger.debug("imagecode:"+request.getSession().getAttribute("rand"));
			logger.debug("screenw:"+screenw);
			logger.debug("screenh:"+screenh);
			
			//从session中获取验证码值
			String imagecode = (String)request.getSession().getAttribute("rand");
			HttpSession session = request.getSession(true);
			
			//登录结果
			/*	lr.getErrorCode()
			 *  > 0 表示成功
			 *  0：用户不存在或密码错误
			 *  -1：用户被锁定, -2：用户ip受限制, -3：用户时间受限制, -4:密码是初始密码
			 */
			//LoginResult lr = systemitf.login(userTag, passWord, ip);
			
			//int code = systemitf.login(userTag, passWord);
			int code = 1;
			
			logger.debug("code:"+code);
			
			//登录成功
			if(code>0){
				if(rand.equals(imagecode)){
					BiUser user = systemitf.getUserByNameAndPass(userTag, passWord);
					logger.debug("登录成功！");
					session.setAttribute("screenw", screenw);
					session.setAttribute("screenh", screenh);
	    			session.setAttribute("loginId", user.getId());//user的id
	    			//session.setAttribute("loginResult", lr);
	    			session.setAttribute("user", user);//user对象
	    			session.setAttribute("dept", user.getUserDept());
	    			//session.setAttribute("role", lr.getRoles());
	    			//session.setAttribute("loginTime", DTime.getDateTimeString(DTime.TIME_SEC_FORMAT));
	    			result = code + "&登录成功";
				}
				else{
					result = "2&验证码错误,请重新输入";
				}
			}
			
			//登录失败
			else{
				
				if(code == 0){
					result = code+"&用户不存在或密码错误";
				}
				
			    else if(code == -1){
					result = code+"&用户被锁定";
				}
				
				else if(code == -2){
					result = code+"&用户ip受限制";
				}
				
				else if(code == -3){
					result = code+"&用户时间受限制";
				}
				
				else{
					result = code+"&密码是初始密码,请修改后再登录系统";
				}
				
				logger.debug("登录失败："+result.split("&")[1]);
			}
			
			
			logger.info("result:"+result);
			PrintWriter out = null;
			response.setContentType("text/text;charset=UTF-8");// 否则ajax接收中文乱码
			out = response.getWriter();
			out.print(result);
			
		}catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 修改密码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/changePwd")
	public ModelAndView changePwd(HttpServletRequest request,
			HttpServletResponse response)throws ProcessException{
		int rev = -1;
		try{
			String account = request.getParameter("account");		//用户账号
			String oldpwd = request.getParameter("oldpwd");			//旧密码
			String newpwd = request.getParameter("newpwd");			//新密码
			//rev = UserHelper.changePass(account, oldpwd, newpwd);	//修改密码

			logger.info("rev:"+rev);
			PrintWriter out = null;
			response.setContentType("text/text;charset=UTF-8");// 否则ajax接收中文乱码
			out = response.getWriter();
			out.print(rev);
			
		}catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
}
