package com.tvv.db.entity;

import com.tvv.service.exception.AppException;

import java.sql.ResultSet;

/**
 * Interface for load data from DB
 * @param <T>  object from entity package
 */
public interface LoadEntity<T> {
    T loadRow(ResultSet rs) throws AppException;
}
