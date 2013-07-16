package net.peaxy.simulator.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class TableSpace implements DataBaseAccessor, Serializable {

	private static final long serialVersionUID = -4989276376257480417L;
	private static TableSpace instance = null;
	
	private ConcurrentHashMap<String, DataTable> allCommandData;
	
	private TableSpace() {
		allCommandData = new ConcurrentHashMap<String, DataTable>();
	}
	
	public static TableSpace getInstance(){
		if(instance == null){
			instance = new TableSpace();
		}
		return instance;
	}
	
	public void addTable(String tableName){
		synchronized (allCommandData) {
			readCommandData(tableName);
			if(!allCommandData.containsKey(tableName)){
				allCommandData.put(tableName, new DataTable());
			}
		}
		saveCommandData(tableName);
	}
	
	public DataTable getTable(String tableName){
		DataTable table = null;
		synchronized (allCommandData) {
			if(!allCommandData.containsKey(tableName)){
				readCommandData(tableName);
			}
			table = allCommandData.get(tableName);
		}
		return table;
	}
	
	private void saveCommandData(String tableName){
		ObjectOutputStream oos = null;
		synchronized (allCommandData) {
			try {
				if(allCommandData.containsKey(tableName)){
					FileOutputStream fos = new FileOutputStream("data/"+tableName+".ser");
					oos = new ObjectOutputStream(fos);
					oos.writeObject(allCommandData.get(tableName));
					oos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(oos != null){
	        		try {
						oos.close();
					} catch (IOException e) {
					}
	        	}
			}
		}
	}
	
	private void readCommandData(String tableName) {
		synchronized (allCommandData) {
			if(!allCommandData.containsKey(tableName)){
				ObjectInputStream ois = null;
				try {
					FileInputStream fis = new FileInputStream("data/"+tableName+".ser");
					ois = new ObjectInputStream(fis);	        
					DataTable data = (DataTable) ois.readObject();
					if(data != null){
						allCommandData.put(tableName, data);
					} else {
						allCommandData.put(tableName, new DataTable());
					}
		        	ois.close(); 
		        } catch (Exception e) {  
		        	//e.printStackTrace();
		        	//allCommandData.put(key, new DataTable());
		        } finally {
		        	if(ois != null){
		        		try {
							ois.close();
						} catch (IOException e) {
						}
		        	}	        	
		        }
			}
		}
	}
	
	@Override
	public boolean hasData(String tableName,String tableKey){
		synchronized (allCommandData) {
			if(allCommandData.containsKey(tableName)){	
				return getTable(tableName).hasTableKey(tableKey);
			}
		}
		return false;
	}

	@Override
	public List<Record> getAllData(String tableName) {
		synchronized (allCommandData) {
			if(allCommandData.containsKey(tableName)){			
				DataTable table = getTable(tableName);
				if(table != null){
					List<Record> list = new ArrayList<Record>();
					for(String key : table.allKeys()){
						list.add(table.getData(key));
					}
					return list;
				}
			}
		}
		return null;
	}

	@Override
	public Record getData(String tableName, String tableKey) {
		synchronized (allCommandData) {
			if(allCommandData.containsKey(tableName)){
				DataTable table = getTable(tableName);
				if(table != null){
					if(table.hasTableKey(tableKey)){
						return table.getData(tableKey);
					} else {
						Record json = table.addEmptyRecord(tableKey);
						return json;					
					}
				}
			}
		}
		return null;
	}

	@Override
	public String getDataValue(String tableName, String tableKey, String dataKey) {
		Record json = getData(tableName,tableKey);
		if(json != null && json.hasKey(dataKey)){
			try {
				return json.getDataValue(dataKey);
			} catch (Exception e) {
			}
		}
		return null;
	}

	@Override
	public void setData(String tableName, String tableKey, JSONObject json) {
		Record data = getData(tableName,tableKey);
		String key;
		if(data != null){
			Iterator<?> iter = json.keys();
			while(iter.hasNext()){
				key = iter.next().toString();
				try {
					//data.put(key, json.get(key));
					data.setDataValue(key, json.get(key).toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		saveCommandData(tableName);
	}
	
	@Override
	public void removeData(String tableName,String tableKey){
		synchronized (allCommandData) {
			if(allCommandData.containsKey(tableName)){
				DataTable table = getTable(tableName);
				if(table != null){
					if(table.hasTableKey(tableKey)){
						table.removeData(tableKey);
					} 
				}
			}
		}
	}
	
	@Override
	public void setData(String tableName, String tableKey, String dataKey,String dataValue) {
		Record json = getData(tableName,tableKey);
		if(json != null ){
			try {
				json.setDataValue(dataKey, dataValue);
			} catch (Exception e) {
			}
		}
		saveCommandData(tableName);
	}

	@Override
	public boolean hasTable(String tableName) {
		return allCommandData.containsKey(tableName);
	}

	@Override
	public boolean isEmptyTable(String tableName) {
		if(hasTable(tableName)){
			return !getTable(tableName).hasData();
		}
		return true;
	}
}
