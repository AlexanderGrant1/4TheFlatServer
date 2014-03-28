<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<h1>Group Analytics</h1>
<h2><center>Basic Group Analytics</center></h2>
<p><b>Most Active Shopper: </b> ${groupAnalytics.bestShopper}</p>
<p><b>Most Popular Shop: </b> ${groupAnalytics.bestStore }</p>
<p><b>Average Shop Cost: </b> ${averageShopCost }</p>
<p><b>Average Time Between Shops: </b> ${averageTimeBetween }</p>
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
		</center>
</html>