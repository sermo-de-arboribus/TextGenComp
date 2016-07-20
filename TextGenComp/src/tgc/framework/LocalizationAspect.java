package tgc.framework;

import java.util.Locale;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class LocalizationAspect
{
	Locale globalLocale;
	
	public LocalizationAspect(Locale locale)
	{
		globalLocale = locale;
	}
	
	@Pointcut("execution(* *(..)) && args(java.util.Locale)") // the pointcut expression
	private void anyMethodCallWithLocaleParam() {}// the pointcut signature
	
	/** 
	  * This is the method which will be executed
	  * before a selected method execution.
	  */
	@Around("anyMethodCallWithLocaleParam()")
	public Object aroundAdvice(final ProceedingJoinPoint pjp)
	{
		// set Locale appropriately
		Object[] args = pjp.getArgs();
   
		for (int i = 0; i < args.length; i++)
		{
			if (args[i] instanceof Locale)
			{
				args[i] = globalLocale;
			}
		}
   
		Object returnValue = null;
		try
		{
			returnValue = pjp.proceed(args);
		}
		catch (Throwable exc)
		{
			// TODO Auto-generated catch block
			System.err.println(exc.getMessage());
			exc.printStackTrace();
		}
		return returnValue;
	}
}