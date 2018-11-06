package com.bi.dds.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


public class IdentityFilter implements Filter{
	/**
	 * 日志对象
	 */
	private static final Logger logger = Logger.getLogger(IdentityFilter.class);

    private static final String[] nochecks = {"login.itm","loginctrl/init.itm","getImage.itm", "logout.itm", "menu.itm", "down.itm" , "count.do"};
    
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException,
        ServletException {
        try{
            if (req instanceof HttpServletRequest &&
                res instanceof HttpServletResponse){
                HttpServletRequest request = (HttpServletRequest) req;
                HttpServletResponse response = (HttpServletResponse) res;
                HttpSession session = request.getSession(true);

                if (session.getAttribute("loginId") == null){
                    String uri = request.getRequestURI();
                    boolean bcheck = true;
                    for (int i = 0; i < nochecks.length; i++){
                        if (uri.indexOf(nochecks[i]) >= 0){
                            bcheck = false;
                            break;
                        }
                    }
                    logger.debug("uri:"+uri);
                    logger.debug("bcheck:"+bcheck);
                    logger.debug("filtering: " + uri + "?" + request.getQueryString()+",bcheck="+bcheck);
                    if (bcheck){
                    	response.setContentType("text/html;charset=utf-8");
                    	//由于前台可能为ajax调用
                    	PrintWriter pw=response.getWriter();
                    	String cpath = request.getContextPath();
                    	pw.println("<script>top.location.href='"+cpath+"/loginctrl/init.itm';</script>");
                    	pw.flush();
                        //String cpath = request.getContextPath();
                        //response.sendRedirect(cpath + "/login.htm");
                        return;
                    }
                }
            }
            chain.doFilter(req, res);
        }
        catch (Exception e)
        {
        	logger.error(e);
            e.printStackTrace();
        }
    }

    protected FilterConfig filterConfig;

    public void init(FilterConfig filterconfig)
    {
        filterConfig = filterconfig;
    }

    public FilterConfig getFilterConfig()
    {
        return filterConfig;
    }
 
    public void setFilterConfig(FilterConfig filterconfig)
    {
        filterConfig = filterconfig;
    }

    public void destroy()
    {
    	logger.debug("Filter Event: Destroy");
        return;
    }
}
