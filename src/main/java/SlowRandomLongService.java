import java.util.Random;

public class SlowRandomLongService {

    private Random random;

    public SlowRandomLongService(Random random) {
        this.random = random;
    }

    public long getRandomLong(long latency) {
        try {
            Thread.sleep(latency);
            return random.nextLong();
        } catch (InterruptedException e) {
            throw new RuntimeException("While sleeping before random number generation", e);
        }
    }
}
