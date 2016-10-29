import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdderServiceTest {

    @Mock
    private Random random;

    /**
     * Fails if timeout is reduced to 3000 because the two calls to the external service are done serially,
     * and they take 2s each.
     */
    @Test(timeout = 5000)
    public void serialExecutionWithoutCompletableFutures() {
        AdderService adderService = new AdderService(new SlowRandomLongService(random));

        when(random.nextLong()).thenReturn(1L).thenReturn(2L);

        assertThat(adderService.generateNValuesWithLatencyThenSumSerial(2, 2000L)).isEqualTo(3L);
    }

    /**
     * Fails if timeout is reduced to 3000 because the two calls to the external service are done serially,
     * and they take 2s each.
     */
    @Test(timeout = 5000)
    public void serialExecutionWithSerialStream() {
        AdderService adderService = new AdderService(new SlowRandomLongService(random));

        when(random.nextLong()).thenReturn(1L).thenReturn(2L);

        assertThat(adderService.generateNValuesWithLatencyThenSumSerialStream(2, 2000L)).isEqualTo(3L);
    }

    @Test(timeout = 3000)
    public void parallelExecutionWithParallelStream() {
        AdderService adderService = new AdderService(new SlowRandomLongService(random));

        when(random.nextLong()).thenReturn(1L).thenReturn(2L);

        assertThat(adderService.generateNValuesWithLatencyThenSumParallelStream(2, 2000L)).isEqualTo(3L);
    }

    @Test(timeout = 3000)
    public void parallelExecutionWithCompletableFutures() {
        AdderService adderService = new AdderService(new SlowRandomLongService(random));

        when(random.nextLong()).thenReturn(1L).thenReturn(2L);

        assertThat(adderService.generateTwoValuesWithLatencyThenSumWithCompletableFutures(2000L)).isEqualTo(3L);
    }

    @Test(timeout = 3000)
    public void parallelExecutionWithCompletableFuturesThenCombine() {
        AdderService adderService = new AdderService(new SlowRandomLongService(random));

        when(random.nextLong()).thenReturn(1L).thenReturn(2L);

        assertThat(adderService.generateTwoValuesWithLatencyThenSumWithCompletableFuturesUsingThenCombine(2000L)).isEqualTo(3L);
    }
}
