package com.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.conn.MongoDBDataStoreUtilities;
import com.conn.MySQLDataStoreUtilities;

public class TrendingServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("==============================Inside doGet() of TrendingServlet==============================");
		PrintWriter out=response.getWriter();
		StringBuilder html=new StringBuilder();
		
		String pageTitle="TRENDING";
		String css="<link rel='stylesheet' href='./CSS/font-awesome-4.6.3/css/font-awesome.min.css'><link rel='stylesheet' type='text/css' href='./CSS/Common.css'><link rel='stylesheet' type='text/css' href='./CSS/HomePage.css'><link rel='stylesheet' href='./CSS/OrderFlowDisplay.css'><link rel='stylesheet' href='./CSS/OrderSummary.css'>";
		String HeaderHTML="<!DOCTYPE html><html><head><meta charset='UTF-8'><title>BestDeal</title>"+css+"</head><body><div id='container'><div id='headerDiv'><div id='header1'><header><div id='bestDealLogo'><a href='HomePage.html'><img src='./images/BestDealLogo.jpg' style='width:200px;height:75px;margin-left:30px;margin-top:15px;cursor:pointer;'></a></div></header><div id='search'><div id='searchDiv'><form method='get' class='searchBox'><input type='text' id='searchTextArea' name='search' placeholder='Search for products'></input><button type='submit' id='searchButton'>Search<i class='fa fa-search' aria-hidden='true'></i></button></form></div></div><div id='userLinks'><ul><li><a href='Logout'>Logout</a></li></ul></div></div><div id='header2'><div id='mainHeader2'><nav><ul><li><span>MOBILE <i class='fa fa-mobile' aria-hidden='true'></i></span></li><li><span>TABLET <i class='fa fa-tablet' aria-hidden='true'></i></span></li><li><span>LAPTOP <i class='fa fa-laptop' aria-hidden='true'></i></span></li><li><span>TV <i class='fa fa-television' aria-hidden='true'></i></span></li></ul></nav></div></div></div><div id='center'>";
		String CustomerLinksHTML="<tr id='customerRow'><td colspan='6'><div id='customerLinks'><a href='ProductsList'>View Products</a></form><a href='MyOrders.html'>My Orders</a><a href=''>Write a Product review <i class='fa fa-pencil-square-o' aria-hidden='true'></i></a><a href='Trending'>Trending</a><a href='logout'>Logout <i class='fa fa-sign-out' aria-hidden='true'></i></a></div></td></tr>";
		
		html.append(HeaderHTML);
		String TableHTML="";
		String FooterHTML="";
		HttpSession session=request.getSession(false);
		String UserId=(String)session.getAttribute("UserId");//UserId is the key
		
		MongoDBDataStoreUtilities mongoDB=new MongoDBDataStoreUtilities();
		String TopLikedProducts=mongoDB.FiveMostLikedProducts();
		
		String Question1HTML="<table><tr><th colspan='2'>Top five most liked products</th><tr><th>Product</th><th>Average Rating</th></tr></tr>"+TopLikedProducts+"</table>";
		
		MySQLDataStoreUtilities mySqlDB=new MySQLDataStoreUtilities();
		String TopFiveMostSoldProducts=mySqlDB.TopFiveMostSoldProducts();
		String Question3HTML="<table><tr><th colspan='2'>Top five most sold products regardless of the rating</th><tr><tr><th>Product</th><th>Quantity Sold</th></tr>"+TopFiveMostSoldProducts+"</table>";
		
		String LoginUserHTML="<tr><td colspan='6' class='total'><div id='pageTitle'><span>"+pageTitle+"</span></div><div id='loginId'><span><i class='fa fa-user' aria-hidden='true'></i>"  +UserId+"</span></div></td></tr>";
		TableHTML="<table>"+CustomerLinksHTML+LoginUserHTML+Question1HTML+Question3HTML+"</table>";
		FooterHTML="</div><div class='footer' id='footer'><div id='contactText'><ul><h4>Contact</h4><li>Sujan Davangere Sunil</li><li>A20354703</li><li><a href='mailto:sdavange@hawk.iit.edu'>sdavange@hawk.iit.edu</a></li><li>CSP 595 - Assignment 2</li></ul></div></div></div></body></html>";
		html.append(TableHTML);
		html.append(FooterHTML);
		out.println(html);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("==============================Inside doPost() of TrendingServlet==============================");
	}
	
}