package org.damm.domain.dao;

import org.damm.domain.pojo.Category;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao implements Dao<Category> {
	
	private static final String SQL_INSERT = "call create_category(?, ?)";
	private static final String SQL_SELECT = "SELECT * FROM category WHERE id_category = ?";
	private static final String SQL_DELETE = "call delete_category(?)";
	private static final String SQL_UPDATE = "call update_category(?, ?, ?)";
	private static final String SQL_SELECT_ALL = "SELECT * FROM category";
	
	private static Category mapResultSetToCategory(ResultSet resultSet) throws SQLException {
		return new Category(
				resultSet.getInt("id_category"),
				resultSet.getString("name"),
				resultSet.getString("description")
		);
	}
	
	private final Connection connection;
	
	public CategoryDao(String driver, String host, int port, String databaseName, String user, String password) throws SQLException {
		String url = "jdbc:" + driver + "://" + host + ":" + port + "/" + databaseName;
		connection = DriverManager.getConnection(url, user, password);
	}
	
	@Override
	public Category findById(Integer id) {
		Category category = null;
		try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT)) {
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) category = mapResultSetToCategory(resultSet);
		} catch (SQLException e) {
			throw new RuntimeException("Error finding category by ID", e);
		} finally {
			closeConnection();
		}
		return category;
	}
	
	@Override
	public List<Category> findAll() {
		List<Category> categories = new ArrayList<>();
		try (
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)
		) {
			while (resultSet.next()) {
				categories.add(mapResultSetToCategory(resultSet));
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error fetching all categories", e);
		} finally {
			closeConnection();
		}
		return categories;
	}
	
	@Override
	public Category save(Category entity) {
		return entity.getIdCategory() == null ? insertCategory(entity) : updateCategory(entity);
	}
	
	@Override
	public void delete(Category entity) {
		try (CallableStatement statement = connection.prepareCall(SQL_DELETE)) {
			statement.setInt(1, entity.getIdCategory());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Error deleting category", e);
		} finally {
			closeConnection();
		}
	}
	
	private Category updateCategory(Category entity) {
		try (CallableStatement statement = connection.prepareCall(SQL_UPDATE)) {
			statement.setInt(1, entity.getIdCategory());
			statement.setString(2, entity.getName());
			statement.setString(3, entity.getDescription());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Error updating category", e);
		} finally {
			closeConnection();
		}
		return entity;
	}
	
	private Category insertCategory(Category entity) {
		try (CallableStatement statement = connection.prepareCall(SQL_INSERT)) {
			statement.setString(1, entity.getName());
			statement.setString(2, entity.getDescription());
			statement.executeUpdate();
			// Retrieve the generated ID
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				entity.setIdCategory(generatedKeys.getInt(1));
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error saving category", e);
		} finally {
			closeConnection();
		}
		return entity;
	}
	
	private void connect() {
	
	}
	
	private void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
