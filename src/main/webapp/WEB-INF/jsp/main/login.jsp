<%@page contentType="text/html;charset=utf-8" language="java"%>
<%@include file="../common/common.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>  
<head>  
<meta http-equiv='Expires' content='-10'>  
<meta http-equiv='Pragma'  content='No-cache'> 
<script type="text/javascript">
	var CTX = "<%= request.getContextPath()%>";
</script> 
<script type="text/javascript" 
				src="<%=request.getContextPath()%>/resources/js/login/login.js"></script>
<title>BI数据分发平台</title>  
<style type="text/css">  
.user{ background:url(<%=request.getContextPath()%>/resources/images/login/user.gif) no-repeat 1px 2px; }   
.key{ background:url(<%=request.getContextPath()%>/resources/images/login/logout.gif) no-repeat 1px 2px; }   
.user,.key,.key{background-color:#FFFFFF;padding-left:20px;font-weight:bold;color:#000033;}
.login{
	background-image:url(<%=request.getContextPath()%>/resources/images/login/logout.gif) !important;
}
</style>  
</head>  
<body background="<%=request.getContextPath()%>/resources/images/login/desk.jpg">  

</body>  
</html>