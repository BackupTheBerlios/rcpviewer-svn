/**
 * 
 */
package org.essentialplatform.core.fixture.progmodel.essential.standard.operation;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Programmatic;

@InDomain
public class CustomerWithProgrammaticPublicVisibilityOperation {
	@Programmatic
	public void placeOrder() {}
	@Programmatic
	public static void create() {}
}