package org.damm;

import javax.swing.SwingUtilities;
import org.damm.dao.Dao;
import org.damm.dao.impl.CategoryDao;
import org.damm.dao.impl.EventDao;
import org.damm.domain.pojo.Category;
import org.damm.domain.pojo.Event;
import org.damm.ui.CrudUI;

public class Main {
	public static void main(String[] args) {
		final String driver = "postgresql";
		final String host = "localhost";
		final int port = 5432;
		final String databaseName = "categories_db";
		final String user = "diego";
		final String password = "1234";
		
		final Dao<Category, Integer> categoryDao = new CategoryDao(driver, host, port, databaseName, user, password);
		final Dao<Event, Integer> eventDao = new EventDao(driver, host, port, databaseName, user, password);
		
		SwingUtilities.invokeLater(() -> new CrudUI(categoryDao, eventDao).setVisible(true));
	}
}
