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

import com.model.pojo.Product;
import com.poc.sujan.ProductDataStore;

public class ProductsList extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("==============================Inside doGet() of ProductList Servlet==============================");
		PrintWriter out=response.getWriter();
		StringBuilder html=new StringBuilder();
		
		HttpSession session=request.getSession(false);
		
		String pageTitle="VIEW PRODUCTS";
		String css="<link rel='stylesheet' href='./CSS/font-awesome-4.6.3/css/font-awesome.min.css'><link rel='stylesheet' type='text/css' href='./CSS/Common.css'><link rel='stylesheet' type='text/css' href='./CSS/HomePage.css'><link rel='stylesheet' href='./CSS/OrderFlowDisplay.css'><link rel='stylesheet' type='text/css' href='./CSS/ViewProducts.css'>";
		String HeaderHTML="<!DOCTYPE html><html><head><meta charset='UTF-8'><title>BestDeal</title>"+css+"</head><body><div id='container'><div id='headerDiv'><div id='header1'><header><div id='bestDealLogo'><a href='HomePage.html'><img src='./images/BestDealLogo.jpg' style='width:200px;height:75px;margin-left:30px;margin-top:15px;cursor:pointer;'></a></div></header><div id='search'><div id='searchDiv'><form method='get' class='searchBox'><input type='text' id='searchTextArea' name='search' placeholder='Search for products'></input><button type='submit' id='searchButton'>Search<i class='fa fa-search' aria-hidden='true'></i></button></form></div></div><div id='userLinks'><ul><li><a href='Logout'>Logout</a></li></ul></div></div><div id='header2'><div id='mainHeader2'><nav><ul><li><span>MOBILE <i class='fa fa-mobile' aria-hidden='true'></i></span></li><li><span>TABLET <i class='fa fa-tablet' aria-hidden='true'></i></span></li><li><span>LAPTOP <i class='fa fa-laptop' aria-hidden='true'></i></span></li><li><span>TV <i class='fa fa-television' aria-hidden='true'></i></span></li></ul></nav></div></div></div><div id='center'>";
		String CustomerLinksHTML="<tr id='customerRow'><td colspan='9'><div id='customerLinks'><a href='ProductsList'>View Products</a></form><a href='MyOrders.html'>My Orders</a><a href=''>Write a Product review <i class='fa fa-pencil-square-o' aria-hidden='true'></i></a><a href='Trending'>Trending</a><a href='logout'>Logout <i class='fa fa-sign-out' aria-hidden='true'></i></a></div></td></tr>";
		String ManagerLinksHTML="<tr id='managerRow'><td colspan='9'><div id='managerLinks'><a href='AddProduct.html'>Add product <i class='fa fa-plus-square-o' aria-hidden='true'></i></a><a href='DeleteProduct.html'>Delete product <i class='fa fa-minus-square-o' aria-hidden='true'></i></a><a href='DataAnalytics'>Data Analytics <i class='fa fa-bar-chart' aria-hidden='true'></i></a></div></td></tr>";
		String orderFlowHTML="<tr><td colspan='9'><div id='orderFlowDisplay'><div class='processName' id='nowText'><span id='firstStep' class='arrow-right' id='nowArrowBefore'></span><p>View Products</p></div><div class='processName'><span class='arrow-right' id='now'></span><p>Order Summary</p></div><div class='processName'><span class='arrow-right'></span><p>Payment</p></div><div class='processName'><span class='arrow-right'></span><p>Order Confirmation</p></div><div class='arrow-right'></div></div></td></tr>";
		String TableHTML="";
		String rowHTML="";
		html.append(HeaderHTML);
		
		if(session!=null){//Session exists for user
			String UserId=(String)session.getAttribute("UserId");//UserId is the key
			String UserRole=(String) session.getAttribute("UserRole");//UserRole is the key
			//int cartItems=(Integer)session.getAttribute("cartItems");
			int cartItems=0;
			String LoginUserHTML="<tr><td colspan='9' class='total'><div id='pageTitle'><span>"+pageTitle+"</span></div><div id='loginId'><i class='fa fa-user' aria-hidden='true'></i><span>" +UserId+"</span></div><div id='CartDisplay'><span><i class='fa fa-shopping-cart' aria-hidden='true'></i> Cart : Items</span><span id='cartItemNumber'>("+cartItems+")</span></div></td></tr>";
			System.out.println("USER ROLE :"+UserRole);
			if(UserRole.equalsIgnoreCase("Customer")||UserRole.equalsIgnoreCase("Salesman")){
				ManagerLinksHTML="";
			}
			
			ProductDataStore productStore=new ProductDataStore(getServletContext().getRealPath("NewProductCatalog.xml"));
	    	HashMap<Integer,Product> productMapPrint=productStore.addProductsToMap();
	    	session.setAttribute("AllProductsMap", productMapPrint);
	    	
	    	Set<Map.Entry<Integer, Product>> entries=productMapPrint.entrySet();
			for(Map.Entry<Integer, Product> prodMap:entries){
				Product product=prodMap.getValue();
				//System.out.println(prodMap.getKey()+" :  "+product.getProductID()+"  "+product.getProductName()+" "+product.getBrand());
								
				String selectQuantity="";
				String checkBox;
				if(product.getQuantityAvailable()>0){
					selectQuantity="<select name='productQuantity'>";
					for(int i=0;i<=product.getQuantityAvailable();i++){
						selectQuantity=selectQuantity+"<option value="+i+">"+i+"</option>";
					}
					selectQuantity=selectQuantity+"</select>";
					checkBox="<input type='checkbox' name='productID' value='"+product.getProductID()+"'></input><input type='hidden' name='"+product.getProductID()+"' value='"+product.getCategory()+":"+product.getProductName()+":"+product.getBrand()+":"+product.getDescription()+":"+product.getPrice()+":"+product.getImage()+"'>";
				}
				else{
					checkBox="<input type='checkbox' name='productID' disabled value='"+product.getProductID()+"'></input><input type='hidden' name='"+product.getProductID()+"' value='"+product.getCategory()+":"+product.getProductName()+":"+product.getBrand()+":"+product.getDescription()+":"+product.getPrice()+":"+product.getImage()+"'>";
				}
				rowHTML=rowHTML+"<tr> 	<td>"+product.getImage()+"</td> 	<td>"+product.getProductID()+"</td> 	<td>"+product.getCategory()+"</td> 	<td>"+product.getProductName()+"</td> 	<td>"+product.getBrand()+"</td> 	<td>"+product.getDescription()+"</td> 	<td>"+product.getPrice()+"</td> 	<td>"+selectQuantity+"</td> <td>"+checkBox+"</td></tr>";
			}
			String productTable="<form action='addToCart' method='post'><tr><th>Image</th><th>ProductID</th><th>ProductCategory</th><th>ProductName</th><th>Brand</th><th>Description</th><th>Price</th><th>Select Quantity</th><th>Select Item</th></tr>"+rowHTML+"<tr><td colspan='9'><div id='AddToCartDiv'><button type='submit' id='AddToCartButton'>ADD TO CART</button></div></td></tr></form>";
			TableHTML="<table>"+CustomerLinksHTML+ManagerLinksHTML+LoginUserHTML+orderFlowHTML+productTable+"</table>";
		}
		else{//Session does not exist for user
			System.out.println("You are logged out. Please Login");
			TableHTML="<table><tr><th>You are logged out. Please Login</th></tr></table>";
		}
		
		String FooterHTML="</div><div class='footer' id='footer'><div id='contactText'><ul><h4>Contact</h4><li>Sujan Davangere Sunil</li><li>A20354703</li><li><a href='mailto:sdavange@hawk.iit.edu'>sdavange@hawk.iit.edu</a></li><li>CSP 595 - Assignment 2</li></ul></div></div></div></body></html>";
		html.append(TableHTML);
		html.append(FooterHTML);
		out.println(html);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("==============================Inside doPost() of ProductList Servlet==============================");
	}
}