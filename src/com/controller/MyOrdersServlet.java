package com.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.conn.MySQLDataStoreUtilities;

public class MyOrdersServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("==============================Inside dopost() of MyOrdersServlet==============================");
		HttpSession session=request.getSession(false);
		PrintWriter out=response.getWriter();
		StringBuilder html=new StringBuilder();
		
		String pageTitle="MY ORDERS";
		String css="<link rel='stylesheet' href='./CSS/font-awesome-4.6.3/css/font-awesome.min.css'><link rel='stylesheet' type='text/css' href='./CSS/Common.css'><link rel='stylesheet' type='text/css' href='./CSS/HomePage.css'><link rel='stylesheet' href='./CSS/MyOrders.css'><link rel='stylesheet' type='text/css' href='./CSS/Payment.css'>";
		String HeaderHTML="<!DOCTYPE html><html><head><meta charset='UTF-8'><title>BestDeal</title>"+css+"</head><body><div id='container'><div id='headerDiv'><div id='header1'><header><div id='bestDealLogo'><a href='HomePage.html'><img src='./images/BestDealLogo.jpg' style='width:200px;height:75px;margin-left:30px;margin-top:15px;cursor:pointer;'></a></div></header><div id='search'><div id='searchDiv'><form method='get' class='searchBox'><input type='text' id='searchTextArea' name='search' placeholder='Search for products'></input><button type='submit' id='searchButton'>Search<i class='fa fa-search' aria-hidden='true'></i></button></form></div></div><div id='userLinks'><ul><li><a href='Logout'>Logout</a></li></ul></div></div><div id='header2'><div id='mainHeader2'><nav><ul><li><span>MOBILE <i class='fa fa-mobile' aria-hidden='true'></i></span></li><li><span>TABLET <i class='fa fa-tablet' aria-hidden='true'></i></span></li><li><span>LAPTOP <i class='fa fa-laptop' aria-hidden='true'></i></span></li><li><span>TV <i class='fa fa-television' aria-hidden='true'></i></span></li></ul></nav></div></div></div><div id='center'>";
		String CustomerLinksHTML="<tr id='customerRow'><td colspan='6'><div id='customerLinks'><a href='ProductsList'>View Products</a></form><a href='MyOrders.html'>My Orders</a><a href=''>Write a Product review <i class='fa fa-pencil-square-o' aria-hidden='true'></i></a><a href='Trending'>Trending</a><a href='logout'>Logout <i class='fa fa-sign-out' aria-hidden='true'></i></a></div></td></tr>";
		String ManagerLinksHTML="<tr id='managerRow'><td colspan='6'><div id='managerLinks'><a href='AddProduct.html'>Add product <i class='fa fa-plus-square-o' aria-hidden='true'></i></a><a href='DeleteProduct.html'>Delete product <i class='fa fa-minus-square-o' aria-hidden='true'></i></a><a href='DataAnalytics'>Data Analytics <i class='fa fa-bar-chart' aria-hidden='true'></i></a></div></td></tr>";
		
		String TableHTML="";
		String FooterHTML="</div><div class='footer' id='footer'><div id='contactText'><ul><h4>Contact</h4><li>Sujan Davangere Sunil</li><li>A20354703</li><li><a href='mailto:sdavange@hawk.iit.edu'>sdavange@hawk.iit.edu</a></li><li>CSP 595 - Assignment 2</li></ul></div></div></div></body></html>";
		
		html.append(HeaderHTML);
		
		if(session!=null){
			String name=(String)session.getAttribute("user");
			String UserId=(String)session.getAttribute("UserId");//UserId is the key
			String UserRole=(String) session.getAttribute("UserRole");//UserRole is the key
			if(UserRole.equals("Customer")||UserRole.equals("Salesman")){
				ManagerLinksHTML="";
			}
			int orderId=Integer.parseInt(request.getParameter("OrderIDSearch"));
			System.out.println("Entered OrderId is :"+orderId);
			MySQLDataStoreUtilities mysql=new MySQLDataStoreUtilities();
			String orderDetailsDB=mysql.getOrder(orderId);
			
			String LoginUserHTML="<tr><td colspan='6' class='total'><div id='pageTitle'><span>"+pageTitle+"</span></div><div id='loginId'><span>"+UserId+"</span></div></td></tr>";
			TableHTML="<table>"+CustomerLinksHTML+ManagerLinksHTML+LoginUserHTML+"<tr><td colspan='6'><div id='orderIDSearch' style='height: auto;'><form method='post' action='SearchOrder' class='searchBox'><input type='text' id='orderIDsearchTextArea' name='OrderIDSearch' placeholder='Enter Order ID'></input><button type='submit' id='orderIDSearchButton'>Search Order <i class='fa fa-search' aria-hidden='true'></i></button></form></div></td></tr><tr><td colspan='6'><div id='enterDiv'><form action='ordersview' method='post'>"+orderDetailsDB+"</form></div></td></tr></table>";
		}
		else{
			TableHTML="<table><tr><td><h2>Session does not exist for the user.Please <a href='Login.html'>login</a> again</h2></td></tr></table>";
		}
		html.append(TableHTML);
		html.append(FooterHTML);
		out.println(html);
	}
}