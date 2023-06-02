package workshop.concurrency.dining;

public interface Philosopher {
    String getName();

    void setLast(boolean last);

    void sitDownNextTo(Philosopher toTheLeft);

    void takeFork(Philosopher taker);

    void dropFork(Philosopher dropper);

    StateOfMind getStateOfMind();

    void think();

    void getHungry();

    void eat();
}
