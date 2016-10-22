package com.poc.sujan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.model.pojo.Product;

public class ProductDataStore extends DefaultHandler{
	Product product;
	List<Product> productsList;
	String productXmlFileName;
	String elementValueRead;
	
	public ProductDataStore(String ProductFile){
		System.out.println("Inside constrctor of ProductDataStore : "+ProductFile);
		this.productXmlFileName = ProductFile;
		productsList = new ArrayList<Product>();
        parseDocument();
        printReadProducts();
        addProductsToMap();
	}
	
	private void parseDocument() {
		System.out.println("Inside parseDocument() method of ProductDataStore : "+productXmlFileName);
		System.out.println("this : "+this);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(productXmlFileName, this);
        } catch (ParserConfigurationException e) {
            System.out.println("ParserConfig error");
        } catch (SAXException e) {
            System.out.println("SAXException : xml not well formed");
        } catch (IOException e) {
            System.out.println("IO error");
        }
    }
	
	private void printReadProducts()
	{
		System.out.println("Inside printMap()");
		
		for (Product product: productsList) {
           System.out.format("%-5s|%-10s|%-20s|%-10s|%-30s|%-10s|%-30s|%-5s\n", product.getProductID(), product.getCategory(), product.getProductName(),product.getBrand(),product.getDescription(),product.getPrice(),product.getImage(),product.getQuantityAvailable());
        }
	}
	
	HashMap<Integer,Product> productMap=new HashMap<Integer,Product>();
	
	public HashMap addProductsToMap(){
		System.out.println("Inside addProductsToMap() method of ProductDataStore");
		for(Product product:productsList){
				productMap.put(product.getProductID(), product);
		}
		return productMap;
	}
	
	private void printMap(){
		Set<Map.Entry<Integer, Product>> entries=productMap.entrySet();
		for(Map.Entry<Integer, Product> prodMap:entries){
			Product product=prodMap.getValue();
			System.out.println(prodMap.getKey()+" :  "+product.getProductID()+"  "+product.getProductName()+" "+product.getBrand());
		}
	}
	 	@Override
	    public void startElement(String str1, String str2, String elementName, Attributes attributes) throws SAXException {
	        if (elementName.equals("product")) {
	            product = new Product();
	        }
	    }
	 	
	 	@Override
	    public void endElement(String str1, String str2, String element) throws SAXException {
	        if (element.equals("product")) {
	        	productsList.add(product);
	        	return;
	        }
	        if (element.equalsIgnoreCase("productID")) {
	        	product.setProductID(Integer.parseInt(elementValueRead));
	        	return;
	        }
	        if (element.equalsIgnoreCase("category")) {
	        	product.setCategory(elementValueRead);
	        	return;
	        }
	        if (element.equalsIgnoreCase("productName")) {
	        	product.setProductName(elementValueRead);
	        	return;
	        }
	        if (element.equalsIgnoreCase("brand")) {
	        	product.setbrand(elementValueRead);
	        	return;
	        }
	        if (element.equalsIgnoreCase("description")) {
	        	product.setDescription(elementValueRead);
	        	return;
	        }
	        if(element.equalsIgnoreCase("price")){
	        	product.setPrice(Double.parseDouble(elementValueRead));
	        	return;
	        }
	        if (element.equalsIgnoreCase("image")) {
	        	product.setImage(elementValueRead);
	        	return;
	        }
	        if(element.equalsIgnoreCase("accessory")){
	        	product.getAccessories().add(elementValueRead);
	        	return;
	        }
	        if(element.equalsIgnoreCase("quantityAvailable")){
	        	product.setQuantityAvailable(Integer.parseInt(elementValueRead));
	        	return;
	        }
	    }
	 	
	 	@Override
	    public void characters(char[] content, int begin, int end) throws SAXException {
	        elementValueRead = new String(content, begin, end);
	    }

}