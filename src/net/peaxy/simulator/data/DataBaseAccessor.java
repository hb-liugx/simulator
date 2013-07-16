package net.peaxy.simulator.data;

import java.util.List;

import org.codehaus.jettison.json.JSONObject;

public interface DataBaseAccessor {
	public List<Record> getAllData(String tableName);

	public Record getData(String tableName, String rowKey);

	public String getDataValue(String tableName, String rowKey, String colName);

	public void setData(String tableName, String rowKey, JSONObject json);

	public void removeData(String tableName,String rowKey);
	
	public void setData(String tableName, String tableKey, String dataKey,
			String dataValue);
	
	public boolean hasTable(String tableName);
	
	public boolean hasData(String tableName,String tableKey);
	
	public boolean isEmptyTable(String tableName);
}
