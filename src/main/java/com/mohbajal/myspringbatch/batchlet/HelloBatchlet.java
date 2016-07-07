package com.mohbajal.myspringbatch.batchlet;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.comparator.BooleanComparator;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.Batchlet;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Created by 908752 on 3/5/16.
 */

public class HelloBatchlet extends AbstractBatchlet {

    @Inject
    private JobContext jobCtx;

    @Inject
    protected StepContext stepContext;

    private int attempts = 0;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    @Override
    public String process() throws Exception {

        String stepName = stepContext.getStepName();
        System.out.println("Processing.." + stepName);
        /*if("step2".equalsIgnoreCase(stepName)) {
            invokeRemoteMethod();
            return "ABEND";
        } else
             return "OK";*/

        RetryTemplate template = new RetryTemplate();

        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(3);

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();

        //how much time (in milliseconds) before next attempt
        fixedBackOffPolicy.setBackOffPeriod(3000);

        template.setRetryPolicy(policy);
        template.setBackOffPolicy(fixedBackOffPolicy);

        template.execute(new RetryCallback<Boolean, RuntimeException>() {

            public Boolean doWithRetry(RetryContext context) {
                return invokeRemoteMethod(stepName);
            }

        });
        return "OK";
    }


    @Retryable(maxAttempts=10,value=RuntimeException.class,backoff = @Backoff(delay = 10000,multiplier=2))
    private Boolean invokeRemoteMethod(String stepName) {
        System.out.println("Running - Attempt " + attempts + " at " + sdf.format(new Date()));
        attempts++;

        if(stepName.equalsIgnoreCase("step2")) {
            System.out.println("Step2 failed");
            throw new RuntimeException("Some failure happened ");
        }
        return true;

    }

    @Override
    public void stop() throws Exception {

    }

}
