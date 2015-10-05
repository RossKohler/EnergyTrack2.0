package com.example.database;

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
import java.util.Calendar;
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

public class DatabaseQuery {
	final static Logger errorLogger = Logger.getLogger(Log4jContextListener.class);
	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.s");

	public static Vector<String> getTableNames() {
		Vector<String> tableNames = new Vector<String>();
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			String[] types = { "TABLE" };

			DatabaseMetaData metaData = currentConnection.getMetaData();
			try (ResultSet rs = metaData.getTables(null, null, "%", types)) {
				while (rs.next()) {
					String tableName = rs.getString("TABLE_NAME");
					if (tableName.contains("floor")
							|| tableName.contains("FLOOR")) {
						tableNames.add(tableName);
					}
				}

			}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}

		return tableNames;
	}

	public static Vector<String> checkTables() {
		Vector<String> tableNames = new Vector<String>();
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection();) {
			String[] types = { "TABLE" };
			DatabaseMetaData metaData = currentConnection.getMetaData();

			try (ResultSet rs = metaData.getTables(null, null, "%", types)) {
				while (rs.next()) {
					String tableName = rs.getString("TABLE_NAME");
					if (tableName.contains("ENERGY_TIPS")
							|| tableName.contains("BUILDING_PROFILE")
							|| tableName.contains("EMPLOYEE_PROFILES")) {
						tableNames.add(tableName);
					}
				}

			}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}

		finally {

		}

		return tableNames;
	}

	public static Vector<Employee> getEmployeeInformation() {
		Vector<Employee> buildingEmployees = new Vector<Employee>();

		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection();) {

			try (Statement stmt = currentConnection.createStatement()) {
				try (ResultSet rs = stmt
						.executeQuery("Select * from Employee_Profiles")) {
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
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}

		return buildingEmployees;

	}

	public static Vector<String> getColumnNames(String tableName) {

		Vector<String> columnNames = new Vector<String>();

		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {

			try (Statement stmt = currentConnection.createStatement()) {

				try (ResultSet rs = stmt
						.executeQuery("select rdb$field_name from rdb$relation_fields where rdb$relation_name= '"
								+ tableName + "'")) {
					while (rs.next()) {
						columnNames.add(rs.getString(1));
					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		return columnNames;
	}

	public static void addEmployee(Employee addEmployee) {
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement stmt = currentConnection
					.prepareStatement("INSERT INTO EMPLOYEE_PROFILES(NAME,FLOOR,EMAIL,TREATMENT) VALUES(?,?,?,?)")) {
				stmt.setString(1, addEmployee.getName());
				stmt.setInt(2, addEmployee.getFloor());
				stmt.setString(3, addEmployee.getEmail());
				stmt.setInt(4, (addEmployee.getTreatment() ? 1 : 0));

				stmt.executeUpdate();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
	}

	public static void editEmployee(Employee editEmployee) {
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {

			try (PreparedStatement stmt = currentConnection
					.prepareStatement("UPDATE EMPLOYEE_PROFILES SET NAME=?,FLOOR=?,EMAIL=?,TREATMENT=? WHERE ID=?")) {

				stmt.setString(1, editEmployee.getName());
				stmt.setInt(2, editEmployee.getFloor());
				stmt.setString(3, editEmployee.getEmail());
				stmt.setInt(4, (editEmployee.getTreatment() ? 1 : 0));
				stmt.setInt(5, editEmployee.getID());

				stmt.executeUpdate();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
	}

	public static void deleteEmployee(Employee deleteEmployee) {

		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection();) {
			try (PreparedStatement stmt = currentConnection
					.prepareStatement("DELETE FROM EMPLOYEE_PROFILES WHERE ID = ?")) {

				stmt.setInt(1, deleteEmployee.getID());

				stmt.executeUpdate();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
	}

	public static void setCurrentUsage() {

		for (int floor = 1; floor <= BuildingFloor.FLOORS; floor++) {
			int currentUsage = 0;
			try (Connection currentConnection = DatabaseConnection.ds
					.getConnection()) {

				try (Statement stmt = currentConnection.createStatement()) {
					try (ResultSet rs = stmt
							.executeQuery("SELECT FIRST 2 DISTINCT METERID,DATETIME,CONSUMPTION from KWH where METERID in (select METERID from PTMETER where description like '%SB"
									+ floor + "') order by datetime desc;")) {
						while (rs.next()) {
							currentUsage += rs.getInt("CONSUMPTION");
						}
						try (PreparedStatement prepared = currentConnection
								.prepareStatement("UPDATE OR INSERT INTO BUILDING_PROFILE(FLOOR,CURRENT_USAGE) VALUES(?,?) MATCHING(FLOOR);")) {
							prepared.setInt(1, floor);
							prepared.setInt(2, currentUsage);
							prepared.executeUpdate();
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorLogger.error("Error in DatabaseQuery", e);
			}
		}

	}

	public static void set24hourUsage() {
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
			try (Connection currentConnection = DatabaseConnection.ds
					.getConnection()) {
				query = "SELECT distinct METERID,DATETIME,CONSUMPTION from KWH where meterID in (Select meterID from PTMETER where description like '%SB"
						+ floor
						+ "') and DATETIME between '"
						+ getDayFromCurrent(1)
						+ "' AND '"
						+ getCurrentDate()
						+ "' order by datetime desc;";
				try (Statement stmt = currentConnection.createStatement()) {
					try (ResultSet rs = stmt.executeQuery(query)) {
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
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					errorLogger.error("Error in DatabaseQuery", e);
				}
				past = true;
				query = "SELECT distinct METERID,DATETIME,CONSUMPTION from KWH where meterID in (Select meterID from PTMETER where description like '%SB"
						+ floor
						+ "') and DATETIME between '"
						+ getDayFromCurrent(2)
						+ "' AND '"
						+ getDayFromCurrent(1) + "' order by datetime desc";
				usage = 0;
				try (Statement stmt = currentConnection.createStatement()) {
					count = 0;
					try (ResultSet rs = stmt.executeQuery(query)) {
						usage = 0;
						while (rs.next()) {

							usage += rs.getFloat("CONSUMPTION");
							count += 0.5;
						}
						energyChange = (normalUsage * (count / usage) - 1) * 100;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					errorLogger.error("Error in DatabaseQuery", e);
				}

				query = "UPDATE or INSERT into Building_Profile (floor,day_normal,day_low,day_high,day_change) VALUES(?,?,?,?,?) MATCHING(floor)";
				try (PreparedStatement prepared = currentConnection
						.prepareStatement(query)) {
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
			}

			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorLogger.error("Error in DatabaseQuery", e);
			}
		}

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
		for (int floor = 1; floor <= BuildingFloor.FLOORS; floor++) {
			try (Connection currentConnection = DatabaseConnection.ds
					.getConnection()) {
				query = "SELECT distinct METERID,DATETIME,CONSUMPTION from KWH where meterID in (Select meterID from PTMETER where description like '%SB"
						+ floor
						+ "') and DATETIME between '"
						+ getWeekFromCurrent(1)
						+ "' AND '"
						+ getCurrentDate()
						+ "' order by datetime desc;";

				try (Statement stmt = currentConnection.createStatement()) {
					try (ResultSet rs = stmt.executeQuery(query)) {
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
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					errorLogger.error("Error in DatabaseQuery", e);
				}

				query = "SELECT distinct METERID,DATETIME,CONSUMPTION from KWH where meterID in (Select meterID from PTMETER where description like '%SB"
						+ floor
						+ "') and DATETIME between '"
						+ getWeekFromCurrent(2)
						+ "' AND '"
						+ getWeekFromCurrent(1) + "' order by datetime desc";
				
				try (Statement stmt = currentConnection.createStatement()) {
					count = 0;
					try (ResultSet rs = stmt.executeQuery(query)) {
						usage = 0;
						while (rs.next()) {
							usage += rs.getFloat("CONSUMPTION");
							count += 0.5;
						}
						energyChange = (normalUsage * (count / usage) - 1) * 100;
					}
					query = "UPDATE or INSERT into Building_Profile (floor,week_normal,week_low,week_high,week_change) VALUES(?,?,?,?,?) MATCHING(floor)";

					try (PreparedStatement prepared = currentConnection
							.prepareStatement(query)) {
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
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorLogger.error("Error in DatabaseQuery", e);
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

		for (int floor = 1; floor <= BuildingFloor.FLOORS; floor++) {
			try (Connection currentConnection = DatabaseConnection.ds
					.getConnection()) {
				query = "SELECT distinct METERID,DATETIME,CONSUMPTION from KWH where meterID in (Select meterID from PTMETER where description like '%SB"
						+ floor
						+ "') and DATETIME between '"
						+ getMonthFromCurrent(1)
						+ "' AND '"
						+ getCurrentDate()
						+ "' order by datetime desc;";
				try (Statement stmt = currentConnection.createStatement()) {
					try (ResultSet rs = stmt.executeQuery(query)) {
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
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					errorLogger.error("Error in DatabaseQuery", e);
				}
				query = "SELECT distinct METERID,DATETIME,CONSUMPTION from KWH where meterID in (Select meterID from PTMETER where description like '%SB"
						+ floor
						+ "') and DATETIME between '"
						+ getMonthFromCurrent(2)
						+ "' AND '"
						+ getMonthFromCurrent(1) + "' order by datetime desc";
				try (Statement stmt = currentConnection.createStatement()) {
					count = 0;
					try (ResultSet rs = stmt.executeQuery(query)) {
						usage = 0;
						while (rs.next()) {
							usage += rs.getFloat("CONSUMPTION");
							count += 0.5;
						}
						energyChange = (normalUsage * (count / usage) - 1) * 100;
					}
					query = "UPDATE or INSERT into Building_Profile (floor,month_normal,month_low,month_high,month_change) VALUES(?,?,?,?,?) MATCHING(floor)";
					try (PreparedStatement prepared = currentConnection
							.prepareStatement(query)) {
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
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorLogger.error("Error in DatabaseQuery", e);
			}
		}

	}

	public static void getFloorInformation(BuildingFloor buildingFloor) {
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			String query = "Select * from Building_profile where floor= ?";
			try (PreparedStatement stmt = currentConnection
					.prepareStatement(query)) {
				int floor = FloorMap.extractDigits(buildingFloor.getFloor());
				stmt.setInt(1, floor);
				try (ResultSet rs = stmt.executeQuery()) {
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
			}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}

	}

	public static void getWeekGraphData(int floor,
			XYChart.Series thisWeekValues, XYChart.Series pastWeekValues) {
		float usage = 0;
		int count = 1;
		LocalTime middayTime = new LocalTime("12:00");
		DateFormat outputFormat = new SimpleDateFormat("EEEEE HH:mm ");
		DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");
		DateTimeFormatter formatter = DateTimeFormat
				.forPattern("yyyy-MM-dd HH:mm:ss.s");
		DateTime currentMonday = formatter.parseDateTime(getCurrentDate())
				.toLocalDate().withDayOfWeek(DateTimeConstants.MONDAY)
				.toDateTime(middayTime);
		String stringPastWeekMonday = formatter.print(currentMonday
				.plusDays(-7));
		String stringCurrentMonday = formatter.print(currentMonday
				.plusMinutes(-30));
		Date date = null;
		String dateString;
		String query;
		query = "SELECT distinct METERID,DATETIME,CONSUMPTION from KWH where meterID in (Select meterID from PTMETER where description like '%SB"
				+ floor
				+ "') and DATETIME between ? AND ? order by datetime asc";
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement(query)) {
				prepared.setString(1, stringPastWeekMonday);
				prepared.setString(2, stringCurrentMonday);
				try (ResultSet rs = prepared.executeQuery()) {
					while (rs.next()) {
						usage += rs.getInt("CONSUMPTION");
						if (count % 2 == 0) {
							dateString = rs.getString("DATETIME");
							date = inputFormat.parse(dateString);
							pastWeekValues.getData().add(
									new XYChart.Data(outputFormat.format(date),
											usage));
							count = 1;
							usage = 0;
						} else {
							count++;
						}
					}

					count = 1;
					usage = 0;

					stringCurrentMonday = formatter.print(currentMonday);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorLogger.error("Error in DatabaseQuery", e);
			}
			try (PreparedStatement prepared = currentConnection
					.prepareStatement(query)) {
				prepared.setString(1, stringCurrentMonday);
				prepared.setString(2, getCurrentDate());
				try (ResultSet rs = prepared.executeQuery()) {
					while (rs.next()) {
						usage += rs.getInt("CONSUMPTION");
						if (count % 2 == 0) {
							dateString = rs.getString("DATETIME");
							date = inputFormat.parse(dateString);
							thisWeekValues.getData().add(
									new XYChart.Data(outputFormat.format(date),
											usage));
							count = 1;
							usage = 0;
						} else {
							count++;
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}

	}

	public static void updateTreatmentInformation(BuildingFloor floor) {
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("UPDATE BUILDING_PROFILE SET TREATMENT = ? where floor=?")) {
				prepared.setString(1, floor.getTreatment());
				prepared.setString(2, floor.getFloor());
				prepared.executeUpdate();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}

	}

	public static void randomEnergyAdvocate() {
		Vector<Integer> floors = getGroupBFloors();
		Iterator<Integer> floorIterator = floors.iterator();

		while (floorIterator.hasNext()) {
			int count = 0;
			String energyAdvocate = "";
			String newEnergyAdvocate = "";
			int floor = floorIterator.next();
			try (Connection currentConnection = DatabaseConnection.ds
					.getConnection()) {
				try (PreparedStatement prepared = currentConnection
						.prepareStatement("SELECT ENERGY_ADVOCATE from building_profile where floor =?")) {
					prepared.setInt(1, floor);
					try (ResultSet rs = prepared.executeQuery()) {
						while (rs.next()) {
							energyAdvocate = rs.getString("ENERGY_ADVOCATE");

						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					errorLogger.error("Error in DatabaseQuery", e);
				}

				try (PreparedStatement prepared = currentConnection
						.prepareStatement("SELECT COUNT(*) from employee_profiles where floor=? and treatment=1 and email is not null;")) {
					prepared.setInt(1, floor);
					try (ResultSet rs = prepared.executeQuery()) {
						while (rs.next()) {
							count = rs.getInt("COUNT");

						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					errorLogger.error("Error in DatabaseQuery", e);
				}

				if (count != 0) {
					try (PreparedStatement prepared = currentConnection
							.prepareStatement("SELECT name from employee_profiles where floor=? and treatment = 1 and email is not null order by RAND()")) {
						prepared.setInt(1, floor);
						while (true) {
							try (ResultSet rs = prepared.executeQuery()) {
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
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								errorLogger.error("Error in DatabaseQuery", e);
							}

						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						errorLogger.error("Error in DatabaseQuery", e);
					}
					try (PreparedStatement prepared = currentConnection
							.prepareStatement("UPDATE BUILDING_PROFILE set energy_advocate =? where floor =?")) {

						prepared.setString(1, energyAdvocate);
						prepared.setInt(2, floor);

						prepared.executeUpdate();

					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorLogger.error("Error in DatabaseQuery", e);
			}
		}

	}

	public static void countEmployees() {

		for (int floor = 1; floor <= BuildingFloor.FLOORS; floor++) {
			int employeeCount = 0;
			try (Connection currentConnection = DatabaseConnection.ds
					.getConnection()) {
				try (PreparedStatement prepared = currentConnection
						.prepareStatement("SELECT COUNT(*) from EMPLOYEE_PROFILES where floor=?")) {
					prepared.setInt(1, floor);

					try (ResultSet rs = prepared.executeQuery()) {
						while (rs.next()) {
							employeeCount = rs.getInt("COUNT");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					errorLogger.error("Error in DatabaseQuery", e);
				}

				try (PreparedStatement prepared = currentConnection
						.prepareStatement("UPDATE BUILDING_PROFILE SET NUMBER_EMPLOYEES=? where floor = ?")) {
					prepared.setInt(1, employeeCount);
					prepared.setInt(2, floor);
					prepared.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
					errorLogger.error("Error in DatabaseQuery", e);
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorLogger.error("Error in DatabaseQuery", e);
			}
		}

	}

	public static void disableEmployees(ArrayList<Employee> employees) {
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (Statement stmt = currentConnection.createStatement()) {
				for (Employee employee : employees) {
					stmt.addBatch("update Employee_profiles set treatment = 0 where ID ="
							+ employee.getID());
				}

				stmt.executeBatch();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
	}

	public static void enableEmployees(ArrayList<Employee> employees) {
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (Statement stmt = currentConnection.createStatement()) {
				for (Employee employee : employees) {
					stmt.addBatch("update Employee_profiles set treatment = 1 where ID ="
							+ employee.getID());
				}

				stmt.executeBatch();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}

	}

	public static void weekCumulativeUsage() {
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
			try (Connection currentConnection = DatabaseConnection.ds
					.getConnection()) {
				String query = "SELECT distinct METERID,DATETIME,CONSUMPTION from KWH where meterID in (Select meterID from PTMETER where description like '%SB"
						+ floor
						+ "') and DATETIME between ? AND ? order by datetime desc";

				try (PreparedStatement prepared = currentConnection
						.prepareStatement(query)) {
					prepared.setString(1, startDateTime);
					prepared.setString(2, endDateTime);
					try (ResultSet rs = prepared.executeQuery()) {
						while (rs.next()) {
							usage += rs.getFloat("CONSUMPTION");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					errorLogger.error("Error in DatabaseQuery", e);
				}
				query = "UPDATE BUILDING_PROFILE set week_Cumulative=? where floor =?";

				try (PreparedStatement prepared = currentConnection
						.prepareStatement(query)) {
					prepared.setInt(2, floor);
					prepared.setFloat(1, usage);
					prepared.executeUpdate();
					usage = 0;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//errorLogger.error("Error in DatabaseQuery", e);
			}
		}
	}

	public static Vector<EnergyTip> getEnergyTips() {
		Vector<EnergyTip> energyTipVector = new Vector<EnergyTip>();

		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (Statement stmt = currentConnection.createStatement()) {
				try (ResultSet rs = stmt
						.executeQuery("SELECT * FROM ENERGY_TIPS")) {
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

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}

		return energyTipVector;
	}

	public static void addEnergyTip(EnergyTip newTip) {
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("INSERT INTO ENERGY_TIPS(SAVINGS_TIP,ROLE,ENABLED) values(?,?,?)")) {
				prepared.setString(1, newTip.getEnergyTip());
				prepared.setString(2, newTip.getRole());
				prepared.setInt(3, (newTip.getEnabled() ? 1 : 0));
				prepared.executeUpdate();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
	}

	public static void editEnergyTip(EnergyTip newTip) {

		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("UPDATE ENERGY_TIPS set SAVINGS_TIP=?,ROLE=?,ENABLED=? where ID =?")) {
				prepared.setString(1, newTip.getEnergyTip());
				prepared.setString(2, newTip.getRole());
				prepared.setInt(3, (newTip.getEnabled() ? 1 : 0));
				prepared.setInt(4, newTip.getID());
				prepared.executeUpdate();
			}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
	}

	public static void deleteEnergyTip(EnergyTip newTip) {
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("DELETE FROM ENERGY_TIPS where id =?")) {
				prepared.setInt(1, newTip.getID());

				prepared.executeUpdate();
			}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}

	}

	public static Vector<String> getMailAddresses(String... args) {
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

		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (Statement stmt = currentConnection.createStatement()) {
				try (ResultSet rs = stmt.executeQuery(query)) {
					while (rs.next()) {
						emailAddress.add(rs.getString("EMAIL"));
					}
				}
			}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}

		return emailAddress;

	}

	public static Vector<Employee> getGroupAEmployees() {
		Vector<Employee> employeeList = new Vector<Employee>();
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (Statement stmt = currentConnection.createStatement()) {
				String query = "select name,email,floor from employee_profiles where floor in (select floor from building_profile where treatment = 'Group A') and email is not null and treatment = 1";
				try (ResultSet rs = stmt.executeQuery(query)) {
					while (rs.next()) {
						Employee employee = new Employee();
						employee.setName(rs.getString("NAME"));
						employee.setEmail(rs.getString("EMAIL"));
						employee.setFloor(rs.getInt("FLOOR"));
						employeeList.add(employee);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		return employeeList;
	}

	public static Vector<Employee> getGroupEmployees() {
		Vector<Employee> employeeList = new Vector<Employee>();
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (Statement stmt = currentConnection.createStatement()) {
				String query = "select name,floor,email from employee_profiles where floor in (select floor from building_profile where treatment = 'Group A' or treatment = 'Group B') and email is not null and treatment = 1";
				try (ResultSet rs = stmt.executeQuery(query)) {
					while (rs.next()) {
						Employee employee = new Employee();
						employee.setName(rs.getString("NAME"));
						employee.setEmail(rs.getString("EMAIL"));
						employee.setFloor(rs.getInt("FLOOR"));
						employee.setTreatment(true);
						employeeList.add(employee);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		return employeeList;
	}

	public static Vector<Employee> getGroupBEmployees() {
		Vector<Employee> employeeList = new Vector<Employee>();
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (Statement stmt = currentConnection.createStatement()) {
				String query = "select name,floor,email from employee_profiles where floor in (select floor from building_profile where treatment = 'Group B') and email is not null and treatment = 1;";
				try (ResultSet rs = stmt.executeQuery(query)) {
					while (rs.next()) {
						Employee employee = new Employee();
						employee.setName(rs.getString("NAME"));
						employee.setEmail(rs.getString("EMAIL"));
						employee.setFloor(rs.getInt("FLOOR"));
						employee.setTreatment(true);
						employeeList.add(employee);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		return employeeList;
	}

	public static void setFloorRanks() {
		Map<Integer, Integer> rankMap = new<Integer, Integer> HashMap();
		int rank = 1;
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			String query = "SELECT FLOOR from building_profile order by WEEK_CHANGE asc";
			try (PreparedStatement prepared = currentConnection
					.prepareStatement(query)) {
				try (ResultSet rs = prepared.executeQuery()) {
					while (rs.next()) {
						rankMap.put(rs.getInt("floor"), rank);
						rank++;
					}

					query = "UPDATE BUILDING_PROFILE set rank=? where floor=?";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorLogger.error("Error in DatabaseQuery", e);
			}
			try (PreparedStatement prepared = currentConnection
					.prepareStatement(query)) {
				for (Map.Entry entry : rankMap.entrySet()) {
					prepared.setInt(1, (int) entry.getValue());
					prepared.setInt(2, (int) entry.getKey());
					prepared.addBatch();
				}
				prepared.executeBatch();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
	}

	public static String getRandomTip(String role) {
		String randomTip = null;
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("SELECT FIRST 1 SAVINGS_TIP from ENERGY_TIPS where role=? and ENABLED =1 order by RAND()")) {
				prepared.setString(1, role);
				try (ResultSet rs = prepared.executeQuery()) {
					while (rs.next()) {
						randomTip = rs.getString("SAVINGS_TIP");
					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		return randomTip;

	}

	public static Vector<BuildingFloor> getFloorRankings() {
		Vector<BuildingFloor> rankedFloors = new<BuildingFloor> Vector();

		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			String query = "SELECT FLOOR,WEEK_CHANGE,RANK FROM BUILDING_PROFILE order by week_Change asc";
			try (PreparedStatement prepared = currentConnection
					.prepareStatement(query)) {
				try (ResultSet rs = prepared.executeQuery()) {
					while (rs.next()) {
						BuildingFloor floor = new BuildingFloor();
						floor.setFloor(rs.getString("FLOOR"));
						floor.setRank(rs.getInt("rank"));
						floor.setWeekChange(rs.getFloat("Week_change"));
						rankedFloors.add(floor);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		return rankedFloors;
	}

	public static Map<Integer, Integer> getFloorRankingsMap() {
		Vector<BuildingFloor> rankedFloors = new<BuildingFloor> Vector();
		Map<Integer, Integer> rankMap = new HashMap<Integer, Integer>();
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			String query = "SELECT FLOOR,WEEK_CHANGE,RANK FROM BUILDING_PROFILE order by week_Change asc";
			try (PreparedStatement prepared = currentConnection
					.prepareStatement(query)) {
				try (ResultSet rs = prepared.executeQuery()) {
					while (rs.next()) {
						rankMap.put(rs.getInt("FLOOR"), rs.getInt("rank"));
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		return rankMap;
	}

	public static float getCumulativeUsage(int i) {
		float usage = 0;
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("SELECT WEEK_CUMULATIVE FROM BUILDING_PROFILE where floor=?")) {
				prepared.setInt(1, i);
				try (ResultSet rs = prepared.executeQuery()) {
					while (rs.next()) {

						usage = rs.getFloat("WEEK_CUMULATIVE");
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		return usage;
	}

	public static float getWeekChange(int floor) {
		float change = 0;
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("SELECT WEEK_CHANGE FROM BUILDING_PROFILE where floor=?")) {
				prepared.setInt(1, floor);
				try (ResultSet rs = prepared.executeQuery()) {
					while (rs.next()) {
						change = rs.getFloat("WEEK_CHANGE");
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		return change;
	}

	public static String getTreatmentStatus(int floor) {
		String treatment = null;
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("SELECT TREATMENT FROM BUILDING_PROFILE WHERE floor =?")) {
				prepared.setInt(1, floor);
				try (ResultSet rs = prepared.executeQuery()) {
					while (rs.next()) {
						treatment = rs.getString("TREATMENT");
					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		return treatment;
	}

	public static void setGroup(Map<String, String> floorGroup) {
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("UPDATE BUILDING_PROFILE set TREATMENT=? where floor=?")) {
				for (Map.Entry<String, String> entry : floorGroup.entrySet()) {

					prepared.setString(1, entry.getValue());
					prepared.setInt(2, FloorMap.extractDigits(entry.getKey()));
					prepared.addBatch();
				}

				prepared.executeBatch();
			}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}

	}

	public static Vector<String> getFloorEmail(int floor) {
		Vector<String> emails = new Vector<String>();
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("SELECT EMAIL FROM EMPLOYEE_PROFILES WHERE FLOOR =? and treatment = 1")) {
				prepared.setInt(1, floor);
				try (ResultSet rs = prepared.executeQuery()) {
					while (rs.next()) {
						emails.add(rs.getString("EMAIL"));
					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		return emails;

	}

	public static Vector<BuildingFloor> getTopFloors() {
		Vector<BuildingFloor> topFloors = new Vector<BuildingFloor>();
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("SELECT FIRST 3 * from BUILDING_PROFILE order by rank asc")) {
				try (ResultSet rs = prepared.executeQuery()) {
					while (rs.next()) {
						BuildingFloor floor = new BuildingFloor();
						floor.setFloor(Integer.toString(rs.getInt("FLOOR")));
						floor.setWeekChange(rs.getFloat("WEEK_CHANGE"));
						floor.setEnergyAdvocate(rs.getString("ENERGY_ADVOCATE"));
						topFloors.add(floor);
					}
				}
			}
		}

		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		return topFloors;

	}

	public static int getFloorRanking(int floor) {

		int rank = 0;
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("SELECT RANK FROM BUILDING_PROFILE where floor =?")) {
				prepared.setInt(1, floor);

				try (ResultSet rs = prepared.executeQuery()) {
					while (rs.next()) {
						rank = rs.getInt("RANK");
					}
				}
			}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		return rank;
	}

	public static String getEnergyAdvocate(int floor) {
		String energyAdvocate = "";
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("SELECT ENERGY_ADVOCATE from building_profile where floor = ?")) {
				prepared.setInt(1, floor);
				try (ResultSet rs = prepared.executeQuery()) {

					while (rs.next()) {
						energyAdvocate = rs.getString("ENERGY_ADVOCATE");
					}
				}
			}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}

		return energyAdvocate;

	}

	public static Vector<String> getThreeRandomTips(String role) {
		Vector<String> randomTips = new Vector<String>();
		String energyTip;
		int count = 0;
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("SELECT FIRST 3 SAVINGS_TIP from ENERGY_TIPS where role=? and ENABLED = 1 order by RAND()")) {
				prepared.setString(1, role);
				try (ResultSet rs = prepared.executeQuery()) {
					while (rs.next()) {
						randomTips.add(rs.getString("SAVINGS_TIP"));
					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		return randomTips;

	}

	public static HashMap<Integer, Employee> getEnergyAdvocates() {
		HashMap<Integer, Employee> energyAdvocates = new HashMap<Integer, Employee>();
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("select name,email,floor from employee_profiles where name in (select energy_advocate from building_profile where treatment = 'Group B' ) and email is not null and treatment = 1")) {
				try (ResultSet rs = prepared.executeQuery()) {
					while (rs.next()) {
						Employee employee = new Employee();
						employee.setName(rs.getString("NAME"));
						employee.setEmail(rs.getString("EMAIL"));
						employee.setFloor(rs.getInt("FLOOR"));
						employee.setTreatment(true);
						energyAdvocates.put(employee.getFloor(), employee);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		return energyAdvocates;

	}

	public static Vector<Integer> getGroupBFloors() {
		Vector<Integer> groupBEmployees = new Vector<Integer>();
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("SELECT FLOOR from building_profile where treatment = 'Group B'")) {
				try (ResultSet rs = prepared.executeQuery()) {
					while (rs.next()) {
						groupBEmployees.add(rs.getInt("FLOOR"));
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}
		return groupBEmployees;

	}

	public static void addEmployees(Vector<Employee> employeeList) {
		Iterator<Employee> employeeIterator = employeeList.iterator();
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("INSERT INTO EMPLOYEE_PROFILES(NAME,FLOOR,EMAIL,TREATMENT) VALUES(?,?,?,?)")) {
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
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
		}

	}

	public static void addEnergyTips(Vector<EnergyTip> energyTipList) {
		Iterator<EnergyTip> energyTipIterator = energyTipList.iterator();
		try (Connection currentConnection = DatabaseConnection.ds
				.getConnection()) {
			try (PreparedStatement prepared = currentConnection
					.prepareStatement("INSERT INTO ENERGY_TIPS(SAVINGS_TIP,ROLE,ENABLED) values(?,?,?)")) {

				while (energyTipIterator.hasNext()) {
					EnergyTip energyTip = energyTipIterator.next();
					prepared.setString(1, energyTip.getEnergyTip());
					prepared.setString(2, energyTip.getRole());
					prepared.setInt(3, (energyTip.getEnabled() ? 1 : 0));
					prepared.addBatch();
				}

				prepared.executeBatch();
			}
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorLogger.error("Error in DatabaseQuery", e);
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

}
