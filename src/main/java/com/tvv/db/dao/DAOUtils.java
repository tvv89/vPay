package com.tvv.db.dao;

import com.tvv.db.entity.EntityID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOUtils {
    public static <T extends EntityID> void getInsertEntityGenerateId(PreparedStatement pstmt, ResultSet rs, T entity) throws SQLException {
        pstmt.executeQuery();
        rs = pstmt.getGeneratedKeys();
        Long entityId = entity.getId();
        if (rs.next()) {
            entityId = rs.getLong(1);
        }
        entity.setId(entityId);
        rs.close();
        pstmt.close();
    }
}
