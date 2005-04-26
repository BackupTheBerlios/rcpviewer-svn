package de.berlios.rcpviewer.metamodel;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.berlios.rcpviewer.metamodel.link.LinkSemanticsType;

/**
 * Set of constants relating to this package.
 * 
 * @author Dan Haywood
 */
public final class Constants {

	/**
	 * Cannot instantiate.
	 */
	private Constants() {
	}

	/**
	 * This happens to be the same set as supported by EMF.
	 */
	public final static Set<Class> JDK_VALUE_TYPES = 
		Collections.unmodifiableSet(
			new HashSet<Class>() {
				private static final long serialVersionUID = 1L;
			{
				add(Boolean.class);   add(boolean.class);
				add(Byte.class);      add(byte.class);
				add(Short.class);     add(short.class);
				add(Integer.class);   add(int.class);
				add(Long.class);      add(long.class);
				add(Float.class);     add(float.class);
				add(Double.class);    add(double.class);
				add(Character.class); add(char.class);
				add(String.class);
				add(BigInteger.class);
				add(BigDecimal.class);
				add(java.util.Date.class);
			}
		});

	/**
	 * TODO: not yet being used, for derivation of links.
	 */
	public final static Set<Class<?>> COLLECTION_TYPES = 
		Collections.unmodifiableSet(
			new HashSet<Class<?>>() {
				private static final long serialVersionUID = 1L;
			{
				add(LinkSemanticsType.LIST.getJavaType());
				add(LinkSemanticsType.SET.getJavaType());
				add(LinkSemanticsType.SORTED_SET.getJavaType());
				add(LinkSemanticsType.MAP.getJavaType());
				add(LinkSemanticsType.SORTED_MAP.getJavaType());
			}
		});
	

	/**
	 * TODO: not yet being used, for derivation of links.
	 */
	public final static Class SIMPLE_REF_TYPE =
		LinkSemanticsType.SIMPLE_REF.getJavaType();

	
}
