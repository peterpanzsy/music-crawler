package com.keystone.analytics.music.entity;

public class Song {
	private String id;
	private String songid;
	private String name;
	private String singer;
	private String singerid;
	private String album;
	private String albumid;
	private String sharetimes;
	private String lyricsBy;//作词
	private String composer;//作曲
	private String listentimes;//收听次数
	
	public String getSongid() {
		return songid;
	}
	public void setSongid(String songid) {
		this.songid = songid;
	}
	public String getSingerid() {
		return singerid;
	}
	public void setSingerid(String singerid) {
		this.singerid = singerid;
	}
	public String getAlbumid() {
		return albumid;
	}
	public void setAlbumid(String albumid) {
		this.albumid = albumid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getSharetimes() {
		return sharetimes;
	}
	public void setSharetimes(String sharetimes) {
		this.sharetimes = sharetimes;
	}
	public String getLyricsBy() {
		return lyricsBy;
	}
	public void setLyricsBy(String lyricsBy) {
		this.lyricsBy = lyricsBy;
	}
	public String getComposer() {
		return composer;
	}
	public void setComposer(String composer) {
		this.composer = composer;
	}
	public String getListentimes() {
		return listentimes;
	}
	public void setListentimes(String listentimes) {
		this.listentimes = listentimes;
	}
	
	
}
