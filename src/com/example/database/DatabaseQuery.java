package com.example.database;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.example.floor.BuildingFloor;
import com.example.floor.FloorMap;
import com.example.employee.Employee;
import com.example.energytips.EnergyTip;
import com.example.energytrack2_0.Log4jContextListener;
import com.example.energytrack2_0.QuartzContextListener;
import com.google.gwt.thirdparty.guava.common.collect.Lists;

public class DatabaseQuery {
	
	private static String url = "/WEB-INF/Resources/PanelTrackCSV";
	final static Logger errorLogger = Logger.getLogger(Log4jContextListener.class);
	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.s");
	 public static DateTimeFormatter csvDateFormat= DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss a");

	public static Vector<String> getTableNames() {
		Connection conn = null;
		ResultSet rs = null;
		
		Vector<String> tableNames = new Vector<String>();
		try {
			conn = DatabaseConnection.ds
					.getConnection();
			String[] types = { "TABLE" };

			DatabaseMetaData metaData = conn.getMetaData();
			rs = metaData.getTables(null, null, "%", types);
			while (rs.next()) {
					String tableName = rs.getString("TABLE_NAME");
					if (tableName.contains("floor")
							|| tableName.contains("FLOOR")) {
						tableNames.add(tableName);
					}
				}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
			
			
		}

		return tableNames;
	}

	public static Vector<String> checkTables() {
		Connection conn = null;
		ResultSet rs = null;
		
		Vector<String> tableNames = new Vector<String>();
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			String[] types = { "TABLE" };
			DatabaseMetaData metaData = conn.getMetaData();

				rs = metaData.getTables(null, null, "%", types);
				while (rs.next()) {
					String tableName = rs.getString("TABLE_NAME");
					if (tableName.contains("ENERGY_TIPS")
							|| tableName.contains("BUILDING_PROFILE")
							|| tableName.contains("EMPLOYEE_PROFILES")) {
						tableNames.add(tableName);
					}
				}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}

			finally{
			    if (rs != null) {
			        try { rs.close(); } catch (SQLException e) { ; }
			        rs = null;
			      }
			      if (conn != null) {
			        try { conn.close(); } catch (SQLException e) { ; }
			        conn = null;
			      }
				
				
			}

		return tableNames;
	}

	public static Vector<Employee> getEmployeeInformation() {
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt=null;
		Vector<Employee> buildingEmployees = new Vector<Employee>();
		try{
		conn = DatabaseConnection.ds
					.getConnection();
		stmt = conn.createStatement();
		rs = stmt.executeQuery("Select * from Employee_Profiles");
		
		
		
					while (rs.next()) {
						Employee employee = new Employee();
						employee.setID(rs.getInt("ID"));
						employee.setName(rs.getString("NAME"));
						employee.setFloor(rs.getInt("FLOOR"));
						employee.setEmail(rs.getString("EMAIL"));
						if (rs.getInt("TREATMENT") == 1) {
							employee.setTreatment(true);
						} else {
							employee.setTreatment(false);
						}
						buildingEmployees.add(employee);

					}

				}
			catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (stmt != null) {
		        try { stmt.close(); } catch (SQLException e) { ; }
		        stmt = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
			
			
		}

		return buildingEmployees;

	}

	public static Vector<String> getColumnNames(String tableName) {
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt=null;
		
		
		
		Vector<String> columnNames = new Vector<String>();

		try{
			conn = DatabaseConnection.ds.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select rdb$field_name from rdb$relation_fields where rdb$relation_name= '"+ tableName + "'");
			while (rs.next()) {
						columnNames.add(rs.getString(1));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (stmt != null) {
		        try { stmt.close(); } catch (SQLException e) { ; }
		        stmt = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
			
			
		}
		
		return columnNames;
	}

	public static void addEmployee(Employee addEmployee) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		
		
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			stmt = conn.prepareStatement("INSERT INTO EMPLOYEE_PROFILES(NAME,FLOOR,EMAIL,TREATMENT) VALUES(?,?,?,?)");
				stmt.setString(1, addEmployee.getName());
				stmt.setInt(2, addEmployee.getFloor());
				stmt.setString(3, addEmployee.getEmail());
				stmt.setInt(4, (addEmployee.getTreatment() ? 1 : 0));
				stmt.executeUpdate();
			}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (stmt != null) {
		        try { stmt.close(); } catch (SQLException e) { ; }
		        stmt = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
			
			
		}
	}

	public static void editEmployee(Employee editEmployee) {
		Connection conn=null;
		PreparedStatement stmt=null;
		ResultSet rs=null;;
		
		try{
			conn = DatabaseConnection.ds.getConnection();
				stmt = conn.prepareStatement("UPDATE EMPLOYEE_PROFILES SET NAME=?,FLOOR=?,EMAIL=?,TREATMENT=? WHERE ID=?");
				stmt.setString(1, editEmployee.getName());
				stmt.setInt(2, editEmployee.getFloor());
				stmt.setString(3, editEmployee.getEmail());
				stmt.setInt(4, (editEmployee.getTreatment() ? 1 : 0));
				stmt.setInt(5, editEmployee.getID());
				stmt.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (stmt != null) {
		        try { stmt.close(); } catch (SQLException e) { ; }
		        stmt = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
			
			
		}
		
	}

	public static void deleteEmployee(Employee deleteEmployee) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		
		
		try{
			conn = DatabaseConnection.ds
					.getConnection();
		    stmt = conn.prepareStatement("DELETE FROM EMPLOYEE_PROFILES WHERE ID = ?");
		    stmt.setInt(1, deleteEmployee.getID());
		    stmt.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (stmt != null) {
		        try { stmt.close(); } catch (SQLException e) { ; }
		        stmt = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
			
			
		}
		
	}

	public static void setCurrentUsage() {
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt= null;
		PreparedStatement prepared= null;
		
		
		for (int floor = 1; floor <= BuildingFloor.FLOORS; floor++) {
			int currentUsage = 0;
			try {
				conn = DatabaseConnection.ds
						.getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT FIRST 2 DISTINCT METERID,DATETIME,CONSUMPTION from KWH where METERID in (select METERID from PTMETER where description like '%SB"
								+ floor + "') order by datetime desc;");
						while (rs.next()) {
							currentUsage += rs.getInt("CONSUMPTION");
						}
						prepared = conn.prepareStatement("UPDATE OR INSERT INTO BUILDING_PROFILE(FLOOR,CURRENT_USAGE) VALUES(?,?) MATCHING(FLOOR);");
						prepared.setInt(1, floor);
						prepared.setInt(2, currentUsage);
						prepared.executeUpdate();
						
					}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorLogger.error("Error in DatabaseQuery", e);
			}
			finally{
			    if (rs != null) {
			        try { rs.close(); } catch (SQLException e) { ; }
			        rs = null;
			      }
			      if (stmt != null) {
			        try { stmt.close(); } catch (SQLException e) { ; }
			        stmt = null;
			      }
			      if (conn != null) {
			        try { conn.close(); } catch (SQLException e) { ; }
			        conn = null;
			      }
			      if (prepared != null) {
				        try { prepared.close(); } catch (SQLException e) { ; }
				        prepared = null;
				  }

			}
		}
	}

	public static void set24hourUsage() {
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		PreparedStatement prepared = null;
		
		boolean current = false;
		boolean past = false;
		float count = 0;
		float usage = 0;
		float normalUsage = 0;
		float highUsage = 0;
		float energyChange = 0;
		float lowUsage = 0;
		int previousMeterId = 0;
		String query;
		for (int floor = 1; floor <= BuildingFloor.FLOORS; floor++) {
			try{
				conn = DatabaseConnection.ds
						.getConnection();
				query = "SELECT distinct METERID,DATETIME,CONSUMPTION from KWH where meterID in (Select meterID from PTMETER where description like '%SB"
						+ floor
						+ "') and DATETIME between '"
						+ getDayFromCurrent(1)
						+ "' AND '"
						+ getCurrentDate()
						+ "' order by datetime desc;";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(query);
						boolean firstRun = true;
						while (rs.next()) {
							int currentMeterId = rs.getInt("METERID");
							if (previousMeterId == currentMeterId) {
								continue;
							} else {
								previousMeterId = currentMeterId;
							}
							usage += rs.getFloat("CONSUMPTION");
							count++;
							if (count % 2 == 0) {
								if (firstRun) {
									lowUsage = usage;
									highUsage = usage;
									firstRun = false;
								} else {
									if (usage < lowUsage) {
										lowUsage = usage;
									}
									if (usage > highUsage) {
										highUsage = usage;
									}

								}

								normalUsage += usage;

								usage = 0;
							}
						}
						normalUsage = normalUsage / (count / 2);
				past = true;
				query = "SELECT distinct METERID,DATETIME,CONSUMPTION from KWH where meterID in (Select meterID from PTMETER where description like '%SB"
						+ floor
						+ "') and DATETIME between '"
						+ getDayFromCurrent(2)
						+ "' AND '"
						+ getDayFromCurrent(1) + "' order by datetime desc";
				usage = 0;
				count = 0;
				rs = stmt.executeQuery(query);
				usage = 0;
				while (rs.next()) {

							usage += rs.getFloat("CONSUMPTION");
							count += 0.5;
				}
						energyChange = (normalUsage * (count / usage) - 1) * 100;
				query = "UPDATE or INSERT into Building_Profile (floor,day_normal,day_low,day_high,day_change) VALUES(?,?,?,?,?) MATCHING(floor)";
				prepared = conn
						.prepareStatement(query);
					prepared.setInt(1, floor);
					prepared.setFloat(2, normalUsage);
					prepared.setFloat(3, lowUsage);
					prepared.setFloat(4, highUsage);
					prepared.setFloat(5, energyChange);

					prepared.executeUpdate();
					count = 0;
					usage = 0;
					highUsage = 0;
				}

			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorLogger.error("Error in DatabaseQuery", e);
			}
			finally{
			    if (rs != null) {
			        try { rs.close(); } catch (SQLException e) { ; }
			        rs = null;
			      }
			      if (stmt != null) {
			        try { stmt.close(); } catch (SQLException e) { ; }
			        stmt = null;
			      }
			      if (conn != null) {
			        try { conn.close(); } catch (SQLException e) { ; }
			        conn = null;
			      }
			      if (prepared != null) {
				        try { prepared.close(); } catch (SQLException e) { ; }
				        prepared = null;
				  }

			}
		}

	}
	public static void setExcelWeekUsage(){
		String [] headerMapping = {"Date & Time","P Sum (kW)","Q Sum (kVAr)","S Sum (kVA)"};
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(headerMapping).withSkipHeaderRecord(true);
		String basepath = QuartzContextListener.context.getRealPath(url);
		File dir = new File(basepath);
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
		
		PreparedStatement prepared = null;
		Connection conn = null;
		
	for(int floor = 1; floor<=24;floor++){
		boolean firstRun = true;
		FileFilter fileFilter = new WildcardFileFilter("* West Sub SB"+Integer.toString(floor)+" *");
		File[] files = dir.listFiles(fileFilter);
		Arrays.sort(files,Collections.reverseOrder());
		
		DateTime weekFromCurrent = null;
		DateTime twoWeeksFromCurrent = null;
		
		double weekConsumption = 0;
		int weekCount = 0;
		
		double twoWeekConsumption = 0;
		double twoWeekCount = 0;
		
		for(int i = 0; i< files.length;i++){
			 CSVParser parser;
			try {

				parser = CSVParser.parse(files[i],csvFileFormat);
				List<CSVRecord> csvRecords = parser.getRecords();
				csvRecords = Lists.reverse(csvRecords);
				 for (CSVRecord csvRecord : csvRecords) {
					 String datetime = csvRecord.get("Date & Time");
					 if(! (datetime.contains("AM")||datetime.contains("PM"))){
						 datetime += " 00:00:00 AM";
					 }
					 DateTime date = csvDateFormat.parseDateTime(datetime);
					
					 if(firstRun==true && i ==0){
						 weekFromCurrent = date.minusWeeks(1);

						 twoWeeksFromCurrent = date.minusWeeks(2);	

						 firstRun = false;
					 }
					 
					 if(date.isAfter(weekFromCurrent)){
						 weekConsumption += Double.parseDouble(csvRecord.get("P Sum (kW)"));
						 weekCount++;
					 }
					 else if(date.isBefore(weekFromCurrent)&&date.isAfter(twoWeeksFromCurrent)){
						 twoWeekConsumption += Double.parseDouble(csvRecord.get("P Sum (kW)"));
						 twoWeekCount++;
					 }
				 }
				 
				 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		double weekAverage = Math.abs(weekConsumption/weekCount);
		double twoWeekAverage = Math.abs(twoWeekConsumption/twoWeekCount);
		double weekChange = ((weekAverage - twoWeekAverage)/weekAverage)*100;
		try {
			conn = DatabaseConnection.ds
					.getConnection();
			String query = "UPDATE or INSERT into Building_Profile (floor,week_change) VALUES(?,?) MATCHING(floor)";
			prepared = conn
					.prepareStatement(query);
			prepared.setInt(1, floor);
			prepared.setDouble(2, weekChange);
			prepared.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		      if (prepared != null) {
			        try { prepared.close(); } catch (SQLException e) { ; }
			        prepared = null;
			  }

		}


	
	}
	
		
	/*try {
			
			CsvReader products = new CsvReader("products.csv");
		
			products.readHeaders();

			while (products.readRecord())
			{
				String productID = products.get("ProductID");
				String productName = products.get("ProductName");
				String supplierID = products.get("SupplierID");
				String categoryID = products.get("CategoryID");
				String quantityPerUnit = products.get("QuantityPerUnit");
				String unitPrice = products.get("UnitPrice");
				String unitsInStock = products.get("UnitsInStock");
				String unitsOnOrder = products.get("UnitsOnOrder");
				String reorderLevel = products.get("ReorderLevel");
				String discontinued = products.get("Discontinued");
				
				// perform program logic here
				System.out.println(productID + ":" + productName);
			}
	
			products.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
	}
	
	

	public static void setWeekUsage() {
		boolean current = false;
		boolean past = false;
		float count = 0;
		float usage = 0;
		float normalUsage = 0;
		float highUsage = 0;
		float energyChange = 0;
		float lowUsage = 0;
		int previousMeterId = 0;
		String query;
		
		Connection conn= null;
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement prepared = null;
		for (int floor = 1; floor <= BuildingFloor.FLOORS; floor++) {
			try{
				conn = DatabaseConnection.ds
						.getConnection();
				query = "SELECT distinct METERID,DATETIME,CONSUMPTION from KWH where meterID in (Select meterID from PTMETER where description like '%SB"
						+ floor
						+ "') and DATETIME between '"
						+ getWeekFromCurrent(1)
						+ "' AND '"
						+ getCurrentDate()
						+ "' order by datetime desc;";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(query);
						boolean firstRun = true;
						while (rs.next()) {
							int currentMeterId = rs.getInt("METERID");
							if (previousMeterId == currentMeterId) {
								continue;
							} else {
								previousMeterId = currentMeterId;
							}
							usage += rs.getFloat("CONSUMPTION");
							count++;
							if (count % 2 == 0) {

								if (firstRun) {
									lowUsage = usage;
									highUsage = usage;
									firstRun = false;
								} else {
									if (usage < lowUsage) {
										lowUsage = usage;
									}
									if (usage > highUsage) {
										highUsage = usage;
									}

								}

								normalUsage += usage;

								usage = 0;
							}
						}
						normalUsage = normalUsage / (count / 2);
		
				query = "SELECT distinct METERID,DATETIME,CONSUMPTION from KWH where meterID in (Select meterID from PTMETER where description like '%SB"
						+ floor
						+ "') and DATETIME between '"
						+ getWeekFromCurrent(2)
						+ "' AND '"
						+ getWeekFromCurrent(1) + "' order by datetime desc";
					stmt = conn.createStatement();
					count = 0;
					rs = stmt.executeQuery(query);
						usage = 0;
						while (rs.next()) {
			
							usage += rs.getFloat("CONSUMPTION");
							count += 0.5;
						}
						energyChange = (normalUsage * (count / usage) - 1) * 100;
					query = "UPDATE or INSERT into Building_Profile (floor,week_normal,week_low,week_high,week_change) VALUES(?,?,?,?,?) MATCHING(floor)";
						prepared = conn
								.prepareStatement(query);
						prepared.setInt(1, floor);
						prepared.setFloat(2, normalUsage);
						prepared.setFloat(3, lowUsage);
						prepared.setFloat(4, highUsage);
						prepared.setFloat(5, energyChange);
						prepared.executeUpdate();
						count = 0;
						usage = 0;
						highUsage = 0;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorLogger.error("Error in DatabaseQuery", e);
			}
			finally{
			    if (rs != null) {
			        try { rs.close(); } catch (SQLException e) { ; }
			        rs = null;
			      }
			      if (stmt != null) {
			        try { stmt.close(); } catch (SQLException e) { ; }
			        stmt = null;
			      }
			      if (conn != null) {
			        try { conn.close(); } catch (SQLException e) { ; }
			        conn = null;
			      }
			      if (prepared != null) {
				        try { prepared.close(); } catch (SQLException e) { ; }
				        prepared = null;
				  }

			}
		}
	}

	public static void setMonthUsage() {
		boolean past = false;
		boolean current = false;
		float count = 0;
		float usage = 0;
		float normalUsage = 0;
		float highUsage = 0;
		float energyChange = 0;
		float lowUsage = 0;
		int previousMeterId = 0;
		String query;
		
		Connection conn= null;
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement prepared = null;
		

		for (int floor = 1; floor <= BuildingFloor.FLOORS; floor++) {
			try{
				conn = DatabaseConnection.ds
						.getConnection();
				query = "SELECT distinct METERID,DATETIME,CONSUMPTION from KWH where meterID in (Select meterID from PTMETER where description like '%SB"
						+ floor
						+ "') and DATETIME between '"
						+ getMonthFromCurrent(1)
						+ "' AND '"
						+ getCurrentDate()
						+ "' order by datetime desc;";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(query);
						boolean firstRun = true;
						while (rs.next()) {
							int currentMeterId = rs.getInt("METERID");
							if (previousMeterId == currentMeterId) {
								continue;
							} else {
								previousMeterId = currentMeterId;
							}
							usage += rs.getFloat("CONSUMPTION");
							count++;
							if (count % 2 == 0) {

								if (firstRun) {
									lowUsage = usage;
									highUsage = usage;
									firstRun = false;
								} else {
									if (usage < lowUsage) {
										lowUsage = usage;
									}
									if (usage > highUsage) {
										highUsage = usage;
									}

								}

								normalUsage += usage;

								usage = 0;
							}
						}
						normalUsage = normalUsage / (count / 2);
				query = "SELECT distinct METERID,DATETIME,CONSUMPTION from KWH where meterID in (Select meterID from PTMETER where description like '%SB"
						+ floor
						+ "') and DATETIME between '"
						+ getMonthFromCurrent(2)
						+ "' AND '"
						+ getMonthFromCurrent(1) + "' order by datetime desc";
				stmt = conn.createStatement();
					count = 0;
				rs = stmt.executeQuery(query);
						usage = 0;
						while (rs.next()) {
							usage += rs.getFloat("CONSUMPTION");
							count += 0.5;
						}
					energyChange = (normalUsage * (count / usage) - 1) * 100;
					query = "UPDATE or INSERT into Building_Profile (floor,month_normal,month_low,month_high,month_change) VALUES(?,?,?,?,?) MATCHING(floor)";
					prepared = conn
							.prepareStatement(query);
						prepared.setInt(1, floor);
						prepared.setFloat(2, normalUsage);
						prepared.setFloat(3, lowUsage);
						prepared.setFloat(4, highUsage);
						prepared.setFloat(5, energyChange);
						prepared.executeUpdate();
						count = 0;
						usage = 0;
						highUsage = 0;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorLogger.error("Error in DatabaseQuery", e);
			}
			
			finally{
			    if (rs != null) {
			        try { rs.close(); } catch (SQLException e) { ; }
			        rs = null;
			      }
			      if (stmt != null) {
			        try { stmt.close(); } catch (SQLException e) { ; }
			        stmt = null;
			      }
			      if (conn != null) {
			        try { conn.close(); } catch (SQLException e) { ; }
			        conn = null;
			      }
			      if (prepared != null) {
				        try { prepared.close(); } catch (SQLException e) { ; }
				        prepared = null;
				  }

			}
		}

	}

	public static void getFloorInformation(BuildingFloor buildingFloor) {
		Connection conn= null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			String query = "Select * from Building_profile where floor= ?";
			stmt = conn
					.prepareStatement(query);
				int floor = FloorMap.extractDigits(buildingFloor.getFloor());
				stmt.setInt(1, floor);
				rs = stmt.executeQuery();

					while (rs.next()) {
						buildingFloor.setCurrentUsage(rs
								.getInt("CURRENT_USAGE"));
						buildingFloor
								.setDayNormalUsage(rs.getInt("DAY_NORMAL"));
						buildingFloor.setDayHighUsage(rs.getInt("DAY_HIGH"));
						buildingFloor.setDayLowUsage(rs.getInt("DAY_LOW"));
						buildingFloor.setDayChange(rs.getInt("DAY_CHANGE"));
						buildingFloor.setWeekNormalUsage(rs
								.getInt("WEEK_NORMAL"));
						buildingFloor.setWeekHighUsage(rs.getInt("WEEK_HIGH"));
						buildingFloor.setWeekLowUsage(rs.getInt("WEEK_LOW"));
						buildingFloor.setWeekChange(rs.getInt("WEEK_CHANGE"));
						buildingFloor.setMonthLowUsage(rs.getInt("MONTH_LOW"));
						buildingFloor
								.setMonthHighUsage(rs.getInt("MONTH_HIGH"));
						buildingFloor.setMonthChange(rs.getInt("MONTH_CHANGE"));
						buildingFloor.setMonthNormalUsage(rs
								.getInt("MONTH_NORMAL"));
						buildingFloor.setTreatment(rs.getString("treatment"));
						buildingFloor.setEnergyAdvocate(rs
								.getString("ENERGY_ADVOCATE"));
						buildingFloor.setEmployeeCount(rs
								.getInt("NUMBER_EMPLOYEES"));
						buildingFloor.setWeekCumulativeUsage(rs
								.getInt("WEEK_CUMULATIVE"));
					}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (stmt != null) {
		        try { stmt.close(); } catch (SQLException e) { ; }
		        stmt = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }

		}

	}

	public static void updateTreatmentInformation(BuildingFloor floor) {
		Connection conn = null;
		PreparedStatement prepared = null;
		
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			prepared = conn
					.prepareStatement("UPDATE BUILDING_PROFILE SET TREATMENT = ? where floor=?");
				prepared.setString(1, floor.getTreatment());
				prepared.setString(2, floor.getFloor());
				prepared.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		      if (prepared != null) {
			        try { prepared.close(); } catch (SQLException e) { ; }
			        prepared = null;
			  }

		}

	}

	public static void randomEnergyAdvocate() {
		Vector<Integer> floors = getGroupBFloors();
		Iterator<Integer> floorIterator = floors.iterator();
		Connection conn = null;
		PreparedStatement prepared= null;
		ResultSet rs = null;
		
		while (floorIterator.hasNext()) {
			int count = 0;
			String energyAdvocate = "";
			String newEnergyAdvocate = "";
			int floor = floorIterator.next();
			try{
				conn = DatabaseConnection.ds
						.getConnection();
				prepared = conn
						.prepareStatement("SELECT ENERGY_ADVOCATE from building_profile where floor =?");
					prepared.setInt(1, floor);
					rs = prepared.executeQuery();
						while (rs.next()) {
							energyAdvocate = rs.getString("ENERGY_ADVOCATE");

						}
					prepared = conn
							.prepareStatement("SELECT COUNT(*) from employee_profiles where floor=? and treatment=1 and email is not null;");
					prepared.setInt(1, floor);
					rs = prepared.executeQuery();
						while (rs.next()) {
							count = rs.getInt("COUNT");

						}
				if (count != 0) {
						prepared = conn
								.prepareStatement("SELECT name from employee_profiles where floor=? and treatment = 1 and email is not null order by RAND()");
						prepared.setInt(1, floor);
						while (true) {
							rs = prepared.executeQuery();
								while (rs.next()) {

									newEnergyAdvocate = rs.getString("name");
								}
								if (count == 1 || energyAdvocate == null) {
									energyAdvocate = newEnergyAdvocate;
									break;
								}

								else if (energyAdvocate
										.equals(newEnergyAdvocate) == false) {
									energyAdvocate = newEnergyAdvocate;
									break;
								}

								else {
									continue;
								}

						}
						prepared = conn
								.prepareStatement("UPDATE BUILDING_PROFILE set energy_advocate =? where floor =?");
						prepared.setString(1, energyAdvocate);
						prepared.setInt(2, floor);

						prepared.executeUpdate();

					}
				}
		 catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorLogger.error("Error in DatabaseQuery", e);
			}
			
			finally{
			    if (rs != null) {
			        try { rs.close(); } catch (SQLException e) { ; }
			        rs = null;
			      }
			      if (conn != null) {
			        try { conn.close(); } catch (SQLException e) { ; }
			        conn = null;
			      }
			      if (prepared != null) {
				        try { prepared.close(); } catch (SQLException e) { ; }
				        prepared = null;
				  }

			}
		}

	}

	public static void countEmployees() {
		Connection conn = null;
		PreparedStatement prepared = null;
		ResultSet rs = null;
		
		for (int floor = 1; floor <= BuildingFloor.FLOORS; floor++) {
			int employeeCount = 0;
		
			try{
				conn = DatabaseConnection.ds
						.getConnection();
				prepared = conn
						.prepareStatement("SELECT COUNT(*) from EMPLOYEE_PROFILES where floor=?");
				
					prepared.setInt(1, floor);

						rs = prepared.executeQuery();
						while (rs.next()) {
							employeeCount = rs.getInt("COUNT");
						}
					prepared = conn
							.prepareStatement("UPDATE BUILDING_PROFILE SET NUMBER_EMPLOYEES=? where floor = ?");
					prepared.setInt(1, employeeCount);
					prepared.setInt(2, floor);
					prepared.executeUpdate();
			
			} catch (Exception e) {
				e.printStackTrace();
				errorLogger.error("Error in DatabaseQuery", e);
			}
			finally{
			    if (rs != null) {
			        try { rs.close(); } catch (SQLException e) { ; }
			        rs = null;
			      }
			      if (conn != null) {
			        try { conn.close(); } catch (SQLException e) { ; }
			        conn = null;
			      }
			      if (prepared != null) {
				        try { prepared.close(); } catch (SQLException e) { ; }
				        prepared = null;
				  }

			}
			
		}

	}

	public static void disableEmployees(ArrayList<Employee> employees) {
		Connection conn = null;
		Statement stmt = null;
		
		
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			stmt = conn.createStatement();
				for (Employee employee : employees) {
					stmt.addBatch("update Employee_profiles set treatment = 0 where ID ="
							+ employee.getID());
				}

				stmt.executeBatch();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{

		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		      if (stmt != null) {
			        try { stmt.close(); } catch (SQLException e) { ; }
			        stmt = null;
			  }

		}
	}

	public static void enableEmployees(ArrayList<Employee> employees) {
		Connection conn = null;
		Statement stmt = null;
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			stmt = conn.createStatement();
				for (Employee employee : employees) {
					stmt.addBatch("update Employee_profiles set treatment = 1 where ID ="
							+ employee.getID());
				}
				stmt.executeBatch();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{

		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		      if (stmt != null) {
			        try { stmt.close(); } catch (SQLException e) { ; }
			        stmt = null;
			  }

		}

	}

	public static void weekCumulativeUsage() {
		Connection conn = null;
		PreparedStatement prepared = null;
		ResultSet rs = null;
		
		float usage = 0;
		LocalTime middayTime = new LocalTime("12:00");
		LocalTime midnightTime = new LocalTime("00:30");

		String startDateTime;
		String endDateTime;
		DateTimeFormatter formatter = DateTimeFormat
				.forPattern("yyyy-MM-dd HH:mm:ss.s");

		String stringCurrent = getCurrentDate();
		LocalDate currentDate = formatter.parseLocalDate(stringCurrent);

		LocalDate mondayDate = currentDate
				.withDayOfWeek(DateTimeConstants.MONDAY);
		LocalDate fridayDate = currentDate
				.withDayOfWeek(DateTimeConstants.FRIDAY);

		DateTime fridayDateTime = fridayDate.toDateTime(middayTime);
		DateTime mondayDateTime = mondayDate.toDateTime(midnightTime);
		DateTime currentDateTime = formatter.parseDateTime(stringCurrent);

		startDateTime = formatter.print(mondayDateTime);
		if (currentDateTime.isBefore(fridayDateTime)) {
			endDateTime = stringCurrent;
		} else {
			endDateTime = formatter.print(fridayDateTime);
		}

		for (int floor = 1; floor < BuildingFloor.FLOORS; floor++) {
			try{
				conn = DatabaseConnection.ds
						.getConnection();
				String query = "SELECT distinct METERID,DATETIME,CONSUMPTION from KWH where meterID in (Select meterID from PTMETER where description like '%SB"
						+ floor
						+ "') and DATETIME between ? AND ? order by datetime desc";

				prepared = conn
						.prepareStatement(query);
					prepared.setString(1, startDateTime);
					prepared.setString(2, endDateTime);
					rs = prepared.executeQuery();

						while (rs.next()) {
							usage += rs.getFloat("CONSUMPTION");
						}
				query = "UPDATE BUILDING_PROFILE set week_Cumulative=? where floor =?";
				prepared = conn
						.prepareStatement(query);
					prepared.setInt(2, floor);
					prepared.setFloat(1, usage);
					prepared.executeUpdate();
					usage = 0;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//errorLogger.error("Error in DatabaseQuery", e);
			}
			finally{
			    if (rs != null) {
			        try { rs.close(); } catch (SQLException e) { ; }
			        rs = null;
			      }
			      if (conn != null) {
			        try { conn.close(); } catch (SQLException e) { ; }
			        conn = null;
			      }
			      if (prepared != null) {
				        try { prepared.close(); } catch (SQLException e) { ; }
				        prepared = null;
				  }

			}
			
		}
	}

	public static Vector<EnergyTip> getEnergyTips() {
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		
		
		Vector<EnergyTip> energyTipVector = new Vector<EnergyTip>();

		try{
			conn = DatabaseConnection.ds
					.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ENERGY_TIPS");
			while (rs.next()) {
						EnergyTip newTip = new EnergyTip();
						newTip.setID(rs.getInt("ID"));
						newTip.setEnergyTip(rs.getString("SAVINGS_TIP"));
						newTip.setRole(rs.getString("ROLE"));
						if (rs.getInt("ENABLED") == 1) {
							newTip.setEnabled(true);
						} else {
							newTip.setEnabled(false);
						}
						energyTipVector.add(newTip);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (stmt != null) {
		        try { stmt.close(); } catch (SQLException e) { ; }
		        stmt = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
			
			
		}

		return energyTipVector;
	}

	public static void addEnergyTip(EnergyTip newTip) {
		Connection conn = null;
		PreparedStatement prepared = null;
		
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			prepared = conn
					.prepareStatement("INSERT INTO ENERGY_TIPS(SAVINGS_TIP,ROLE,ENABLED) values(?,?,?)");
				prepared.setString(1, newTip.getEnergyTip());
				prepared.setString(2, newTip.getRole());
				prepared.setInt(3, (newTip.getEnabled() ? 1 : 0));
				prepared.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{

		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		      if (prepared != null) {
			        try { prepared.close(); } catch (SQLException e) { ; }
			        prepared = null;
			  }

		}
	}

	public static void editEnergyTip(EnergyTip newTip) {
		Connection conn = null;
		PreparedStatement prepared = null;
		
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			prepared = conn
					.prepareStatement("UPDATE ENERGY_TIPS set SAVINGS_TIP=?,ROLE=?,ENABLED=? where ID =?");
				prepared.setString(1, newTip.getEnergyTip());
				prepared.setString(2, newTip.getRole());
				prepared.setInt(3, (newTip.getEnabled() ? 1 : 0));
				prepared.setInt(4, newTip.getID());
				prepared.executeUpdate();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{

		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		      if (prepared != null) {
			        try { prepared.close(); } catch (SQLException e) { ; }
			        prepared = null;
			  }

		}
	}

	public static void deleteEnergyTip(EnergyTip newTip) {
		Connection conn = null;
		PreparedStatement prepared = null;
		
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			prepared = conn
					.prepareStatement("DELETE FROM ENERGY_TIPS where id =?");
				prepared.setInt(1, newTip.getID());
				prepared.executeUpdate();
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{

		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		      if (prepared != null) {
			        try { prepared.close(); } catch (SQLException e) { ; }
			        prepared = null;
			  }

		}
		

	}

	public static Vector<String> getMailAddresses(String... args) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		
		String query = "select name,email,floor from employee_profiles where floor in (select floor from building_profile where ";
		int count = 1;
		for (String arg : args) {
			if (count == args.length) {
				query += "treatment='" + arg + "'";
			} else {
				query += "treatment='" + arg + "' or ";
				count++;
			}
		}
		query += ") and email is not null and treatment = 1";

		Vector<String> emailAddress = new Vector<String>();

		try{
			conn = DatabaseConnection.ds
					.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
					while (rs.next()) {
						emailAddress.add(rs.getString("EMAIL"));
					}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{

		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		      if (stmt != null) {
			        try { stmt.close(); } catch (SQLException e) { ; }
			        stmt = null;
			  }
		      if (rs != null) {
			        try { rs.close(); } catch (SQLException e) { ; }
			        rs = null;
			  }

		}

		return emailAddress;

	}

	public static Vector<Employee> getGroupAEmployees() {
		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = null;
		
		Vector<Employee> employeeList = new Vector<Employee>();
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			stmt = conn.createStatement();
			String query = "select name,email,floor from employee_profiles where floor in (select floor from building_profile where treatment = 'Group A') and email is not null and treatment = 1";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
						Employee employee = new Employee();
						employee.setName(rs.getString("NAME"));
						employee.setEmail(rs.getString("EMAIL"));
						employee.setFloor(rs.getInt("FLOOR"));
						employeeList.add(employee);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (stmt != null) {
		        try { stmt.close(); } catch (SQLException e) { ; }
		        stmt = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
			
			
		}
		return employeeList;
	}

	public static Vector<Employee> getGroupEmployees() {
		Vector<Employee> employeeList = new Vector<Employee>();
		Connection conn=null;
		Statement stmt=null;
		ResultSet rs=null;
		
		
		try {
			conn = DatabaseConnection.ds
					.getConnection();
			stmt = conn.createStatement();
			String query = "select name,floor,email from employee_profiles where floor in (select floor from building_profile where treatment = 'Group A' or treatment = 'Group B') and email is not null and treatment = 1";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				Employee employee = new Employee();
				employee.setName(rs.getString("NAME"));
				employee.setEmail(rs.getString("EMAIL"));
				employee.setFloor(rs.getInt("FLOOR"));
				employee.setTreatment(true);
				employeeList.add(employee);
					}

		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (stmt != null) {
		        try { stmt.close(); } catch (SQLException e) { ; }
		        stmt = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
			
			
		}
		return employeeList;
	}

	public static Vector<Employee> getGroupBEmployees() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		
		Vector<Employee> employeeList = new Vector<Employee>();

		
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			stmt = conn.createStatement();
			String query = "select name,floor,email from employee_profiles where floor in (select floor from building_profile where treatment = 'Group B') and email is not null and treatment = 1;";
			rs = stmt.executeQuery(query);
					while (rs.next()) {
						Employee employee = new Employee();
						employee.setName(rs.getString("NAME"));
						employee.setEmail(rs.getString("EMAIL"));
						employee.setFloor(rs.getInt("FLOOR"));
						employee.setTreatment(true);
						employeeList.add(employee);
					}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (stmt != null) {
		        try { stmt.close(); } catch (SQLException e) { ; }
		        stmt = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		}
		return employeeList;
	}

	public static void setFloorRanks() {
		Connection conn = null;
		PreparedStatement prepared = null;
		ResultSet rs = null;
		
		Map<Integer, Integer> rankMap = new<Integer, Integer> HashMap();
		int rank = 1;
			try{
			conn = DatabaseConnection.ds
					.getConnection();
			String query = "SELECT FLOOR from building_profile order by WEEK_CHANGE asc";
			prepared = conn
					.prepareStatement(query);
			rs = prepared.executeQuery();
					while (rs.next()) {
						rankMap.put(rs.getInt("floor"), rank);
						rank++;
					}

					query = "UPDATE BUILDING_PROFILE set rank=? where floor=?";
			prepared = conn
							.prepareStatement(query);
				for (Map.Entry entry : rankMap.entrySet()) {
					prepared.setInt(1, (int) entry.getValue());
					prepared.setInt(2, (int) entry.getKey());
					prepared.addBatch();
				}
				prepared.executeBatch();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
			finally{
			    if (rs != null) {
			        try { rs.close(); } catch (SQLException e) { ; }
			        rs = null;
			      }
			      if (prepared != null) {
			        try { prepared.close(); } catch (SQLException e) { ; }
			        prepared = null;
			      }
			      if (conn != null) {
			        try { conn.close(); } catch (SQLException e) { ; }
			        conn = null;
			      }
			}
			
	}

	public static String getRandomTip(String role) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement prepared = null;
		
		
		String randomTip = null;
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			prepared = conn
					.prepareStatement("SELECT FIRST 1 SAVINGS_TIP from ENERGY_TIPS where role=? and ENABLED =1 order by RAND()");
				prepared.setString(1, role);
			rs = prepared.executeQuery();
					while (rs.next()) {
						randomTip = rs.getString("SAVINGS_TIP");
					}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (prepared != null) {
		        try { prepared.close(); } catch (SQLException e) { ; }
		        prepared = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		}
		
		
		return randomTip;

	}

	public static Vector<BuildingFloor> getFloorRankings() {
		Connection conn = null;
		PreparedStatement prepared = null;
		ResultSet rs = null;
		
		Vector<BuildingFloor> rankedFloors = new<BuildingFloor> Vector();

		try{
			conn = DatabaseConnection.ds
					.getConnection();
			String query = "SELECT FLOOR,WEEK_CHANGE,RANK FROM BUILDING_PROFILE order by week_Change asc";
			prepared = conn
					.prepareStatement(query);
			rs = prepared.executeQuery();
					while (rs.next()) {
						BuildingFloor floor = new BuildingFloor();
						floor.setFloor(rs.getString("FLOOR"));
						floor.setRank(rs.getInt("rank"));
						floor.setWeekChange(rs.getFloat("Week_change"));
						rankedFloors.add(floor);
					}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (prepared != null) {
		        try { prepared.close(); } catch (SQLException e) { ; }
		        prepared = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		}
		return rankedFloors;
	}

	public static Map<Integer, Integer> getFloorRankingsMap() {
		Connection conn = null;
		PreparedStatement prepared = null;
		ResultSet rs = null;
		
		Vector<BuildingFloor> rankedFloors = new<BuildingFloor> Vector();
		Map<Integer, Integer> rankMap = new HashMap<Integer, Integer>();
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			
			String query = "SELECT FLOOR,WEEK_CHANGE,RANK FROM BUILDING_PROFILE order by week_Change asc";
			prepared =conn
					.prepareStatement(query);
			rs = prepared.executeQuery();
					while (rs.next()) {
						rankMap.put(rs.getInt("FLOOR"), rs.getInt("rank"));
					}
				
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (prepared != null) {
		        try { prepared.close(); } catch (SQLException e) { ; }
		        prepared = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		}
		
		return rankMap;
	}

	public static float getCumulativeUsage(int i) {
		Connection conn = null;
		PreparedStatement prepared = null;
		ResultSet rs = null;
		float usage = 0;
		try  {
			conn = DatabaseConnection.ds
					.getConnection();
			prepared = conn
					.prepareStatement("SELECT WEEK_CUMULATIVE FROM BUILDING_PROFILE where floor=?");
				prepared.setInt(1, i);
				rs = prepared.executeQuery();
				while (rs.next()) {
						usage = rs.getFloat("WEEK_CUMULATIVE");
					}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (prepared != null) {
		        try { prepared.close(); } catch (SQLException e) { ; }
		        prepared = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		}
		return usage;
	}

	public static float getWeekChange(int floor) {
		Connection conn = null;
		PreparedStatement prepared = null;
		ResultSet rs = null;
		
		float change = 0;
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			prepared = conn
					.prepareStatement("SELECT WEEK_CHANGE FROM BUILDING_PROFILE where floor=?");
			prepared.setInt(1, floor);
			rs = prepared.executeQuery();
			while (rs.next()) {
						change = rs.getFloat("WEEK_CHANGE");
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}	
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (prepared != null) {
		        try { prepared.close(); } catch (SQLException e) { ; }
		        prepared = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		}
		
		return change;
	}

	public static String getTreatmentStatus(int floor) {
		Connection conn = null;
		PreparedStatement prepared = null;
		ResultSet rs = null;
		
		String treatment = null;
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			prepared = conn
					.prepareStatement("SELECT TREATMENT FROM BUILDING_PROFILE WHERE floor =?");
			prepared.setInt(1, floor);
			rs = prepared.executeQuery();
			while (rs.next()) {
						treatment = rs.getString("TREATMENT");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (prepared != null) {
		        try { prepared.close(); } catch (SQLException e) { ; }
		        prepared = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		}
		return treatment;
	}

	public static void setGroup(Map<String, String> floorGroup) {
		Connection conn = null;
		PreparedStatement prepared = null;
		ResultSet rs = null;
		
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			prepared = conn
					.prepareStatement("UPDATE BUILDING_PROFILE set TREATMENT=? where floor=?");
				for (Map.Entry<String, String> entry : floorGroup.entrySet()) {

					prepared.setString(1, entry.getValue());
					prepared.setInt(2, FloorMap.extractDigits(entry.getKey()));
					prepared.addBatch();
				}

				prepared.executeBatch();
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (prepared != null) {
		        try { prepared.close(); } catch (SQLException e) { ; }
		        prepared = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		}
		

	}

	public static Vector<String> getFloorEmail(int floor) {
		Connection conn = null;
		PreparedStatement prepared = null;
		ResultSet rs = null;
		
		Vector<String> emails = new Vector<String>();
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			prepared = conn
					.prepareStatement("SELECT EMAIL FROM EMPLOYEE_PROFILES WHERE FLOOR =? and treatment = 1");
				prepared.setInt(1, floor);
				rs = prepared.executeQuery();
					while (rs.next()) {
						emails.add(rs.getString("EMAIL"));
					}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (prepared != null) {
		        try { prepared.close(); } catch (SQLException e) { ; }
		        prepared = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		}
		return emails;

	}

	public static Vector<BuildingFloor> getTopFloors() {
		Connection conn = null;
		PreparedStatement prepared = null;
		ResultSet rs = null;
		
		Vector<BuildingFloor> topFloors = new Vector<BuildingFloor>();
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			prepared = conn
					.prepareStatement("SELECT FIRST 3 * from BUILDING_PROFILE order by rank asc");
			rs = prepared.executeQuery();
					while (rs.next()) {
						BuildingFloor floor = new BuildingFloor();
						floor.setFloor(Integer.toString(rs.getInt("FLOOR")));
						floor.setWeekChange(rs.getFloat("WEEK_CHANGE"));
						floor.setEnergyAdvocate(rs.getString("ENERGY_ADVOCATE"));
						topFloors.add(floor);
					}
		}

		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (prepared != null) {
		        try { prepared.close(); } catch (SQLException e) { ; }
		        prepared = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		}
		
		return topFloors;

	}

	public static int getFloorRanking(int floor) {
		Connection conn = null;
		PreparedStatement prepared = null;
		ResultSet rs = null;
		int rank = 0;
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			prepared = conn
					.prepareStatement("SELECT RANK FROM BUILDING_PROFILE where floor =?");
			prepared.setInt(1, floor);
			rs = prepared.executeQuery();
					while (rs.next()) {
						rank = rs.getInt("RANK");
					}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (prepared != null) {
		        try { prepared.close(); } catch (SQLException e) { ; }
		        prepared = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		}
		
		
		return rank;
	}

	public static String getEnergyAdvocate(int floor) {
		Connection conn = null;
		PreparedStatement prepared = null;
		ResultSet rs = null;
		String energyAdvocate = "";
		try{
			conn = DatabaseConnection.ds
					.getConnection();
	
				prepared = conn
						.prepareStatement("SELECT ENERGY_ADVOCATE from building_profile where floor = ?");
				prepared.setInt(1, floor);
				rs = prepared.executeQuery();

					while (rs.next()) {
						energyAdvocate = rs.getString("ENERGY_ADVOCATE");
					}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (prepared != null) {
		        try { prepared.close(); } catch (SQLException e) { ; }
		        prepared = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		}

		return energyAdvocate;

	}

	public static Vector<String> getThreeRandomTips(String role) {
		Connection conn = null;
		PreparedStatement prepared = null;
		ResultSet rs = null;
		
		Vector<String> randomTips = new Vector<String>();
		String energyTip;
		int count = 0;
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			prepared = conn
					.prepareStatement("SELECT FIRST 3 SAVINGS_TIP from ENERGY_TIPS where role=? and ENABLED = 1 order by RAND()");
				prepared.setString(1, role);
				rs = prepared.executeQuery();
				while (rs.next()) {
						randomTips.add(rs.getString("SAVINGS_TIP"));
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (prepared != null) {
		        try { prepared.close(); } catch (SQLException e) { ; }
		        prepared = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		}
		
		return randomTips;

	}

	public static HashMap<Integer, Employee> getEnergyAdvocates() {
		
		Connection conn = null;
		PreparedStatement prepared = null;
		ResultSet rs = null;
		
		HashMap<Integer, Employee> energyAdvocates = new HashMap<Integer, Employee>();
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			prepared = conn
					.prepareStatement("select name,email,floor from employee_profiles where name in (select energy_advocate from building_profile where treatment = 'Group B' ) and email is not null and treatment = 1");
			rs = prepared.executeQuery();
					while (rs.next()) {
						Employee employee = new Employee();
						employee.setName(rs.getString("NAME"));
						employee.setEmail(rs.getString("EMAIL"));
						employee.setFloor(rs.getInt("FLOOR"));
						employee.setTreatment(true);
						energyAdvocates.put(employee.getFloor(), employee);
					}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (prepared != null) {
		        try { prepared.close(); } catch (SQLException e) { ; }
		        prepared = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		}
		
		return energyAdvocates;

	}

	public static Vector<Integer> getGroupBFloors() {
		Connection conn = null;
		PreparedStatement prepared = null;
		ResultSet rs = null;
		
		Vector<Integer> groupBEmployees = new Vector<Integer>();
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			prepared = conn
					.prepareStatement("SELECT FLOOR from building_profile where treatment = 'Group B'");
					
			rs = prepared.executeQuery();
					while (rs.next()) {
						groupBEmployees.add(rs.getInt("FLOOR"));
					}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		    if (rs != null) {
		        try { rs.close(); } catch (SQLException e) { ; }
		        rs = null;
		      }
		      if (prepared != null) {
		        try { prepared.close(); } catch (SQLException e) { ; }
		        prepared = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		}
		
		return groupBEmployees;

	}

	public static void addEmployees(Vector<Employee> employeeList) {
		Connection conn = null;
		PreparedStatement prepared = null;
		
		Iterator<Employee> employeeIterator = employeeList.iterator();
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			prepared = conn
					.prepareStatement("INSERT INTO EMPLOYEE_PROFILES(NAME,FLOOR,EMAIL,TREATMENT) VALUES(?,?,?,?)");
				while (employeeIterator.hasNext()) {
					Employee employee = employeeIterator.next();
					prepared.setString(1, employee.getName());
					prepared.setInt(2, employee.getFloor());
					prepared.setString(3, employee.getEmail());
					prepared.setInt(4, (employee.getTreatment() ? 1 : 0));
					prepared.addBatch();
				}

				prepared.executeBatch();
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		      if (prepared != null) {
		        try { prepared.close(); } catch (SQLException e) { ; }
		        prepared = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		}

	}

	public static void addEnergyTips(Vector<EnergyTip> energyTipList) {
		Connection conn = null;
		PreparedStatement prepared = null;
		
		Iterator<EnergyTip> energyTipIterator = energyTipList.iterator();
		try{
			conn = DatabaseConnection.ds
					.getConnection();
			prepared = conn
					.prepareStatement("INSERT INTO ENERGY_TIPS(SAVINGS_TIP,ROLE,ENABLED) values(?,?,?)");
				while (energyTipIterator.hasNext()) {
					EnergyTip energyTip = energyTipIterator.next();
					prepared.setString(1, energyTip.getEnergyTip());
					prepared.setString(2, energyTip.getRole());
					prepared.setInt(3, (energyTip.getEnabled() ? 1 : 0));
					prepared.addBatch();
				}

				prepared.executeBatch();
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		finally{
		      if (prepared != null) {
		        try { prepared.close(); } catch (SQLException e) { ; }
		        prepared = null;
		      }
		      if (conn != null) {
		        try { conn.close(); } catch (SQLException e) { ; }
		        conn = null;
		      }
		}
		
	}

	public static String getDayFromCurrent(int days) {
		String dateFromCurrent = "";
		Date dateBefore = null;
		dateBefore = new DateTime(new Date()).minusDays(days).toDate();

		dateFromCurrent = dateFormat.format(dateBefore);
		return dateFromCurrent;
	}

	public static String getWeekFromCurrent(int weeks) {
		String dateFromCurrent = "";
		Date dateBefore = new DateTime(new Date()).minusWeeks(weeks).toDate();
		dateFromCurrent = dateFormat.format(dateBefore);
		return dateFromCurrent;
	}

	public static String getMonthFromCurrent(int months) {
		String dateFromCurrent = "";
		Date dateBefore = new DateTime(new Date()).minusMonths(months).toDate();
		dateFromCurrent = dateFormat.format(dateBefore);
		return dateFromCurrent;
	}

	public static String getCurrentDate() {
		String currentDate = "";
		String date = dateFormat.format(new Date());
		return date;

	}
	/*public static String getDayFromCurrent(int days) {
		String dateFromCurrent = "";
		Date dateBefore = null;
		Date date = null;
		try {
			date = dateFormat.parse("2014-11-14 12:16:07.0");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dateBefore = new DateTime(date).minusDays(days).toDate();
		dateFromCurrent = dateFormat.format(dateBefore);
		return dateFromCurrent;
	}

	public static String getWeekFromCurrent(int weeks) {
		String dateFromCurrent = "";
		Date date = null;
		try {
			date = dateFormat.parse("2014-11-14 12:16:07.0");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date dateBefore = new DateTime(date).minusWeeks(weeks).toDate();
		dateFromCurrent = dateFormat.format(dateBefore);
		return dateFromCurrent;
	}

	public static String getMonthFromCurrent(int months) {
		String dateFromCurrent = "";
		Date date = null;
		try {
			date = dateFormat.parse("2014-11-14 12:16:07.0");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date dateBefore = new DateTime(date).minusMonths(months).toDate();
		dateFromCurrent = dateFormat.format(dateBefore);
		return dateFromCurrent;
	}

	public static String getCurrentDate() {
		String currentDate = "";
		Date date = null;
		try {
			date = dateFormat.parse("2014-11-14 12:16:07.0");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentDate = dateFormat.format(date);
		return currentDate;

	}*/

}
