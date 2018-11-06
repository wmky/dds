<%@page contentType="text/html;charset=utf-8" language="java"%>
<%@include file="../common/common.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <script type="text/javascript" 
				src="<%=request.getContextPath()%>/resources/js/test/test.js"></script>
	<script type="text/javascript" 
				src="<%=request.getContextPath()%>/resources/js/test/SearchField.js"></script>
	<script type="text/javascript" 
				src="<%=request.getContextPath()%>/resources/js/test/ProgressBarPager.js"></script>
	<script type="text/javascript" 
				src="<%=request.getContextPath()%>/resources/js/test/PanelResizer.js"></script>
	<link rel="stylesheet" type="text/css" 
    		href="<%=request.getContextPath()%>/resources/style/common/grid_button.css" />
  </head>
  
  <body>
    <div style="height:100%" id='perfinfodiv'></div>
  </body>
</html>
