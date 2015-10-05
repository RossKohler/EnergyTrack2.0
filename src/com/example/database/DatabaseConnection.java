package com.example.database;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.*;

import javax.sql.*;
import javax.naming.*;

import org.apache.log4j.Logger;

import snaq.db.DBPoolDataSource;

import com.example.energytrack2_0.Log4jContextListener;
import com.example.programpreferences.ProgramPreferences;
import com.vaadin.data.util.sqlcontainer.connection.J2EEConnectionPool;


public class DatabaseConnection{
	public static DataSource ds;
	final static Logger errorLogger = Logger.getLogger(Log4jContextListener.class);
	
	public static void createConnectionPool(){
		 Context context;
		try {
			context = new InitialContext();
			Class.forName("org.firebirdsql.jdbc.FBDriver");
			ds = (DataSource) context.lookup("java:comp/env/jdbc/PanelTrackDB");
		Connection c = ds.getConnection();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			errorLogger.error("An Error Occured:",e);
			e.printStackTrace();
		}  catch (ClassNotFoundException e) {
			errorLogger.error("An Error Occured:",e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			errorLogger.error("An Error Occured:",e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}


	public static J2EEConnectionPool retrieveJ2EEConnectionPool(){
		return (new J2EEConnectionPool(ds));
		
	}}



	
