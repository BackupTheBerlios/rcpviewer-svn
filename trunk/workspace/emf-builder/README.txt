Prereqs:
3.1m6
aspectJ-1.2m3
JRE 1.5.0_02
Compiler compliance level: 5.0

java>build path>user libraries
  EMF_CORE
    org.eclipse,emf.ecore_2.0.1/runtime/ ecore.jar, ecore.resources.jar
    org.eclipse,emf.common_2.0.1/runtime/ common.jar, common.resources.jar

To compile:
may be necessary to remove and then re-add the AspectJ nature (from package explorer)

Things to try out:
  - define some POJOs (eg simple Customer, Department, Employee) and instantiate
  - go to the DomainClassRegistry (a singleton) and fetch DomainClasses
    - see TestRegisterDomainClassAspect
  - use DomainClassRegistry to fetch the DomainObject for a pojo
    - see TestDomainAspect#testCanGetDomainObjectFromDomainClassRegistry()


Currently only accessor attributes of DomainClasses are noticed and added to the metamodel.

++++++++++++++++++++++++++++++++++++
Generation control flags (reference, from http://download.eclipse.org/tools/emf/scripts/docs.php?doc=references/overview/EMF.html)

(we will use to represent equivalent semantics)

* Unsettable (default is false) - represented in programming model

      A feature that is declared to be unsettable has a notion of an explicit unset or no-value 
      state. For example, a boolean attribute that is not unsettable can take on one of two values: 
      true or false. If, instead, the attribute is declared to be unsettable, it can then have any 
      of three values: true, false, or unset.

      The get method on a feature that is not set will return its default value, but for an 
      unsettable feature, there is a distinction between this state and when the feature has been 
      explicitly set to the default value. Since the unset state is outside of the set of allowed 
      values, we need to generate additional methods to put a feature in the unset state and to 
      determine if it is in that state. For example, if the pages attribute in class Book is 
      declared to be unsettable, then we'll get two more generated methods:

  boolean isSetPages();
  void unsetPages();

      in addition to the original two:

  int getPages();
  void setPages(int value);

      The isSet method returns true if the feature has been explicitly set. The unset method changes 
      an attribute that has been set back to its unset state.

      When unsettable is false, we don't get the generated isSet or unset methods, but we still get 
      implementations of the reflective versions: eIsSet() and eUnset() (which every EObject must 
      implement). For non-unsettable attributes, eIsSet() returns true if the current value is 
      different from the default value, and eUnset() sets the feature to the default value (more 
      like a reset).
      
* ResolveProxies (default is true)

      ResolveProxies only applies to non-containment references. ResolveProxies implies that the 
      reference may span documents, and therefore needs to include proxy checking and resolution in 
      the get method, as described earlier in this paper.

      You can optimize the generated get pattern for references that you know will never be used in 
      a cross document scenario by setting resolveProxies to false. In that case, the generated get 
      method will be optimally efficient[21] .
      
