package com.tvv.db.entity;

import java.io.Serializable;

/**
 * Same fields for all entity
 */
public abstract class EntityID implements Serializable {

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
