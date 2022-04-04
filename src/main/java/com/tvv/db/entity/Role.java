package com.tvv.db.entity;

/**
 * User role: ADMIN and USER
 */
public enum Role {
	ADMIN, USER;

	/**
	 * get user role
	 * @param user user, which role you want to get
	 * @return
	 */
	public static Role getRole(User user) {
		int roleId = user.getRole();
		return Role.values()[roleId];
	}
	
	public String getName() {
		return name().toLowerCase();
	}
	
}
