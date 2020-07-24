package com.example.jetCacheTest;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: qulingxiao
 * @Date: 2020/7/22 08:47
 * @Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableCreateCacheAnnotation
public class RemoteTest {

    @CreateCache(name = "RemoteTest", expire = 600000, cacheType = CacheType.REMOTE)
    private Cache<String, String> cache;

    @Test
    public void putRemote() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(50000);
        Long start = System.currentTimeMillis();
        for(int i= 0; i< 10; i++){
            final int cur = i*5000;
            executorService.execute(() -> {
                for (int j= 0; j< 5000; j++) {
                    cache.put("j"+j+cur, "j"+j+cur);
                    latch.countDown();
                }
            });
        }
        latch.await();
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void getRemote() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(50000);
        Long start = System.currentTimeMillis();
        for(int i= 0; i< 10; i++){
            final int cur = i*5000;
            executorService.execute(() -> {
                for (int j= 0; j< 5000; j++) {
                    cache.get("j"+cur);
                    latch.countDown();
                }
            });
        }
        latch.await();
        cache.put("11","Value");
        System.out.println(System.currentTimeMillis() - start);
    }
}
