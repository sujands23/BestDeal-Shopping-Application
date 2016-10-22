package com.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.conn.MongoDBDataStoreUtilities;

public class SubmitReviewServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Inside doPost() of SubmitReviewServlet");
		StringBuilder html=new StringBuilder();
		PrintWriter out=response.getWriter();
		HttpSession session=request.getSession(false);
		
		String pageTitle="PRODUCT REVIEW FORM";
		String css="<link rel='stylesheet' href='./CSS/font-awesome-4.6.3/css/font-awesome.min.css'><link rel='stylesheet' type='text/css' href='./CSS/Common.css'><link rel='stylesheet' type='text/css' href='./CSS/HomePage.css'><link rel='stylesheet' type='text/css' href='./CSS/ProductReviewForm.css'>";
		String HeaderHTML="<!DOCTYPE html><html><head><meta charset='UTF-8'><title>BestDeal</title>"+css+"</head><body><div id='container'><div id='headerDiv'><div id='header1'><header><div id='bestDealLogo'><a href='HomePage.html'><img src='./images/BestDealLogo.jpg' style='width:200px;height:75px;margin-left:30px;margin-top:15px;cursor:pointer;'></a></div></header><div id='search'><div id='searchDiv'><form method='get' class='searchBox'><input type='text' id='searchTextArea' name='search' placeholder='Search for products'></input><button type='submit' id='searchButton'>Search<i class='fa fa-search' aria-hidden='true'></i></button></form></div></div><div id='userLinks'><ul><li><a href='Logout'>Logout</a></li></ul></div></div><div id='header2'><div id='mainHeader2'><nav><ul><li><span>MOBILE <i class='fa fa-mobile' aria-hidden='true'></i></span></li><li><span>TABLET <i class='fa fa-tablet' aria-hidden='true'></i></span></li><li><span>LAPTOP <i class='fa fa-laptop' aria-hidden='true'></i></span></li><li><span>TV <i class='fa fa-television' aria-hidden='true'></i></span></li></ul></nav></div></div></div><div id='center'>";
		String CustomerLinksHTML="<tr id='customerRow'><td colspan='6'><div id='customerLinks'><a href='ProductsList'>View Products</a></form><a href='MyOrders.html'>My Orders</a><a href=''>Write a Product review <i class='fa fa-pencil-square-o' aria-hidden='true'></i></a><a href='ChangePassword.html'>Change password</a><a href='logout'>Logout <i class='fa fa-sign-out' aria-hidden='true'></i></a></div></td></tr>";
		String ManagerLinksHTML="<tr id='managerRow'><td colspan='6'><div id='managerLinks'><a href='AddProduct.html'>Add product <i class='fa fa-plus-square-o' aria-hidden='true'></i></a><a href='DeleteProduct.html'>Delete product <i class='fa fa-minus-square-o' aria-hidden='true'></i></a><a href='DataAnalytics'>Data Analytics <i class='fa fa-bar-chart' aria-hidden='true'></i></a></div></td></tr>";
		
		String UserId=(String)session.getAttribute("UserId");
		String UserRole=(String) session.getAttribute("UserRole");//UserRole is the key
		
		if(session!=null){
			if(UserRole.equals("Customer")||UserRole.equals("Salesman")){
				ManagerLinksHTML="";
			}
		}
		
		html.append(HeaderHTML);
		String LoginUserHTML="";
		String TableHTML="";
		
		int productID=Integer.parseInt(request.getParameter("hiddenProductId"));
		String productCategory=request.getParameter("manufacturerName");
		String productBrand=request.getParameter("manufacturerName");
		String productName=request.getParameter("productName");
		String manufacturerRebate=request.getParameter("manufacturerRebate");
		String productPrice=request.getParameter("productPrice");
		
		String retailerName=request.getParameter("retailerName");
		String retailerZip=request.getParameter("retailerZip");
		String retailerCity=request.getParameter("retailerCity");
		String retailerState=request.getParameter("retailerState");
		String productOnSale=request.getParameter("productSale");
		
		String userId=request.getParameter("UserID");
		int userAge=Integer.parseInt(request.getParameter("UserAge"));
		String userGender=request.getParameter("gender");
		String userOccupation=request.getParameter("userOccupation");
		
		int reviewRating=Integer.parseInt(request.getParameter("star"));
		String reviewText=request.getParameter("reviewText");
		
		MongoDBDataStoreUtilities mongoDBInsert=new MongoDBDataStoreUtilities();
		Boolean SuccessFlag=mongoDBInsert.insertReview(productID,productCategory,productBrand,productName,manufacturerRebate,productPrice,retailerName,retailerZip,retailerCity,retailerState,productOnSale,userId,userAge,userGender,userOccupation,reviewRating,reviewText);
		System.out.println("Review form entered values :"+productID+"|"+productCategory+"|"+productBrand+"|"+productName+"|"+manufacturerRebate+"|"+productPrice+"|"+retailerName+"|"+retailerZip+"|"+retailerCity+"|"+retailerState+"|"+productOnSale+"|"+userId+"|"+userAge+"|"+userGender+"|"+userOccupation+"|"+reviewRating+"|"+reviewText);
		LoginUserHTML="<tr><td colspan='6' class='total'><div id='pageTitle'><span>"+pageTitle+"</span></div><div id='loginId'><i class='fa fa-user' aria-hidden='true'></i><span>" +UserId+"</span></div></td></tr>";
		if(SuccessFlag){
			TableHTML="<table>"+CustomerLinksHTML+ManagerLinksHTML+LoginUserHTML+"<tr><th style='color: #1FA66F;background-color: white;border: 2px solid #1FA66F;'>Review submitted successfully <i class='fa fa-check' aria-hidden='true'></i></th></tr></table>";
		}
		String FooterHTML="</div><div class='footer' id='footer'><div id='contactText'><ul><h4>Contact</h4><li>Sujan Davangere Sunil</li><li>A20354703</li><li><a href='mailto:sdavange@hawk.iit.edu'>sdavange@hawk.iit.edu</a></li><li>CSP 595 - Assignment 2</li></ul></div></div></div></body></html>";
		html.append(TableHTML);
		html.append(FooterHTML);
		out.println(html);
	}
}