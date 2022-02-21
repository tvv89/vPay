package com.tvv.db.entity;

public enum Role {
	ADMIN, USER;
	
	public static Role getRole(User user) {
		int roleId = user.getRole();
		return Role.values()[roleId];
	}
	
	public String getName() {
		return name().toLowerCase();
	}
	
}
