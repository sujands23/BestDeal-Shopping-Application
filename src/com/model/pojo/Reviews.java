package com.model.pojo;

import java.util.Date;

public class Reviews {
	String reviewerName;
	public String getReviewerName() {
		return reviewerName;
	}
	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}
	int reviewId;
	int reviewRating;
	String reviewText;
	Date reviewDate;
	
	public Reviews(){
		System.out.println("Inside no argument constructor of Reviews");
	}
	public Reviews(int reviewId,String reviewerName, int reviewRating, String reviewText, Date reviewDate) {
		super();
		this.reviewId = reviewId;
		this.reviewerName=reviewerName;
		this.reviewRating = reviewRating;
		this.reviewText = reviewText;
		this.reviewDate = reviewDate;
	}
	public int getReviewId() {
		return reviewId;
	}
	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}
	public int getReviewRating() {
		return reviewRating;
	}
	public void setReviewRating(int reviewRating) {
		this.reviewRating = reviewRating;
	}
	public String getReviewText() {
		return reviewText;
	}
	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}
	public Date getReviewDate() {
		return reviewDate;
	}
	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}
}
