package org.essentialplatform.runtime.client.transaction;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.aspectj.lang.SoftException;

/**
 * Base class for defining advice executed by aspects.
 * 
 * <p>
 * The reasons factoring out of advice is two-fold:
 * <ul>
 * <li> firstly, as a work-around for lack of comprehensive refactoring 
 *      capabilities of AJDT.
 * <li> to allow functionality to be composed rather than inherited. 
 * </ul>
 * 
 * <p>
 * Each aspect should instantiate an instance of its corresponding advice 
 * object; each advice in the aspect should delegate to a corresponding method 
 * in the advice object, with a naming convention of <tt>adviceType$pointcut</tt>.
 * 
 * <p>
 * So far as possible the signature of the advice methods should map to the
 * actual advices of the advice in the aspect, with the following provisos:
 * <li> where a join point is required, it should be passed through
 * <li> where an around advice is being delegated, a <tt>Callable</tt> should 
 *      be supplied that wraps the proceed as follows:
 *      <pre>
 *      new Callable() { 
 *        public Object call() { 
 *          return proceed(...);
 *        }
 *      }
 *      </pre>
 * </ul>
 *
 * <p>
 * Note that a third almost-reason for factoring out advice objects is for
 * testing purposes, though note that any join points would need to be
 * mocked to do this properly.
 *      
 * @author Dan Haywood
 */
public abstract class AbstractAspectAdvice {

	protected abstract Logger getLogger();
	
	/**
	 * Wraps any exceptions into an AspectJ soft exception.
	 * 
	 * @param <V>
	 * @param proceed
	 * @return
	 */
	protected final <V> V call(Callable<V> proceed) {
		try {
			return proceed.call();
		} catch (RuntimeException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SoftException(ex);
		}
	}

}
