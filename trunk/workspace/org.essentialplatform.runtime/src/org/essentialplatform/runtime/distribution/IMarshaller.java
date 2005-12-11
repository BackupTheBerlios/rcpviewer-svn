package org.essentialplatform.runtime.distribution;

import java.io.OutputStream;

public interface IMarshaller {
	
	public void marshalTo(Object pojo, OutputStream os);

}
