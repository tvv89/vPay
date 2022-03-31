package com.tvv.web.command;

import com.tvv.service.exception.AppException;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Command implements Serializable {	

	public abstract void executePost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException;

	public abstract void executeGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException;
	
	@Override
	public final String toString() {
		return getClass().getSimpleName();
	}
}