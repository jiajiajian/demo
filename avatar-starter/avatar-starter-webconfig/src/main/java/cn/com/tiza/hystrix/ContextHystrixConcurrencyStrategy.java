package cn.com.tiza.hystrix;

import cn.com.tiza.context.BaseContextHandler;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.properties.HystrixProperty;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ContextHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

    private HystrixConcurrencyStrategy existingConcurrencyStrategy;

    public ContextHystrixConcurrencyStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy) {
        this.existingConcurrencyStrategy = existingConcurrencyStrategy;
    }

    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getBlockingQueue(maxQueueSize)
                : super.getBlockingQueue(maxQueueSize);
    }

    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(
            HystrixRequestVariableLifecycle<T> rv) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getRequestVariable(rv)
                : super.getRequestVariable(rv);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixProperty<Integer> corePoolSize,
                                            HystrixProperty<Integer> maximumPoolSize,
                                            HystrixProperty<Integer> keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getThreadPool(threadPoolKey, corePoolSize,
                maximumPoolSize, keepAliveTime, unit, workQueue)
                : super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize,
                keepAliveTime, unit, workQueue);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixThreadPoolProperties threadPoolProperties) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getThreadPool(threadPoolKey,
                threadPoolProperties)
                : super.getThreadPool(threadPoolKey, threadPoolProperties);
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        String token = BaseContextHandler.getToken();
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy
                .wrapCallable(new WrappedCallable<T>(callable, token))
                : super.wrapCallable(new WrappedCallable<T>(callable, token));
    }

    static class WrappedCallable<T> implements Callable<T> {
        private final Callable<T> target;
        private final String token;
        public WrappedCallable(Callable<T> target, String token) {
            this.target = target;
            this.token = token;
        }
        @Override
        public T call() throws Exception {
            try {
                BaseContextHandler.setToken(token);
                return target.call();
            } finally {
                BaseContextHandler.remove();
            }
        }
    }
}
