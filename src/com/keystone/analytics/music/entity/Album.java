package com.keystone.analytics.music.entity;

import java.util.ArrayList;

public class Album {
	String id;
	String albumid;
	String name;
	String language;
	String company;
	String date;
	String type;
	String style;
	int playCount;
	int collectedCount;
	int reviewCount;
	String score;
	ArrayList<Review> reviewArray;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAlbumid() {
		return albumid;
	}
	public void setAlbumid(String albumid) {
		this.albumid = albumid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public int getPlayCount() {
		return playCount;
	}
	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}
	public int getCollectedCount() {
		return collectedCount;
	}
	public void setCollectedCount(int collectedCount) {
		this.collectedCount = collectedCount;
	}
	public int getReviewCount() {
		return reviewCount;
	}
	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public ArrayList<Review> getReviewArray() {
		return reviewArray;
	}
	public void setReviewArray(ArrayList<Review> reviewArray) {
		this.reviewArray = reviewArray;
	}
	
}
