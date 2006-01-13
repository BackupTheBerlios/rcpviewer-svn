package org.essentialplatform.runtime.shared.remoting.packaging.standard;

public class StandardAttributeData {
	public StandardAttributeData(String name, Object value) {
		_name = name;
		_value = value;
	}
	
	private String _name;
	public String getName() {
		return _name;
	}
	
	private Object _value;
	public Object getValue() {
		return _value;
	}
}
