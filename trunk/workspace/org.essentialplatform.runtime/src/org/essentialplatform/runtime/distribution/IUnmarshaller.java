package org.essentialplatform.runtime.distribution;

import java.io.InputStream;
import java.io.OutputStream;

public interface IUnmarshaller {
	
	public Object unmarshalFrom(InputStream is);

}
