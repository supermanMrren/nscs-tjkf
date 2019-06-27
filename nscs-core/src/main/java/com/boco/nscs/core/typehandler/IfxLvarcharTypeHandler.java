package com.boco.nscs.core.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedJdbcTypes(JdbcType.LONGVARCHAR)
public class IfxLvarcharTypeHandler extends BaseTypeHandler<String> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String s, JdbcType jdbcType) throws SQLException {
        ps.setString(i, s);
    }

    @Override
    public String getNullableResult(ResultSet rs, String s) throws SQLException {
        return rs.getString(s);
    }

    @Override
    public String getNullableResult(ResultSet rs, int i) throws SQLException {
        return rs.getString(i);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int i) throws SQLException {
        return cs.getString(i);
    }
}
