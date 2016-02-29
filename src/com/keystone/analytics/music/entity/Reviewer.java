package com.keystone.analytics.music.entity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Reviewer {
	String id;
	String reviewerid;
	String nick;
	String gender;
	String age;
	String area;
	String joinDate;
	int playCount;
	String level;
	int fansCount;
	int followCount;
	int shareCount;
	int visitedCount;
	ArrayList<Song> recentSongs;
	
	public void setReviewerid(String reviewerid) {
		this.reviewerid = reviewerid;
	}
	public String getReviewerid() {
		return reviewerid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	public int getPlayCount() {
		return playCount;
	}
	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public int getFansCount() {
		return fansCount;
	}
	public void setFansCount(int fansCount) {
		this.fansCount = fansCount;
	}
	public int getFollowCount() {
		return followCount;
	}
	public void setFollowCount(int followCount) {
		this.followCount = followCount;
	}
	public int getShareCount() {
		return shareCount;
	}
	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}
	public int getVisitedCount() {
		return visitedCount;
	}
	public void setVisitedCount(int visitedCount) {
		this.visitedCount = visitedCount;
	}
	public ArrayList<Song> getRecentSongs() {
		return recentSongs;
	}
	public void setRecentSongs(ArrayList<Song> recentSongs) {
		this.recentSongs = recentSongs;
	}
	
}
