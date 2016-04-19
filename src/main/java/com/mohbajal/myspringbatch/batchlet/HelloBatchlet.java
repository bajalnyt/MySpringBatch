package com.mohbajal.myspringbatch.batchlet;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.Batchlet;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import java.util.Properties;

/**
 * Created by 908752 on 3/5/16.
 */
public class HelloBatchlet extends AbstractBatchlet {

    @Inject
    private JobContext jobCtx;

    @Inject
    protected StepContext stepContext;

    @Override
    public String process() throws Exception {

        String stepName = stepContext.getStepName();
        System.out.println("Processing.." + stepName);
        if("step2".equalsIgnoreCase(stepName)) {
            //System.out.println("Step 2");
            throw new RuntimeException("Something bad happened ");
        } else
        return "OK";
    }

    @Override
    public void stop() throws Exception {

    }

}
