package org.damm;

import org.damm.domain.dao.CategoryDao;
import org.damm.domain.dao.Dao;
import org.damm.domain.dao.EventDao;
import org.damm.domain.pojo.Category;
import org.damm.domain.pojo.Event;

import java.sql.SQLException;
import java.util.Date;

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
		
		Category c = new Category();
		c.setName("Category 1");
		c.setDescription("Category Description");
		Event e = new Event();
		e.setName("Event 1");
		e.setDescription("Event Description");
		e.setEventDate(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24)));
		e.setCategory(c);
		
		System.out.println(eventDao.save(e));
		
		e.setName("Event 1 updated");
		
		System.out.println(eventDao.save(e));
		c.setName("Category 1 updated");
		System.out.println(categoryDao.save(c));
		
		System.out.println(eventDao.findById(e.getIdEvent()));
	}
}
