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