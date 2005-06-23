package de.berlios.rcpviewer.progmodel.standard;
import java.lang.annotation.*;

/**
 * Persistable and visualisable domain object which names the domain to which
 * it belongs.
 * 
 * <p>
 * For many applications all domain objects will reside in a single domain, 
 * hence the name of that domain does not need to be provided: the literal
 * "default" is - erm - defaulted.  However, for applications which allow
 * domain objects from different domains to interact, the name of the domain
 * should be specified (though one can still be left as the default domain).
 * 
 * <p>
 * In implementation terms, a separate metamodel is instantiated for each 
 * domain.  Each metamodel is globally available (through a singleton) via a 
 * hash, keyed on domain name.
 * 
 * <p>
 * Consumed by reflection as well as AspectJ, hence runtime retention policy.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.TYPE)
public @interface InDomain {
	String value() default ProgModelConstants.DEFAULT_DOMAIN_NAME;
	/**
	 * Whether the type can be searched for using some generic search
	 * capability.  Default is false.
	 * 
	 * <p>
	 * If searchable, the UI may render this as enabling from Search menu.
	 * 
	 * @return
	 */
	boolean searchable() default false;
	/**
	 * Whether the type can be explicitly instantiated.  Default is false.
	 * 
	 * <p>
	 * If instantiatable, the UI may render this as enabling 'File>New', for
	 * example.
	 * 
	 * @return
	 */
	boolean instantiable() default false;
	/**
	 * Whether the type can be explicitly saved.  Default is false.
	 * 
	 * <p>
	 * The UI may render this as disabling the 'File>Save', for example.
	 * 
	 * <p>
	 * Note: this does not mean that the object can never be persisted, only
	 * that it isn't surfaced through a generic mechanism of the UI.
	 * 
	 * @return
	 */
	boolean saveable() default false;
}
