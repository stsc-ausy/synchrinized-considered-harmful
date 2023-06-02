package workshop.concurrency.barriers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CyclicBarrierDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(CyclicBarrierDemo.class);

    private static final CyclicBarrier BARRIER = new CyclicBarrier(10, CyclicBarrierDemo::closer);

    private static List<String> DATA;

    private static final List<Worker> WORKERS = new ArrayList<>(10);

    public static void main(String[] args) {
        DATA = new ArrayList<>(List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"));
        for (int i = 0; i < 10; i++) {
            Worker w = new Worker(DATA.get(i));
            WORKERS.add(w);
            new Thread(w, "worker-" + i).start();
        }
    }

    private static class Worker implements Runnable {

        private String line;

        public Worker(String line) {
            this.line = line;
        }

        public String getLine() {
            return line;
        }

        @Override
        public void run() {
            LOGGER.info("Working on {}", line);
            try {
                Thread.sleep((long) (Math.random() * 100));
                line = line.toUpperCase();
                LOGGER.info("Done: {}", line);
                BARRIER.await();
            } catch (Exception e) {}
        }
    }

    private static void closer() {
        for (int i = 0; i < 10; i++) {
            DATA.set(i, WORKERS.get(i).getLine());
        }
        LOGGER.info("Finished: {}", DATA);
    }
}
