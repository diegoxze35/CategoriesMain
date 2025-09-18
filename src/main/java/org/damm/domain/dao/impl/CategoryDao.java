package org.damm.domain.dao.impl;

import org.damm.domain.dao.DatabaseDao;
import org.damm.domain.pojo.Category;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao extends DatabaseDao<Category, Integer> {
	
	private static final String SQL_INSERT = "call create_category(?, ?, ?)";
	private static final String SQL_SELECT = "call select_one(?)";
	private static final String SQL_DELETE = "call delete_category(?)";
	private static final String SQL_UPDATE = "call update_category(?, ?, ?)";
	private static final String SQL_SELECT_ALL = "call select_all()";
	
	public CategoryDao(String driver, String host, int port, String databaseName, String user, String password) throws SQLException {
		super(driver, host, port, databaseName, user, password);
	}
	
	private static Category mapResultSetToCategory(ResultSet resultSet) throws SQLException {
		return new Category(
				resultSet.getInt("id_category"),
				resultSet.getString("name"),
				resultSet.getString("description")
		);
	}
	
	@Override
	public Category findById(Integer id) {
		connect();
		Category category = null;
		try (CallableStatement statement = connection.prepareCall(SQL_SELECT)) {
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
		connect();
		List<Category> categories = new ArrayList<>();
		try (
				CallableStatement statement = connection.prepareCall(SQL_SELECT_ALL);
				ResultSet resultSet = statement.executeQuery()
		) {
			while (resultSet.next()) categories.add(mapResultSetToCategory(resultSet));
		} catch (SQLException e) {
			throw new RuntimeException("Error fetching all categories", e);
		} finally {
			closeConnection();
		}
		return categories;
	}
	
	@Override
	public Category save(Category entity) {
		connect();
		return entity.getIdCategory() == null ? insertCategory(entity) : updateCategory(entity);
	}
	
	@Override
	public void delete(Category entity) {
		connect();
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
			statement.registerOutParameter(3, Types.INTEGER);
			statement.execute();
			entity.setIdCategory(statement.getInt(3));
		} catch (SQLException e) {
			throw new RuntimeException("Error saving category", e);
		} finally {
			closeConnection();
		}
		return entity;
	}
	
}
