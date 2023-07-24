import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class Test {
    @org.junit.Test
    public void testNewFixedThreadPool(){

        ThreadPoolExecutor fixedThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        //输出一下线程池的初始状态
        log.info("fixedThreadPool: max-{}", fixedThreadPool.getMaximumPoolSize());

        //尝试修改线程数量
        fixedThreadPool.setMaximumPoolSize(8);

        //再次输出线程池的初始状态
        log.info("fixedThreadPool: max-{}", fixedThreadPool.getMaximumPoolSize());

        ThreadPoolExecutor singleThreadExecutor = (ThreadPoolExecutor) Executors.newSingleThreadExecutor();
        log.info("singleThreadExecutor: max-{}", singleThreadExecutor.getMaximumPoolSize());
        singleThreadExecutor.setMaximumPoolSize(8);
        log.info("singleThreadExecutor: max-{}", singleThreadExecutor.getMaximumPoolSize());
    }
}
