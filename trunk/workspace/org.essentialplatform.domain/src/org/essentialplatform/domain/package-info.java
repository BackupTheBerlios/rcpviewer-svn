/**
 * The structure of a domain.
 * 
 * <p>
 * Each collection of classes pertaining to a domain of interest will belong
 * and be associated with an instance of a {@link IDomain}.  (Alternate
 * names for this concept include "meta-model" and "schema").  
 * 
 * <p>
 * POJO classes are registered directly with the IDomain; registering the
 * classes causes it to be analyzed using a default {@link IDomainAnalyzer}
 * specific to the {@link IDomain} implementation.  Additional 
 * {@link IDomainAnalyzer}s can also be installed on the {@link IDomain} to 
 * allow additional semantics to be gleaned.  To get such analyzers to do 
 * their stuff, invoke the {@link IDomain#done()}.
 * 
 * <p>
 * {@link IDomain}s are stored globally (accessed via a singleton), indexed by
 * their name.  
 * 
 */
package org.essentialplatform.domain;
