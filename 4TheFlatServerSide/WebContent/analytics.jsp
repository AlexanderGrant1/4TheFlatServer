<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css" type="text/css" /> <!-- stylesheet -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3/jquery.min.js" type="text/javascript"></script><!-- jquery lib -->
 <script src="//code.jquery.com/jquery-1.9.1.js"></script>
  <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Group Analytics</title>
<div id="titleImage"><img src="${pageContext.request.contextPath}/img/title.jpg"></div>
<script>
$(document).ready(function(){
	
updateAnalytics();
});

function updateAnalytics()
{
    	$.ajax({
    		cache: false,//internet explorer support
    	    type:'POST',
    	    url: "${pageContext.request.contextPath}/analytics/${activeUser.getGroupID()}",//no argument required here
    		    success: 
    		        function(msg){
    		            showNewAnalytics();
    		        }                  
    	    });
    	setTimeout(updateAnalytics, 60000);
}

function showNewAnalytics()
{
    	$.ajax({
    		cache: false,//internet explorer support
    	    type:'GET',
    		    success: 
    		        function(msg){
    		    		$("#largecontainer").html(msg);
    		        }                  
    	    });
}
</script>
</head>
<body>
<div id="largecontainer">
<h1>Group Analytics</h1>
<h2><center>Basic Group Analytics</center></h2>
<p><b>Most Active Shopper: </b> ${groupAnalytics.bestShopper}.</p>
<p><b>Most Popular Shop: </b> ${groupAnalytics.bestStore }.</p>
<p><b>Average Shop Cost: </b> ${averageShopPrice } pence.</p>
<p><b>Average Time Between Shops: </b> ${averageTimeBetween } days.</p>
<h2><center>Average Price Paid Per Product</center></h2>
<center>
		<div class="CSS_Table_Example" style="width:600px;">
			<table >
				<tr> 
					<td>Products</td>
					<td>Average Cost</td>
				</tr>
				<c:forEach items="${groupProducts}" var="individualProduct">
				<tr> 
					<td><b>${individualProduct.product }</b></td>
					<td>${individualProduct.avgCost } pence</td>
				</tr>
				</c:forEach>
			</table>
		</div>
		<h2><center>Most Frequently Visited Shops</center></h2>
				<div class="CSS_Table_Example" style="width:600px;">
			<table >
				<tr> 
					<td>Shop</td>
					<td>Number of Visits</td>
				</tr>
				<c:forEach items="${shopAnalytics.entrySet()}" var="individualItem">
				<tr> 
					<td><b>${individualItem.key }</b></td>
					<td>${individualItem.value }</td>
				</tr>
				</c:forEach>
			</table>
		</div>
				<h2><center>Most Frequent Shoppers</center></h2>
				<div class="CSS_Table_Example" style="width:600px;">
			<table >
				<tr> 
					<td>Flat Member</td>
					<td>Number of Shops</td>
				</tr>
				<c:forEach items="${userShopAnalytics.entrySet()}" var="individualItem">
				<tr> 
					<td><b>${individualItem.key }</b></td>
					<td>${individualItem.value }</td>
				</tr>
				</c:forEach>
			</table>
		</div>
						<h2><center>Group Member Statistics</center></h2>
				<div class="CSS_Table_Example" style="width:600px;">
			<table >
				<tr> 
					<td>Flat Member</td>
					<td>Average Shop Cost</td>
					<td>Favourite Shop</td>
					<td>Favourite Product</td>
				</tr>
				<c:forEach items="${userList}" var="individualUser">
				<tr> 
					<td><b>${individualUser.getUsername() }</b></td>
					<td>${individualUser.getAvgShopCost() }</td>
					<td>${individualUser.getFavShop() }</td>
					<td>${individualUser.getFavProduct() }</td>
				</tr>
				</c:forEach>
			</table>
		</div>
		</center>
</div>
</body>
</html>