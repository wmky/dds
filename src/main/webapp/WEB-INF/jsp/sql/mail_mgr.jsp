<%@page contentType="text/html;charset=utf-8" language="java"%>
<%@include file="../common/common.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   	<script type="text/javascript">
  		var CTX = "<%= request.getContextPath()%>";
  	</script>
  	<script type="text/javascript" 
				src="<%=request.getContextPath()%>/resources/js/sql/mail_mgr.js"></script>
	<script type="text/javascript" 
				src="<%=request.getContextPath()%>/resources/js/common/validate.js"></script>
	<script type="text/javascript" 
				src="<%=request.getContextPath()%>/resources/js/common/SearchField.js"></script>
	<script type="text/javascript" 
				src="<%=request.getContextPath()%>/resources/js/common/ProgressBarPager.js"></script>
	<script type="text/javascript" 
				src="<%=request.getContextPath()%>/resources/js/common/PanelResizer.js"></script>
   <link rel="stylesheet" type="text/css" 
    		href="<%=request.getContextPath()%>/resources/style/common/grid_button.css" />
  	<title>数据质量监控平台</title>
  </head>
  <body>
    <div style="height:100%" id='searchdiv'></div>
     <input type="hidden" value="${user.userName}" id="hideUserName">
  </body>
  <script type="text/javascript">
  	var width = document.getElementById('searchdiv').parentNode.clientWidth;
  	 document.getElementById('searchdiv').style.width = width + "px";
  </script>
</html>
