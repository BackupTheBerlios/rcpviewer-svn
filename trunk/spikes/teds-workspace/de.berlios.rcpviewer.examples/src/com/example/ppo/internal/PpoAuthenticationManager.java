package com.example.ppo.internal;

import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.AuthenticationException;
import net.sf.acegisecurity.AuthenticationManager;

/**
 * A bogus authentication manager that authenticates everyone.
 * 
 * @author ted stockwell
 */
public class PpoAuthenticationManager
implements AuthenticationManager
{

	public Authentication authenticate(Authentication pAuthentication) 
	throws AuthenticationException 
	{
		pAuthentication.setAuthenticated(true);
		return pAuthentication;
	}

}
