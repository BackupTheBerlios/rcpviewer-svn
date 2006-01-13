package org.essentialplatform.runtime.shared.remoting.packaging.standard;

import java.util.ArrayList;
import java.util.List;

import org.essentialplatform.runtime.shared.domain.Handle;

public class StandardCollectionReferenceData {
	
	private List<Handle> _handles = new ArrayList<Handle>();
	
	public StandardCollectionReferenceData(String name) {
		_name = name;
	}
	
	private String _name;
	public String getName() {
		return _name;
	}

	public void addHandle(Handle handle) {
		_handles.add(handle);
	}
}
