package com.conn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.model.pojo.Reviews;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoDBDataStoreUtilities {
	static DBCollection reviewCollection;
	public static MongoClient mongo;
	public static void getConnection(){
		mongo=new MongoClient("localhost",27017);
		
		DB db=mongo.getDB("productReviews");
		reviewCollection=db.getCollection("reviews");
	}
	
	public static boolean insertReview(int productId,String ProductCategory,String ProductBrand,String ProductName, String ManufacturerRebate, String ProductPrice, String RetailerName, String RetailerZip, String RetailerCity, String RetailerState, String ProductOnSale,String UserID,int UserAge, String UserGender,String UserOccupation, int ReviewRating, String ReviewText){
		System.out.println("Inside MongoDBDataStoreUtilities class insertReview() method");
		Boolean InsertToDBFlag=false;
		
		int productID=productId;
		String productCategory=ProductCategory;
		String productBrand=ProductBrand;
		String productName=ProductName;
		String manufacturerRebate=ManufacturerRebate;
		String productPrice=ProductPrice;
		
		String retailerName=RetailerName;
		String retailerZip=RetailerZip;
		String retailerCity=RetailerCity;
		String retailerState=RetailerState;
		String productOnSale=ProductOnSale;
		
		String userId=UserID;
		int userAge=UserAge;
		String userGender=UserGender;
		String userOccupation=UserOccupation;
		
		int reviewRating=ReviewRating;
		Date reviewDate=new Date();
		String reviewText=ReviewText;
		
		getConnection();
		BasicDBObject dbObj=new BasicDBObject();
		dbObj.put("productId", productID);
		dbObj.put("productCategory",productCategory);
		dbObj.put("productBrand",productBrand);
		dbObj.put("productName",productName);
		dbObj.put("manufacturerRebate",manufacturerRebate);//Yes/No;
		dbObj.put("productPrice",productPrice);
		dbObj.put("retailerName",retailerName);
		dbObj.put("retailerZip",retailerZip);
		dbObj.put("retailerCity",retailerCity);
		dbObj.put("retailerState",retailerState);
		dbObj.put("productOnSale",productOnSale);//Yes/No
		dbObj.put("userId",userId);
		dbObj.put("userAge",userAge);
		dbObj.put("userGender",userGender);
		dbObj.put("userOccupation",userOccupation);
		dbObj.put("reviewRating",reviewRating);
		dbObj.put("reviewDate",reviewDate);
		dbObj.put("reviewText",reviewText);
		
		BasicDBObject reviewDocument = new BasicDBObject();
		reviewCollection.insert(dbObj);
		InsertToDBFlag=true;
		mongo.close();
		return InsertToDBFlag;
	}
	
	public static ArrayList<Reviews> selectReview(int productId){
		System.out.println("Inside MongoDBDataStoreUtilities class selectReview() method");
		getConnection();
		
		BasicDBObject dbObj=new BasicDBObject();
		dbObj.put("productId", productId);
		System.out.println("Basic DB Object :"+dbObj);
		
		DBCursor cursor=reviewCollection.find(dbObj);//Query
		int i=0;
		ArrayList<Reviews> reviewList=new ArrayList<Reviews>();
		while(cursor.hasNext()){
			BasicDBObject obj=(BasicDBObject)cursor.next();
			reviewList.add(new Reviews(i,obj.getString("userId"),obj.getInt("reviewRating"),obj.getString("reviewText"),new Date()));
			i++;
		}
		System.out.println("Reviews sent from Mongo DB");
		cursor.close();
		mongo.close();
		return reviewList;
	}
	
	public static String FiveMostLikedProducts(){
		System.out.println("Inside MongoDBDataStoreUtilities class FiveMostLikedProducts() method");
		String tableHtml="";
		getConnection();
		
		 Iterable<DBObject> output = reviewCollection.aggregate(Arrays.asList(
				 (DBObject) new BasicDBObject("$group", new BasicDBObject("_id", "$productId").append("avgRating", new BasicDBObject("$avg","$reviewRating"))),
				 (DBObject) new BasicDBObject("$sort", new BasicDBObject("avgRating", -1)),
				 (DBObject) new BasicDBObject("$limit", 5)
				 )).results();
		
		 for (DBObject dbObject : output)
		 {
			 System.out.println(dbObject);
			 tableHtml=tableHtml+"<tr><td>"+dbObject.get("_id").toString()+"</td><td>"+dbObject.get("avgRating").toString()+"</td></tr>";
		 }
		mongo.close();
		return tableHtml;
	}
}