package org.damm.domain.dao;

import java.util.List;

public interface Dao<T> {
	public T findById(Integer id);
	public List<T> findAll();
	public T save(T entity);
	public void delete(T entity);
}
