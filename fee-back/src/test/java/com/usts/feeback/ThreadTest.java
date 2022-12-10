package com.usts.feeback;

import java.util.concurrent.*;

/**
 * @author leenadz
 * @since 2022-12-10 21:59
 */
public class ThreadTest {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                3,//核心线程池大小
                5,//获取CPU核数 System.out.println(Runtime.getRuntime().availableProcessors());
                3,//超时时间，没人调用时就会释放
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1),
                Executors.defaultThreadFactory(),
               new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 20; i++) {
            MyTask myTask = new MyTask(i);
            //threadPoolExecutor.execute(myTask);
            threadPoolExecutor.submit(myTask);
            System.out.println("线程池中线程数目：" + threadPoolExecutor.getPoolSize() + "，队列中等待执行的任务数目：" +
                    threadPoolExecutor.getQueue().size() + "，已执行完别的任务数目：" + threadPoolExecutor.getCompletedTaskCount());
        }
        threadPoolExecutor.shutdown();


    }

    static class MyTask implements Runnable {
        private int taskNum;

        public MyTask(int num) {
            this.taskNum = num;
        }

        @Override
        public void run() {
            System.out.println("正在执行task " + taskNum);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("task " + taskNum + "执行完毕");
        }
    }
}

