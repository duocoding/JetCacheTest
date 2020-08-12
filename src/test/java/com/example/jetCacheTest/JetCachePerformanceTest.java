package com.example.jetCacheTest;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.*;

/**
 * @Auther: qulingxiao
 * @Date: 2020/6/17 14:33
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JetCacheTestApplication.class)
@EnableCreateCacheAnnotation
public class JetCachePerformanceTest {
    @CreateCache(name = "RemoteTest", expire = 600000, cacheType = CacheType.BOTH)

    private Cache<Object,Object> userCache;


    @Test
    public void putAndGet(){
        userCache.put("key", "value");
        Assert.assertEquals("value", userCache.get("key"));
    }

    @Test
    public void putTest () throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);
        Long start = System.currentTimeMillis();
        for (int i= 0; i< 10; i++) {
            executorService.execute( () -> {
                for (int k= 0; k< 10; k++){
                    for (int j= 0; j< 20000; j++){
                        userCache.put(converetCacheKey("j"+j), j);
                    }
                }
                latch.countDown();
            });

        }
       latch.await();
        System.out.println(System.currentTimeMillis() - start);
        executorService.shutdown();
    }

    @Test
    public void getTest () throws InterruptedException {
        putTest();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);
        for (int j= 0; j< 2000; j++){
            userCache.get(converetCacheKey("j"+j));
        }
        Long start = System.currentTimeMillis();
        for (int i= 0; i< 10; i++) {
            executorService.execute( () -> {
                for (int k= 0; k< 10; k++){
                    for (int j= 0; j< 20000; j++){
                        userCache.get(converetCacheKey("j"+j));
                    }
                }
                latch.countDown();
            });
        }
        latch.await();
        System.out.println(System.currentTimeMillis() - start);
        executorService.shutdown();
    }

    private String converetCacheKey(String key){
        StringBuilder builder = new StringBuilder(16);
        return builder.append("prefix").append(key).toString();
    }
}
