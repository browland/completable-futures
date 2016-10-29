import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.LongStream;

public class AdderService {

    private SlowRandomLongService slowRandomLongService;

    public AdderService(SlowRandomLongService slowRandomLongService) {
        this.slowRandomLongService = slowRandomLongService;
    }

    public long generateNValuesWithLatencyThenSumSerial(int numberOfValues, long latency) {
        return LongStream.rangeClosed(1L, numberOfValues)
                .map((i) -> slowRandomLongService.getRandomLong(latency))
                .sum();
    }

    public long generateNValuesWithLatencyThenSumSerialStream(int numberOfValues, long latency) {
        return LongStream.rangeClosed(1L, numberOfValues)
                .map((i) -> slowRandomLongService.getRandomLong(latency))
                .sum();
    }

    public long generateNValuesWithLatencyThenSumParallelStream(int numberOfValues, long latency) {
        return LongStream.rangeClosed(1L, numberOfValues)
                .parallel()
                .map((i) -> slowRandomLongService.getRandomLong(latency))
                .sum();
    }

    /**
     * TODO make this work with a LongStream so we can generate/sum N values
     */
    public long generateTwoValuesWithLatencyThenSumWithCompletableFutures(long latency) {
        CompletableFuture<Long> cf1 = CompletableFuture.supplyAsync(() -> slowRandomLongService.getRandomLong(latency));
        CompletableFuture<Long> cf2 = CompletableFuture.supplyAsync(() -> slowRandomLongService.getRandomLong(latency));

        try {
            long value1 = cf1.get();
            long value2 = cf2.get();

            return value1 + value2;

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("While getting result of getting random long", e);
        }
    }

    /**
     * TODO make this work with a LongStream so we can generate/sum N values
     */
    public long generateTwoValuesWithLatencyThenSumWithCompletableFuturesUsingThenCombine(long latency) {
        CompletableFuture<Long> cf1 = CompletableFuture.supplyAsync(() -> slowRandomLongService.getRandomLong(latency));
        CompletableFuture<Long> cf2 = CompletableFuture.supplyAsync(() -> slowRandomLongService.getRandomLong(latency));

        CompletableFuture<Long> combined = cf1.thenCombine(cf2, (value1, value2) -> value1 + value2);

        try {
            return combined.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("While getting result of getting random long", e);
        }
    }
}
