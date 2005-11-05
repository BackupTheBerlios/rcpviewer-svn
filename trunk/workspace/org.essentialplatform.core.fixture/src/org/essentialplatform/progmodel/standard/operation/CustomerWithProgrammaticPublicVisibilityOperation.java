/**
 * 
 */
package org.essentialplatform.progmodel.standard.operation;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Programmatic;
import org.essentialplatform.progmodel.standard.*;
@InDomain
public class CustomerWithProgrammaticPublicVisibilityOperation {
	@Programmatic
	public void placeOrder() {}
	@Programmatic
	public static void create() {}
}