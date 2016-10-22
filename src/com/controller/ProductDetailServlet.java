package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.conn.MongoDBDataStoreUtilities;
import com.model.pojo.Product;
import com.model.pojo.Reviews;

public class ProductDetailServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("==============================Inside doGet() of ProductDetailServlet Servlet==============================");
		int productId=Integer.parseInt(request.getParameter("productId"));
		System.out.println(productId);
		
		PrintWriter out=response.getWriter();
		StringBuilder html=new StringBuilder();
		String TableHTML="";
		String productHTML="";
		HttpSession session=request.getSession(false);
		
		String pageTitle="PRODUCT DETAILS";
		String css="<link rel='stylesheet' href='./CSS/font-awesome-4.6.3/css/font-awesome.min.css'><link rel='stylesheet' type='text/css' href='./CSS/Common.css'><link rel='stylesheet' type='text/css' href='./CSS/HomePage.css'><link rel='stylesheet' href='./CSS/OrderFlowDisplay.css'><link rel='stylesheet' type='text/css' href='./CSS/ProductDetail.css'>";
		String HeaderHTML="<!DOCTYPE html><html><head><meta charset='UTF-8'><title>BestDeal</title>"+css+"</head><body><div id='container'><div id='headerDiv'><div id='header1'><header><div id='bestDealLogo'><a href='HomePage.html'><img src='./images/BestDealLogo.jpg' style='width:200px;height:75px;margin-left:30px;margin-top:15px;cursor:pointer;'></a></div></header><div id='search'><div id='searchDiv'><form method='get' class='searchBox'><input type='text' id='searchTextArea' name='search' placeholder='Search for products'></input><button type='submit' id='searchButton'>Search<i class='fa fa-search' aria-hidden='true'></i></button></form></div></div><div id='userLinks'><ul><li><a href='Logout'>Logout</a></li></ul></div></div><div id='header2'><div id='mainHeader2'><nav><ul><li><span>MOBILE <i class='fa fa-mobile' aria-hidden='true'></i></span></li><li><span>TABLET <i class='fa fa-tablet' aria-hidden='true'></i></span></li><li><span>LAPTOP <i class='fa fa-laptop' aria-hidden='true'></i></span></li><li><span>TV <i class='fa fa-television' aria-hidden='true'></i></span></li></ul></nav></div></div></div><div id='center'>";
		String CustomerLinksHTML="<tr id='customerRow'><td colspan='6'><div id='customerLinks'><a href='ProductsList'>View Products</a></form><a href='MyOrders.html'>My Orders</a><a href=''>Write a Product review <i class='fa fa-pencil-square-o' aria-hidden='true'></i></a><a href='Trending'>Trending</a><a href='logout'>Logout <i class='fa fa-sign-out' aria-hidden='true'></i></a></div></td></tr>";
		String ManagerLinksHTML="<tr id='managerRow'><td colspan='6'><div id='managerLinks'><a href='AddProduct.html'>Add product <i class='fa fa-plus-square-o' aria-hidden='true'></i></a><a href='DeleteProduct.html'>Delete product <i class='fa fa-minus-square-o' aria-hidden='true'></i></a><a href='DataAnalytics'>Data Analytics <i class='fa fa-bar-chart' aria-hidden='true'></i></a></div></td></tr>";
		
		html.append(HeaderHTML);
		
		if(session!=null){
			MongoDBDataStoreUtilities mongodb=new MongoDBDataStoreUtilities();
			ArrayList<Reviews> readReviewslist=mongodb.selectReview(productId);
			
			String UserId=(String)session.getAttribute("UserId");
			String UserRole=(String) session.getAttribute("UserRole");//UserRole is the key
			if(UserRole.equals("Customer")||UserRole.equals("Salesman")){
				ManagerLinksHTML="";
			}
			int CartItems=0;
			String personName;
				int personRating;
			String personReviewText;
			
			float averageRating=0;
			float totalRating=0;
			Reviews personReview;
			
			String reviewRow="";
			String startText="";
			for(int i=0;i<readReviewslist.size();i++){
				
				personReview=readReviewslist.get(i);
				personName=personReview.getReviewerName();
				personRating=personReview.getReviewRating();
				personReviewText=personReview.getReviewText();
				totalRating=totalRating+personRating;
				System.out.println("Person Name: "+personName+"	| Rating: "+personRating+"	|	Review text: "+personReviewText);
				for(int j=0;j<personRating;j++){
					startText=startText+"<i class='fa fa-star' aria-hidden='true'></i>";
				}
				reviewRow=reviewRow+"<tr><td colspan='6'><ul><li><h3>"+personName+"<h3></li><li><div class='personStarRating'>"+startText+personRating+"</div></li><li>"+personReviewText+"</li></ul><hr></td></tr>";
				startText="";
			}
			System.out.println("Reviews count:"+readReviewslist.size());
			averageRating=totalRating/readReviewslist.size();
			System.out.println("Average rating * : "+averageRating);
			
			HashMap<Integer,Product> productMapPrint=(HashMap<Integer,Product>)session.getAttribute("AllProductsMap");
			Product selectedproduct=productMapPrint.get(productId);
			StringBuilder selectOption=new StringBuilder();
			for(int i=1;i<selectedproduct.getQuantityAvailable();i++){
				selectOption.append("<option value='"+i+"'>0</option>");
			}
			
			System.out.println("SelectedProduct"+selectedproduct.getBrand()+selectedproduct.getProductName());
			String LoginUserHTML="<tr><td colspan='6' class='total'><div id='pageTitle'><span>"+pageTitle+"</span></div><div id='loginId'><i class='fa fa-user' aria-hidden='true'></i><span>" +UserId+"</span></div><div id='CartDisplay'><span><i class='fa fa-shopping-cart' aria-hidden='true'></i> Cart : Items</span><span id='cartItemNumber'>("+CartItems+")</span></div></td></tr>";
			TableHTML="<table>"+CustomerLinksHTML+ManagerLinksHTML+LoginUserHTML+"<tr><th colspan='2' id='productTitle'>"+selectedproduct.getBrand()+" "+selectedproduct.getProductName()+"</th></tr><tr><td><img src='./images/"+selectedproduct.getImage()+"' style='width:500px;height:500px'></td><td><ul id='productDetailList'><li><div id='productRating'><i class='fa fa-star' aria-hidden='true'></i> "+averageRating+"</div></li><li><h3>Brand</h3></li><li>"+selectedproduct.getBrand()+"</li><li><h3>Model</h3></li><li>"+selectedproduct.getProductName()+"</li><li><h3>Description</h3></li><li>"+selectedproduct.getDescription()+"</li><li><h3>Price</h3></li><li><h4>$"+selectedproduct.getPrice()+"</h4></li></ul><ul><div id='userInputs'><form action='addToCart' method='post'><li><h3>Quantity</h3></li><li><select id='quantitySelect'>"+selectOption+"</select></li><li><button type='submit' id='AddToCartButton'><i class='fa fa-shopping-cart' aria-hidden='true' style='color:black'></i>ADD TO CART</button></li></form><form action='WriteReview' method='post'><li><button type='submit' id='WriteReviewButton' name='reviewProductId' value='"+productId+"'><i class='fa fa-pencil-square-o' aria-hidden='true'></i> Write product Review</button></li></form></div></ul></td></tr><tr id='reviewsHeading'><td><h2>REVIEWS<h2><td></tr>"+reviewRow+"</table>";
		}else{
			TableHTML="<table><tr><th>You are logged out. Please Login</th></tr></table>";
		}
		String FooterHTML="</div><div class='footer' id='footer'><div id='contactText'><ul><h4>Contact</h4><li>Sujan Davangere Sunil</li><li>A20354703</li><li><a href='mailto:sdavange@hawk.iit.edu'>sdavange@hawk.iit.edu</a></li><li>CSP 595 - Assignment 2</li></ul></div></div></div></body></html>";
		html.append(TableHTML);
		html.append(FooterHTML);
		out.println(html);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("==============================Inside doPost() of ProductDetailServlet Servlet==============================");
	}
}