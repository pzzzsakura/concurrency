package com.example.concurrency;

import com.example.concurrency.annoations.NotThreadSafe;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Ma.li.ran
 * @datetime: 2018/08/30 21:50
 * @desc:
 * @environment: jdk1.8.0_121/IDEA 2017.2.6/Tomcat8.0.47/mysql5.7
 */
@Slf4j
@NotThreadSafe
public class ConcurrencyTest {

  // 请求总数
  public static int clientTotal = 5000;

  // 同时并发执行的线程数
  public static int threadTotal = 200;

  public static int count = 0;

  public static void main(String[] args) throws Exception {
    ExecutorService executorService = Executors.newCachedThreadPool();
    final Semaphore semaphore = new Semaphore(threadTotal);
    final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
    for (int i = 0; i < clientTotal ; i++) {
      executorService.execute(() -> {
        try {
          semaphore.acquire();
          add();
          semaphore.release();
        } catch (Exception e) {
          log.error("exception", e);
        }
        countDownLatch.countDown();
      });
    }
    countDownLatch.await();
    executorService.shutdown();
    log.info("count:{}", count);
  }

  private static void add() {
    count++;
  }
}
