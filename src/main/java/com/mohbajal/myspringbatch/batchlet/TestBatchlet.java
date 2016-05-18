package com.mohbajal.myspringbatch.batchlet;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 908752 on 3/5/16.
 */

public class TestBatchlet extends AbstractBatchlet {

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
        if("step2".equalsIgnoreCase(stepName)) {
            return "ABEND";
        } else {
            return "OK";
        }

    }



    @Override
    public void stop() throws Exception {

    }

}
