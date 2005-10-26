package de.berlios.rcpviewer.petstore.domain;

import static de.berlios.rcpviewer.progmodel.extended.Prerequisites.require;
import de.berlios.rcpviewer.progmodel.extended.FieldLengthOf;
import de.berlios.rcpviewer.progmodel.extended.IAppContainer;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Lifecycle;
import de.berlios.rcpviewer.progmodel.extended.MaxLengthOf;
import de.berlios.rcpviewer.progmodel.extended.ImmutableOncePersisted;
import de.berlios.rcpviewer.progmodel.extended.RelativeOrder;
import de.berlios.rcpviewer.progmodel.extended.Optional;
import de.berlios.rcpviewer.progmodel.extended.SaveOperation;
import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.standard.Programmatic;


/**
 * Wholly owned component of {@link Customer} that represents the login details
 * for a Customer who shops with the Petstore.
 *  
 * <p>
 * TODOs
 * <ul>
 * <li>TODO: Hibernate mapping to T_ACCOUNT
 * <li>TODO: possibly replace with Ted's OSGi-based authentication.
 * </ul>
 * 
 * <p>
 * Adapted from original xpetstore implementation by Herve Tchepannou.
 * 
 * @author Dan Haywood
 */
@InDomain
@DescribedAs("Account details for a customer")
public class Account {
	
	/**
	 * Constructor.
	 * 
     * <p>
     * <i>
     * See overview for discussion on programming model conventions and 
     * annotations.
     * </i>
	 */
    public Account() {
    }

    
    /**
     * User ID that the customer uses to access their account.
     * 
     * <p>
     * Automatically assigned by the persistence layer.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> The {@link #getUserIdPre()} method demonstrates a programmatic way 
     *      of indicating that the attribute cannot be edited once the object
     *      has initially been persisted.
     * <li> An alternative and much simpler approach would be to simply use the 
     *      {@link ImmutableOncePersisted} annotation.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     * 
     * @return
     */
    @RelativeOrder(1)
	@MaxLengthOf(10)
    @DescribedAs("User ID that the customer uses to access their account.")
    public String getUserId() {
        return _userId;
    }
	/**
	 * Cannot change user id (once persisted).
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
     * Password that the customer must supply to access their account.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul> 
     * <li> The {@link #setPasswordPre(String)} provides a way for the domain
     *      object to veto a proposed value. 
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     */
    @RelativeOrder(2)
	@MaxLengthOf(10)
    @DescribedAs("Password that the customer must supply to access their account.")
    public String getPassword() {
        return _password;
    }
    public void setPassword(final String password) {
        _password = password;
    }
	/**
	 * Ensure that the password is strong enough (in terms of containing
	 * a mix of alphabetic characters, numeric characters, punctuation and
	 * long enough).
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
	 * is strong enough (contains a sufficient mix of alphabetic, numeric and
	 * other characters).
	 * 
	 * <p>
	 * This implementation simply requires that the password is at least
	 * 6 characters in length and contains at least one digit.
	 * 
	 * @param password
	 * @return true if strong enough and, erm, false if not.
	 */
    private boolean isStrongEnough(String password) {
		return password != null &&
		       password.length() > 6 &&
		       password.matches("[0-9]+");
	}


    /**
     * Whether the supplies password matches the one held by this account.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> Normally <code>public</code> methods that are not associated with
     *      the definition of an attribute (getters, setters, getXxxPre and so
     *      forth) are considered to be operations, thus invokable through the
     *      UI.  However, the {@link Programmatic} annotation indicates
     *      that this method should not be considered as such - it is simply
     *      a method to be called programmatically by other domain objects that
     *      (for whatever reason) requires <code>public</code> visibility.
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
     * @see #getAppContainer()
     * @return
     */
    private boolean isPersistent() {
		return getAppContainer().isPersistent(this);
	}

    
    
    /**
     * Save the object in the persistent object store (either creating if was 
     * not already persistent, or updating if was.)
     *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> The {@link SaveOperation} annotation indicates that it is
     *      this method that is the save method.  By convention the method is
     *      called <code>save</code>, but it must be <code>public</code>, take 
     *      no arguments and return <code>void</code>.
     * <li> The <code>unusableReason</code> effectively disables the save
     *      operation (this is a component object).
     * <li> No {@link Order} annotation is required for the save operation.
     * </ul>
     * </i>
     */
    @SaveOperation(unusableReason="Save owning Customer.")
    public void save() {
    	// nothing to do since unusable, see Pre() method  
    }

    

    /**
     * Application container that controls lifecycle of this pojo, injected
     * by the platform.
     * 
     */
	public IAppContainer getAppContainer() {
		return _appContainer;
	}
	public void setAppContainer(final IAppContainer appContainer) {
		_appContainer = appContainer;
	}
	private IAppContainer _appContainer;

}
