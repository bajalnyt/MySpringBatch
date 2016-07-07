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
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.jsr.launch.JsrJobOperator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.stereotype.Component;

public class JobLauncher {


    private static boolean asyncTasks = false;

    private static JsrJobOperator jobOperator;

    @Autowired
    static JobExplorer jobExplorer;

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
        String jobName="ACER2";

        jobOperator = new JsrJobOperator();

        if (!asyncTasks){
            jobOperator.setTaskExecutor(new SyncTaskExecutor());
        }
        else {
            jobOperator.setTaskExecutor(new SimpleAsyncTaskExecutor());
        }

        String[] configLocation  = { "META-INF/batch.xml" };
        ApplicationContext context = new ClassPathXmlApplicationContext(configLocation);

        try {
            //CatalogDbUtils.cleanCatalog();
            jobExplorer = (JobExplorer) context.getBean("jobExplorer");
            System.out.println("Available jobs :"+jobOperator.getJobNames());

            List<JobInstance> instances = jobExplorer.getJobInstances(jobName, 0, 100);
            System.out.println("Explorer Size : " + instances.size());

            if(!instances.isEmpty()) {
                List<JobExecution> executions = jobExplorer.getJobExecutions(instances.get(0));
                System.out.println("Executions size : " + executions.size());

                if(executions.size() > 0) {
                    //Latest execution
                    JobExecution jobExecution = executions.get(0);
                    executionId = executions.get(0).getId();
                    System.out.println("Restarting job with ID :" + executionId);
                    if(jobExecution.getStatus().name().equals("FAILED")) {
                        long a= jobOperator.restart(executionId , jobProperties);
                    }
                }
                 else {
                    System.out.println("Job has not been run before");
                    executionId = jobOperator.start(jobName,jobProperties);
                }
            }
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

    private static long getlastExecutionId(List<JobExecution> executions) {
        long lastExecutionId=0;
        for(JobExecution execution: executions){
            System.out.println(execution.getJobId());
            if(execution.getJobId() > lastExecutionId)
                lastExecutionId = execution.getJobId();
        }
        return lastExecutionId-1;
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
