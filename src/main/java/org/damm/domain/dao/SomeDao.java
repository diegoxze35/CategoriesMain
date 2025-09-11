package org.damm.domain.dao;

import org.damm.domain.pojo.Event;

import java.util.List;

public class SomeDao implements Dao<String, Event> {
	@Override
	public String findById(Event event) {
		return "0L";
	}
	
	@Override
	public List<String> findAll() {
		return List.of();
	}
	
	@Override
	public String save(String entity) {
		return "0L";
	}
	
	@Override
	public void delete(String entity) {
	
	}
}
