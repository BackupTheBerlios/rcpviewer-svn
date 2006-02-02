package org.essentialplatform.services.authentication.foobar;

import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.AuthenticationException;
import net.sf.acegisecurity.AuthenticationManager;

/**
 * A bogus authentication manager that authenticates everyone.
 * 
 * @author ted stockwell
 */
public class FooBarAuthenticationManager implements AuthenticationManager
{

	public Authentication authenticate(Authentication authentication) 
		throws AuthenticationException {
		final String name = authentication.getName();
		final Object credentials = authentication.getCredentials();
		
		final boolean authenticated = "foo".equals(name) && "bar".equals(credentials);
		
		authentication.setAuthenticated(authenticated);
		return authentication;
	}

}
