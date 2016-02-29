package com.keystone.analytics.music.entity;

public class Review {
	String id;
	String content;
	String reviewer;
	String date;
	int agreeCount;
	int disagreeCount;
	String songid;
	String reviewerid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReviewer() {
		return reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getAgreeCount() {
		return agreeCount;
	}
	public void setAgreeCount(int agreeCount) {
		this.agreeCount = agreeCount;
	}
	public int getDisagreeCount() {
		return disagreeCount;
	}
	public void setDisagreeCount(int disagreeCount) {
		this.disagreeCount = disagreeCount;
	}
	public String getSongid() {
		return songid;
	}
	public void setSongid(String songid) {
		this.songid = songid;
	}
	public String getReviewerid() {
		return reviewerid;
	}
	public void setReviewerid(String reviewerid) {
		this.reviewerid = reviewerid;
	}
	
}
