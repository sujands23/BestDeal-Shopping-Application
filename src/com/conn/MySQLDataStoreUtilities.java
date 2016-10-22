package com.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLDataStoreUtilities {
	static final String JDBC_Driver="com.mysql.jdbc.Driver";
	static final String DB_URL="jdbc:mysql://localhost:3306/BestDealDatabase?useSSL=false";
	static final String User_Name="root";
	static final String Password="root";
	
	public int InsertIntoOrdersTable(String UserId,Double Price,int orderedItems,String ShippingAddress,String CreditCardNo,String orderStatus){
		System.out.println("Inside MySQLDataStoreUtilities class InsertIntoOrdersTable() method");
		Connection conn=null;
		int OrderId=0;
		try{
			Class.forName(JDBC_Driver);//Register the Driver
			conn=(Connection) DriverManager.getConnection(DB_URL,User_Name,Password);
			if(conn!=null){
				String InsertNewOrderSQL="INSERT into ORDERS values(NULL,'"+UserId+"',"+Price+","+orderedItems+",'"+ShippingAddress+"','"+CreditCardNo+"','"+orderStatus+"');";
				PreparedStatement prepstmtInsert=(PreparedStatement) conn.prepareStatement(InsertNewOrderSQL);
				int rows = prepstmtInsert.executeUpdate();
				if(rows>0){
					System.out.println("Order inserted successfully");
				}
				
				String getNewOrderIdSQL="SELECT LAST_INSERT_ID();";
				PreparedStatement prepstmtGet=(PreparedStatement) conn.prepareStatement(getNewOrderIdSQL);
				ResultSet rsGet = prepstmtGet.executeQuery();
				if(!rsGet.next()){
					System.out.println("Order not placed");
				}
				else{
					OrderId=rsGet.getInt("LAST_INSERT_ID()");
					System.out.println("Order is placed and new Order Id is : "+OrderId);
				}
			}
			else
			{
				System.out.println("Connection is null");
			}
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
		catch(Exception e)
		{
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
		return OrderId;
	}
	
	public void insertIntoOrderProduct(int orderId,int[] productId,int productQuantity[]){
		System.out.println("Inside MySQLDataStoreUtilities class insertIntoOrderProduct() method");
		Connection conn=null;
		try{
			Class.forName(JDBC_Driver);//Register the Driver
			conn=(Connection) DriverManager.getConnection(DB_URL,User_Name,Password);
			if(conn!=null){
				String OrderProductQty="";
				String querySeperator;
					for(int i=0;i<productId.length;i++){
						OrderProductQty=OrderProductQty+"("+orderId+","+productId[i]+","+productQuantity[i]+"),";
					}
				OrderProductQty=OrderProductQty.substring(0,OrderProductQty.length()-1);
				String insertIntoOrderProductSQL="INSERT INTO ORDERPRODUCT(ORDERID,PRODUCT_ID,ORDERQUANTITY) VALUES"+OrderProductQty+";";
				System.out.println("New Query is :"+insertIntoOrderProductSQL);
				PreparedStatement prepstmtInsert=(PreparedStatement) conn.prepareStatement(insertIntoOrderProductSQL);
				int rows = prepstmtInsert.executeUpdate();
				if(rows>0){
					System.out.println("OrderId and ProductId inserted successfully");
				}
			}
			else
			{
				System.out.println("Connection is null");
			}
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
		catch(Exception e)
		{
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
	
	public String getOrder(int orderId){
		System.out.println("Inside MySQLDataStoreUtilities class getOrder() method");
		Connection conn=null;
		int OrderId,ProductId,ProductQuantity;
		Double OrderTotal;
		String ProductCategory,ProductBrand,ProductName,OrderStatus;
		String orderDetails="";
		String orderFields="";
		String productFields="";
		try{
			Class.forName(JDBC_Driver);//Register the Driver
			conn=(Connection) DriverManager.getConnection(DB_URL,User_Name,Password);
			if(conn!=null){
				String getOrderDetailsSQL="SELECT DISTINCT ORDERS.ORDERID,ORDERS.OrderStatus,ORDERS.OrderAmount,ORDERPRODUCT.PRODUCT_ID,ORDERQUANTITY,CATEGORY,BRAND,PRODUCT_NAME FROM ORDERPRODUCT INNER JOIN PRODUCTS ON ORDERPRODUCT.PRODUCT_ID=PRODUCTS.PRODUCT_ID INNER JOIN ORDERS ON ORDERPRODUCT.ORDERID=ORDERS.ORDERID WHERE ORDERPRODUCT.ORDERID="+orderId+";";
				PreparedStatement prepstmtGet=(PreparedStatement) conn.prepareStatement(getOrderDetailsSQL);
				ResultSet rs = prepstmtGet.executeQuery();
				if(rs.next()){
					OrderId=rs.getInt("ORDERID");
					OrderStatus=rs.getString("OrderStatus");
					OrderTotal=rs.getDouble("OrderAmount");
					orderDetails=orderDetails+OrderId+"|"+OrderStatus+"|"+OrderTotal;
					orderFields="<table id='orderTable'><tr><th colspan='1'>Order ID	:	<b>"+OrderId+"</b></th><th colspan='1'>Order Status	:<b>"+OrderStatus+"</b></th></tr><tr><td><ul><li>Order Date			:	<b></b></li><li>Total Amount Paid 			:	<b>"+OrderTotal+"</b></li></ul><h4>Order contents</h4><hr><ul>";
					
					do{
						ProductId=rs.getInt("PRODUCT_ID");
						ProductQuantity=rs.getInt("ORDERQUANTITY");
						ProductCategory=rs.getString("CATEGORY");
						ProductBrand=rs.getString("BRAND");
						ProductName=rs.getString("PRODUCT_NAME");
						
						//productFields=orderDetails+"|"+ProductId+"|"+ProductCategory+"|"+ProductBrand+"|"+ProductName+"|"+ProductQuantity;
						productFields=productFields+"<li>"+ProductId+"|"+ProductCategory+"|"+ProductBrand+"|"+ProductName+"|"+ProductQuantity+"</li>";
					}while(rs.next());
					
					orderDetails=orderFields+productFields+"</ul></td><td><form action='cancelOrder' method='post'><button type='submit' id='cancelOrderButton' value='"+OrderId+"' name='cancelProduct'>Cancel Order <i class='fa fa-times' aria-hidden='true'></i></button></form></td></tr></table>";
					System.out.println("Order Details : "+orderDetails);
				}
				else{
					System.out.println("Order Id is invalid");
					orderDetails="<table><tr><th style='color: red;background-color: white;border: 2px solid red;'>Order Id is invalid!</th></tr></table>";
				}
			}
			else
			{
				System.out.println("Connection is null");
			}
			
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
		catch(Exception e)
		{
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
		return orderDetails;
	}
	
	///////////////////////////////////////////////////////////////////////////////
	
	public String TopFiveMostSoldProducts(){
		System.out.println("Inside MySQLDataStoreUtilities class TopFiveMostSoldProducts() method");
		Connection conn=null;
		String productName;
		String productId;
		String totalSoldQuantity;
		String rowHtml="";
		try{
			Class.forName(JDBC_Driver);//Register the Driver
			conn=(Connection) DriverManager.getConnection(DB_URL,User_Name,Password);
			if(conn!=null){
				String getOrderDetailsSQL="select sum(orderquantity),ORDERPRODUCT.product_id,PRODUCTS.PRODUCT_NAME from ORDERPRODUCT INNER JOIN PRODUCTS ON ORDERPRODUCT.PRODUCT_ID=PRODUCTS.PRODUCT_ID where orderproduct.product_id in (select  product_id from orderproduct) group by product_id order by sum(orderquantity) desc limit 5";
				PreparedStatement prepstmtGet=(PreparedStatement) conn.prepareStatement(getOrderDetailsSQL);
				ResultSet rs = prepstmtGet.executeQuery();
				if(rs.next()){
					do{
						productName=rs.getString("PRODUCT_NAME");
						productId=rs.getString("product_id");
						totalSoldQuantity=rs.getString("sum(orderquantity)");
						System.out.println("Product Name : "+productName+" Quantity Sold : "+totalSoldQuantity);
						rowHtml=rowHtml+"<tr><td>"+productName+"</td><td>"+totalSoldQuantity+"</td></tr>";
					}while(rs.next());
					
				}
				else{
					System.out.println("Order Id is invalid");
				}
			}
			else
			{
				System.out.println("Connection is null");
			}
			
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
		catch(Exception e)
		{
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
		return rowHtml;
	}
	
	////////////////////////////////////////////////////////
	public void DeleteOrder(int OrderId){
		System.out.println("Inside MySQLDataStoreUtilities class DeleteOrder() method");
		Connection conn=null;
		try{
			Class.forName(JDBC_Driver);//Register the Driver
			conn=(Connection) DriverManager.getConnection(DB_URL,User_Name,Password);
			if(conn!=null){
				String deleteOrderProductSQL="DELETE FROM OrderProduct WHERE ORDERID='"+OrderId+"';";
				PreparedStatement prepstmtGet=(PreparedStatement) conn.prepareStatement(deleteOrderProductSQL);
				ResultSet rs1 = prepstmtGet.executeQuery();
				String deleteOrderSQL="DELETE FROM Orders WHERE ORDERID='"+OrderId+"';";
				PreparedStatement prepstmtDelete=(PreparedStatement) conn.prepareStatement(deleteOrderSQL);
				ResultSet rs2 = prepstmtDelete.executeQuery();
				/*if(rs.next()){
					System.out.println("Deleted order from orderProduct");
				}
				else{
					System.out.println("Order Id is invalid");
				}*/
			}
			else
			{
				System.out.println("Connection is null");
			}
			
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
		catch(Exception e)
		{
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