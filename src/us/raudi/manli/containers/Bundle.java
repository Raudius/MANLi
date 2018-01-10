package us.raudi.manli.containers;

import java.util.HashMap;
import java.util.Vector;

import com.esotericsoftware.kryo.Kryo;

public class Bundle {
	private HashMap<String, Object> map = new HashMap<>();

	public Boolean putBool(String key, Boolean value) {
		System.out.println(map);
		return (Boolean) map.put(key, value);
	}
	
	public Byte putByte(String key, Byte value) {
		return (Byte) map.put(key, value);
	}
	
	public Character putChar(String key, Character value) {
		return (Character) map.put(key, value);
	}
	
	public Short putShort(String key, Short value) {
		return (Short) map.put(key, value);
	}
	
	public Integer putInt(String key, Integer value) {
		return (Integer) map.put(key, value);
	}
	
	public Long putLong(String key, Long value) {
		return (Long) map.put(key, value);
	}
	
	public Float putFloat(String key, Float value) {
		return (Float) map.put(key, value);
	}
	
	public Double putDouble(String key, Double value) {
		return (Double) map.put(key, value);
	}
	
	public String putString(String key, String value) {
		return (String) map.put(key, value);
	}
	
	
	public Boolean getBool(String key) {
		return (Boolean) getObject(key, Boolean.class);
	}
	
	public Byte getByte(String key) {
		return (Byte) getObject(key, Byte.class);
	}
	
	public Character getChar(String key) {
		return (Character) getObject(key, Character.class);
	}
	
	public Short getShort(String key) {
		return (Short) getObject(key, Short.class);
	}
	
	public Integer getInt(String key) {
		return (Integer) getObject(key, Integer.class);
	}
	
	public Long getLong(String key) {
		return (Long) getObject(key, Long.class);
	}
	
	public Float getFloat(String key) {
		return (Float) getObject(key, Float.class);
	}
	
	public Double getDouble(String key) {
		return (Double) getObject(key, Double.class);
	}
	
	public String getString(String key) {
		return (String) getObject(key, String.class);
	}
	
	
	private Object getObject(String key, Class<?> c) {
		Object obj = map.get(key);
		if(obj != null && c.isInstance(obj))
			return obj;
		
		return null;
	}
	
	public Object remove(String key) {
		return map.remove(key);
	}
	
	
	public static void register(Kryo k) {
		k.register(Bundle.class);
		k.register(HashMap.class);
		k.register(Vector.class);
	}
	
	
	public static void main(String[] args) {
		Bundle b = new Bundle();
		
		b.putBool("bool", true);
		b.putByte("Byte", (byte) 'a');
		b.putChar("char", 'a');
		b.putDouble("Double", 3.14);
		b.putFloat("Float", 3.14f);
		b.putInt("int", 42);
		b.putLong("Long", 42L);
		b.putShort("Short", (short) 1);
		b.putString("string", "hello world");
		b.putString("string", "hello world!");

		System.out.println(  b.getBool("bool")  );
		System.out.println(  b.getByte("Byte")  );
		System.out.println(  b.getChar("char")  );
		System.out.println(  b.getDouble("Double")  );
		System.out.println(  b.getFloat("Float")  );
		System.out.println(  b.getInt("int")  );
		System.out.println(  b.getLong("Long")  );
		System.out.println(  b.getShort("Short")  );
		System.out.println(  b.getString("string")  );
	}
}
