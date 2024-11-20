package se.yrgo.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class PerformanceTimingAdvice {

    public Object performTimingMeasurement(ProceedingJoinPoint method) throws Throwable {

        long startTime = System.nanoTime();

        try {
            return method.proceed();
        } catch (Exception ex) {
            System.err.println("Error in " +
                    method.getSignature().getDeclaringTypeName() + "." +
                    method.getSignature().getName());
        } finally {
            long endTime = System.nanoTime();
            double timeTaken = endTime - startTime;

            System.out.println("The method " +
                    method.getSignature().getDeclaringTypeName() + "." +
                    method.getSignature().getName() + " took " + timeTaken
                    / 1000000 + " ms");
        }
        return null;
    }
}
