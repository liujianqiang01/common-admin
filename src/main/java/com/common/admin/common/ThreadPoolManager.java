package com.common.admin.common;

import java.util.concurrent.*;

/**
 * @Author: liujianqiang
 * @Date: 2019-04-18
 * @Description:
 */
public class ThreadPoolManager<T> {
    /**
     * 根据cpu的数量动态的配置核心线程数和最大线程数
     */
    private static final int CPU_COUNT =10;
    /**
     * 核心线程数 = CPU核心数 + 1
     */
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    /**
     * 线程池最大线程数 = CPU核心数 * 2 + 1
     */
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    /**
     * 非核心线程闲置时超时1s
     */
    private static final int KEEP_ALIVE = 1;
    /**
     * 线程池的对象
     */
    private ThreadPoolExecutor executor;

    /**
     * 要确保该类只有一个实例对象，避免产生过多对象消费资源，所以采用单例模式
     */
    private ThreadPoolManager() {
        createExecutor();
    }

    private static ThreadPoolManager sInstance;

    /**
     * 双重检验锁初始化
     * @return
     */
    public static ThreadPoolManager getsInstance() {
        if (sInstance == null) {
            synchronized (ThreadPoolManager.class) {
                if (sInstance == null) {
                    sInstance = new ThreadPoolManager();
                }
            }
        }
        return sInstance;
    }
    /**
     * corePoolSize:核心线程数
     * maximumPoolSize：线程池所容纳最大线程数(workQueue队列满了之后才开启)
     * keepAliveTime：非核心线程闲置时间超时时长
     * unit：keepAliveTime的单位
     * workQueue：等待队列，存储还未执行的任务
     * threadFactory：线程创建的工厂
     * handler：异常处理机制
     */
    private void createExecutor() {
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 提交无返回结果的线程
     *
     * @param r
     */
    public void execute(Runnable r) {
        executor.execute(r);
    }

    /**
     * 提交一个有返回结果的线程
     *
     * @param r
     * @return
     */
    public Future<T> submit(Callable<T> r) {
        return executor.submit(r);
    }

    /**
     * 把任务移除等待队列
     *
     * @param r
     */
    public void removeTask(Runnable r) {
        if (r != null) {
            executor.getQueue().remove(r);
        }
    }

    public String getThreadPoolInfo(){
        StringBuffer buffer = new StringBuffer();
        //队列长度
        int size = executor.getQueue().size();
        int activeCount = executor.getActiveCount();
        int poolSize = executor.getPoolSize();
        buffer.append("队列长度:").append(size);
        buffer.append("，线程数量:").append(poolSize);
        buffer.append("，活跃线程:").append(activeCount);
        return buffer.toString();
    }

}