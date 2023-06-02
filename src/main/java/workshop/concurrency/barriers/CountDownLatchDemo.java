package workshop.concurrency.barriers;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountDownLatchDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(CountDownLatchDemo.class);

    private static final CountDownLatch START = new CountDownLatch(1);
    private static final CountDownLatch STOP = new CountDownLatch(10);

    private static boolean running = true;

    public static void main(String[] args) {
        Thread closer = new Thread(CountDownLatchDemo::closer, "closer");
        for (int i = 0; i < 10; i++) {
            new Thread(CountDownLatchDemo::worker, "worker-" + i).start();
        }
        closer.start();
    }

    private static void worker() {
        try {
            START.await();
            LOGGER.info("Counting down ...");
            Thread.sleep((long) (Math.random() * 100));
            STOP.countDown();
        } catch (InterruptedException e) {
        }
    }

    private static void closer() {
        START.countDown();;
        try {
            STOP.await();
            LOGGER.info("Finished");
        } catch (InterruptedException e) {}
    }
}
