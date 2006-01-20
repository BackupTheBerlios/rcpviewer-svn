package org.essentialplatform.runtime.shared.remoting.marshalling.xstream;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling;
import org.essentialplatform.runtime.shared.remoting.packaging.standard.StandardAttributeData;

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

	
	public String marshal(Object pojo) {
		return xstream.toXML(pojo);
	}


	public Object unmarshal(String marshalledObject) {
		return xstream.fromXML(marshalledObject);
	}


	/**
	 * Exposes the alias feature of XStream itself, for more compact XML.
	 *  
	 * @param name
	 * @param type
	 */
	public <V> void alias(String name, Class<V> type) {
		xstream.alias(name, type);
	}


}
