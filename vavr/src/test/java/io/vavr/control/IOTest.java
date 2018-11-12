package io.vavr.control;

import io.vavr.Function0;
import org.junit.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class IOTest {

    @Test
    public void shouldExecuteSuccessfully() {
        Either<Throwable, Integer> operations = IO.of(() -> 1 + 1).attempt();
        assertThat(operations.isRight()).isTrue();
    }

    @Test
    public void shouldFailInCaseOfException() {
        Either<Throwable, Object> boom = IO.of(() -> {
            throw new RuntimeException("Boom");
        }).attempt();

        assertThat(boom.isLeft()).isTrue();
    }

    @Test
    public void shouldExecuteComposedFunctions() {
        Function0<Integer> sum = () -> 2 + 2;
        Function<Integer, Integer> multiplyBy2 = n -> n * 2;
        sum.andThen(multiplyBy2);
        IO<Integer> operation = IO.of(sum);
        assertThat(operation.attempt().isRight()).isTrue();
    }

    @Test
    public void shouldExecuteAsyncOperation() throws InterruptedException {
        Function0<Integer> sum = () -> {
            try {
                System.out.println("an expensive method");
                Thread.sleep(2000);
            } catch (Exception ignored) {
            }
            return 2 + 2;
        };

        IO.of(sum).async(it -> {
            System.out.println("operation finished with result " + it.get());
            assertThat(it.isRight()).isTrue();

        });

        System.out.println("Waiting result..");
        Thread.sleep(4000);
    }
}