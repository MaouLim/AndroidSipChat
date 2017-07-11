package bupt.androidsipchat.sip.util;

/*
 * Created by Maou Lim on 2017/7/5.
 */

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SimpleThreadPool {

    public static final int CORE_SIZE = 4;
    public static final int MAX_SIZE = 8;
    public static final long KEEP_ALIVE = 10; /* SECOND */

    private static ThreadPoolExecutor executor =
            new ThreadPoolExecutor(
                    CORE_SIZE,
                    MAX_SIZE,
                    KEEP_ALIVE,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>()
            );

    public static void execute(Runnable runnable) {
        executor.execute(runnable);
    }
}
