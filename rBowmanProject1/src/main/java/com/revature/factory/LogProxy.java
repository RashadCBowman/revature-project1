package com.revature.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogProxy implements InvocationHandler {
	private Logger log;
	private Object obj;
	
	public LogProxy(Object o ) {
		obj = o;
		log = LogManager.getLogger(o.getClass());
	}

	@Override
	public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
		Object result = null;
		try {
			if(args == null) {
				log.trace("Method "+m+" called with no arguments");
				result = m.invoke(obj);
			} else {
				log.trace("Method "+m+" called with args: "+Arrays.toString(args));
				result = m.invoke(obj, args);
			}
			log.trace("Method returned: "+result);
		} catch(Exception e) {
			log.error("Method threw exception: "+e);
			for(StackTraceElement s : e.getStackTrace()) {
				log.warn(s);
			}
			if(e.getCause() != null) {
				Throwable t = e.getCause();
				log.error("Method threw wrapped exception: "+t);
				for(StackTraceElement s : t.getStackTrace()) {
					log.warn(s);
				}
			}
			throw e; // we don't want our proxy to have the side-effect of
			// stopping the exception from being thrown (it needs to be handled elsewhere)
			// but we do want to log it for ourselves.
		}
		return result;
	}

}
