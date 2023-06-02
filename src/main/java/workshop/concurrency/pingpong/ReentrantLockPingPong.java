package workshop.concurrency.pingpong;

import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReentrantLockPingPong {

    private static final Logger LOG = LoggerFactory.getLogger(ReentrantLockPingPong.class);

    private static final ReentrantLock BALL = new ReentrantLock(true);

    public static void main(String[] args) throws Exception {
        Thread ping = new Thread(new Player("Ping!"), "ping");
        Thread pong = new Thread(new Player("Pong!"), "pong");
        ping.start();
        pong.start();
    }
    private static class Player implements Runnable {


        private final String name;

        private int hits;

        Player(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            while (hits < 10) {
                BALL.lock();
                try {
                    LOG.info("{}: {}", ++hits, name);
                } finally {
                    BALL.unlock();
                }
            }
        }

    }
}
