package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model.pojo.*;

public class CartServlet extends HttpServlet {
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Add to card button clicked");
		System.out.println("==============================Inside doPost() of Cart Servlet==============================");
		HttpSession session=request.getSession(false);
		PrintWriter out=response.getWriter();
		StringBuilder html=new StringBuilder();
		String TableHTML;
		String FooterHTML="";
		
		if(session!=null){
			String name=(String)session.getAttribute("user");
			String UserId=(String)session.getAttribute("UserId");//UserId is the key
			String UserRole=(String) session.getAttribute("UserRole");//UserRole is the key
			
			System.out.println("UserId"+UserId);
			System.out.println("UserRole"+UserRole);
			
			String productID[]=request.getParameterValues("productID");//Value from CheckBox-Array
			String productQuantity[]=request.getParameterValues("productQuantity");//Value from Select Dropdown
			
			String productFields[];
			int prodID;
			String prodCategory;
			String prodName;
			String prodBrand;
			String prodDescription;
			Double prodPrice;
			String prodImage;
			
			int CartItems=0;//Number of items in the cart
			int Quantity=0;//Quantity of each product ordered
			Double SubTotal=0.0;//SubTotal of each product ordered
			Double Total=0.0;//Total price of all the items in the cart
			
			String pageTitle="ORDER SUMMARY";
			String css="<link rel='stylesheet' href='./CSS/font-awesome-4.6.3/css/font-awesome.min.css'><link rel='stylesheet' type='text/css' href='./CSS/Common.css'><link rel='stylesheet' type='text/css' href='./CSS/HomePage.css'><link rel='stylesheet' href='./CSS/OrderFlowDisplay.css'><link rel='stylesheet' href='./CSS/OrderSummary.css'>";
			String HeaderHTML="<!DOCTYPE html><html><head><meta charset='UTF-8'><title>BestDeal</title>"+css+"</head><body><div id='container'><div id='headerDiv'><div id='header1'><header><div id='bestDealLogo'><a href='HomePage.html'><img src='./images/BestDealLogo.jpg' style='width:200px;height:75px;margin-left:30px;margin-top:15px;cursor:pointer;'></a></div></header><div id='search'><div id='searchDiv'><form method='get' class='searchBox'><input type='text' id='searchTextArea' name='search' placeholder='Search for products'></input><button type='submit' id='searchButton'>Search<i class='fa fa-search' aria-hidden='true'></i></button></form></div></div><div id='userLinks'><ul><li><a href='Logout'>Logout</a></li></ul></div></div><div id='header2'><div id='mainHeader2'><nav><ul><li><span>MOBILE <i class='fa fa-mobile' aria-hidden='true'></i></span></li><li><span>TABLET <i class='fa fa-tablet' aria-hidden='true'></i></span></li><li><span>LAPTOP <i class='fa fa-laptop' aria-hidden='true'></i></span></li><li><span>TV <i class='fa fa-television' aria-hidden='true'></i></span></li></ul></nav></div></div></div><div id='center'>";
			String CustomerLinksHTML="<tr id='customerRow'><td colspan='6'><div id='customerLinks'><a href='ProductsList'>View Products</a></form><a href='MyOrders.html'>My Orders</a><a href=''>Write a Product review <i class='fa fa-pencil-square-o' aria-hidden='true'></i></a><a href='Trending'>Trending</a><a href='logout'>Logout <i class='fa fa-sign-out' aria-hidden='true'></i></a></div></td></tr>";
			String ManagerLinksHTML="<tr id='managerRow'><td colspan='6'><div id='managerLinks'><a href='AddProduct.html'>Add product <i class='fa fa-plus-square-o' aria-hidden='true'></i></a><a href='DeleteProduct.html'>Delete product <i class='fa fa-minus-square-o' aria-hidden='true'></i></a><a href='DataAnalytics'>Data Analytics <i class='fa fa-bar-chart' aria-hidden='true'></i></a></div></td></tr>";
			String orderFlowHTML="<tr><td colspan='6'><div id='orderFlowDisplay'><div class='processName'><span id='firstStep' class='arrow-right'></span><p>View Products</p></div><div class='processName' id='nowText'><span class='arrow-right' id='nowArrowBefore'></span><p>Order Summary</p></div><div class='processName'><span class='arrow-right' id='now'></span><p>Payment</p></div><div class='processName'><span class='arrow-right'></span><p>Order Confirmation</p></div><div class='arrow-right'></div></div></td></tr>";
			
			html.append(HeaderHTML);
			
			String productDetails;
			
			//Delivery date after 14 days
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			String todate = dateFormat.format(date);
			Calendar cal = Calendar.getInstance();
	        cal.add(Calendar.DATE, +14);
	        Date todate1 = cal.getTime();    
	        String deliveryDate = dateFormat.format(todate1);
	        
	        HashMap<Integer,Product> cartMap=new HashMap<Integer, Product>();
	        if(UserRole.equals("Customer")||UserRole.equals("Salesman")){
				ManagerLinksHTML="";
			}
			String rowHtml="";
			
			for(int i=0;i<productID.length;i++){
				productDetails=request.getParameter(""+productID[i]+"");//value from Hidden input
				
				productFields=productDetails.split(":");
				
				prodCategory=productFields[0];
				prodName=productFields[1];
				prodBrand=productFields[2];
				prodDescription=productFields[3];
				prodPrice=Double.parseDouble(productFields[4]);
				prodImage=productFields[5];
				
				prodID=Integer.parseInt(productID[i]);
				
				Quantity=Integer.parseInt(productQuantity[i]);
				SubTotal=Quantity*prodPrice;
				Total=Total+SubTotal;
				
				CartItems=CartItems+Quantity;
				
				cartMap.put(prodID,new Product(prodID,prodCategory,prodName,prodBrand,prodDescription,prodPrice,prodImage,Quantity));
				rowHtml=rowHtml+"<tr><td><image src='./images/"+prodImage+"'></td><td><ul><li>"+prodBrand+"</li><li>"+prodName+"</li><li>"+prodDescription+"</li><li>Retailer</li></ul><span id='RemoveItemButtonSpan'><form action='RemoveProductFromCart' method='post'><button id='removeButton' name='RemoveProductFromCart' type='submit' value='"+prodID+"'>Remove<i class='fa fa-trash' aria-hidden='true'></i></button></form></span></td><td>"+prodPrice+"</td><td>"+Quantity+"</td><td>"+SubTotal+"</td><td>"+deliveryDate+"</td> </tr>";
			}
			
			session.setAttribute("userOrder",cartMap);
			session.setAttribute("total", Total);
			session.setAttribute("cartItems", CartItems);
			
			String LoginUserHTML="<tr><td colspan='6' class='total'><div id='pageTitle'><span>"+pageTitle+"</span></div><div id='loginId'><i class='fa fa-user' aria-hidden='true'></i><span>" +UserId+"</span></div><div id='CartDisplay'><span><i class='fa fa-shopping-cart' aria-hidden='true'></i> Cart : Items</span><span id='cartItemNumber'>("+CartItems+")</span></div></td></tr>";
			TableHTML="<table>"+CustomerLinksHTML+ManagerLinksHTML+LoginUserHTML+orderFlowHTML+"<tr><th>Image</th>     <th>ITEM</th>     <th>PRICE</th>     <th>QUANTITY</th>     <th>SUBTOTAL</th>     <th>DELIVERY DETAILS</th> </tr>"+rowHtml+" <tr> 	<td colspan='6' class='total'>     <div id='totalAmount'> 		<span id='totalAmountText'>TOTAL AMOUNT</span> 		<span id='totalAmountValue'>"+Total+"</span> 	</div> 	</td> </tr>  <tr> 	<td colspan='6'> 		<div id='placeOrderDiv'><button value='placeOrder' id='placeOrderButton'><a href='Payment'>PLACE ORDER</a></button></div> 	</td> </tr> </table>";
		}
		else{
			TableHTML="<table><tr><td><h2>Session does not exist for the user.Please <a href='Login.html'>login</a> again</h2></td></tr></table>";
		}
		FooterHTML="</div><div class='footer' id='footer'><div id='contactText'><ul><h4>Contact</h4><li>Sujan Davangere Sunil</li><li>A20354703</li><li><a href='mailto:sdavange@hawk.iit.edu'>sdavange@hawk.iit.edu</a></li><li>CSP 595 - Assignment 2</li></ul></div></div></div></body></html>";
		html.append(TableHTML);
		html.append(FooterHTML);
		out.println(html);
	}
}