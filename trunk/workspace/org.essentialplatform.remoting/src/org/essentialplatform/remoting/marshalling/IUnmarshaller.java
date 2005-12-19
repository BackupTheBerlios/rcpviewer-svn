package org.essentialplatform.remoting.marshalling;

import java.io.InputStream;
import java.io.OutputStream;

public interface IUnmarshaller {
	
	public Object unmarshalFrom(InputStream is);

}
