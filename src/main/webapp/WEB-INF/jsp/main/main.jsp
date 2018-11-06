<%@page contentType="text/html;charset=utf-8" language="java"%>
<%@include file="../common/common.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<script type="text/javascript">
  	var CTX = "<%= request.getContextPath()%>";
  	</script>
    <link rel="stylesheet" type="text/css" 
    		href="<%=request.getContextPath()%>/resources/style/login/main_css.css" />
    <link rel="stylesheet" type="text/css" 
    		href="<%=request.getContextPath()%>/resources/style/common/grid_button.css" />
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/login/main.js"></script>
    <title>BI数据分发平台</title>
  <script type="text/javascript">
  		var account = '${user.id}';//登录账号
  		function showTime(){//显示走动的时间
				// if (!document.layers&&!document.all) return;
				now = new Date();
				hours = now.getHours();
				minutes = now.getMinutes();
				seconds = now.getSeconds();
				var month = (now.getMonth()+1)+"";
				if(month.length>1)
					timeValue = now.getYear()+"-"+month+"-"+now.getDate()+"&nbsp;";
				else
					timeValue = now.getYear()+"-"+("0"+month)+"-"+now.getDate()+"&nbsp;";
				timeValue += hours + ":";
				timeValue += ((minutes < 10) ? "0" : "") + minutes + ":";
				timeValue += ((seconds < 10) ? "0" : "") + seconds + "";
				document.all.clock.innerHTML = timeValue;
				setTimeout("showTime()",100);
			}
  </script>
  </head>
  <body id="mainbody" onload="showTime();">
	  	 <div style="position:absolute;left:0px;top:0px;width:100%;height:58px;background:url('<%=request.getContextPath()%>/resources/images/top/it_bg.jpg');z-index:0;"></div>
		 <div style="position:absolute;left:0px;top:0px;width:100%;height:58px;background:url('<%=request.getContextPath()%>/resources/images/top/it_top.jpg') no-repeat;z-index:1;"></div>
	  	 <div style="position:absolute;top:3px;right:3px;z-index:1;">
				[<a href="#" onclick="alert('建设ing')">帮助</a>]
				[<a href="#" onclick="changepwd()">设置密码</a>]
				[<a href="#" onclick="logout();">注销</a>]
		 </div>
		 <div style="position:absolute;top:20px;right:3px;z-index:1;"><b>系统时间</b>:<span id="clock"></span></div>
		 <div style="position:absolute;top:40px;right:3px;z-index:1;">
			<b>登录用户</b>:<c:out value="${user.userName}" />|<b> 部门</b>:<c:out value="${user.userDept}" />|<b>登录时间</b>:<c:out value="${loginTime }" /> 
		 </div>
  </body>
</html>
