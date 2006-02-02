package org.essentialplatform.louis.factory;

import java.util.List;

public interface IGuiFactories {

	/**
	 * Called by Essential.
	 *
	 */
	public void init();
	
	/**
	 * Returns first factory that is applicable for the passed arguments.
	 * @param model
	 * @param parent
	 * @return
	 */
	public IGuiFactory<?> getFactory(Object model, IGuiFactory parent);

	/**
	 * Returns all factories that are applicable for the passed arguments.
	 * <br>Will never be empty as will include the default factory if no
	 * others found.  However if others are found then will <code>not</code>
	 * include the default factory.
	 * @param model
	 * @param parent
	 * @return
	 */
	public List<IGuiFactory<?>> getFactories(Object model, IGuiFactory<?> parent);

}