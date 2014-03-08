package com.example.imageloader;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {
    private static final int ALIVE_TIME = 1;
    private static final TimeUnit ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private static final int NUMBER_CORES = Runtime.getRuntime().availableProcessors();
    private static ThreadPoolExecutor _threadManager = null;
    private static BlockingQueue<Runnable> _workQueue = null;
    
    public static ThreadPoolExecutor getInstance() {
        if(_threadManager == null && _workQueue == null) {
            init();
        }
        return _threadManager;
    }
    private static void init() {
        _workQueue = new LinkedBlockingQueue<Runnable>();
        _threadManager = new ThreadPoolExecutor(NUMBER_CORES, NUMBER_CORES, ALIVE_TIME, ALIVE_TIME_UNIT, _workQueue);
        
        
        
    }

}
