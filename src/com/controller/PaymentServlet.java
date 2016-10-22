package com.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model.pojo.*;


public class PaymentServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("==============================Inside doGet() of PaymentServlet==============================");
		HttpSession session=request.getSession(false);
		PrintWriter out=response.getWriter();
		StringBuilder html=new StringBuilder();
		String LoginUserHTML="";
		
		String pageTitle="CREDIT CARD PAYMENT";
		String css="<link rel='stylesheet' href='./CSS/font-awesome-4.6.3/css/font-awesome.min.css'><link rel='stylesheet' type='text/css' href='./CSS/Common.css'><link rel='stylesheet' type='text/css' href='./CSS/HomePage.css'><link rel='stylesheet' href='./CSS/OrderFlowDisplay.css'><link rel='stylesheet' type='text/css' href='./CSS/Payment.css'>";
		String HeaderHTML="<!DOCTYPE html><html><head><meta charset='UTF-8'><title>BestDeal</title>"+css+"</head><body><div id='container'><div id='headerDiv'><div id='header1'><header><div id='bestDealLogo'><a href='HomePage.html'><img src='./images/BestDealLogo.jpg' style='width:200px;height:75px;margin-left:30px;margin-top:15px;cursor:pointer;'></a></div></header><div id='search'><div id='searchDiv'><form method='get' class='searchBox'><input type='text' id='searchTextArea' name='search' placeholder='Search for products'></input><button type='submit' id='searchButton'>Search<i class='fa fa-search' aria-hidden='true'></i></button></form></div></div><div id='userLinks'><ul><li><a href='Logout'>Logout</a></li></ul></div></div><div id='header2'><div id='mainHeader2'><nav><ul><li><span>MOBILE <i class='fa fa-mobile' aria-hidden='true'></i></span></li><li><span>TABLET <i class='fa fa-tablet' aria-hidden='true'></i></span></li><li><span>LAPTOP <i class='fa fa-laptop' aria-hidden='true'></i></span></li><li><span>TV <i class='fa fa-television' aria-hidden='true'></i></span></li></ul></nav></div></div></div><div id='center'>";
		String CustomerLinksHTML="<tr id='customerRow'><td colspan='6'><div id='customerLinks'><a href='ProductsList'>View Products</a></form><a href='MyOrders.html'>My Orders</a><a href=''>Write a Product review <i class='fa fa-pencil-square-o' aria-hidden='true'></i></a><a href='Trending'>Trending</a><a href='logout'>Logout <i class='fa fa-sign-out' aria-hidden='true'></i></a></div></td></tr>";
		String ManagerLinksHTML="<tr id='managerRow'><td colspan='6'><div id='managerLinks'><a href='AddProduct.html'>Add product <i class='fa fa-plus-square-o' aria-hidden='true'></i></a><a href='DeleteProduct.html'>Delete product <i class='fa fa-minus-square-o' aria-hidden='true'></i></a><a href='DataAnalytics'>Data Analytics <i class='fa fa-bar-chart' aria-hidden='true'></i></a></div></td></tr>";
		String orderFlowHTML="<tr><td colspan='6'><div id='orderFlowDisplay'><div class='processName'><span id='firstStep' class='arrow-right'></span><p>View Products</p></div><div class='processName'><span class='arrow-right'></span><p>Order Summary</p></div><div class='processName' id='nowText'><span class='arrow-right' id='nowArrowBefore'></span><p>Payment</p></div><div class='processName'><span class='arrow-right' id='now'></span><p>Order Confirmation</p></div><div class='arrow-right'></div></div></td></tr>";
		
		html.append(HeaderHTML);
		
		if(session!=null){
			String name=(String)session.getAttribute("user");
			String UserId=(String)session.getAttribute("UserId");//UserId is the key
			String UserRole=(String) session.getAttribute("UserRole");//UserRole is the key
			String Total=request.getParameter("total");
			int cartItems=(Integer)session.getAttribute("cartItems");
			
			System.out.println("UserId:	  "+UserId);
			System.out.println("UserRole:	"+UserRole);
			System.out.println("Number of items in Cart:	"+cartItems);
			System.out.println("Cart total:	 "+Total);
			if(UserRole.equals("Customer")||UserRole.equals("Salesman")){
				ManagerLinksHTML="";
			}
			LoginUserHTML="<tr><td colspan='6' class='total'><div id='pageTitle'><span>"+pageTitle+"</span></div><div id='loginId'><i class='fa fa-user' aria-hidden='true'></i><span>" +UserId+"</span></div><div id='CartDisplay'><span><i class='fa fa-shopping-cart' aria-hidden='true'></i> Cart : Items</span><span id='cartItemNumber'>("+cartItems+")</span></div></td></tr>";
			
		}
		else{
			html.append("<html><head></head><body><h2>Session does not exist for the user.Please <a href='Login.html'>login</a> again</h2></body></html>");
		}
		String TableHTML="<table>"+CustomerLinksHTML+ManagerLinksHTML+LoginUserHTML+orderFlowHTML+"<tr><td colspan='6'><div id='creditCardDetails'><form action='OrderConfirmation' method='post'><div><input type='text' id='cardNumber' name='cardNumber' class='cardDetail' placeholder='Card Number' maxlength='16'></input></div><div><div id='cardDateMonth'><span class='cardDateDiv'><input type='text' class='cardDetail cardTwo' placeholder='MM' maxlength='2'></input></span><span class='delimiter'>/</span><span class='cardDateDiv'><input type='text' class='cardDetail cardTwo' placeholder='YY' maxlength='2'></input></span></div><span class='cardCVV'><input type='password' id='cvvWidth' class='cardDetail' placeholder='CVV' maxlength='3'></input></span></div><div><input type='text' id='nameOnCard' class='cardDetail' placeholder='Name on card'></input></div><div id='payButtonDiv'><button type='submit' class='cardDetail' id='payButton'>Make Payment</button></div></form></div></td></tr></table>";
		String FooterHTML="</div><div class='footer' id='footer'><div id='contactText'><ul><h4>Contact</h4><li>Sujan Davangere Sunil</li><li>A20354703</li><li><a href='mailto:sdavange@hawk.iit.edu'>sdavange@hawk.iit.edu</a></li><li>CSP 595 - Assignment 2</li></ul></div></div></div></body></html>";
		html.append(TableHTML);
		html.append(FooterHTML);
		out.println(html);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Inside doPost of PaymentServlet");	
	}
}