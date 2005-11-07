package org.essentialplatform.progmodel.louis.app;
import java.lang.annotation.*;

/**
 * Provide RCPViewer-specific metadata to allow an Eclipse ImageDescriptor to
 * be created.
 * 
 * <p>
 * ImageDescriptors are typically built using 
 * <tt>ImageDescriptor(Class clazz, String fileName)</tt> (where the file is
 * colocated with the class), or  <tt>ImageDescriptor(String url)</tt>.  
 * This annotation is to provide support for the second of these two, specifying 
 * a <tt>url</tt> to pick up the image.
 * 
 * <p>
 * Discussion: we do not - in the standard programming model - provide support 
 * for the first of these because there is already enough  information for an 
 * RCPViewer to work out where the image file might be.  The <tt>clazz</tt> is 
 * the domain object being annotated, and the <tt>fileName</tt> can be derived 
 * from this.  
 * 
 * <p>
 * For example, for a class of <i>com.mycompany.Customer</i>, the RCPViewer can
 * search for any of <i>Customer.png</i>, <i>Customer.gif</i>, <i>Customer.jpg</i>.
 * (This follows the same conventions as the Naked Objects Framework).
 * 
 * <p>
 * Consumed reflectively for building meta-model.
 * 
 * @author Dan Haywood
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.TYPE)
public @interface ImageUrlAt {
	String value();
	class Factory {
		private Factory() {}
		public static ImageUrlAt create(final String value) {
			return new ImageUrlAt(){
				public String value() { return value; }
				public Class<? extends Annotation> annotationType() { return ImageUrlAt.class; }
			};
		}
	}
}
