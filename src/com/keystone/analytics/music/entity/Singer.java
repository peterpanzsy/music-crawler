package com.keystone.analytics.music.entity;

import java.util.ArrayList;

public class Singer {
	String id;
	String singerid;
	String name;
	String desc;
	int listenedCount;
	int fanCount;
	ArrayList<Singer>[] simSingers;
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public String getSingerid() {
		return singerid;
	}
	public void setSingerid(String singerid) {
		this.singerid = singerid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getListenedCount() {
		return listenedCount;
	}
	public void setListenedCount(int listenedCount) {
		this.listenedCount = listenedCount;
	}
	public int getFanCount() {
		return fanCount;
	}
	public void setFanCount(int fanCount) {
		this.fanCount = fanCount;
	}
	public ArrayList<Singer>[] getSimSingers() {
		return simSingers;
	}
	public void setSimSingers(ArrayList<Singer>[] simSingers) {
		this.simSingers = simSingers;
	}

	
}
