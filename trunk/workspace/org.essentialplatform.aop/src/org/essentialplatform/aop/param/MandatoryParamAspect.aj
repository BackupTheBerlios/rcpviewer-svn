package org.essentialplatform.aop.param;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature; 
import org.aspectj.lang.reflect.CodeSignature; 
import org.aspectj.lang.reflect.ConstructorSignature; 
import java.lang.reflect.*;
import java.lang.annotation.*;

public aspect MandatoryParamAspect {

	pointcut executeConstrArg0(Object arg) : execution(@WithPreconditions new(Object+, ..)) && args(arg);
	pointcut executeMethodArg0(Object arg) : execution(@WithPreconditions * *(Object+, ..)) && args(arg);
	
	pointcut executeConstrArg1(Object arg) : execution(@WithPreconditions new(*, Object+, ..)) && args(*, arg, ..);  
	pointcut executeMethodArg1(Object arg) : execution(@WithPreconditions * *(*, Object+, ..)) && args(*, arg, ..);
	
	pointcut executeConstrArg2(Object arg) : execution(@WithPreconditions new(*, *, Object+, ..)) && args(*, *, arg, ..);
	pointcut executeMethodArg2(Object arg) : execution(@WithPreconditions * *(*, *, Object+, ..)) && args(*, *, arg, ..);
	
	pointcut executeConstrArg3(Object arg) : execution(@WithPreconditions new(*, *, *, Object+, ..)) && args(*, *, *, arg, ..);
	pointcut executeMethodArg3(Object arg) : execution(@WithPreconditions * *(*, *, *, Object+, ..)) && args(*, *, *, arg, ..);
	
	pointcut executeConstrArg4(Object arg) : execution(@WithPreconditions new(*, *, *, *, Object+, ..)) && args(*, *, *, *, arg, ..);
	pointcut executeMethodArg4(Object arg) : execution(@WithPreconditions * *(*, *, *, *, Object+, ..)) && args(*, *, *, *, arg, ..);
	
	pointcut executeConstrArg5(Object arg) : execution(@WithPreconditions new(*, *, *, *, *, Object+, ..)) && args(*, *, *, *, *, arg, ..);
	pointcut executeMethodArg5(Object arg) : execution(@WithPreconditions * *(*, *, *, *, *, Object+, ..)) && args(*, *, *, *, *, arg, ..);
	
	pointcut executeConstrArg6(Object arg) : execution(@WithPreconditions new(*, *, *, *, *, *, Object+, ..)) && args(*, *, *, *, *, *, arg, ..);
	pointcut executeMethodArg6(Object arg) : execution(@WithPreconditions * *(*, *, *, *, *, *, Object+, ..)) && args(*, *, *, *, *, *, arg, ..);
	
	pointcut executeConstrArg7(Object arg) : execution(@WithPreconditions new(*, *, *, *, *, *, *, Object+, ..)) && args(*, *, *, *, *, *, *, arg, ..);
	pointcut executeMethodArg7(Object arg) : execution(@WithPreconditions * *(*, *, *, *, *, *, *, Object+, ..)) && args(*, *, *, *, *, *, *, arg, ..);

	pointcut executeWithPreconditions() : 
		(execution(@WithPreconditions new(..)) ||
		 execution(@WithPreconditions * *(..))   ); 


	before(Object arg): executeConstrArg0(arg) { assertConstrArgNotNull(thisJoinPointStaticPart, 0, arg); }
	before(Object arg): executeMethodArg0(arg) { assertMethodArgNotNull(thisJoinPointStaticPart, 0, arg); }
	before(Object arg): executeConstrArg1(arg) { assertConstrArgNotNull(thisJoinPointStaticPart, 1, arg); }
	before(Object arg): executeMethodArg1(arg) { assertMethodArgNotNull(thisJoinPointStaticPart, 1, arg); }
	before(Object arg): executeConstrArg2(arg) { assertConstrArgNotNull(thisJoinPointStaticPart, 2, arg); }
	before(Object arg): executeMethodArg2(arg) { assertMethodArgNotNull(thisJoinPointStaticPart, 2, arg); }
	before(Object arg): executeConstrArg3(arg) { assertConstrArgNotNull(thisJoinPointStaticPart, 3, arg); }
	before(Object arg): executeMethodArg3(arg) { assertMethodArgNotNull(thisJoinPointStaticPart, 3, arg); }
	before(Object arg): executeConstrArg4(arg) { assertConstrArgNotNull(thisJoinPointStaticPart, 4, arg); }
	before(Object arg): executeMethodArg4(arg) { assertMethodArgNotNull(thisJoinPointStaticPart, 4, arg); }
	before(Object arg): executeConstrArg5(arg) { assertConstrArgNotNull(thisJoinPointStaticPart, 5, arg); }
	before(Object arg): executeMethodArg5(arg) { assertMethodArgNotNull(thisJoinPointStaticPart, 5, arg); }
	before(Object arg): executeConstrArg6(arg) { assertConstrArgNotNull(thisJoinPointStaticPart, 6, arg); }
	before(Object arg): executeMethodArg6(arg) { assertMethodArgNotNull(thisJoinPointStaticPart, 6, arg); }
	before(Object arg): executeConstrArg7(arg) { assertConstrArgNotNull(thisJoinPointStaticPart, 7, arg); }
	before(Object arg): executeMethodArg7(arg) { assertMethodArgNotNull(thisJoinPointStaticPart, 7, arg); }
	
	before(): executeWithPreconditions() { 
		CodeSignature codeSignature = (CodeSignature)thisJoinPointStaticPart.getSignature();
		if (codeSignature.getParameterTypes().length > 8) {
			throw new IllegalArgumentException("Cannot check preconditions of methods with >8 arguments");
		}
	}

	private void assertConstrArgNotNull(JoinPoint.StaticPart staticPart, int argNum, Object arg) {
		if (arg != null) { return; }
		ConstructorSignature constructorSignature = (ConstructorSignature)staticPart.getSignature();
		Constructor constructor = constructorSignature.getConstructor();
		assertNoMandatoryAnnotation(constructor.getParameterAnnotations()[argNum]);
	}
	private void assertMethodArgNotNull(JoinPoint.StaticPart staticPart, int argNum, Object arg) {
		if (arg != null) { return; }
		MethodSignature methodSignature = (MethodSignature)staticPart.getSignature();
		Method method = methodSignature.getMethod();
		assertNoMandatoryAnnotation(method.getParameterAnnotations()[argNum]);
	}
	private void assertNoMandatoryAnnotation(Annotation[] annotations) {
		for(Annotation annot: annotations) {
			if (annot.annotationType() == Mandatory.class) {
				throw new IllegalArgumentException("Mandatory argument");
			}
		}
	}
}
