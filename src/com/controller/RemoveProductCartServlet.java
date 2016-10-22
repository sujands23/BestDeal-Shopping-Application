package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model.pojo.Product;

public class RemoveProductCartServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("==============================Inside doPost() of RemoveProductCartServlet Servlet==============================");
		HttpSession session=request.getSession(false);
		PrintWriter out=response.getWriter();
		StringBuilder html=new StringBuilder();
		String TableHTML="";
		String FooterHTML="";
		String rowHtml="";
		
		String pageTitle="ORDER SUMMARY";
		String css="<link rel='stylesheet' href='./CSS/font-awesome-4.6.3/css/font-awesome.min.css'><link rel='stylesheet' type='text/css' href='./CSS/Common.css'><link rel='stylesheet' type='text/css' href='./CSS/HomePage.css'><link rel='stylesheet' href='./CSS/OrderFlowDisplay.css'><link rel='stylesheet' href='./CSS/OrderSummary.css'>";
		String HeaderHTML="<!DOCTYPE html><html><head><meta charset='UTF-8'><title>BestDeal</title>"+css+"</head><body><div id='container'><div id='headerDiv'><div id='header1'><header><div id='bestDealLogo'><a href='HomePage.html'><img src='./images/BestDealLogo.jpg' style='width:200px;height:75px;margin-left:30px;margin-top:15px;cursor:pointer;'></a></div></header><div id='search'><div id='searchDiv'><form method='get' class='searchBox'><input type='text' id='searchTextArea' name='search' placeholder='Search for products'></input><button type='submit' id='searchButton'>Search<i class='fa fa-search' aria-hidden='true'></i></button></form></div></div><div id='userLinks'><ul><li><a href='Logout'>Logout</a></li></ul></div></div><div id='header2'><div id='mainHeader2'> 				<nav><ul><li><span onmouseover='openNav()'>PRODUCTS<i class='fa fa-chevron-down' aria-hidden='true'></i></span></li><li><span>SERVICE</span></li><li><span>CONTACTS</span></li><li><span>OFFERS</span></li><li><span>MOBILE <i class='fa fa-mobile' aria-hidden='true'></i></span></li><li><span>TABLET <i class='fa fa-tablet' aria-hidden='true'></i></span></li><li><span>LAPTOP <i class='fa fa-laptop' aria-hidden='true'></i></span></li><li><span>TV <i class='fa fa-television' aria-hidden='true'></i></span></li></ul></nav></div></div></div><div id='center'>";
		String CustomerLinksHTML="<tr id='customerRow'><td colspan='6'><div id='customerLinks'><a href='ProductsList'>View Products</a></form><a href='MyOrders.html'>My Orders</a><a href=''>Write a Product review <i class='fa fa-pencil-square-o' aria-hidden='true'></i></a><a href='Trending'>Trending</a><a href='logout'>Logout <i class='fa fa-sign-out' aria-hidden='true'></i></a></div></td></tr>";
		String ManagerLinksHTML="<tr id='managerRow'><td colspan='6'><div id='managerLinks'><a href='AddProduct.html'>Add product <i class='fa fa-plus-square-o' aria-hidden='true'></i></a><a href='DeleteProduct.html'>Delete product <i class='fa fa-minus-square-o' aria-hidden='true'></i></a><a href='DataAnalytics'>Data Analytics <i class='fa fa-bar-chart' aria-hidden='true'></i></a></div></td></tr>";
		String orderFlowHTML="<tr><td colspan='6'><div id='orderFlowDisplay'><div class='processName'><span id='firstStep' class='arrow-right'></span><p>View Products</p></div><div class='processName' id='nowText'><span class='arrow-right' id='nowArrowBefore'></span><p>Order Summary</p></div><div class='processName'><span class='arrow-right' id='now'></span><p>Payment</p></div><div class='processName'><span class='arrow-right'></span><p>Order Confirmation</p></div><div class='arrow-right'></div></div></td></tr>";
		
		html.append(HeaderHTML);
		
		if(session!=null){
			String name=(String)session.getAttribute("user");
			String UserId=(String)session.getAttribute("UserId");//UserId is the key
			String UserRole=(String) session.getAttribute("UserRole");//UserRole is the key
			int CartItems=(Integer)session.getAttribute("cartItems");
			Double Total=(Double)session.getAttribute("total");
			
			System.out.println("UserId"+UserId);
			System.out.println("UserRole"+UserRole);
			
			int removeproductId=Integer.parseInt(request.getParameter("RemoveProductFromCart"));
			System.out.println("Removed product Id : "+removeproductId);
			
			Date deliveryDate=new Date();
			
			HashMap<Integer,Product> cartMap;
			cartMap=(HashMap<Integer, Product>) session.getAttribute("userOrder");
			cartMap.get(removeproductId).getPrice();
			
			System.out.println("+++++++++"+cartMap.get(removeproductId));
			Product deleteProduct=cartMap.get(removeproductId);
			System.out.println("deleteProduct - Price:"+deleteProduct.getPrice()+" |  Quantity:"+deleteProduct.getOrderQuantity());
			/*Total=Total-(deleteProduct.getPrice()*deleteProduct.getOrderQuantity());*/
			cartMap.remove(removeproductId);
			Set<Map.Entry<Integer, Product>> entries=cartMap.entrySet();
			for(Map.Entry<Integer, Product> prodMap:entries){
				Product product=prodMap.getValue();
				System.out.println("Cart after removing the Product : "+prodMap.getKey()+" :  "+product.getProductID()+"		|	"+product.getCategory()+"	|	"+product.getProductName()+"	|	"+product.getBrand()+"	|	"+product.getDescription()+"	|	"+product.getPrice()+"	|	"+product.getImage());
				rowHtml=rowHtml+"<tr><td><image src='./images/"+product.getImage()+"'></td><td><ul><li>"+product.getBrand()+"</li><li>"+product.getProductName()+"</li><li>"+product.getDescription()+"</li><li>Retailer</li></ul><span id='RemoveItemButtonSpan'><form action='RemoveProductFromCart' method='post'><button id='removeButton' name='RemoveProductFromCart' type='submit' value='"+product.getProductID()+"'>Remove<i class='fa fa-trash' aria-hidden='true'></i></button></form></span></td><td>"+product.getPrice()+"</td><td>"+product.getOrderQuantity()+"</td><td></td><td>"+deliveryDate+"</td> </tr>";
				//rowHtml=rowHtml+"<tr><td><image src='./images/"+prodImage+"'></td><td><ul><li>"+prodBrand+"</li><li>"+prodName+"</li><li>"+prodDescription+"</li><li>Retailer</li></ul><span id='RemoveItemButtonSpan'>                                           <form action='RemoveProductFromCart' method='post'><button id='removeButton' name='RemoveProductFromCart' type='submit' value='"+prodID+"'>Remove<i class='fa fa-trash' aria-hidden='true'></i></button>                </form></span></td><td>"+prodPrice+"</td><td>"+Quantity+"</td><td>"+SubTotal+"</td><td>"+deliveryDate+"</td> </tr>";
			}
			
			String LoginUserHTML="<tr><td colspan='6' class='total'><div id='pageTitle'><span>"+pageTitle+"</span></div><div id='loginId'><i class='fa fa-user' aria-hidden='true'></i><span>" +UserId+"</span></div><div id='CartDisplay'><span><i class='fa fa-shopping-cart' aria-hidden='true'></i> Cart : Items</span><span id='cartItemNumber'>("+CartItems+")</span></div></td></tr>";
			TableHTML="<table>"+CustomerLinksHTML+ManagerLinksHTML+LoginUserHTML+orderFlowHTML+"<tr><th>Image</th>     <th>ITEM</th>     <th>PRICE</th>     <th>QUANTITY</th>     <th>SUBTOTAL</th>     <th>DELIVERY DETAILS</th> </tr>"+rowHtml+" <tr> 	<td colspan='6' class='total'>     <div id='totalAmount'> 		<span id='totalAmountText'>TOTAL AMOUNT</span> 		<span id='totalAmountValue'>"+Total+"</span> 	</div> 	</td> </tr>  <tr> 	<td colspan='6'> 		<div id='placeOrderDiv'><button value='placeOrder' id='placeOrderButton'><a href='Payment'>PLACE ORDER</a></button></div> 	</td> </tr> </table>";
		}else{
			TableHTML="<table><tr><td><h2>Session does not exist for the user.Please <a href='Login.html'>login</a> again</h2></td></tr></table>";
		}
		FooterHTML="</div><div class='footer' id='footer'><div id='contactText'><ul><h4>Contact</h4><li>Sujan Davangere Sunil</li><li>A20354703</li><li><a href='mailto:sdavange@hawk.iit.edu'>sdavange@hawk.iit.edu</a></li><li>CSP 595 - Assignment 2</li></ul></div></div></div></body></html>";
		html.append(TableHTML);
		html.append(FooterHTML);
		out.println(html);
	}
}