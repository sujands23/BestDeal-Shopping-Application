package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model.pojo.Product;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.poc.sujan.ProductDataStore;


public class LoginServlet extends HttpServlet {
      
	static final String JDBC_Driver="com.mysql.jdbc.Driver";
	static final String DB_URL="jdbc:mysql://localhost:3306/BestDealDatabase";
	static final String User_Name="root";
	static final String Password="root";

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("==============================Inside doPost() of Login Servlet==============================");
		
		String EnteredUserId=request.getParameter("EmailId");
		String EnteredPassword=request.getParameter("Password");
		String EnteredUserRole=request.getParameter("UserRole");
		System.out.println("Entered parameters - Name	:	EmailId	:	"+EnteredUserId+"	Password	:	"+EnteredPassword+"		UserRole	:	"+EnteredUserRole);
		
		PrintWriter out=response.getWriter();
		
		StringBuilder html=new StringBuilder();
		StringBuilder MobileListHTML=new StringBuilder();
		StringBuilder TabletListHTML=new StringBuilder();
		StringBuilder LaptopListHTML=new StringBuilder();
		StringBuilder TVListHTML=new StringBuilder();
		String sideNavigation="";
		
		Connection conn=null;
		
		String LoginSql="SELECT * FROM USERACCOUNTS where USERNAME='"+EnteredUserId+"';";
		
		try{
			Class.forName(JDBC_Driver);//Register the Driver
			conn=(Connection) DriverManager.getConnection(DB_URL,User_Name,"root");
			
			System.out.println("Outside connection : "+conn);
			System.out.println("Query is : "+LoginSql);
			PreparedStatement prepstmt=(PreparedStatement) conn.prepareStatement(LoginSql);
			ResultSet rs= prepstmt.executeQuery();
			
			if(!rs.next()){//Checking for Entered UserID in DB
				System.out.println("Error! --- User account does not exist");
				out.println("Error! --- User account does not exist");
			}
			else{
				System.out.println("User account exists");
				do{
					String UserIdDB = rs.getString("Username");
					String PasswordDB = rs.getString("Passwords");
					String UserRoleDB=rs.getString("Role");

					System.out.println("DB UserName : " + UserIdDB);
					System.out.println("DB password : " + PasswordDB);
					
					if(EnteredPassword.equals(PasswordDB)){//Checking if entered password and DB password are same
						System.out.println("Entered password valid");
						//out.println("You are now logged in as : "+UserIdDB+" .You are a "+UserRoleDB);
						
						HttpSession session=request.getSession(true);
						session.setAttribute("UserId",UserIdDB);
						session.setAttribute("UserRole", UserRoleDB);

						int cartItems=0;
						String pageTitle="Home";
						String css="<link rel='stylesheet' href='./CSS/font-awesome-4.6.3/css/font-awesome.min.css'><link rel='stylesheet' type='text/css' href='./CSS/Common.css'><link rel='stylesheet' type='text/css' href='./CSS/HomePage.css'><link rel='stylesheet' type='text/css' href='./CSS/Payment.css'>";
						String HeaderHTML="<!DOCTYPE html><html><head><meta charset='UTF-8'><title>BestDeal</title>"+css+"</head><body><div id='container'><div id='headerDiv'><div id='header1'><header><div id='bestDealLogo'><a href='HomePage.html'><img src='./images/BestDealLogo.jpg' style='width:200px;height:75px;margin-left:30px;margin-top:15px;cursor:pointer;'></a></div></header><div id='search'><div id='searchDiv'><form method='get' class='searchBox'><input type='text' id='searchTextArea' name='search' placeholder='Search for products'></input><button type='submit' id='searchButton'>Search<i class='fa fa-search' aria-hidden='true'></i></button></form></div></div><div id='userLinks'><ul><li><a href='Logout'>Logout</a></li></ul></div></div><div id='header2'><div id='mainHeader2'><nav><ul><li><span>MOBILE <i class='fa fa-mobile' aria-hidden='true'></i></span></li><li><span>TABLET <i class='fa fa-tablet' aria-hidden='true'></i></span></li><li><span>LAPTOP <i class='fa fa-laptop' aria-hidden='true'></i></span></li><li><span>TV <i class='fa fa-television' aria-hidden='true'></i></span></li></ul></nav></div></div></div><div id='center'>";
						String CustomerLinksHTML="<tr id='customerRow'><td colspan='6'><div id='customerLinks'><a href='ProductsList'>View Products</a></form><a href='MyOrders.html'>My Orders</a><a href=''>Write a Product review <i class='fa fa-pencil-square-o' aria-hidden='true'></i></a><a href='Trending'>Trending</a><a href='logout'>Logout <i class='fa fa-sign-out' aria-hidden='true'></i></a></div></td></tr>";
						String ManagerLinksHTML="<tr id='managerRow'><td colspan='6'><div id='managerLinks'><a href='AddProduct.html'>Add product <i class='fa fa-plus-square-o' aria-hidden='true'></i></a><a href='DeleteProduct.html'>Delete product <i class='fa fa-minus-square-o' aria-hidden='true'></i></a><a href='DataAnalytics'>Data Analytics <i class='fa fa-bar-chart' aria-hidden='true'></i></a></div></td></tr>";
						String LoginUserHTML="<tr><td colspan='6' class='total'><div id='pageTitle'><span>"+pageTitle+"</span></div><div id='loginId'><span><i class='fa fa-user' aria-hidden='true'></i>"  +UserIdDB+"</span></div><div id='CartDisplay'><span><i class='fa fa-shopping-cart' aria-hidden='true'></i> Cart : Items</span><span id='cartItemNumber'>("+cartItems+")</span></div></td></tr>";
						if(UserRoleDB.equals("Customer")||UserRoleDB.equals("Salesman")){
							ManagerLinksHTML="";
						}
						
						ProductDataStore productStore=new ProductDataStore(getServletContext().getRealPath("NewProductCatalog.xml"));
				    	HashMap<String,Product> productMapPrint=productStore.addProductsToMap();
				    	
				    	Set<Map.Entry<String, Product>> entries=productMapPrint.entrySet();
				    	//sidebarListHTML.append("<div id='sidebar' class='sidenav'><ul>");
						for(Map.Entry<String, Product> prodMap:entries){
							Product product=prodMap.getValue();
							
							if(product.getCategory().equalsIgnoreCase("Mobile")){
								MobileListHTML.append("<li><a href='ViewProductDetail?productId="+product.getProductID()+"'>"+product.getBrand()+" "+product.getProductName()+"</a></li>");
								System.out.println("MOBILES : "+product.getProductName());
							}

							if(product.getCategory().equalsIgnoreCase("Tablet")){
								TabletListHTML.append("<li><a href='ViewProductDetail?productId="+product.getProductID()+"'>"+product.getBrand()+" "+product.getProductName()+"</a></li>");
								System.out.println("TABLETS : "+product.getProductName());
							}
							
							if(product.getCategory().equalsIgnoreCase("Laptop")){
								LaptopListHTML.append("<li><a href='ViewProductDetail?productId="+product.getProductID()+"'>"+product.getBrand()+" "+product.getProductName()+"</a></li>");
								System.out.println("LAPTOPS : "+product.getProductName());
							}
							
							if(product.getCategory().equalsIgnoreCase("TV")){
								TVListHTML.append("<li><a href='ViewProductDetail?productId="+product.getProductID()+"'>"+product.getBrand()+" "+product.getProductName()+"</a></li>");
								System.out.println("TV's : "+product.getProductName());
							}
						}	
						String centerImages="<div id='centerContents'> <ul> <li><img src='./images/Laptop.jpg' style='width:400px;height:400px;'></li> <li><img src='./images/TV.jpg' style='width:400px;height:400px;'></li> </ul> <ul> <li><img src='./images/Mobile.jpg' style='width:400px;height:400px;'></li> <li><img src='./images/Tablet2.jpg' style='width:400px;height:400px;'></li> </ul> </div>";
						sideNavigation="<tr><td><form id='productDetailForm' action='ViewProductDetail' method='post'><div id='sidebar' class='sidenav'><ul><li><h4>Mobiles</h4><ul>"+MobileListHTML+"</ul></li><li><h4>Tablets</h4><ul>"+TabletListHTML+"</ul></li><li><h4>Laptops</h4><ul>"+LaptopListHTML+"</ul></li><li><h4>TV's</h4><ul>"+TVListHTML+"</ul></li></ul></div>"+centerImages+"</form></td></tr>";
						
						//String testReview="<tr><td><div id='testReview'><form action='ViewProductDetail' method='post'><li><button type='submit' name='reviewProduct' value='103' id='WriteReviewButton'><i class='fa fa-pencil-square-o' aria-hidden='true'></i> Write product Review</button></li></form></div></td></tr>";
						String TableHTML="<table>"+CustomerLinksHTML+ManagerLinksHTML+LoginUserHTML+sideNavigation+"</table>";
						
						String FooterHTML="</div><div class='footer' id='footer'><div id='contactText'><ul><h4>Contact</h4><li>Sujan Davangere Sunil</li><li>A20354703</li><li><a href='mailto:sdavange@hawk.iit.edu'>sdavange@hawk.iit.edu</a></li><li>CSP 595 - Assignment 2</li></ul></div></div></div></body></html>";
						html.append(HeaderHTML);
						html.append(TableHTML);
						html.append(FooterHTML);
					}
					else{
						System.out.println("Error! --- Invalid password entered");
						out.println("Error! --- Invalid Password");
					}
					out.print(html);
				}while(rs.next());
			}
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			try
			{
			if(conn!=null)
				conn.close();
			}
			catch(SQLException se)
			{
				se.printStackTrace();
			}
		}
	}
}