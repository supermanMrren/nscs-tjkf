package com.boco.nscs.core.dynamicds;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 *
 */
public class DynamicDataSource  extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
	    //获取当前数据源
		return DataSourceContextHolder.getDataSourceType();
	}

}
