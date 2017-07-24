<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="ou" tagdir="/WEB-INF/tags" %>
<html>
<head>
<title>首页</title>
</head>
<body>
	<shiro:guest>
		欢迎游客访问：<a href="${pageContext.request.contextPath}/login.jsp">登录</a><br>
	</shiro:guest>
	
	<shiro:user>
		欢迎[<shiro:principal/>]登录， <a href="${pageContext.request.contextPath}/logout">退出</a><br>
	</shiro:user>
	
	<shiro:authenticated>
		用户[<shiro:principal/>]已身份验证通过<br>
	</shiro:authenticated>
	
	<shiro:notAuthenticated>
		未身份验证（包括记住我）<br>
	</shiro:notAuthenticated>
	
	<shiro:hasRole name="admin">
		用户[<shiro:principal/>]拥有角色admin<br/>
	</shiro:hasRole>
	
	<shiro:hasAnyRoles name="admin,user">
		 用户[<shiro:principal/>]拥有角色admin或user<br/>
	</shiro:hasAnyRoles>
	
	<shiro:lacksRole name="abc">
		用户[<shiro:principal/>]没有角色 abc<br/>
	</shiro:lacksRole>
	
	<shiro:hasPermission name="user:create">
		用户[<shiro:principal/>]拥有权限 user:create<br/>
	</shiro:hasPermission>
	
	<shiro:lacksPermission name="org:create">
		用户[<shiro:principal/>]没有权限 org:create<br/>
	</shiro:lacksPermission>
	
	<ou:hasAllRoles name="admin,user">
		用户[<shiro:principal/>]拥有角色admin和user<br/>
	</ou:hasAllRoles>
	
	<ou:hasAllPermissions name="user:create,user:update">
		用户[<shiro:principal/>]拥有权限user:create和user:update<br/>
	</ou:hasAllPermissions>
	
	<ou:hasAnyPermissions name="user:create,abc:update">
		用户[<shiro:principal/>]拥有权限user:create或abc:update<br/>
	</ou:hasAnyPermissions>
</html>