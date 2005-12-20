package org.essentialplatform.remoting.marshalling;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

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
		super();
		xstream = new XStream();
	}

	/*
	 * @see org.essentialplatform.runtime.distribution.IMarshaller#marshalTo(java.lang.Object, java.io.OutputStream)
	 */
	public void marshalTo(Object pojo, OutputStream os) {
		getLogger().debug(os);
		xstream.toXML(pojo, new OutputStreamWriter(os));
	}

	
	/*
	 * @see org.essentialplatform.runtime.distribution.IUnmarshaller#unmarshalFrom(java.io.InputStream)
	 */
	public Object unmarshalFrom(InputStream is) {
		return xstream.fromXML(new InputStreamReader(is));
	}


}
