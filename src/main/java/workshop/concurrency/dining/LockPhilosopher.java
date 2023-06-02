package workshop.concurrency.dining;

import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockPhilosopher implements Philosopher {

    private static final Logger LOG = LoggerFactory.getLogger(LockPhilosopher.class);
    private ReentrantLock fork = new ReentrantLock(true);
    private StateOfMind stateOfMind = StateOfMind.THINKING;
    private Philosopher toTheLeft;

    private int portionsEaten;
    private String name;

    private boolean last;
    public LockPhilosopher(String name) {
        this.name = name;
    }

    @Override
    public void setLast(boolean last) {
        this.last = last;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void sitDownNextTo(Philosopher toTheLeft) {
        this.toTheLeft = toTheLeft;
    }

    @Override
    public void takeFork(Philosopher taker) {
        fork.lock();
        if (taker == this) {
            LOG.info("{} takes his fork", name);
        } else {
            LOG.info("{} steals {}'s fork", taker.getName(), name);
        }
    }

    @Override
    public void dropFork(Philosopher dropper) {
        if (dropper == this) {
            LOG.info("{} drops his fork", name);
        } else {
            LOG.info("{} returns {}'s fork", dropper.getName(), name);
        }
        fork.unlock();
    }

    @Override
    public StateOfMind getStateOfMind() {
        return stateOfMind;
    }

    @Override
    public void think() {
        LOG.info("{} is thinking ...", name);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            LOG.error("Interrupted ...");
        }
        stateOfMind = StateOfMind.HUNGRY;
    }

    @Override
    public void getHungry() {
        LOG.info("{} gets hungry  ...", name);
        if (last) {
            LOG.info("{} will take {}'s fork first", name, toTheLeft.getName());
            toTheLeft.takeFork(this);
            takeFork(this);
        } else {
            LOG.info("{} will take his own fork first", name);
            takeFork(this);
            toTheLeft.takeFork(this);
        }
        stateOfMind = StateOfMind.EATING;
    }

    @Override
    public void eat() {
        LOG.info("{} is eating ...", name);
        portionsEaten += 1;
        dropFork(this);
        toTheLeft.dropFork(this);
        stateOfMind = (portionsEaten < 5) ? StateOfMind.THINKING : StateOfMind.SATISFIED;
    }
}
