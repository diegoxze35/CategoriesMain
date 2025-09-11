package org.damm.domain.dao;

import java.util.List;

public interface Dao<ENTITY, ID> {
	ENTITY findById(ID id);
	List<ENTITY> findAll();
	ENTITY save(ENTITY entity);
	void delete(ENTITY entity);
}
