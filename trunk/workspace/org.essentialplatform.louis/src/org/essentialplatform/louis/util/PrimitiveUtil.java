package org.essentialplatform.louis.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Static methods to aid with primitive classes
 * @author Mike
 */
public class PrimitiveUtil {
	
	private static final Map<Class,Class> WRAPPERS;
	private static final Map<Class,Method> VALUE_OF_METHODS;
	
	static {
		WRAPPERS = new HashMap<Class,Class>();
		WRAPPERS.put( boolean.class, Boolean.class );
		WRAPPERS.put( byte.class, Byte.class );
		WRAPPERS.put( char.class, Character.class );
		WRAPPERS.put( short.class, Short.class );
		WRAPPERS.put( int.class, Integer.class );
		WRAPPERS.put( long.class, Long.class );
		WRAPPERS.put( float.class, Float.class );
		WRAPPERS.put( double.class, Double.class );
		
		VALUE_OF_METHODS = new HashMap<Class,Method>();
		try {
			Method method;
			Class clazz;
			
			clazz = Byte.class;
			method = clazz.getMethod( "valueOf", new Class[]{ String.class } ); //$NON-NLS-1$
			VALUE_OF_METHODS.put( clazz, method );
			VALUE_OF_METHODS.put( byte.class, method );
			
			clazz = Short.class;
			method = clazz.getMethod( "valueOf", new Class[]{ String.class } ); //$NON-NLS-1$
			VALUE_OF_METHODS.put( clazz, method );
			VALUE_OF_METHODS.put( short.class, method );
			
			clazz = Integer.class;
			method = clazz.getMethod( "valueOf", new Class[]{ String.class } ); //$NON-NLS-1$
			VALUE_OF_METHODS.put( clazz, method );
			VALUE_OF_METHODS.put( int.class, method );
			
			clazz = Long.class;
			method = clazz.getMethod( "valueOf", new Class[]{ String.class } ); //$NON-NLS-1$
			VALUE_OF_METHODS.put( clazz, method );
			VALUE_OF_METHODS.put( long.class, method );
			
			clazz = Float.class;
			method = clazz.getMethod( "valueOf", new Class[]{ String.class } ); //$NON-NLS-1$
			VALUE_OF_METHODS.put( clazz, method );
			VALUE_OF_METHODS.put( float.class, method );
			
			clazz = Double.class;
			method = clazz.getMethod( "valueOf", new Class[]{ String.class } ); //$NON-NLS-1$
			VALUE_OF_METHODS.put( clazz, method );
			VALUE_OF_METHODS.put( double.class, method );	
		}
		catch ( NoSuchMethodException nsme ) {
			assert false : "no valueof method " ; //$NON-NLS-1$
		}
	}
	
	/**
	 * Whether the passed class is a wrapper class to a primitive.
	 * @param clazz
	 * @return
	 */
	public static final boolean isWrapperClass( Class clazz ) {
		return WRAPPERS.values().contains( clazz );
	}
	
	/**
	 * Returns the wrapper class for the passed primitve class - or throws
	 * an <code>IllegalArgumentException</code> if not an primitive class
	 * @param clazz
	 * @return
	 */
	public static final Class getWrapperClass( Class clazz ) {
		if( clazz == null ) throw new IllegalArgumentException();
		if( !clazz.isPrimitive() ) throw new IllegalArgumentException();
		return WRAPPERS.get( clazz );
	}
	
	/**
	 * Whether the passed class has a 'valueOf(String)' method.
	 * <br>If not a primitive or a wrapper class throws an 
	 * <code>IllegalArgumentException</code> 
	 * @param clazz
	 * @return
	 */
	public static final boolean hasValueOfMethod( Class clazz ) {
		if( clazz == null ) throw new IllegalArgumentException();
		if( !clazz.isPrimitive() && !isWrapperClass( clazz ) ) {
			throw new IllegalArgumentException();
		}
		return VALUE_OF_METHODS.get( clazz ) != null;
	}
	
	/**
	 * Returns the passed class has a 'valueOf(String)' method - could
	 * be null - check with <code>hasValueOfMethod</code> if necessary
	 * <br>If not a primitive or a wrapper class throws an 
	 * <code>IllegalArgumentException</code> 
	 * @param clazz
	 * @return
	 */
	public static final Method getValueOfMethod( Class clazz ) {
		if( clazz == null ) throw new IllegalArgumentException();
		if( !clazz.isPrimitive() && !isWrapperClass( clazz ) ) {
			throw new IllegalArgumentException();
		}
		return VALUE_OF_METHODS.get( clazz );		
	}

	// prevent instantiation
	private PrimitiveUtil() {
		super();
	}

}
