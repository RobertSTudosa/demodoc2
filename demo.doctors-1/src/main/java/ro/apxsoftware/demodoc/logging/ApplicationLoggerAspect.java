package ro.apxsoftware.demodoc.logging;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



@Aspect
@Component 
public class ApplicationLoggerAspect {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Pointcut("within(ro.apxsoftware.demodoc.controllers..*) || within(ro.apxsoftware.demodoc.security..*) || within(ro.apxsoftware.demodoc.service..*)")
	public void definePackagePointcuts() {
		//empty method just to name the location specified in the pointcut
	}
	
	@Around("definePackagePointcuts()") 
	public Object logAround(ProceedingJoinPoint jp ) {
		
		log.debug("\n \n \n ");
		log.debug("***************** BEFORE "+jp.getSignature().getName()+ " EXECUTION**************** \n {}.{} () with arguments[s] = {}",
				jp.getSignature().getDeclaringTypeName(),
				jp.getSignature().getName(),
				Arrays.toString(jp.getArgs()));
		log.debug("__________________________________________________________ \n \n ");
		
		Object o=null;
		try {
			o = jp.proceed();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.debug("\n \n \n ");
		log.debug("***************** AFTER "+jp.getSignature().getName()+ " EXECUTION**************** \n {}.{} () with arguments[s] = {}",
				jp.getSignature().getDeclaringTypeName(),
				jp.getSignature().getName(),
				Arrays.toString(jp.getArgs()));
		log.debug("__________________________________________________________ \n \n ");
		
		return o;
		
	}
	
	
	

}
