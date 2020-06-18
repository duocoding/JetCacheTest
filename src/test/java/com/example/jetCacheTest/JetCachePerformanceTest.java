package com.example.jetCacheTest;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.alicp.jetcache.embedded.CaffeineCache;
import com.alicp.jetcache.embedded.EmbeddedCacheConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Auther: qulingxiao
 * @Date: 2020/6/17 14:33
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JetCacheTestApplication.class)
public class JetCachePerformanceTest {
    @CreateCache(expire = 600,name = "aliCache",area = "com.example.jetCacheTest",cacheType = CacheType.LOCAL)
    private static Cache<Object,Object> userCache;

//    @PostConstruct
//    public void init () {
//        userCache = new CaffeineCache<>(new EmbeddedCacheConfig<>());
//    }

    static {
        userCache = new CaffeineCache<>(new EmbeddedCacheConfig<>());
    }


    @Test
    public void putTest () throws ExecutionException, InterruptedException {
        int threadNum = 8;
        List<Future<?>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        Long start = System.currentTimeMillis();
        for (int i= 0; i< threadNum; i++) {
            Future<?> future = executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for(int j= 0; j< 1000000; j++)
                    {
                        userCache.put(j,j);
                    }
                    return "";
                }
            });
            futures.add(future);
        }
        for (Future<?> future: futures) {
            future.get();
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void getTest () throws ExecutionException, InterruptedException {
        int threadNum = 8;
        List<Future<?>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        Long start = System.currentTimeMillis();
        for (int i= 0; i< threadNum; i++) {
            Future<?> future = executorService.submit(new Callable<Object>() {
                @Override
                public Object call() {
                    for(int j= 0; j< 1000000; j++)
                    {
                        userCache.get(j);
                    }
                    return "";
                }
            });
            futures.add(future);
        }
        for (Future<?> future: futures) {
            future.get();
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
