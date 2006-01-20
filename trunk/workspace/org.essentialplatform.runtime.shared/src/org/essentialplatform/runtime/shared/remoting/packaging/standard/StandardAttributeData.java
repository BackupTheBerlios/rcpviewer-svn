package org.essentialplatform.runtime.shared.remoting.packaging.standard;

public class StandardAttributeData implements INamed  {
	public StandardAttributeData(String name, Object value) {
		_name = name;
		_value = value;
	}
	
	private String _name;
	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.standard.INameValuePair#getName()
	 */
	public String getName() {
		return _name;
	}
	
	private Object _value;
	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.standard.INameValuePair#getValue()
	 */
	public Object getValue() {
		return _value;
	}
}
