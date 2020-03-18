package de.digitalcollections.blueprints.crud.frontend.webapp.aop;

import java.lang.reflect.Proxy;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * Aspect: Logging method calls. Log all public service calls and all public repository calls.
 */
@Aspect
@Component
public class AopMethodLogger {

  private static Logger logger = LoggerFactory.getLogger(AopMethodLogger.class);

  @Pointcut(
          "execution(public * de.digitalcollections.blueprints.crud.frontend.webapp.controller..*Controller.*(..)) || "
          + "execution(public * de.digitalcollections.blueprints.crud.business.impl.service..*Service.*(..)) || "
          + "execution(public * de.digitalcollections.blueprints.crud.backend.impl.jpa.repository..*.*Repository.*(..))")
  public void methodsToBeLogged() {
  }

  /**
   * Protokolliert die Ausführung eines join points und dessen Parameter. Paremeter werden nur angezeigt, wenn der
   * Wert nicht null ist.
   *
   * In aspect-oriented programming a set of join points is called a pointcut. A join point is a specification of
   * when, in the corresponding main program, the aspect code should be executed. The join point is a point of
   * execution in the base code where the advice specified in a corresponding pointcut is applied.
   *
   * @param joinPoint Das Event des Advices
   */
  @Before("methodsToBeLogged()")
  public void logMethodCall(JoinPoint joinPoint) {
    final Object target = joinPoint.getTarget();
    Class<?> targetClass = target.getClass();
    if (target instanceof Proxy) {
      targetClass = AopUtils.getTargetClass(target);
    }
    String targetClassName = targetClass.getName();
    String logMsg = createMsgForLogMethodCall(joinPoint);
    Logger targetLog = LoggerFactory.getLogger(targetClassName);
    targetLog.info(logMsg);
  }

  String createMsgForLogMethodCall(JoinPoint joinPoint) {
    StringBuilder buffer = new StringBuilder();
    String targetMethodName = joinPoint.getSignature().getName();
    buffer.append(targetMethodName);
    buffer.append("(");
    final Object[] args = joinPoint.getArgs();
    CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
    String[] argNames = codeSignature.getParameterNames();
    for (int i = 0; i < args.length; i++) {
      final Object arg = args[i];
      if (arg != null) {
        String argName = null;
        if (argNames != null) {
          argName = argNames[i];
          if (argName != null) {
            buffer.append(argNames[i]);
            buffer.append("=");
          }
        }
        buffer.append(arg.getClass().getSimpleName());
        buffer.append((" "));

        String argValue = arg.toString();

        // mask security sensible data
        if (argName != null && (argName.contains("password") || argName.contains("pwd"))) {
          argValue = "***";
        }
        if (argValue.length() > 50) {
          argValue = argValue.substring(0, 49) + "...";
        }
        buffer.append(argValue);
        if (i < (args.length - 1)) {
          buffer.append(", ");
        }
      }
    }
    buffer.append(")");
    return buffer.toString();
  }

  /**
   *
   * Protokolliert die Laufzeit des join points. Das logging ist nur aktiv, wenn {@link #logger} mindestens
   * debug-Level hat.
   *
   * @param call the original method call
   * @return the result of the original method call
   * @throws Throwable when the original method call throws any Error or Exception
   */
  @Around("methodsToBeLogged()")
  public Object logMethodDuration(ProceedingJoinPoint call) throws Throwable { // NOSONAR
    // proceed benötigt das Weiterreichen der Exception (throws Throwable)...
    Object returnValue;
    if (logger.isDebugEnabled()) {
      String targetClassName = call.getTarget().getClass().getName();
      String targetMethodName = call.getSignature().getName();
      Logger targetLog = LoggerFactory.getLogger(targetClassName);
      if (targetLog.isDebugEnabled()) {
        StopWatch clock = new StopWatch(getClass().getName());
        try {
          clock.start(call.toShortString());
          returnValue = call.proceed();
        } finally {
          clock.stop();
          String msg = createMsgForLogMethodDuration(targetMethodName,
                  clock.getTotalTimeMillis());
          targetLog.debug(msg);
        }
      } else {
        returnValue = call.proceed();
      }
    } else {
      returnValue = call.proceed();
    }
    return returnValue;
  }

  String createMsgForLogMethodDuration(String targetMethodName, long duration) {
    final StringBuilder sb = new StringBuilder();
    sb.append(targetMethodName).append("(): ");
    sb.append("duration ").append(duration).append(" ms");
    return sb.toString();
  }

  /**
   * Existiert zum Testen, so kann ein Logger-Mock gesetzt werden.
   *
   * @param logger
   */
  static void setLogger(Logger logger) {
    AopMethodLogger.logger = logger;
  }
}
