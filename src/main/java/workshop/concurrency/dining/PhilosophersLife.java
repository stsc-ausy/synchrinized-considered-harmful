package workshop.concurrency.dining;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhilosophersLife implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhilosophersLife.class);

    private final Philosopher philosopher;

    public PhilosophersLife(Philosopher philosopher) {
        this.philosopher = philosopher;
    }

    @Override
    public void run() {
        while (StateOfMind.SATISFIED != philosopher.getStateOfMind()) {
            switch (philosopher.getStateOfMind()) {
                case THINKING -> philosopher.think();
                case HUNGRY -> philosopher.getHungry();
                case EATING -> philosopher.eat();
                default -> {}
            }
        }
        LOGGER.info("{} is fed up", philosopher.getName());
    }
}
