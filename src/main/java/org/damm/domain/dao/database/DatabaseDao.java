package org.damm.domain.dao.database;

import org.damm.domain.dao.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DatabaseDao<T, ID> implements Dao<T, ID> {
	
	protected Connection connection;
	private final String driver;
	private final String host;
	private final int port;
	private final String databaseName;
	private final String user;
	private final String password;
	
	public DatabaseDao(String driver, String host, int port, String databaseName, String user, String password) {
		this.driver = driver;
		this.host = host;
		this.port = port;
		this.databaseName = databaseName;
		this.user = user;
		this.password = password;
	}
	
	private void connectToDatabase() throws SQLException {
		String url = "jdbc:" + driver + "://" + host + ":" + port + "/" + databaseName;
		connection = DriverManager.getConnection(url, user, password);
	}
	
	protected void connect() {
		try {
			connectToDatabase();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			connection = null;
		}
	}
	
}
