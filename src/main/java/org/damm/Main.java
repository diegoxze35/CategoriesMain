package org.damm;

import org.damm.domain.dao.Dao;
import org.damm.domain.dao.impl.CategoryDao;
import org.damm.domain.dao.impl.EventDao;
import org.damm.domain.pojo.Category;
import org.damm.domain.pojo.Event;
import org.damm.ui.CrudUI;

import javax.swing.SwingUtilities;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) throws SQLException {
		
		final String driver = "mysql";
		final String host = "localhost";
		final int port = 3306;
		final String databaseName = "categories_db";
		final String user = "diego";
		final String password = "1234";
		
		final Dao<Category, Integer> categoryDao = new CategoryDao(driver, host, port, databaseName, user, password);
		final Dao<Event, Integer> eventDao = new EventDao(driver, host, port, databaseName, user, password);
		
		SwingUtilities.invokeLater(() -> new CrudUI(categoryDao, eventDao).setVisible(true));
	}
}
