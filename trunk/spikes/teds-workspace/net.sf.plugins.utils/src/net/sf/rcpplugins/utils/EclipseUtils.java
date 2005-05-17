package net.sf.rcpplugins.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;


/**
 * @author ted stockwell
 *
 */
public class EclipseUtils
{

    static public Object getAdapter(Object object, Class target) 
    {
        Object adapter= null;
        
        if (object == null)
            return null;
        
        if (object instanceof IAdaptable)
            adapter= ((IAdaptable)object).getAdapter(target);
       
        if (adapter == null) 
            adapter= Platform.getAdapterManager().getAdapter(object, target);
        
        if (adapter == null) 
            if (target.isAssignableFrom(object.getClass()))
                adapter= object;
        
        return adapter;
    }

    /**
     * Finds all the parameter subelements of the given configuration element 
     * and returns a Map that contains all parameter values indexed by thier 
     * associated parameter names.
     * 
     * For instance, passing the IConfigurationElement for the element should 
     * below... 
     * 
     * <run>
     *  <parameter name="aaa" value="value1"/>
     *  <parameter name="bbb" value="value2"/>
     * </run>
     * 
     * ...would return a map with two keys, 'aaa' & 'bbb'.   
     *    
     */
    static public Map getParameters(IConfigurationElement configurationElement) {
        HashMap map= new HashMap();
        if (configurationElement != null) {
            IConfigurationElement[] children= configurationElement.getChildren("parameter");
            for (int i= 0; i < children.length; i++) {
                IConfigurationElement child= children[i];
                String name= child.getAttribute("name");
                String value= child.getAttribute("value");
                map.put(name, value);
            }
        }
        return map;
    }

    /**
     * A convenience method for converting an exception into a CoreException
     */
    static public void throwCoreException(Throwable t) 
    throws CoreException
    {
        throwCoreException(Status.ERROR, "com.rpc.core", 0, t.getMessage(), t);
    }
    /**
     * A convenience method for converting an exception into a CoreException
     */
    static public void throwCoreException(IConfigurationElement element, Throwable t) 
    throws CoreException
    {
        throwCoreException(element.getDeclaringExtension(), t);
    }
    /**
     * A convenience method for converting an exception into a CoreException
     */
    static public void throwCoreException(IConfigurationElement element, String message, Throwable t) 
    throws CoreException
    {
        throwCoreException(element.getDeclaringExtension(), message, t);
    }
    static public void throwCoreException(IExtension extension, Throwable t) 
    throws CoreException
    {
        throwCoreException(extension.getDeclaringPluginDescriptor(), t);
    }
    static public void throwCoreException(IExtension extension, String message, Throwable t) 
    throws CoreException
    {
        throwCoreException(extension.getDeclaringPluginDescriptor(), message, t);
    }
    static public void throwCoreException(IPluginDescriptor descriptor, Throwable t) 
    throws CoreException
    {
        throwCoreException(Status.ERROR, descriptor.getUniqueIdentifier(), 0, t.getMessage(), t);
    }
    static public void throwCoreException(IPluginDescriptor descriptor, String message, Throwable t) 
    throws CoreException
    {
        throwCoreException(Status.ERROR, descriptor.getUniqueIdentifier(), 0, message, t);
    }
    static public void throwCoreException(IConfigurationElement element, String message) 
    throws CoreException
    {
        throwCoreException(element.getDeclaringExtension(), message);
    }
    static public void throwCoreException(IExtension extension, String message) 
    throws CoreException
    {
        throwCoreException(extension.getDeclaringPluginDescriptor(), message);
    }
    static public void throwCoreException(IPluginDescriptor descriptor, String message) 
    throws CoreException
    {
        throwCoreException(Status.ERROR, descriptor.getUniqueIdentifier(), 0, message, null);
    }
    /**
     * A convenience method for converting an exception into a CoreException
     */
    static public void throwCoreException(String message, Throwable t) 
    throws CoreException
    {
        throwCoreException(Status.ERROR, "com.rpc.core", 0, message, t);
    }
    /**
     * A convenience method for converting an exception into a CoreException
     */
    static public void throwCoreException(int severity, String pluginId, int code, String message, Throwable t) 
    throws CoreException
    {
        if (t instanceof CoreException)
            throw (CoreException)t;
        Status status= new Status(severity, pluginId, code, message, t);
        throw new CoreException(status);
    }
}
