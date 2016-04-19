package com.mohbajal.myspringbatch;

/**
 * Created by 908752 on 3/5/16.
 */

import java.util.List;
import java.util.Properties;

import java.util.Properties;

import javax.batch.operations.JobSecurityException;
import javax.batch.operations.JobStartException;
import javax.batch.runtime.BatchStatus;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.jsr.launch.JsrJobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.stereotype.Component;


@Component
public class JobLauncher {


    private static boolean asyncTasks = false;

    private static JsrJobOperator jobOperator;

    @Autowired
    private static JobExplorer jobExplorer;

    static long executionId;


    private static boolean batchFlag = false;
    public static boolean getBatchFlag() {
        return batchFlag;
    }

    public static void main(String[] args){
        JobLauncher.start(args);
    }

    public static String start(String[] args){
        long startTimestamp = System.currentTimeMillis();

        batchFlag = true;

        String jobStatus="255";

        Properties jobProperties = new Properties();
        //override params
        String jobName="ACER";

        jobOperator = new JsrJobOperator();
        if (!asyncTasks){
            jobOperator.setTaskExecutor(new SyncTaskExecutor());
        }
        else {
            jobOperator.setTaskExecutor(new SimpleAsyncTaskExecutor());
        }


        try {
            //CatalogDbUtils.cleanCatalog();
           /* List<JobInstance> instances = jobExplorer.getJobInstances("ACER", 0, 10);
            System.out.println("Explorer Size : " + instances.size());*/

            //executionId = jobOperator.start(jobName,jobProperties);

            jobOperator.restart(1, jobProperties);


            jobStatus=jobOperator.getJobExecution(executionId).getExitStatus();
            long time = System.currentTimeMillis() - startTimestamp;

            System.out.println("Job with id "+executionId+" ended with status "+jobOperator.getJobExecution(executionId).getBatchStatus()+", "+
                   " return code "+jobStatus+", in "+time +" milliseconds.");
        }
        catch(JobStartException e) {
            e.printStackTrace();
        }
        catch(JobSecurityException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally{

        }
        return jobStatus;
    }

    public static boolean isStillRunning() {
        BatchStatus status = getBatchStatus();
        if(status == BatchStatus.STARTING || status == BatchStatus.STARTED) {
            return true;
        }
        return false;
    }

    public static BatchStatus getBatchStatus() {
        return jobOperator.getJobExecution(executionId).getBatchStatus();
    }

    public static boolean isRunSuccess() {
        BatchStatus status = getBatchStatus();
        if(status == BatchStatus.FAILED || status == BatchStatus.ABANDONED) {
            return false;
        }
        return true;
    }


    public static JsrJobOperator getJobOperator() {
        return jobOperator;
    }
}
