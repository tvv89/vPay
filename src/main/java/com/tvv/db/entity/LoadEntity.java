package com.tvv.db.entity;

import com.tvv.service.exception.AppException;

import java.sql.ResultSet;

public interface LoadEntity<T> {
    T loadRow(ResultSet rs) throws AppException;
}
