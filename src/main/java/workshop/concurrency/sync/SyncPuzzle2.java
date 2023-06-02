package workshop.concurrency.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncPuzzle2 {

    private static final Object MONITOR = new Object();

    public static void main(String[] args) throws Exception {
        BlockMe blockMe = new BlockMe();
        Thread toBeBlocked = new Thread(blockMe, "blockMe");
        Blocker blocker = new Blocker(MONITOR);
        Thread blocking =  new Thread(blocker, "blocker");
        blocking.start();
        toBeBlocked.start();
        Thread.sleep(7);
        blocker.stop();
    }

    private static class BlockMe implements Runnable {
        private static final Logger LOGGER = LoggerFactory.getLogger(BlockMe.class);

        @Override
        public void run() {
            sayHello();
        }

        private synchronized void sayHello() {
            LOGGER.info("Hello world from {}!", Thread.currentThread().getName());
        }
    }

    private static class Blocker implements Runnable {
        private static final Logger LOGGER = LoggerFactory.getLogger(Blocker.class);
        private final Object lock;

        private boolean running = true;

        public Blocker(Object lock) {
            this.lock = lock;
        }

        public void stop() {
            running = false;
        }

        @Override
        public void run() {
            synchronized (lock) {
                while (running) {
                    LOGGER.info("Blocking ...");
                }
            }
        }
    }
}
