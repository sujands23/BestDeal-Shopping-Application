package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model.pojo.Product;
import com.mysql.fabric.xmlrpc.base.Data;

public class WriteReviewServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("==============================Inside doPost() of WriteReviewServlet Servlet==============================");
		HttpSession session=request.getSession(false);
		PrintWriter out=response.getWriter();
		StringBuilder html=new StringBuilder();
		String TableHTML="";
		
		String pageTitle="PRODUCT REVIEW FORM";
		String css="<link rel='stylesheet' href='./CSS/font-awesome-4.6.3/css/font-awesome.min.css'><link rel='stylesheet' type='text/css' href='./CSS/Common.css'><link rel='stylesheet' type='text/css' href='./CSS/HomePage.css'><link rel='stylesheet' type='text/css' href='./CSS/ProductReviewForm.css'>";
		String HeaderHTML="<!DOCTYPE html><html><head><meta charset='UTF-8'><title>BestDeal</title>"+css+"</head><body><div id='container'><div id='headerDiv'><div id='header1'><header><div id='bestDealLogo'><a href='HomePage.html'><img src='./images/BestDealLogo.jpg' style='width:200px;height:75px;margin-left:30px;margin-top:15px;cursor:pointer;'></a></div></header><div id='search'><div id='searchDiv'><form method='get' class='searchBox'><input type='text' id='searchTextArea' name='search' placeholder='Search for products'></input><button type='submit' id='searchButton'>Search<i class='fa fa-search' aria-hidden='true'></i></button></form></div></div><div id='userLinks'><ul><li><a href='Logout'>Logout</a></li></ul></div></div><div id='header2'><div id='mainHeader2'><nav><ul><li><span>MOBILE <i class='fa fa-mobile' aria-hidden='true'></i></span></li><li><span>TABLET <i class='fa fa-tablet' aria-hidden='true'></i></span></li><li><span>LAPTOP <i class='fa fa-laptop' aria-hidden='true'></i></span></li><li><span>TV <i class='fa fa-television' aria-hidden='true'></i></span></li></ul></nav></div></div></div><div id='center'>";
		String CustomerLinksHTML="<tr id='customerRow'><td colspan='6'><div id='customerLinks'><a href='ProductsList'>View Products</a></form><a href='MyOrders.html'>My Orders</a><a href=''>Write a Product review <i class='fa fa-pencil-square-o' aria-hidden='true'></i></a><a href='Trending'>Trending</a><a href='logout'>Logout <i class='fa fa-sign-out' aria-hidden='true'></i></a></div></td></tr>";
		String ManagerLinksHTML="<tr id='managerRow'><td colspan='6'><div id='managerLinks'><a href='AddProduct.html'>Add product <i class='fa fa-plus-square-o' aria-hidden='true'></i></a><a href='DeleteProduct.html'>Delete product <i class='fa fa-minus-square-o' aria-hidden='true'></i></a><a href='DataAnalytics'>Data Analytics <i class='fa fa-bar-chart' aria-hidden='true'></i></a></div></td></tr>";
		
		html.append(HeaderHTML);
		
		if(session!=null){
			int productId=Integer.parseInt(request.getParameter("reviewProductId"));
			System.out.println(productId);
			
			String name=(String)session.getAttribute("user");
			String UserId=(String)session.getAttribute("UserId");//UserId is the key
			String UserRole=(String) session.getAttribute("UserRole");//UserRole is the key
			int cartItems=0;
			if(UserRole.equals("Customer")||UserRole.equals("Salesman")){
				ManagerLinksHTML="";
			}
			
			HashMap<Integer,Product> productMapPrint=(HashMap<Integer,Product>)session.getAttribute("AllProductsMap");
			Product reviewproduct=productMapPrint.get(productId);
			StringBuilder selectOption=new StringBuilder();
		
			System.out.println("Reviewing Product"+reviewproduct.getCategory()+reviewproduct.getBrand()+reviewproduct.getProductName()+reviewproduct.getPrice());
			Date todaysDate=new Date();
			String LoginUserHTML="<tr><td colspan='6' class='total'><div id='pageTitle'><span>"+pageTitle+"</span></div><div id='loginId'><span><i class='fa fa-user' aria-hidden='true'></i>"  +UserId+"</span></div><div id='CartDisplay'><span><i class='fa fa-shopping-cart' aria-hidden='true'></i> Cart : Items</span><span id='cartItemNumber'>("+cartItems+")</span></div></td></tr>";
			TableHTML="<table>"+CustomerLinksHTML+ManagerLinksHTML+LoginUserHTML+"<tr> <td colspan='6'> <div id='Message'>Review submitted successfully!</div> <div id='enterDiv'> <form action='SubmitReview' method='post'> <ul class='reviewForm'> <li><input type='text'  readonly  value='"+reviewproduct.getCategory()+"' class='credentials' name='productCategory' placeholder='Product Category' maxlength='15'></input></li> <li><input type='text' readonly  value='"+reviewproduct.getProductName()+"' class='credentials' name='productName' placeholder='Product name' maxlength='25'></input></li> <li><input readonly  value='"+reviewproduct.getPrice()+"' type='text' class='credentials' name='productPrice' placeholder='Price'></input></li> <hr> <li><input type='text'  class='credentials' name='retailerName' placeholder='Retailer Name'></li> <li><input type='text'  class='credentials' name='retailerZip' placeholder='Retailer ZIP'></li> <li><input type='text'  class='credentials' name='retailerCity' placeholder='Retailer City'></li> <li><input type='text'  class='credentials' name='retailerState' placeholder='Retailer State'></li> <hr> <li>Product on sale <ul id='productOnSale'> <li><input type='radio' name='productSale' value='saleYes'>Yes</li> <li><input type='radio' name='productSale' value='saleNo'>No<br></li> </ul> </li> <li><input readonly  value='"+reviewproduct.getBrand()+"' type='text'  class='credentials' name='manufacturerName' placeholder='Manufacturer name' maxlength='15'></input></li> <li><div>Manufacturer Rebate</div> <ul id='manufacturerRebate'> <li><input type='radio' name='manufacturerRebate' value='rabateYes'>Yes</li> <li><input type='radio' name='manufacturerRebate' value='rebateNo'>No<br></li> </ul> </li> <hr> <li><input readonly  type='text' value='"+UserId+"' class='credentials' name='UserID' placeholder='User ID'></li> <li><input type='text'  class='credentials' name='UserAge' placeholder='User Age'></li> <li><div>User Gender</div> <ul id='gender'> <li><input type='radio' name='gender' value='male'> Male</li> <li><input type='radio' name='gender' value='female'> Female<br></li> </ul> </li> <li><input type='text'  class='credentials' name='UserOccupation' placeholder='User occupation'></li> <li><div>Review rating</div> <div class='stars'> <input class='star star-5' value='5' id='star-5' type='radio' name='star'/> <label class='star star-5' for='star-5'></label> <input class='star star-4' value='4' id='star-4' type='radio' name='star'/> <label class='star star-4' for='star-4'></label> <input class='star star-3' id='star-3' value='3' type='radio' name='star'/> <label class='star star-3' for='star-3'></label> <input class='star star-2' value='2' id='star-2' type='radio' name='star'/> <label class='star star-2' for='star-2'></label> <input class='star star-1' value='1' id='star-1' type='radio' name='star'/> <label class='star star-1' for='star-1'></label> </div> </li> <li>Review Date <input value='"+todaysDate+"' type='date' name='bday'></li> <li> <textarea name='reviewText' rows='5' cols='40' maxlength='100'>Review text...</textarea> </li> <li><button type='submit' id='addProductButton' class='registerButtons'>Submit Review <i class='fa fa-pencil-square' aria-hidden='true'></i></button></li></ul><input type='hidden' name='hiddenProductId' value='"+reviewproduct.getProductID()+"'></input></form></div></td></tr></table>";
		}else{
			TableHTML="<table><tr><td><h2>Session does not exist for the user.Please <a href='Login.html'>login</a> again</h2></td></tr></table>";
		}
		String FooterHTML="</div><div class='footer' id='footer'><div id='contactText'><ul><h4>Contact</h4><li>Sujan Davangere Sunil</li><li>A20354703</li><li><a href='mailto:sdavange@hawk.iit.edu'>sdavange@hawk.iit.edu</a></li><li>CSP 595 - Assignment 2</li></ul></div></div></div></body></html>";
		html.append(TableHTML);
		html.append(FooterHTML);
		out.println(html);
	}
}