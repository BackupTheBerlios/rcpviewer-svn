package de.berlios.rcpviewer.app;

import java.net.URL;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.nakedobjects.NakedObjects;
import org.nakedobjects.container.configuration.Configuration;
import org.nakedobjects.container.configuration.ConfigurationFactory;
import org.nakedobjects.container.configuration.ConfigurationPropertiesLoader;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.defaults.LoadedObjectsHashtable;
import org.nakedobjects.object.defaults.LocalReflectionFactory;
import org.nakedobjects.object.defaults.NakedObjectSpecificationImpl;
import org.nakedobjects.object.defaults.NakedObjectSpecificationLoaderImpl;
import org.nakedobjects.object.persistence.OidGenerator;
import org.nakedobjects.object.persistence.defaults.LocalObjectManager;
import org.nakedobjects.object.persistence.defaults.SimpleOidGenerator;
import org.nakedobjects.object.persistence.defaults.TransientObjectStore;
import org.nakedobjects.object.reflect.PojoAdapter;
import org.nakedobjects.object.reflect.PojoAdapterHashImpl;
import org.nakedobjects.object.security.ClientSession;
import org.nakedobjects.reflector.java.JavaBusinessObjectContainer;
import org.nakedobjects.reflector.java.control.SimpleSession;
import org.nakedobjects.reflector.java.fixture.JavaFixture;
import org.nakedobjects.reflector.java.fixture.JavaFixtureBuilder;
import org.nakedobjects.reflector.java.reflect.JavaReflectorFactory;
import org.nakedobjects.system.AboutNakedObjects;
import org.osgi.framework.BundleContext;

import de.berlios.rcpviewer.config.ConfigUtil;

/**
 * Normal NO views are picked up from the NO configuation file and then 
 * instantiated via an 'Exploration' class.  However the RCP lifecycle is 
 * a little different - specifically a deployed RCP would be started by a 
 * native launcher, not as a Java app.  Hence this plugin acts as the 
 * 'Exploration' equivalent.
 * @author Mike
 */
public class ExplorationPlugin extends AbstractUIPlugin {
	
	
	// static fields
    private static final Logger LOG = Logger.getLogger(ExplorationPlugin.class);
	

	// instance fields
	private NakedObject rootObject = null;
	private RCPApplicationContext applicationContext= new RCPApplicationContext();		
	
	
	/**
	 * Returns the shared instance (effectively a singleton as defined so in
	 * the manifest file)
	 */
	public static ExplorationPlugin getDefault() {		
		Plugin plugin= Platform.getPlugin("de.berlios.rcpviewer");
		return (ExplorationPlugin)plugin;
	}
	
	/**
	 * The constructor.
	 */
	public ExplorationPlugin() {
		super();
	}

	/**
	 * What would normally go into the <code>XXXExploration.main()</code> 
	 * method goes here.
	 * <br>This means that NO start-up errors are currently buried within 
	 * the RCP start-up - not ideal.
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		// the sequence of instantiation activities is copied from 
		// JavaExploration but carries out RCP-equivalent operations.
		
		// instead of hard-coded fixtures, pick up from an extension point
		JavaFixture[] fixtures = ConfigUtil.instantiateFixtures();
		int numFixtures = fixtures.length;
		if ( numFixtures == 0 ) throw new Exception( "No fixtures defined" );
		
		
		// NO configuration - again this should propbably come from the
		// dependent bundle root rather than this one
		URL noUrl = context.getBundle().getResource( "/nakedobjects.properties"); 
		Properties noProps = null;
		if ( noUrl != null ) {
			noProps = ConfigurationPropertiesLoader.loadProperties( noUrl, false );
		}
        Configuration configuration = new Configuration();
		if ( noProps != null ) {
			configuration.add( noProps );
		}
        NakedObjects.setConfiguration(configuration);
        ConfigurationFactory.setConfiguration(configuration);

		Logger log = Logger.getLogger("Naked Objects");
        log.info(AboutNakedObjects.getName());
        log.info(AboutNakedObjects.getVersion());
        log.info(AboutNakedObjects.getBuildId());

		// locale
        String localeSpec
        	= ConfigurationFactory.getConfiguration().getString("locale");
        if (localeSpec != null) {
            int pos = localeSpec.indexOf('_');
            Locale locale;
            if (pos == -1) {
                locale = new Locale(localeSpec, "");
            } else {
                String language = localeSpec.substring(0, pos);
                String country = localeSpec.substring(pos + 1);
                locale = new Locale(language, country);
            }
            Locale.setDefault(locale);
            LOG.info("Locale set to " + locale);
        }
        LOG.debug("locale is " + Locale.getDefault());

		// naked object stuff - not understood yet so blindly copied and
		// changed only where necessary - any changes noted:
		
        JavaBusinessObjectContainer container
        	= new JavaBusinessObjectContainer();
        LoadedObjectsHashtable loadedObjectsHashtable
        	= new LoadedObjectsHashtable();
        
		// change - need an object factory than deal with RCP classloader
		// vaguaries
		RCPObjectFactory objectFactory = new RCPObjectFactory();
		// JavaObjectFactory objectFactory = new JavaObjectFactory();
		
        objectFactory.setContainer(container);
        container.setObjectFactory(objectFactory);
        TransientObjectStore objectStore = new TransientObjectStore();
        objectStore.setLoadedObjects(loadedObjectsHashtable);
        OidGenerator oidGenerator = new SimpleOidGenerator();
		
		LocalObjectManager objectManager = new LocalObjectManager();
        objectManager.setObjectStore(objectStore);
        objectManager.setObjectFactory(objectFactory);
        objectManager.setOidGenerator(oidGenerator);
        objectManager.setLoadedObjects(loadedObjectsHashtable);
        NakedObjects.setObjectManager(objectManager);
        container.setObjectManger(objectManager);
		
		//@TODO the default spec loader must be replaced by a loader that is Eclipse-aware
        new NakedObjectSpecificationLoaderImpl();
		
        LocalReflectionFactory reflectionFactory = new LocalReflectionFactory();
        JavaReflectorFactory reflectorFactory = new JavaReflectorFactory();
        PojoAdapter.setPojoAdapterHash(new PojoAdapterHashImpl());
        PojoAdapter.setReflectorFactory(reflectorFactory);
        NakedObjectSpecificationImpl.setReflectionFactory(reflectionFactory);
        NakedObjectSpecificationLoaderImpl.setReflectorFactory(reflectorFactory);
        reflectorFactory.setObjectFactory(objectFactory);
		
		// add all fixtures
		JavaFixtureBuilder builder = new JavaFixtureBuilder();
		for ( int i=0 ; i < numFixtures ; i++ ) {
			builder.addFixture( fixtures[i] );
		}
		// problem here - NakedObjectSpecificationLoaderImpl.loadSpecification()
		// uses a Class.forName() which, if fails, (as it will), changes behaviour
		builder.installFixtures();

		
		// equivalent to 'Exploration.start() but no viewer to display
        ClientSession.setSession( new SimpleSession() );
        String[] classes = builder.getClasses();
        for (int i = 0, num = classes.length; i < numFixtures ; i++) {
			applicationContext.addClass(classes[i]);
        }
		rootObject = PojoAdapter.createNOAdapter( applicationContext);
		
        /* here for reference only:
        SkylarkViewer viewer = new SkylarkViewer();
        viewer.setApplication(context);
        viewer.setObjectManager(objectManager);
        viewer.show();
        */
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	
	/**
	 * Accessor
	 * @return
	 */
	public NakedObject getRootObject() {
		return rootObject;
	}

}
