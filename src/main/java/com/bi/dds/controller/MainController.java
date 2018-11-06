package com.bi.dds.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.bi.dds.service.SystemItf;
import com.bi.dds.util.ProcessException;


@Controller
@RequestMapping(value="mainctrl")
public class MainController{

	private static final Logger logger = Logger.getLogger(MainController.class);

	//系统服务接口
	@Autowired
	private SystemItf systemitf;
	
	/**
	 * 主框架初始化
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 */
	@RequestMapping(value="/init")
	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response)throws ProcessException{
		
		logger.debug("主框架初始化");
		
		request.setAttribute("user", request.getSession().getAttribute("user"));
		request.setAttribute("dept", request.getSession().getAttribute("dept"));
		request.setAttribute("loginTime", request.getSession().getAttribute("loginTime"));
		
		return new ModelAndView("/main/main");
	}
	
	/**
	 * 根据登录用户的权限获取菜单
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 */
	/*
	public ModelAndView getFirstMenuTree(HttpServletRequest request,
			HttpServletResponse response)throws ProcessException{
		
		String result = "";//返回ajax结果
		try{
			
			HttpSession session = request.getSession(true);
			
			
			//取出一级菜单
			//菜单值以+号连接 id+name
			//模块菜单之间以@连接	模块菜单@模块菜单
			//子菜单与模块菜单以-连接  模块菜单-子菜单
			//例：  模块菜单1.id+模块菜单1.name-模块菜单1子菜单1.id+模块菜单1子菜单1.name@模块菜单2.id+模块菜单2.name-模块菜单2子菜单1.id+模块菜单2子菜单1.name
			List fml = systemitf.getFirstMenu();
			logger.debug("list-size:"+fml.size());
			
			for(int i=0;i<fml.size();i++){
				Menu m = (Menu)fml.get(i);
				result = result + m.getId() + "+" + m.getName() + "-";//"-"模块菜单连接子菜单
				
				//判断是否有子菜单
				if(!m.getChild().isEmpty()){
					Set it = (Set)m.getChild();
					if(it!=null){
						Iterator child = it.iterator();
						while(child.hasNext()){
							Menu cm =(Menu)child.next();
							result = result + cm.getId() + "+" +cm.getName();
						}
					}
				}
				result = result + "@";//"@"模块菜单连接模块菜单
				
			}
			if(result.length()>0){
				result = result.substring(0,result.length()-1);//去掉最后一个多出来的 "@"
			}
			logger.debug("result:"+result);
			PrintWriter out = null;
			response.setContentType("text/text;charset=UTF-8");// 否则ajax接收中文乱码
			out = response.getWriter();
			out.print(result);
		
		}catch (Exception e) {
			logger.debug(e);
			e.printStackTrace();
		}
		
		return null;
	}
	*/
	
	/**
	 * 获取子菜单
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 */
	/*
	public ModelAndView getMenu(HttpServletRequest request,
			HttpServletResponse response)throws ProcessException{
		
		String result = "[";
		try{
			String pid = request.getParameter("pid");
			HttpSession session = request.getSession(true);
			
			//从session中获取LoginResult
			LoginResult lr = (LoginResult)session.getAttribute("loginResult");
			logger.debug("pid:"+pid);
			List list = systemitf.getChildMenu(pid);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Menu m =(Menu)list.get(i);
					result = result + 
					 		 "{id:'"+m.getId()+"',text:'"+m.getName()+"',href:'"+m.getUrl()+"',expanded:true,leaf:true";
					
					//判断是否有子节点
					Set child = m.getChild();
					if(child!=null){
						Iterator it = child.iterator();
						result = result + ",children:[";
						while(it.hasNext()){
							Menu c = (Menu)it.next();
							result = result +
									 "{id:'"+c.getId()+"',text:'"+c.getName()+"',href:'"+c.getUrl()+"',leaf:true},";
						}
						//去掉最后子节点的","
						if(!child.isEmpty()){
							result = result.substring(0,result.length()-1);
						}
						result = result + "]";
					}
					
					result = result + "},";
				}
				//去掉最后一个节点的","
				result = result.substring(0,result.length()-1);
			}
			result = result + "]";
			
			logger.debug("result:"+result);
			PrintWriter out = null;
			response.setContentType("text/text;charset=UTF-8");// 否则ajax接收中文乱码
			out = response.getWriter();
			out.print(result);
		}catch (Exception e) {
			logger.debug(e);
			e.printStackTrace();
		}
		return null;
	}
	*/
	
	public ModelAndView gopage(HttpServletRequest request,
			HttpServletResponse response){
		
		String url = request.getParameter("url");
		logger.debug("url:"+url);
		return new ModelAndView(url);
	}
	
	/**
	 * 测试
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 */
	/*
	public ModelAndView test(HttpServletRequest request,
			HttpServletResponse response)throws ProcessException{
		
		String result = "{'totalProperty':";
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String para = request.getParameter("para");
		
		logger.debug("para:"+para);
		logger.debug("start:"+start);
		logger.debug("limit:"+limit);
		try{
			
			int totalcount = systemitf.getCMListSize(para);
			result += totalcount+",'root':[";
			
			List list = systemitf.getPagingCMList(para, start, limit);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Menu m =(Menu)list.get(i);
					result += "{id:'"+m.getId()+"',";
					result += "number:'"+(Integer.parseInt(start)+i+1)+"',";
					result += "name:'"+m.getName()+"',";
					result += "type:'"+m.getType()+"',";
					result += "pname:'"+m.getParent().getName()+"',";
					String url = m.getUrl()==null?"":m.getUrl();
					result += "url:'"+url+"'},";
				}
				result = result.substring(0,result.length()-1);
			}
			result +="]}";
			logger.debug("test===="+result);
			PrintWriter out = null;
			response.setContentType("text/text;charset=UTF-8");// 否则ajax接收中文乱码
			out = response.getWriter();
			out.print(result);
		}catch (Exception e) {
			logger.debug(e);
			e.printStackTrace();
		}
		return null;
	}
	*/
	
	/**
	 * 获取非叶子菜单
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 */
	/*
	public ModelAndView getUnleafMenu(HttpServletRequest request,
			HttpServletResponse response)throws ProcessException{
		
		String result = "{'root':[";
		try{
			
			List list = systemitf.getUnleafMenu();
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Menu m =(Menu)list.get(i);
					result += "{id:'"+m.getId()+"',";
					result += "name:'"+m.getName()+"'},";
				}
				result = result.substring(0,result.length()-1);
			}
			result += "]}";
			logger.debug("test===="+result);
			PrintWriter out = null;
			response.setContentType("text/text;charset=UTF-8");// 否则ajax接收中文乱码
			out = response.getWriter();
			out.print(result);
			
		}catch (Exception e) {
			logger.debug(e);
			e.printStackTrace();
		}
		return null;
	}
	*/
	
	/**
	 * 新增菜单
	 * @param request
	 * @param response
	 * @return
	 * @throws ProcessException
	 */
	public ModelAndView addMenu(HttpServletRequest request,
			HttpServletResponse response)throws ProcessException{
		String result = "success";
		try{
			String name = request.getParameter("name");
			String pid = request.getParameter("parentmenu");
			String type = request.getParameter("type");
			String url = request.getParameter("url");
			
			logger.debug("name:"+name);
			logger.debug("pid:"+pid);
			logger.debug("type:"+type);
			logger.debug("url:"+url);
			
			PrintWriter out = null;
			response.setContentType("text/text;charset=UTF-8");// 否则ajax接收中文乱码
			out = response.getWriter();
			out.print(result);
			
		}catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			result = "faild";
		}
		return null;
	}
}
