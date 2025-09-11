package org.damm.domain.dao;

import org.damm.domain.pojo.Category;
import org.damm.domain.pojo.Event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDao extends DatabaseDao<Event, Integer> {
	
	private static final String SQL_SELECT_ALL = "select * from event inner join category on event.id_category = category.id_category";
	private static final String SQL_SELECT = "select * from event inner join category " +
			"on event.id_category = category.id_category where event.id_event = ?";
	private static final String SQL_INSERT_UPDATE = "call save_event(?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_DELETE = "delete from event where id_event = ?";
	
	public EventDao(String driver, String host, int port, String databaseName, String user, String password) throws SQLException {
		super(driver, host, port, databaseName, user, password);
	}
	
	private static Event mapResultSetToEvent(ResultSet resultSet) throws SQLException {
		return new Event(
				resultSet.getInt("id_event"),
				resultSet.getString("event.description"),
				resultSet.getDate("date_event"),
				resultSet.getString("event.name"),
				new Category(
						resultSet.getInt("category.id_category"),
						resultSet.getString("category.name"),
						resultSet.getString("category.description")
				)
		);
	}
	
	@Override
	public Event findById(Integer id) {
		Event event = null;
		connect();
		try (PreparedStatement statement = connection.prepareCall(SQL_SELECT)) {
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) event = mapResultSetToEvent(resultSet);
		} catch (SQLException e) {
			throw new RuntimeException("Error finding category by ID", e);
		} finally {
			closeConnection();
		}
		return event;
	}
	
	@Override
	public List<Event> findAll() {
		connect();
		List<Event> events = new ArrayList<>();
		try (
				PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
				ResultSet resultSet = statement.executeQuery()
		) {
			while (resultSet.next()) events.add(mapResultSetToEvent(resultSet));
		} catch (SQLException e) {
			throw new RuntimeException("Error fetching all categories", e);
		} finally {
			closeConnection();
		}
		return events;
	}
	
	@Override
	public Event save(Event entity) {
		connect();
		try (PreparedStatement statement = connection.prepareCall(SQL_INSERT_UPDATE)) {
			// Establecer parámetros para el stored procedure
			// 1. id_event (puede ser null para INSERT)
			if (entity.getIdEvent() == null) {
				statement.setNull(1, java.sql.Types.INTEGER);
			} else {
				statement.setInt(1, entity.getIdEvent());
			}
			
			// 2. id_category (puede ser null para crear nueva categoría)
			Category category = entity.getCategory();
			if (category == null) {
				throw new RuntimeException("Event must have a category");
			}
			
			if (category.getIdCategory() == null) {
				statement.setNull(2, java.sql.Types.INTEGER);
			} else {
				statement.setInt(2, category.getIdCategory());
			}
			
			// 3. category_name
			statement.setString(3, category.getName());
			// 4. category_description
			statement.setString(4, category.getDescription());
			// 5. event_description
			statement.setString(5, entity.getDescription());
			// 6. event_date
			statement.setDate(6, new java.sql.Date(entity.getEventDate().getTime()));
			// 7. event_name
			statement.setString(7, entity.getName());
			
			// Ejecutar el stored procedure
			boolean hasResults = statement.execute();
			
			// Procesar resultados si es un INSERT (nuevo evento)
			if (entity.getIdEvent() == null && hasResults) {
				try (ResultSet rs = statement.getResultSet()) {
					if (rs.next()) {
						// Obtener el ID generado para el evento
						entity.setIdEvent(rs.getInt("id_event"));
						
						// Si se creó una nueva categoría, actualizar el ID de la categoría
						if (category.getIdCategory() == null) {
							category.setIdCategory(rs.getInt("id_category"));
						}
					}
				}
			}
			
			return entity;
		} catch (SQLException e) {
			throw new RuntimeException("Error saving Event", e);
		} finally {
			closeConnection();
		}
	}
	
	@Override
	public void delete(Event entity) {
		connect();
		try (PreparedStatement statement = connection.prepareCall(SQL_DELETE)) {
			statement.setInt(1, entity.getIdEvent());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Error deleting Event", e);
		} finally {
			closeConnection();
		}
	}
	
}
