package org.essentialplatform.runtime.shared.remoting.packaging;

import org.essentialplatform.runtime.shared.remoting.marshalling.IMarshalling;

/**
 * Factored out since common to both client- and server-side packaging and 
 * unpackaging.
 * 
 * @author Dan Haywood
 *
 */
public interface IMarshallingOptimizer {

	
	/**
	 * Optionally optimize the marshalling to be used.
	 * 
	 * <p>
	 * Certain combinations of <tt>IPackager</tt>s and <tt>IMarshalling</tt>s 
	 * may be optimised, eg to reduce network traffic.  For example the 
	 * StandardPackager can configure XStreamMarshalling to alias its internal
	 * classes for more readable and smaller XML.
	 * 
	 * <p>
	 * Neither the packager nor the marshaller should require that this method
	 * is called.   However, the packager can rely on the fact that if it is
	 * called before marshalling then it will also be called (if on another
	 * instance and/or address space) before unmarshalling.
	 * 
	 * @param marshalling to optimize.
	 */
	public void optimize(IMarshalling marshalling);

}
