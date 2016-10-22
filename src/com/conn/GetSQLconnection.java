package com.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetSQLconnection {
	static final String JDBC_Driver="com.mysql.jdbc.Driver";
	static final String DB_URL="jdbc:mysql://localhost:3306/BestDealDatabase?useSSL=false";
	static final String User_Name="root";
	static final String Password="root";
	
	public static void main(String[] args){
		Connection conn=null;
		try{
			Class.forName(JDBC_Driver);//Register the Driver
			conn=(Connection) DriverManager.getConnection(DB_URL,User_Name,Password);
			if(conn!=null){
				System.out.println("returning connection"+conn);
				int OrderId;
				String GetMaxOrderIdSQL="SELECT MAX(ORDERID) FROM ORDERS;";
				PreparedStatement prepstmt=(PreparedStatement) conn.prepareStatement(GetMaxOrderIdSQL);
				ResultSet rs= prepstmt.executeQuery();
				if(!rs.next()){
					System.out.println("Orders table is empty");
					OrderId=1;
				}
				else{
					OrderId=rs.getInt("MAX(ORDERID)");
					System.out.println("Order Id exists, Maximum Order Id is "+OrderId);
				}
				//return conn;
			}
			/*else{
				return null;
			}*/
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
		//return conn;
	}
}