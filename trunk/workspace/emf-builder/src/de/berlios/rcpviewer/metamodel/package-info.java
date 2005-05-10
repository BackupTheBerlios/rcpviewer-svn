/**
 * The structure of the MetaModel.
 * 
 * <p>
 * {@link MetaModel} <i>is</i> the metamodel.  Typically POJO classes are 
 * registered directly with the MetaModel, and the its {@link MetaModel#done()}
 * method called to allow analysis of references (associations) between clasess
 * to be performed.
 * 
 * <p>
 * In addition, {@link IMetaModelExtension}s can be installed on the
 * {@link MetaModel} to allow additional semantics to be gleaned.  When an 
 * extension is installed, it is asked to analyse all {@link IDomainClass}es
 * known at that point. 
 * 
 * <p>
 * Note that MetaModel is not necessarily a singleton - we may have a number
 * of co-existing domains each with their own metamodel (eg user preferences
 * vs back-end business domain).
 * 
 */
package de.berlios.rcpviewer.metamodel;