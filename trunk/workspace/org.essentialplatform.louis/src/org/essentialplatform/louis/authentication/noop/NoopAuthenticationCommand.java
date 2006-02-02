package org.essentialplatform.louis.authentication.noop;

import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.GrantedAuthority;
import net.sf.acegisecurity.providers.TestingAuthenticationToken;

import org.eclipse.core.runtime.CoreException;
import org.essentialplatform.louis.authentication.IAuthenticationCommand;

/**
 * Returns an {@link net.sf.acegisecurity.Authentication} with 'foo' as the 
 * principal, 'bar' as the credentials, and an empty array of 
 * {@link GrantedAuthority}s.
 *  
 * @author Dan Haywood
 *
 */
public class NoopAuthenticationCommand implements IAuthenticationCommand {

	public Authentication run() throws CoreException {
		return new TestingAuthenticationToken("foo", "bar", new GrantedAuthority[]{});
	}

}
