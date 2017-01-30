package com.tw.ds;

import org.junit.Test;

public class AppClientTest {


    @Test
    public void testWriteAndRead() throws Exception {

        try {
            AppClient appClient = new AppClient();

            for (int i = 1; i > 0; i = i + 1000000) {
                System.out.println("i = " + i);
                appClient.put(i, i + 1);
            }
            System.out.println("Done Writing = ");

            Thread.sleep(5000);

            for (int i = 1; i > 0; i = i + 10000000) {
                System.out.println("i = " + i + ":" + appClient.get(i));
            }
            appClient.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testConcurrentWrites() {
        try {
            AppClient appClient1 = new AppClient("akka.tcp://appSystem@127.0.0.1:2558/user/appActor", "appclient");
            AppClient appClient2 = new AppClient("akka.tcp://appSystem@127.0.0.1:2559/user/appActor", "appclient2");

            Runnable runnable = () -> appClient1.put(10, 1001);

            Runnable runnable1 = () -> appClient2.put(10, 1000);

            runnable1.run();
            runnable.run();

            Thread.sleep(500);

            System.out.println("i = " + 10 + ":" + appClient1.get(10));
            System.out.println("i = " + 10 + ":" + appClient2.get(10));

            appClient1.shutdown();
            appClient2.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testWriteWithVersionInfo() {
        try {
            AppClient appClient1 = new AppClient("akka.tcp://appSystem@127.0.0.1:2558/user/appActor", "appclient");
            Runnable runnable = () -> appClient1.put(10, 1001);
            runnable.run();
            Thread.sleep(500);
            System.out.println("i = " + 10 + ":" + appClient1.get(10));
            appClient1.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}