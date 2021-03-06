package org.essentialplatform.runtime.shared.remoting.packaging.standard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.essentialplatform.runtime.shared.domain.Handle;

public class StandardCollectionReferenceData implements INamed {
	
	private List<Handle> _handles = new ArrayList<Handle>();
	
	public StandardCollectionReferenceData(String name) {
		_name = name;
	}
	
	private String _name;
	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.standard.INamed#getName()
	 */
	public String getName() {
		return _name;
	}

	public void addHandle(Handle handle) {
		_handles.add(handle);
	}
	
	public Collection<Handle> getHandles() {
		return Collections.unmodifiableList(_handles);
	}
}
