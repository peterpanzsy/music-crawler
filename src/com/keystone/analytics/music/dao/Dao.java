package com.keystone.analytics.music.dao;

import java.sql.Connection;
import java.sql.SQLException;



public abstract class Dao {
	protected Connection conn = null;

	public Dao() throws ClassNotFoundException{
		DBConnection myDB = new DBConnection();
		myDB.startConnection();
		conn = myDB.getConn();
	}
	
	public void closeConnection() {
		if (this.conn!=null) {
			try {
				conn.close();
			} catch ( SQLException e) {
				// TODO: handle exception
			}finally{
				conn=null;
			}
		}
	}
}
