package org.damm;

import java.sql.SQLException;
import org.damm.domain.dao.CategoryDao;
import org.damm.domain.dao.Dao;
import org.damm.domain.pojo.Category;

public class Main {
	public static void main(String[] args) throws SQLException {
		Dao<Category> categoryDao = new CategoryDao(
				"mysql",
				"localhost",
				3306,
				"categories_db",
				"diego",
				"123"
		);
		Category c = new Category();
		c.setName("Category 1");
		c.setDescription("Description 1");
		System.out.println("INSERT " + categoryDao.save(c));
		c.setDescription("Updated Description 1");
		System.out.println("UPDATE " + categoryDao.save(c));
		for (Category cc : categoryDao.findAll()) {
			System.out.println(cc);
		}

		System.out.println("SELECT BY ID " + categoryDao.findById(1));
		categoryDao.delete(c);
	}
}