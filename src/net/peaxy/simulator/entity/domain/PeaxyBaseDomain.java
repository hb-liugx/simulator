package net.peaxy.simulator.entity.domain;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class PeaxyBaseDomain implements JSONable {
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		JSONObject jsonClass = new JSONObject();
		Class<?> clazz = null;
		try {
			clazz = this.getClass();
			json.put(clazz.getName(), jsonClass);
			parseObject2Json(this, clazz, jsonClass);
			while ((clazz = clazz.getSuperclass()) != null) {
				parseObject2Json(this, clazz, jsonClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public static PeaxyBaseDomain toDomain(String jsonStr) {
		try {
			JSONObject json = new JSONObject(jsonStr);
			Iterator<?> keys = json.keys();
			if (keys.hasNext()) {
				String className = keys.next().toString();
				Class<?> clazz = Class.forName(className);
				Object instance = clazz.newInstance();
				// JSONObject jsonClass = json.getJSONObject(className);
				JSONObject jsonClass = new JSONObject(json.get(className)
						.toString());
				if (instance instanceof PeaxyBaseDomain) {
					while ((clazz = clazz.getSuperclass()) != null) {
						parseJson2Object(jsonClass, instance, clazz);
					}
					parseJson2Object(jsonClass, instance, instance.getClass());
					return (PeaxyBaseDomain) instance;
				} else {
					throw new Exception("the string " + jsonStr
							+ " is not a PeaxyBaseDomain object.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void parseObject2Json(Object instance, Class<?> clazz,
			JSONObject jsonClass) throws Exception {
		String fieldName;
		Object fieldValue;
		Class<?> fieldType;
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			fieldName = field.getName();
			if (!jsonClass.has(fieldName)) {
				fieldType = field.getType();
				fieldValue = field.get(instance);
				if(fieldType.isInterface() && fieldValue != null)
					fieldType = fieldValue.getClass();
				if (JSONable.class.isAssignableFrom(fieldType)) {
					if (fieldValue == null) {
						jsonClass.put(fieldName, String.valueOf((Object) null));
					} else {
						jsonClass.put(fieldName,
								((JSONable) fieldValue).toJSON());
					}
				} else if (isPrimitive(fieldType)) {
					if (fieldValue == null) {
						jsonClass.put(fieldName, String.valueOf((Object) null));
					} else {
						jsonClass.put(fieldName, fieldValue);
					}
				} else if (java.util.List.class.isAssignableFrom(fieldType)) {
					Type t = field.getGenericType();
					if (ParameterizedType.class.isAssignableFrom(t.getClass())) {
						parseListField(fieldName, fieldValue, jsonClass);
					}
				} else if (java.util.Map.class.isAssignableFrom(fieldType)) {
					Type t = field.getGenericType();
					if (ParameterizedType.class.isAssignableFrom(t.getClass())) {
						Type[] t2 = ((ParameterizedType) t)
								.getActualTypeArguments();
						parseMapField(fieldName, fieldValue, jsonClass,
								(Class<?>) t2[0], (Class<?>) t2[1]);
					}
				} else if (fieldType.isArray()) {
					parseArrayField(fieldName, fieldValue, jsonClass);
				} else if (fieldType.isEnum()) {
					fieldType.getEnumConstants();
					parseEnumField(fieldName, fieldValue, jsonClass,fieldType);
				} else {
					//throw new Exception("the field " + fieldName + " of class "
					//	+ clazz.getName() + " is not a jsonable type.");
				}
			}
		}
	}

	private void parseMapField(String fieldName, Object fieldValue,
			JSONObject jsonClass, Class<?> keyType, Class<?> valueType)
			throws Exception {
		if (!isPrimitive(keyType)) {
			throw new Exception(fieldName
					+ " Map key must be String or Number.");
		}
		if (!isPrimitive(valueType)
				&& !JSONable.class.isAssignableFrom(valueType)) {
			throw new Exception(fieldName + " Map value is not a valid type.");
		}
		if (fieldValue == null) {
			jsonClass.put(fieldName, String.valueOf((Object) null));
		} else {
			Map<?, ?> map = (Map<?, ?>) fieldValue;
			Map<Object, Object> m = new Hashtable<Object, Object>();
			Iterator<?> keys = map.keySet().iterator();
			Object key;
			while (keys.hasNext()) {
				key = keys.next();
				if (map.get(key) instanceof JSONable)
					m.put(key, ((JSONable) map.get(key)).toJSON());
				else
					m.put(key, map.get(key));
			}
			jsonClass.put(fieldName, m);
		}
	}

	private void parseArrayField(String fieldName, Object fieldValue,
			JSONObject jsonClass) throws Exception {
		if (fieldValue == null) {
			jsonClass.put(fieldName, String.valueOf((Object) null));
		} else {
			int length = Array.getLength(fieldValue);
			JSONArray jsonArray = new JSONArray();
			Object obj;
			for (int i = 0; i < length; i++) {
				obj = Array.get(fieldValue, i);
				if (obj instanceof JSONable) {
					jsonArray.put(i, ((JSONable) obj).toJSON());
				} else if (isPrimitive(obj.getClass())) {
					jsonArray.put(i, obj);
				}
			}
			jsonClass.put(fieldName, jsonArray);
		}
	}

	private void parseEnumField(String fieldName, Object fieldValue,
			JSONObject jsonClass,Class<?> fieldType) throws Exception {
		if (fieldValue == null) {
			try{
				jsonClass.put(fieldName, String.valueOf(fieldType.getEnumConstants()[0]));
			} catch(Exception e){
				jsonClass.put(fieldName, "null");
			}
		} else {
			jsonClass.put(fieldName, fieldValue.toString());
		}
	}
	private void parseListField(String fieldName, Object fieldValue,
			JSONObject jsonClass) throws Exception {
		if (fieldValue == null) {
			jsonClass.put(fieldName, String.valueOf((Object) null));
		} else {
			List<?> list = (List<?>) fieldValue;
			JSONArray jsonArray = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof JSONable) {
					jsonArray.put(i, ((JSONable) list.get(i)).toJSON());
				} else if (isPrimitive(list.get(i).getClass())) {
					jsonArray.put(i, list.get(i));
				} else if (list.get(i).getClass().isEnum()) {
					jsonArray.put(i, list.get(i).toString());
				}
			}
			jsonClass.put(fieldName, jsonArray);
		}
	}

	private static void parseJson2Object(JSONObject jsonClass, Object instance,
			Class<?> clazz) throws Exception {
		String fieldName;
		Class<?> fieldType;
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			fieldType = field.getType();
			fieldName = field.getName();
			if(jsonClass.has(fieldName)){
				if (PeaxyBaseDomain.class.isAssignableFrom(fieldType)) {
					field.set(instance, PeaxyBaseDomain.toDomain(jsonClass.get(
							fieldName).toString()));
				} else if (isPrimitive(fieldType)) {
					if (String.valueOf((Object) null).equalsIgnoreCase(
							jsonClass.get(fieldName).toString())) {
						field.set(instance, null);
					} else {
						String val = jsonClass.get(fieldName).toString();
						try {
							if(java.lang.Boolean.TYPE == fieldType)
								field.set(instance, Boolean.parseBoolean(val));
							else if(Number.class.isAssignableFrom(fieldType))
								field.set(instance, Integer.parseInt(val));
							else if(fieldType.isPrimitive())
								field.set(instance, Integer.parseInt(val));
							else if(String.class.isAssignableFrom(fieldType))
								field.set(instance, val);
							else 
								field.set(instance, val);
						} catch (Exception e) {
						}
					}
				} else if (java.util.List.class.isAssignableFrom(fieldType)) {
					Type t = field.getGenericType();
					if (ParameterizedType.class.isAssignableFrom(t.getClass())) {
						t = ((ParameterizedType) t).getActualTypeArguments()[0];
						parseJson2List(jsonClass, instance, (Class<?>) t, field);
					}
				} else if (java.util.Map.class.isAssignableFrom(fieldType)) {
					Type t = field.getGenericType();
					if (ParameterizedType.class.isAssignableFrom(t.getClass())) {
						Type t1 = ((ParameterizedType) t).getActualTypeArguments()[0];
						Type t2 = ((ParameterizedType) t).getActualTypeArguments()[1];
						parseJson2Map(jsonClass, instance, (Class<?>) t1,
								(Class<?>) t2, field);
					}
				} else if (fieldType.isArray()) {
					parseJson2Array(jsonClass, instance,
							fieldType.getComponentType(), field);
				} else if (fieldType.isEnum()) {
					parseJson2Enum(jsonClass, instance,
							fieldType.getComponentType(), field,fieldType);
				} else {
					if(fieldType.isInterface()){
						JSONObject json = new JSONObject(jsonClass.getString(fieldName));
						fieldType = Class.forName(json.keys().next().toString());
						if (PeaxyBaseDomain.class.isAssignableFrom(fieldType)) {
							field.set(instance, PeaxyBaseDomain.toDomain(jsonClass.get(
									fieldName).toString()));
						}
					}
					//throw new Exception("the field " + fieldName + " of class "
					//		+ clazz.getName() + " is not a jsonable type.");
				}
			}
		}
	}

	private static void parseJson2Array(JSONObject jsonClass, Object instance,
			Class<?> clazz, Field field) throws Exception {
		if (!isPrimitive(clazz) && !JSONable.class.isAssignableFrom(clazz)) {
			throw new Exception(field.getName()
					+ " Array value is not a valid type.");
		}
		JSONArray array = (JSONArray) jsonClass.get(field.getName());
		field.set(instance, Array.newInstance(clazz, array.length()));
		for (int i = 0; i < array.length(); i++) {
			if (PeaxyBaseDomain.class.isAssignableFrom(clazz)) {
				Array.set(field.get(instance), i,
						PeaxyBaseDomain.toDomain(array.get(i).toString()));
			} else if (isPrimitive(clazz)) {
				Array.set(field.get(instance), i, array.get(i));
			}
		}
	}
	
	private static void parseJson2Enum(JSONObject jsonClass, Object instance,
			Class<?> clazz, Field field,Class<?> fieldType) throws Exception {
		String value = jsonClass.get(field.getName()).toString();
		for(int i=0;i<fieldType.getEnumConstants().length;i++){
			if(fieldType.getEnumConstants()[i].toString().equalsIgnoreCase(value)){
				field.set(instance, fieldType.getEnumConstants()[i]);
			}
		}
	}

	private static void parseJson2List(JSONObject jsonClass, Object instance,
			Class<?> clazz, Field field) throws Exception {
		if (!isPrimitive(clazz) && !JSONable.class.isAssignableFrom(clazz) && !clazz.isEnum()) {
			throw new Exception(field.getName()
					+ " List value is not a valid type.");
		}
		List<Object> list = new ArrayList<Object>();
		if(jsonClass.has(field.getName())){
			if(!jsonClass.get(field.getName()).equals("null")){
				JSONArray array = (JSONArray) jsonClass.get(field.getName());
				for (int i = 0; i < array.length(); i++) {
					if (PeaxyBaseDomain.class.isAssignableFrom(clazz)) {
						list.add(PeaxyBaseDomain.toDomain(array.get(i).toString()));
					} else if (isPrimitive(clazz)) {
						list.add(array.get(i));
					} else if (clazz.isEnum()) {
						for(int j=0;j<clazz.getEnumConstants().length;j++){
							if(clazz.getEnumConstants()[j].toString().equalsIgnoreCase(array.get(i).toString())){
								list.add(clazz.getEnumConstants()[j]);
							}
						}
					}
				}
			}
		}
		field.set(instance, list);
	}

	private static void parseJson2Map(JSONObject jsonClass, Object instance,
			Class<?> keyType, Class<?> valueType, Field field) throws Exception {
		if (!isPrimitive(keyType)) {
			throw new Exception(field.getName()
					+ " Map key must be String or Number.");
		}
		if (!isPrimitive(valueType)
				&& !JSONable.class.isAssignableFrom(valueType)) {
			throw new Exception(field.getName()
					+ " Map value is not a valid type.");
		}
		Map<Object, Object> map = new Hashtable<Object, Object>();
		JSONObject obj = jsonClass.getJSONObject(field.getName());
		Iterator<?> iter = obj.keys();
		Object key;
		while (iter.hasNext()) {
			key = iter.next();
			if (PeaxyBaseDomain.class.isAssignableFrom(valueType)) {
				map.put(key, PeaxyBaseDomain.toDomain(obj.get(key.toString())
						.toString()));
			} else if (isPrimitive(valueType)) {
				map.put(key, obj.get(key.toString()));
			}
		}
		field.set(instance, map);
	}

	private static boolean isPrimitive(Class<?> type) {
		return type.isPrimitive() || String.class.isAssignableFrom(type)
				|| Number.class.isAssignableFrom(type)
				|| Boolean.class.isAssignableFrom(type);
	}
}
