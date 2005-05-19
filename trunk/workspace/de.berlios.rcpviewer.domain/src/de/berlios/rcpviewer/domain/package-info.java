/**
 * The structure of a domain.
 * 
 * <p>
 * Each collection of classes pertaining to a domain of interest will belong
 * and be associated with an instance of a {@link Domain} class.  (Alternate
 * names for this concept include "meta-model" and "schema").  
 * 
 * <p>
 * Typically POJO classes are registered directly with the Domain, and the its 
 * {@link Domain#done()} method called to allow analysis of references 
 * (associations) between classes to be performed.
 * 
 * <p>
 * In addition, {@link IDomainAnalyzer}s can be installed on the
 * {@link Domain} to allow additional semantics to be gleaned.  To get such
 * analyzers to do their stuff, invoke the {@link Domain#done()}.
 * 
 * <p>
 * {@link Domain}s are stored globally (accessed via a singleton), indexed by
 * their name.  
 * 
 */
package de.berlios.rcpviewer.domain;