package org.essentialplatform.remoting.marshalling.xstream;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;
import org.essentialplatform.remoting.marshalling.IMarshalling;

import com.thoughtworks.xstream.XStream;

public final class XStreamMarshalling implements IMarshalling {

	protected Logger getLogger() {
		return Logger.getLogger(XStreamMarshalling.class);
	}

	/**
	 * Threadsafe.
	 */
	private final XStream xstream;
	
	public XStreamMarshalling() {
		xstream = new XStream();
	}

	
	/*
	 * @see org.essentialplatform.remoting.marshalling.IMarshaller#marshal(java.lang.Object)
	 */
	public String marshal(Object pojo) {
		return xstream.toXML(pojo);
	}


	/*
	 * @see org.essentialplatform.remoting.marshalling.IUnmarshaller#unmarshal(java.lang.String)
	 */
	public Object unmarshal(String marshalledObject) {
		return xstream.fromXML(marshalledObject);
	}


}
