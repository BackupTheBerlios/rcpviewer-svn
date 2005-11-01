package org.essentialplatform;

import org.essentialplatform.domain.IDomain;
import org.essentialplatform.domain.IDomainClass;

/**
 * Encapsulates the differences for obtaining {@link IDomain}s and
 * {@link IDomainClass}es between running tests in a runtime vs compiletime 
 * environments.
 *
 * <p>
 * The tests in this plugin are applicable to both deployment environments, 
 * however the means for getting hold of the {@link IDomain} necessarily
 * varies.  The implementations of this interface provide a way for otherwise
 * generic tests to be run in either environment.
 * 
 * <p>
 * For example, the runtime {@link IDomain} uses Java reflection whereas the
 * compile-time equivalent uses Eclipse's Java AST.  The implementation 
 * therefore uses the appropriate API.
 * 
 * @author Dan Haywood
 */
public interface IDeploymentSpecifics {

	IDomain getDomainInstance();
	IDomain getDomainInstance(final String domainName);

	/**
	 * The run-time Domain implementation uses Java classes as an identifier, 
	 * whereas the compile-time Domain implementation does not.  However, 
	 * during testing the Java classes representing the fixture POJOs <i>are</i> 
	 * available.  It is therefore the job of implementations of <i>this</i>
	 * class (specifically, the compile-time implementation) to convert the 
	 * Java class into representation appropriate to do a lookup of the 
	 * corresponding Domain implementation.
	 *  
	 * 
	 * @param <T>
	 * @param domainClassIdentifier
	 * @return
	 */
	<T> IDomainClass lookupAny(Class<T> domainClassIdentifier);
	void resetAll();

}
