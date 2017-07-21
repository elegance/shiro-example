<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<html>
<head>
    <title>登录</title>
    <style>.error{ color:red; }</style>
</head>
<body>
	欢迎<shiro:principal />登录成功！<a href="${pageContext.request.contextPath}/logout">退出</a>
	${subject.principal }
</body>
</html>