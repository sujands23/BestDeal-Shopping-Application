package com.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.conn.MySQLDataStoreUtilities;
import com.model.pojo.Product;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;


public class OrderConfirmationServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("==============================Inside doPost() of orderConfirmation==============================");
		StringBuilder html=new StringBuilder();
		PrintWriter out=response.getWriter();
		HttpSession session=request.getSession(false);
		
		String pageTitle="Order Confirmation";
		String css="<link rel='stylesheet' href='./CSS/font-awesome-4.6.3/css/font-awesome.min.css'><link rel='stylesheet' type='text/css' href='./CSS/Common.css'><link rel='stylesheet' type='text/css' href='./CSS/HomePage.css'><link rel='stylesheet' href='./CSS/OrderFlowDisplay.css'><link rel='stylesheet' type='text/css' href='./CSS/OrderConfirmation.css'>";
		String HeaderHTML="<!DOCTYPE html><html><head><meta charset='UTF-8'><title>BestDeal</title>"+css+"</head><body><div id='container'><div id='headerDiv'><div id='header1'><header><div id='bestDealLogo'><a href='HomePage.html'><img src='./images/BestDealLogo.jpg' style='width:200px;height:75px;margin-left:30px;margin-top:15px;cursor:pointer;'></a></div></header><div id='search'><div id='searchDiv'><form method='get' class='searchBox'><input type='text' id='searchTextArea' name='search' placeholder='Search for products'></input><button type='submit' id='searchButton'>Search<i class='fa fa-search' aria-hidden='true'></i></button></form></div></div><div id='userLinks'><ul><li><a href='Logout'>Logout</a></li></ul></div></div><div id='header2'><div id='mainHeader2'><nav><ul><li><span>MOBILE <i class='fa fa-mobile' aria-hidden='true'></i></span></li><li><span>TABLET <i class='fa fa-tablet' aria-hidden='true'></i></span></li><li><span>LAPTOP <i class='fa fa-laptop' aria-hidden='true'></i></span></li><li><span>TV <i class='fa fa-television' aria-hidden='true'></i></span></li></ul></nav></div></div></div><div id='center'>";
		String CustomerLinksHTML="<tr id='customerRow'><td colspan='6'><div id='customerLinks'><a href='ProductsList'>View Products</a></form><a href='MyOrders.html'>My Orders</a><a href=''>Write a Product review <i class='fa fa-pencil-square-o' aria-hidden='true'></i></a><a href='Trending'>Trending</a><a href='logout'>Logout <i class='fa fa-sign-out' aria-hidden='true'></i></a></div></td></tr>";
		String ManagerLinksHTML="<tr id='managerRow'><td colspan='6'><div id='managerLinks'><a href='AddProduct.html'>Add product <i class='fa fa-plus-square-o' aria-hidden='true'></i></a><a href='DeleteProduct.html'>Delete product <i class='fa fa-minus-square-o' aria-hidden='true'></i></a><a href='DataAnalytics'>Data Analytics <i class='fa fa-bar-chart' aria-hidden='true'></i></a></div></td></tr>";
		String orderFlowHTML="<tr><td colspan='6'><div id='orderFlowDisplay'><div class='processName'><span id='firstStep' class='arrow-right'></span><p>View Products</p></div><div class='processName'><span class='arrow-right'></span><p>Order Summary</p></div><div class='processName'><span class='arrow-right'></span><p>Payment</p></div><div class='processName'id='nowText'><span class='arrow-right' id='nowArrowBefore'></span><p>Order Confirmation</p></div><div class='arrow-right' id='now'></div></div></td></tr>";
		html.append(HeaderHTML);
		
		if(session!=null){
			String name=(String)session.getAttribute("user");
			String UserId=(String)session.getAttribute("UserId");//UserId is the key
			String UserRole=(String) session.getAttribute("UserRole");//UserRole is the key
			Double OrderTotal=(Double) session.getAttribute("total");
			int OrderedItems=(Integer)session.getAttribute("cartItems");
			
			System.out.println("UserId:	  "+UserId);
			System.out.println("UserRole:	"+UserRole);
			System.out.println("Number of items ordered:	"+OrderedItems);
			System.out.println("Order total:	 "+OrderTotal);
			
			if(UserRole.equals("Customer")||UserRole.equals("Salesman")){
				ManagerLinksHTML="";
			}
			
			HashMap<Integer,Product> orderMap;
			
			int OrderId=1;
			String ShippingAddress="Siddhi";
			String orderStatus="Ordered";
			int[] OrderedProductId;
			int[] OrderedProductQuantity;
			int i=0;
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			String orderDate = dateFormat.format(date);
			
			System.out.println("Order Number : "+OrderId);
			System.out.println("Order date"+orderDate);
			
			String cardNumber=request.getParameter("cardNumber");
			String lastFourDigits =cardNumber.substring((cardNumber.length()-4), cardNumber.length());
			
			String rowHtml="";
			rowHtml="<li><b>Product ID | Product Category | Product Name | Brand | Description | Price | Order Quantity</b></li>";
			
			orderMap=(HashMap<Integer, Product>) session.getAttribute("userOrder");
			OrderedProductId=new int[orderMap.size()];
			OrderedProductQuantity=new int[orderMap.size()];
			Set<Map.Entry<Integer, Product>> entries=orderMap.entrySet();
			for(Map.Entry<Integer, Product> prodMap:entries){
				Product product=prodMap.getValue();
				System.out.println("Order Confirmation : "+prodMap.getKey()+" :  "+product.getProductID()+"		|	"+product.getCategory()+"	|	"+product.getProductName()+"	|	"+product.getBrand()+"	|	"+product.getDescription()+"	|	"+product.getPrice()+"	|	"+product.getImage());
				System.out.format("%-5s|%-10s|%-20s|%-10s|%-30s|%-10s|%-30s|%-5s\n", product.getProductID(), product.getCategory(), product.getProductName(),product.getBrand(),product.getDescription(),product.getPrice(),product.getImage(),product.getOrderQuantity());
				rowHtml=rowHtml+"<li>"+product.getProductID()+"		|	"+product.getCategory()+"	|	"+product.getProductName()+"	|	"+product.getBrand()+"	|	"+product.getDescription()+"	|	"+product.getPrice()+"	|	"+product.getOrderQuantity()+"	|	"+product.getImage()+"</li>";
				OrderedProductId[i]=product.getProductID();
				OrderedProductQuantity[i]=product.getOrderQuantity();
				i++;
			}
			/*for(int j=0;j<OrderedProductId.length;j++)
				System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+OrderedProductId[j]+","+OrderedProductQuantity[j]);*/
			MySQLDataStoreUtilities mysql=new MySQLDataStoreUtilities();
			OrderId=mysql.InsertIntoOrdersTable(UserId, OrderTotal, OrderedItems, ShippingAddress, cardNumber, orderStatus);
			System.out.println("Order ID returned : "+OrderId);
			
			mysql.insertIntoOrderProduct(OrderId, OrderedProductId, OrderedProductQuantity);
			
			String OrderConfirmationTableHTML="<table><tr><td colspan='1' class='total'><div id='pageTitle'><span>"+pageTitle+"<i class='fa fa-check' aria-hidden='true'></i></span></div><div id='loginId'><span>"+UserId+"</span></div></td></tr><tr><td><h3>Your Payment information</h3><hr><ul><li>Order Number	:	<b>"+OrderId+"</b></li><li>Payment method				:	Credit Card XXXX-XXXX-XXXX-"+lastFourDigits+"<b></b></li><li>Total Amount Paid 			:	<b>"+OrderTotal+"</b></li><li>Transaction Date			:	<b>"+orderDate+"</b></li></ul><h3>Order Details</h3><hr><div><p>Total products ordered	:	<b>"+OrderedItems+"</b></p></div><ul>";
			String LoginUserHTML="<tr><td colspan='6' class='total'><div id='pageTitle'><span>"+pageTitle+"</span></div><div id='loginId'><span>"+UserId+"</span></div><div id='CartDisplay'><span><i class='fa fa-shopping-cart' aria-hidden='true'></i> Cart : Items</span><span id='cartItemNumber'>("+OrderedItems+")</span></div></td></tr>";
			String TableHTML="<table>"+CustomerLinksHTML+ManagerLinksHTML+orderFlowHTML+OrderConfirmationTableHTML+rowHtml+"</ul></td></tr></table>";
			String FooterHTML="</div><div class='footer' id='footer'><div id='contactText'><ul><h4>Contact</h4><li>Sujan Davangere Sunil</li><li>A20354703</li><li><a href='mailto:sdavange@hawk.iit.edu'>sdavange@hawk.iit.edu</a></li><li>CSP 595 - Assignment 2</li></ul></div></div></div></body></html>";
			
			html.append(TableHTML);
			html.append(FooterHTML);
			out.println(html);
		}
		else{
			html.append("<html><head></head><body><h2>Session does not exist for the user.Please <a href='Login.html'>login</a> again</h2></body></html>");
		}
	}
}