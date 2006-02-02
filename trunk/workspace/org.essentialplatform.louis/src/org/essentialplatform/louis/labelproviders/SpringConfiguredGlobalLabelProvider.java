/**
 * 
 */
package org.essentialplatform.louis.labelproviders;

import java.util.ArrayList;
import java.util.List;


/**
 * A global <code>ILouisLabelProvider</code> that can handle any object.
 * 
 * <p>
 * Designed to be set up with Spring configuration files (as opposed to being
 * set up using extension points, for example).
 * 
 * <p>
 * Typical setup would be something like:
 * <pre>
 * &lt;bean id="globalLabelProvider" 
 *     class="org.essentialplatform.louis.labelproviders.SpringConfiguredGlobalLabelProvider">
 *     &lt;list>
 *         &lt;ref bean="labelProvider1"/>
 *         &lt;ref bean="labelProvider2"/>
 *     &lt;/list>
 * &lt;/bean>
 * 
 * &lt;bean id="labelProvider1" 
 *     class="some.example.LabelProvider">
 * &lt;/bean>
 * 
 * &lt;bean id="labelProvider2" 
 *     class="some.other.example.LabelProvider">
 * &lt;/bean>
 * </pre> 
 * <p>
 * Where the <tt>labelProvider1</tt> and <tt>labelProvider2</tt> implement
 * {@link ILouisLabelProvider} in turn.
 * 
 * @author Dan Haywood
 */
public final class SpringConfiguredGlobalLabelProvider extends AbstractLabelProviderChain {

	private List<ILouisLabelProvider> _labelProviders = new ArrayList<ILouisLabelProvider>();
	public List<ILouisLabelProvider> getLabelProviders() {
		return _labelProviders;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; if not specified then defaults to an empty list.
	 * 
	 * @param labelProviders
	 */
	public void setLabelProviders(List<ILouisLabelProvider> labelProviders) {
		_labelProviders = labelProviders;
	}

}
