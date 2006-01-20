package org.essentialplatform.runtime.shared.remoting.marshalling;


public interface IUnmarshaller {

	/**
	 * Unmarshalls an object represented as a string.
	 * 
	 * @param marshalledObject, represented as a string
	 * @return the unmarshalled object
	 * 
	 */
	public Object unmarshal(String marshalledObject);

}
