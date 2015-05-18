package com.hmkcode.spring.mybatis;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(java.util.Date.class)
public class LocalDateTypeHandler extends BaseTypeHandler<Date> {
	@Override
	public void setNonNullParameter(PreparedStatement preparedStatement, int i,
			Date localDate, JdbcType jdbcType) throws SQLException {
		preparedStatement.setDate(i, localDate);
	}

	@Override
	public Date getNullableResult(ResultSet resultSet, String s)
			throws SQLException {
		Date date = resultSet.getDate(s);
		if (date == null) {
			return null;
		}
		return date;
	}

	@Override
	public Date getNullableResult(ResultSet resultSet, int i)
			throws SQLException {
		return resultSet.getDate(i);
	}

	@Override
	public Date getNullableResult(CallableStatement callableStatement,
			int i) throws SQLException {
		return callableStatement.getDate(i);
	}
}