/**
 * 
 */
package org.essentialplatform.runtime.server.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.shared.domain.bindings.IAttributeRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectAttributeRuntimeBinding;

/**
 * Represents server-specific functionality for an <i>instance of</i> a 
 * {@link IAttribute} of a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectAttributeServerBinding extends IObjectAttributeRuntimeBinding, IObjectMemberServerBinding {
}