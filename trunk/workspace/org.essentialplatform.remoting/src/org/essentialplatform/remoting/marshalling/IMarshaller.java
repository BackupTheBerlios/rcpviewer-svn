package org.essentialplatform.remoting.marshalling;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public interface IMarshaller {
	
	/**
	 * 
	 * @param pojo - the object to be marshalled.
	 * @return the object marshalled to a string (eg XML)
	 */
	public String marshal(Object pojo);

}
