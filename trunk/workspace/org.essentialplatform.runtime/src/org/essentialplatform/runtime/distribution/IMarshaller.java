package org.essentialplatform.runtime.distribution;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public interface IMarshaller {
	
	public void marshalTo(Object pojo, OutputStream os);

}
