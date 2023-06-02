package workshop.concurrency.dining;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DiningPhilosophers {

    public static void main(String[] args) {

        Stream<String> names = Stream.of("Kant", "Nietzsche", "Schopenhauer", "Hegel", "Wittgenstein");
        List<Philosopher> philosophers = names.map(ConditionPhilosopher::new)
                .collect(Collectors.toList());
        int last = philosophers.size() - 1;
        for (int i = 0; i <= last ; i++) {
            int next = i < last ? i+1 : 0;
            Philosopher current = philosophers.get(i);
            Philosopher neighbor = philosophers.get(next);
            current.sitDownNextTo(neighbor);
            if (i == last) {
                current.setLast(true);
            }
        }
        philosophers.stream()
                .map(p -> new Thread(new PhilosophersLife(p), p.getName().toLowerCase()))
                .forEach(Thread::start);

    }
}
