package com.tvv.db.entity;

import java.sql.ResultSet;

public interface LoadEntity<T> {
    T loadRow(ResultSet rs);
}
