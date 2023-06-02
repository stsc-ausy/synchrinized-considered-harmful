package workshop.concurrency.pingpong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyPingPong {

    private static final Logger LOG = LoggerFactory.getLogger(NotifyPingPong.class);

    private static final Object BALL = new Object();
    private static final Game GAME = new Game();

    public static void main(String[] args) throws Exception {
        Thread ping = new Thread(new Player("Ping!", "Pong!"), "ping");
        Thread pong = new Thread(new Player("Pong!", "Ping!"), "pong");
        GAME.setPlayerSide("Ping!");
        ping.start();
        pong.start();
    }

    private static class Player implements Runnable {

        private final String name;
        private final String other;

        private int hits;
        Player(String name, String other) {
            this.name = name;
            this.other = other;
        }

        @Override
        public void run() {
            while (hits < 10) {
                try {
                    synchronized (BALL) {
                        while (!GAME.getPlayerSide().equals(name))
                            BALL.wait();
                        LOG.info("{}: {}", ++hits, name);
                        GAME.setPlayerSide(other);
                        BALL.notify();
                    }
                } catch (InterruptedException e) {
                    System.err.println(e);
                }
            }
        }

    }

    private static class Game {

        private String playerSide;

        public String getPlayerSide() {
            return playerSide;
        }

        public void setPlayerSide(String playerSide) {
            this.playerSide = playerSide;
        }
    }
}
