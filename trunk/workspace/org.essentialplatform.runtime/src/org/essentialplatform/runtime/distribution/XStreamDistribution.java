package org.essentialplatform.runtime.distribution;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.thoughtworks.xstream.XStream;

public final class XStreamDistribution implements IDistribution {

	/**
	 * Threadsafe.
	 */
	private final XStream xstream;
	
	public XStreamDistribution() {
		super();
		xstream = new XStream();
	}

	/*
	 * @see org.essentialplatform.runtime.distribution.IMarshaller#marshalTo(java.lang.Object, java.io.OutputStream)
	 */
	public void marshalTo(Object pojo, OutputStream os) {
		xstream.toXML(pojo, new OutputStreamWriter(os));
	}

	
	/*
	 * @see org.essentialplatform.runtime.distribution.IUnmarshaller#unmarshalFrom(java.io.InputStream)
	 */
	public Object unmarshalFrom(InputStream is) {
		return xstream.fromXML(new InputStreamReader(is));
	}


}
