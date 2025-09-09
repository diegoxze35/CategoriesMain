package org.damm;

import org.damm.domain.dao.CategoryDao;
import org.damm.domain.dao.Dao;
import org.damm.domain.pojo.Category;

import java.sql.SQLException;

public class Main {
	public static void main(String[] args) throws SQLException {
		Dao<Category> categoryDao = new CategoryDao("mysql", "localhost", 3306, "categories_db", "diego", "1234");
		Category c = new Category();
		c.setName("Category 1");
		c.setDescription("Description 1");
		categoryDao.save(c);
		c.setDescription("Updated Description 1");
		categoryDao.save(c);
		for (Category cc : categoryDao.findAll()) {
			System.out.println(cc);
		}
	}
}