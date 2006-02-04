package org.essentialplatform.louis.app;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.louis.Bootstrap;
import org.essentialplatform.louis.DomainBootstrapJob;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.SessionBootstrapJob;
import org.essentialplatform.louis.app.workbench.ApplicationWorkbenchAdvisor;
import org.essentialplatform.louis.authentication.IAuthenticationCommand;
import org.essentialplatform.louis.authentication.noop.NoopAuthenticationCommand;
import org.essentialplatform.louis.dnd.IDndTransferProvider;
import org.essentialplatform.louis.factory.IGuiFactories;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.louis.labelproviders.ILouisLabelProvider;
import org.essentialplatform.louis.util.JobUtil;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.progmodel.louis.runtime.LouisProgModelRuntimeBuilder;
import org.essentialplatform.runtime.client.domain.bindings.RuntimeClientBinding;
import org.essentialplatform.runtime.shared.RuntimePlugin;
import org.essentialplatform.runtime.shared.domain.ExtensionPointReadingDomainRegistry;
import org.essentialplatform.runtime.shared.domain.IDomainBootstrap;
import org.essentialplatform.runtime.shared.domain.SingleDomainRegistry;
import org.essentialplatform.runtime.shared.session.SessionBinding;

public final class SecureApplication implements IApplication {

	private Logger getLogger() { return Logger.getLogger(SecureApplication.class); }
	
	public SecureApplication() {}


	////////////////////////////////////////////////////////////////////
	// init
	////////////////////////////////////////////////////////////////////


	/**
	 * Provided by {@link Bootstrap}.
	 */
	public void init(IDomainDefinition domainDefinition, String objectStoreName) {
		_domainDefinition = domainDefinition;
		_sessionBinding = new SessionBinding(domainDefinition.getDomainName(), objectStoreName);
	}

	private IDomainDefinition _domainDefinition;
	public IDomainDefinition getDomainDefinition() {
		return _domainDefinition;
	}
	
	private SessionBinding _sessionBinding;
	public SessionBinding getSessionBinding() {
		return _sessionBinding;
	}
	
	
	////////////////////////////////////////////////////////////////////
	// run
	////////////////////////////////////////////////////////////////////

	
	/*
	 * @see org.eclipse.core.runtime.IPlatformRunnable#run(java.lang.Object)
	 */
	public Object run(Object args) throws Exception {
		
		Binding.setBinding(
				new RuntimeClientBinding(_domainDefinition.getDomainBuilder()));

		IDomainBootstrap bootstrap = _domainDefinition.getDomainBootstrap();
		bootstrap.getSecondaryBuilders().add(new LouisProgModelRuntimeBuilder());


		final String domainName = _domainDefinition.getDomainName();
		try {
			// authenticate the user
			if (_authenticationCommand.run() == null) {
				return IPlatformRunnable.EXIT_OK;  
			}

			// Register self with plugin.
			// TODO: this needs to move out of LouisPlugin and into the
			// IClientDomainBinding.  Louis should then look at the object that
			// it is interacting with and pick up the information from its
			// domainClass' domain's binding.
			LouisPlugin.getDefault().setApplication(this);

			// domain initialisation
			SingleDomainRegistry domainRegistry = 
				new SingleDomainRegistry(bootstrap, domainName);
			RuntimePlugin.getDefault().setDomainRegistry(domainRegistry);
			DomainBootstrapJob domainJob = new DomainBootstrapJob(domainRegistry);
			domainJob.schedule();

			// session initialisation (default domain & store for now )
			SessionBootstrapJob sessionJob = new SessionBootstrapJob(getSessionBinding());
			sessionJob.schedule();

			// instantiate fields
			_guiFactories = _domainDefinition.getGuiFactories();
			_guiFactories.init();
			_globalLabelProvider = _domainDefinition.getGlobalLabelProvider();
			_globalLabelProvider.init();
			
			// effectively running jobs synchronously at the moment
			JobUtil.waitForJob( domainJob, getLogger() );
			JobUtil.waitForJob( sessionJob, getLogger() );
			
			// this must be run once domain classes known
			_transferProvider = _domainDefinition.getGlobalDndTransferProvider();
			_transferProvider.init();
			
			int returnCode = PlatformUI.createAndRunWorkbench(_display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IPlatformRunnable.EXIT_RESTART;
			}
			return IPlatformRunnable.EXIT_OK;

		} catch(NullPointerException ex) {
			/**
			 * Getting an NPE.  Think it's my fault, though not sure why, somewhere
			 * in the CurrTran view.
			 * 
			 * *** Strangely, this code doesn't seem to be picking up the NPE either.
			 * 
			 * TODO: obviously, this catch clause is a kludge.  Perhaps Mike can help me solve it?!?
			 * 
			 * @see http://dev.eclipse.org/newslists/news.eclipse.platform.rcp/msg00508.html
			 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=74139
			 */
			// do nothing
			return IPlatformRunnable.EXIT_OK;
		}
		finally {
			Platform.endSplash();
		}
	}


	////////////////////////////////////////////////////////////////////
	// GuiFactories
	////////////////////////////////////////////////////////////////////
	
	private IGuiFactories _guiFactories= null;
	
	
	/**
	 * Returns first factory that is applicable for the passed arguments.
	 * @param object
	 * @param context
	 * @return gui factory
	 */
	public IGuiFactory<?> getGuiFactory( Object model, IGuiFactory parent ) {
		return _guiFactories.getFactory( model, parent );
	}
	
	/**
	 * Returns all factories that are applicable for the passed arguments.
	 * <br>Will never be empty as will include the default factory if no
	 * others found.
	 * @param object
	 * @param context
	 * @return list of gui factories
	 */
	public List<IGuiFactory<?>> getGuiFactories( Object model, IGuiFactory parent ) {
		return _guiFactories.getFactories( model, parent );
	}


	///////////////////////////////////////////////////////////////
	// Display (injected)
	///////////////////////////////////////////////////////////////
	
	private Display _display;
	public Display getDisplay() {
		return _display;
	}
	/**
	 * Mandatory.
	 * 
	 * @param display
	 */
	public void setDisplay(Display display) {
		_display = display;
	}

	///////////////////////////////////////////////////////////////
	// LoginWizardAuthenticationCommand (injected)
	///////////////////////////////////////////////////////////////
	
	private IAuthenticationCommand _authenticationCommand = new NoopAuthenticationCommand();
	public IAuthenticationCommand getAuthenticationCommand() {
		return _authenticationCommand;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; defaults to the {@link NoopAuthenticationCommand}.
	 * 
	 * @param authenticationCommand
	 */
	public void setAuthenticationCommand(
			IAuthenticationCommand authenticationCommand) {
		_authenticationCommand = authenticationCommand;
	}


	////////////////////////////////////////////////////////////////////
	// GlobalLabelProvider
	////////////////////////////////////////////////////////////////////
	

	private ILouisLabelProvider _globalLabelProvider = null;
	
	/**
	 * Accessor to global label provider
	 * 
	 * @return label provider
	 */
	public ILouisLabelProvider getGlobalLabelProvider() {
		return _globalLabelProvider;
	}


	////////////////////////////////////////////////////////////////////
	// GlobalTransferProvider
	////////////////////////////////////////////////////////////////////

	private IDndTransferProvider _transferProvider = null;
	
	/**
	 * Accessor to global transfer provider.
	 * 
	 * @return transfer provider
	 */
	public IDndTransferProvider getGlobalTransferProvider() {
		return _transferProvider;
	}



}
