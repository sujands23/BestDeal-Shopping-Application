package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.conn.GetSQLconnection;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;


public class SignupServlet extends HttpServlet {

	static final String JDBC_Driver="com.mysql.jdbc.Driver";
	static final String DB_URL="jdbc:mysql://localhost:3306/BestDealDatabase";
	static final String User_Name="root";
	static final String Password="root";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("==============================Inside doPost() of LoginServlet==============================");
		PrintWriter out=response.getWriter();
		
		String EnteredName=request.getParameter("Name");
		String EnteredUserId=request.getParameter("EmailId");
		String EnteredPassword=request.getParameter("Password");
		String EnteredRepeatPassword=request.getParameter("RepeatPassword");
		String EnteredUserRole=request.getParameter("userRoleSelect");
		
		System.out.println("Entered parameters - Name	:	"+EnteredName+" EmailId	:	"+EnteredUserId+"	Password	:	"+EnteredPassword+" RepeatPassword		:	"+EnteredRepeatPassword+" UserRole		:	"+EnteredUserRole);	
		
		if(EnteredPassword!=null&&EnteredRepeatPassword!=null){
			if(!EnteredPassword.equals(EnteredRepeatPassword)){
				out.println("Error! --- Password and RepeatPassword should be same");
			}
		}
			
		StringBuffer html=new StringBuffer();
		String message=" ";
		String errorMessage=" ";
		boolean errorFlag=false;
		
		Connection conn=null;
	
		try{
			//String CheckUserSql="";
			//conn=GetSQLconnection.getConnection();
			
			Class.forName(JDBC_Driver);//Register the Driver
			conn=(Connection) DriverManager.getConnection(DB_URL,User_Name,"root");
			
			String CheckUserSql="SELECT * FROM USERACCOUNTS where UserName='"+EnteredUserId+"';";//Check if the entered user already exist
			String CreateUserSql="INSERT into USERACCOUNTS values('"+EnteredUserId+"','"+EnteredPassword+"','"+EnteredUserRole+"');";
			
			System.out.println("Outside connection : "+conn);
			System.out.println("Query is : "+CheckUserSql);
			PreparedStatement prepstmt=(PreparedStatement) conn.prepareStatement(CheckUserSql);
			ResultSet rs= prepstmt.executeQuery();
			
			if(!rs.next()){//Checking for Entered UserID in DB.If account does not exist, then create new account.
				System.out.println("User account does not exist");
				System.out.println("Query is : "+CreateUserSql);
				PreparedStatement prepstmtInsert=(PreparedStatement) conn.prepareStatement(CreateUserSql);
				int rows = prepstmtInsert.executeUpdate();
				if(rows>0){
					System.out.println("User account created successfully");
					out.println("User account created successfully");
					
					RequestDispatcher rd=request.getRequestDispatcher("Login.html");  
				    rd.forward(request, response);
				}
			}
			else{//User account already exist. 
				System.out.println("Error! --- Account with entered UserId already exist");
				out.println("Error! --- Account with entered UserId already exist");
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