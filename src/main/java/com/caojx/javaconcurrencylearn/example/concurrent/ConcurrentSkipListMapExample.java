package com.caojx.javaconcurrencylearn.example.concurrent;

import com.caojx.javaconcurrencylearn.annoations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 测试ConcurrentSkipListMap的线程安全性，线程安全
 *
 * @author caojx
 * @version $Id: ConcurrentSkipListMapExample.java,v 1.0 2019-05-26 17:45 caojx
 * @date 2019-05-26 17:45
 */
@Slf4j
@ThreadSafe
public class ConcurrentSkipListMapExample {

    private static Map<Integer, Integer> map = new ConcurrentSkipListMap<>();

    // 请求总数
    private static int clientNum = 5000;

    // 同时并发执行的线程数
    private static int threadNum = 200;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadNum);
        for (int index = 0; index < clientNum; index++) {
            final int num = index;
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    func(num);
                    semaphore.release();
                } catch (Exception e) {
                    log.error("exception", e);
                }
            });
        }
        executorService.shutdown();
        log.info("size:{}", map.size());
    }

    private static void func(int num) {
        map.put(num, num);
    }
}