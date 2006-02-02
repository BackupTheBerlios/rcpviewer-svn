package org.essentialplatform.louis.factory;

import java.util.ArrayList;
import java.util.List;


/**
 * A global <code>IGuiFactories</code> that can handle any object.
 * 
 * <p>
 * Designed to be set up with Spring configuration files (as opposed to being
 * set up using extension points, for example).
 * 
 * <p>
 * Typical setup would be something like:
 * <pre>
 * &lt;bean id="guiFactories" 
 *     class="org.essentialplatform.louis.factory.SpringConfiguredguiFactories">
 *     &lt;list>
 *         &lt;ref bean="factories1"/>
 *         &lt;ref bean="factories2"/>
 *     &lt;/list>
 * &lt;/bean>
 * 
 * &lt;bean id="factories1" 
 *     class="some.example.GuiFactories">
 * &lt;/bean>
 * 
 * &lt;bean id="factories2" 
 *     class="some.other.example.GuiFactories">
 * &lt;/bean>
 * </pre> 
 * <p>
 * Where the <tt>factories1</tt> and <tt>factories2</tt> implement
 * {@link IGuiFactories} in turn.
 * 
 * @author Dan Haywood
 */
public final class SpringConfiguredGuiFactories extends AbstractGuiFactoriesChain {
	
	private List<IGuiFactories> _guiFactories = new ArrayList<IGuiFactories>();
	public List<IGuiFactories> getGuiFactories() {
		return _guiFactories;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; if not specified then defaults to an empty list.
	 * 
	 * @param guiFactories
	 */
	public void setGuiFactories(List<IGuiFactories> guiFactories) {
		_guiFactories = guiFactories;
	}
	
}
