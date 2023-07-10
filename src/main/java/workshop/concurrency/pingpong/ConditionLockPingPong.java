package workshop.concurrency.pingpong;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConditionLockPingPong {

    private static final Logger LOG = LoggerFactory.getLogger(ConditionLockPingPong.class);

    private static final ReentrantLock LOCK = new ReentrantLock(true);

    private static final Condition BALL = LOCK.newCondition();

    private static Player player1 = new Player("Ping!");
    private static Player player2 = new Player("Pong!");
    private static final Game GAME = new Game(player1, player2);

    public static void main(String[] args) throws Exception {
        Thread ping = new Thread(player1, "ping");
        Thread pong = new Thread(player2, "pong");
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
                LOCK.lock();
                try {
                    while (!GAME.getCurrent().equals(this)) {
                        BALL.await();
                    }
                    GAME.switchPlayers();
                    LOG.info("{}: {}", ++hits, name);
                    BALL.signalAll();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    LOCK.unlock();
                }
            }
        }

    }

    private static class Game {

        private final Player player1;
        private final Player player2;

        private  Player current;

        public Game(Player player1, Player player2) {
            this.player1 = player1;
            this.player2 = player2;
            this.current = player1;
        }

        public void switchPlayers() {
            if (current.equals(player1)) {
                current = player2;
            } else {
                current = player1;
            }
        }

        public Player getCurrent() {
            return current;
        }
    }
}
