package com.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("==============================Inside doGet() of Logout servlet==============================");
		HttpSession session=request.getSession(false);
		PrintWriter out=response.getWriter();
		          
		System.out.println("Inside session exists");
		if(session!=null){//If sesssion exists, invalidate
			System.out.println("Inside session exists");
			session.removeAttribute("user");
			session.invalidate();
			out.println("You are logged out");
			RequestDispatcher rd=request.getRequestDispatcher("Login.html");  
		    rd.forward(request, response);
		}
		else{
			out.println("Session does not exist");
		}
	}
}
