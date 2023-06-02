package workshop.concurrency.pingpong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SynchronizedPingPong {
    private static final Logger LOG = LoggerFactory.getLogger(SynchronizedPingPong.class);

    private static final Object BALL = new Object();

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
                synchronized (BALL) {
                    LOG.info("{}: {}", ++hits, name);
                }
            }
        }
    }

}
