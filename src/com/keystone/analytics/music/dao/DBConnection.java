package com.keystone.analytics.music.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private Connection conn = null;	
	public  Connection startConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String dburl = "jdbc:mysql://localhost:3306?useUnicode=true&characterEncoding=utf8";
			conn = DriverManager.getConnection(dburl, "root", "admin");
			System.out.println("connection built");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public Connection getConn(){
		return conn;
	}
	
	public void closeConnection(Connection conn) throws SQLException{
		if(conn!=null){
			conn.close();
			conn=null;
		}
	}
}
