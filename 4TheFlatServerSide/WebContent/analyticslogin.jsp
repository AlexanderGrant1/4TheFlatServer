<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css" type="text/css" /> <!-- stylesheet -->
<title>4TheFlat Analytics Login</title>
<script>
function validateLogin()
{
	var username=document.forms["loginForm"]["username"].value;
	var password=document.forms["loginForm"]["userPassword"].value;
	if ( username == "")
	{
		alert("You didn't enter a username.");
		return false;
	}
	if(email.length < 4 && email != "")
	{
		alert("Your email address is too short, check it's valid and try again.");	
		return false;
	}
	if (!emailRegex.test(email))
	{
		alert("You entered an invalid email address.");
		return false;
	}
	if(password.length < 6 || password.length > 18 && password != "")
	{
		alert("You entered your password incorrectly.");
		return false;
	}
	if(password == "")
	{
		alert("You didn't enter a password.");
		return false;
	}

	return true;
}
</script>
</head>
<body>
<% if(request.getSession().getAttribute("activeUser") != null)
	{
		response.sendRedirect("groupanalytics");
	}
%>
<div class="smallcontainer">
    <h1>4TheFlat Analytics Login</h1>
    <form name="loginForm" method="post" action="analyticslogin" onsubmit="return loginFormValidation()">
      <p><input type="text" name="username" value="" placeholder="Username"></p>
      <p><input type="password" name="userPassword" value="" placeholder="Password"></p>
      <p class="submit"><input type="submit" name="loginButton" value="Login"></p>
    </form>
    <c:if test="${errorMessage != null }">
    <p><center><font color="#ff0000" >Invalid email address and/or password. </font></center></p>
    </c:if>
</div>
 
</body>

</html>