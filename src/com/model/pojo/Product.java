package com.model.pojo;

import java.util.ArrayList;
import java.util.List;

public class Product {
	String retailer;
	String productName;
	int productID;
	String category;
	String brand;
	String description;
	double price;
	String image;
	List<String> accessories;
	int quantityAvailable;
	int orderQuantity;
	
	public int getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(int orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public Product(){
		accessories=new ArrayList<String>();
	}
	
	public Product(int productID,String category,String productName,String brand,String description,double Price,String image,int orderQuantity){
		this.productID=productID;
		this.category=category;
		this.productName=productName;
		this.brand=brand;
		this.description=description;
		this.price=price;
		this.image=image;
		this.orderQuantity=orderQuantity;
	}
	public String getRetailer() {
		return retailer;
	}

	public void setRetailer(String retailer) {
		this.retailer = retailer;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBrand() {
		return brand;
	}

	public void setbrand(String brand) {
		this.brand = brand;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<String> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<String> accessories) {
		this.accessories = accessories;
	}

	public int getQuantityAvailable() {
		return quantityAvailable;
	}

	public void setQuantityAvailable(int quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}
	
}
