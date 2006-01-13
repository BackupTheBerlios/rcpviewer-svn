package org.essentialplatform.runtime.shared.remoting.packaging.standard;

import org.essentialplatform.runtime.shared.domain.Handle;

public class StandardOneToOneReferenceData {
	public StandardOneToOneReferenceData(String name, Handle value) {
		_name = name;
		_handle = value;
	}
	
	private String _name;
	public String getName() {
		return _name;
	}
	
	private Handle _handle;
	public Handle getHandle() {
		return _handle;
	}
}
