package de.berlios.rcpviewer.petstore.domain;

import static de.berlios.rcpviewer.progmodel.extended.IPrerequisites.Constraint.INVISIBLE;
import static de.berlios.rcpviewer.progmodel.extended.Prerequisites.require;

import java.io.Serializable;

import de.berlios.rcpviewer.progmodel.extended.IAppContainer;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.MaxLengthOf;
import de.berlios.rcpviewer.progmodel.extended.Order;
import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.standard.LowerBoundOf;
import de.berlios.rcpviewer.progmodel.standard.Programmatic;


/**
 * Wholly owned component of {@link Customer} that represents the login details
 * for a Customer who shops with us.
 *  
 * <p>
 * Adapted from original xpetstore implementation by Herve Tchepannou.
 * 
 * <p>
 * <ul>
 * <li>TODO: Hibernate mapping: T_ACCOUNT
 * <li>TODO: probably replace with Ted's OSGi-based authentication.
 * </ul>
 * 
 * <p>
 * <i>
 * Programming Model notes:
 * <ul>
 * <li> ...
 * </ul>
 * </i>
 * 
 * @author Dan Haywood
 */
@InDomain
@DescribedAs("Account details for a customer")
public class Account {
	
	/**
	 * no-arg constructor required for persistence layer.
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
	 */
    public Account() {
    }

    
    /**
     * Unique identifier for this account.
     * 
     * <p>
     * Automatically assigned by the persistence layer.
     * 
     * <p>
     * <i>
     * Programming Model notes: since there is no <code>FieldLength</code> 
     * annotation the field length will be taken from <code>MaxLength</code>.
     * </i>
     *  
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
     * 
     * @return
     */
    @Order(1)
    @DescribedAs("The user ID that the customer uses to access their account")
	@MaxLengthOf(10) 
    public String getUserId() {
        return _userId;
    }
	/**
	 * Cannot change user id (once persisted).
	 * 
	 * <p>
	 * <i>
	 * Programming Notes: 
	 * <ul>
	 * <li> an alternative way of specifying this would be to use the 
	 *      <code>@ImmutableOncePersisted</code> annotation.
	 * </i>
	 * 
	 * @return
	 */
	public IPrerequisites getUserIdPre() {
		return require(!isPersistent(), "User Id cannot be changed");
	}
	public void setUserId(final String userId) {
        _userId = userId;
    }
	private String _userId;



    /**
     * <p>
     * <i>
     * Programming Model notes:
     * <ul> 
     * <li> field length is defaulted from <code>MaxLength</code> annotation.
     * <li> mandatory since no <code>MaxLength</code> annotation.
     * </ul>
     * </i>
     */
    @Order(2)
    @DescribedAs("The password that the customer must supply to access their account")
	@MaxLengthOf(10) // = @FieldLengthOf since latter not specified explicitly
    public String getPassword() {
        return _password;
    }
    public void setPassword( String password ) {
        _password = password;
    }
	/**
	 * Ensure that the password is strong enough (in terms of containing
	 * a mix of alphabetic characters, numeric characters, punctuation and
	 * long enough).
	 * 
	 * <p>
	 * Note that the minimum length requirements are specified declaratively on
	 * the setter.
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
     * 
	 * @param password - the proposed password.
	 * @return
	 */
	public IPrerequisites setPasswordPre(final String password) {
        return require(isStrongEnough(password), "Password is not strong enough");
    }
	private String _password;
	/**
	 * Helper method that encapsulates the algorithm as to whether a password 
	 * is strong enough.
	 * 
	 * <p>
	 * TODO - provide some sort of crude implementation,
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>

	 * @param password
	 * @return true if strong enough and, erm, false if not.
	 */
    private boolean isStrongEnough(String password) {
		return true;
	}


    /**
     * Whether the supplies password matches the one held by this account.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
     *
     * @param password
     * @return true if there is a match, false otherwise.
     */
    @Programmatic
    public boolean matchPassword(final String password) {
        return password == null
               ? _password == null
               : password.equals( _password );
    }

    
    /**
     * Helper method to allow this pojo to know where it is in its lifecycle
     * (not yet persisted or persisted).
     * 
     * <p>
     * Implementation delegates to equivalent method on the injected
     * application container.
     * 
     * @see #getAppContainer()
     * @return
     */
    private boolean isPersistent() {
		return getAppContainer().isPersistent(this);
	}


    /**
     * Application container that controls lifecycle of this pojo, injected
     * by the framework.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
     */
	public IAppContainer getAppContainer() {
		return _appContainer;
	}
	/**
	 * Allows framework to inject application container.
	 */
	public void setAppContainer(final IAppContainer appContainer) {
		_appContainer = appContainer;
	}
	private IAppContainer _appContainer;



}
