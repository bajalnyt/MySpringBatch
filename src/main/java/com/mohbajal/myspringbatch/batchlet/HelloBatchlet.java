package com.mohbajal.myspringbatch.batchlet;

import javax.batch.api.Batchlet;

/**
 * Created by 908752 on 3/5/16.
 */
public class HelloBatchlet implements Batchlet {


    @Override
    public String process() throws Exception {
        System.out.println("process");
        return "OK";
    }

    @Override
    public void stop() throws Exception {

    }
}
