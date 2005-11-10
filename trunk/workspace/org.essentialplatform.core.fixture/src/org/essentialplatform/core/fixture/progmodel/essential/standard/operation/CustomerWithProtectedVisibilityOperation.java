/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.standard.operation;
import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class CustomerWithProtectedVisibilityOperation {
	protected void placeOrder() {}
	protected static void create() {}
}