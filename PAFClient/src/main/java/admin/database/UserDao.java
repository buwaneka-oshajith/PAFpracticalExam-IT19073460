package admin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import admin.bean.User;

public class UserDao {
	
	private String jdbcURL = "jdbc:mysql://localhost:3306/admindb?useSSL=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "";
	private String jdbcDriver = "com.mysql.cj.jdbc.Driver";
	
	
//USERS SQL STATEMENTS
	
	
	//Insert SQL statement
	private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, email, country) VALUES "
			+ " (?, ?, ?);";
	//Select user by id SQL statement
	private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
	//Select all users SQL statement
	private static final String SELECT_ALL_USERS = "select * from users";
	//Delete users SQL statement
	private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
	//Update Users SQL statement
	private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";
	
	
	
	

	
	//User Database connection
	
public UserDao() {
}
	//Database connection
protected Connection getConnection() {
	Connection connection = null;
	try {
		Class.forName(jdbcDriver);
		connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return connection;
}




//USERS CRUD OPERATIONS



//Add user to the System

public void insertUser(User user) throws SQLException {
	System.out.println(INSERT_USERS_SQL);
	// try-with-resource statement will auto close the connection.
	//connect with Server to Insert Data to System
	try (Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
		preparedStatement.setString(1, user.getName());
		preparedStatement.setString(2, user.getEmail());
		preparedStatement.setString(3, user.getCountry());
		System.out.println(preparedStatement);
		preparedStatement.executeUpdate();
	} catch (SQLException e) {
		printSQLException(e);
	}
}

	//Select users from id

public User selectUser(int id) {
	User user = null;
	//Establishing a Connection
	try (Connection connection = getConnection();
			//Create a statement using connection object
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
		preparedStatement.setInt(1, id);
		System.out.println(preparedStatement);
		//update query
		ResultSet rs = preparedStatement.executeQuery();

		//get user fields
		//
		while (rs.next()) {
			String name = rs.getString("name");
			String email = rs.getString("email");
			String country = rs.getString("country");
			user = new User(id, name, email, country);
		}
	} catch (SQLException e) {
		printSQLException(e);
	}
	return user;
}


//Select All the Users

public List<User> selectAllUsers() {

	// Create Array list users
	List<User> users = new ArrayList<>();
	//Establish a Connection
	try (Connection connection = getConnection();

			//Create a statement using connection object
			//Select All users
		PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
		System.out.println(preparedStatement);
		//Query execute or update
		ResultSet rs = preparedStatement.executeQuery();

		//get user fields
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String email = rs.getString("email");
			String country = rs.getString("country");
			users.add(new User(id, name, email, country));
		}
	} catch (SQLException e) {
		printSQLException(e);
	}
	return users;
}

//update user details in the  system
public boolean updateUser(User user) throws SQLException {
	boolean rowUpdated;
	//Establish a Connection
	try (Connection connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
		System.out.println("updated USer:"+statement);
		statement.setString(1, user.getName());
		statement.setString(2, user.getEmail());
		statement.setString(3, user.getCountry());
		statement.setInt(4, user.getId());

		rowUpdated = statement.executeUpdate() > 0;
	}
	return rowUpdated;
}

//Delete user form the system

public boolean deleteUser(int id) throws SQLException {
	boolean rowDeleted;
	//Establish a Connection
	try (Connection connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
		statement.setInt(1, id);
		rowDeleted = statement.executeUpdate() > 0;
	}
	return rowDeleted;
}





//Exceptions
private void printSQLException(SQLException ex) {
	for (Throwable e : ex) {
		if (e instanceof SQLException) {
			e.printStackTrace(System.err);
			System.err.println("SQLState: " + ((SQLException) e).getSQLState());
			System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
			System.err.println("Message: " + e.getMessage());
			Throwable t = ex.getCause();
			while (t != null) {
				System.out.println("Cause: " + t);
				t = t.getCause();
			}
		}
	}
}






}
