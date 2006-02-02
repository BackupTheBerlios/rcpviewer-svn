package org.essentialplatform.services.authentication.noop;

import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.AuthenticationException;
import net.sf.acegisecurity.AuthenticationManager;

/**
 * A bogus authentication manager that authenticates everyone.
 * 
 * @author ted stockwell
 */
public class NoopAuthenticationManager implements AuthenticationManager
{

	public Authentication authenticate(Authentication authentication) 
		throws AuthenticationException {
		authentication.setAuthenticated(true);
		return authentication;
	}

}
