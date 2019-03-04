package io.vavr.control;

import io.vavr.Function0;
import io.vavr.concurrent.Future;
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
        IO<Integer> operation = IO.of(simpleOperation());
        assertThat(operation.attempt().isRight()).isTrue();
    }

    @Test
    public void shouldExecuteAsyncOperation() throws InterruptedException {
        IO.of(expensiveOperation()).async(it -> {
            System.out.println("operation finished with result " + it.get());
            assertThat(it.isRight()).isTrue();
        });
        System.out.println("Waiting result..");
        Thread.sleep(1000);
    }

    @Test
    public void shouldGetFutureOfAsyncOperation() {
        IO<Integer> io = IO.of(expensiveOperation());
        Future<Either<Throwable, Integer>> future = io.async();
        future.await();
        assertThat(future.isCompleted()).isTrue();
        assertThat(future.get().isRight()).isTrue();
    }

    private Function0<Integer> simpleOperation() {
        Function0<Integer> sum = () -> 2 + 2;
        Function<Integer, Integer> multiplyBy2 = n -> n * 2;
        sum.andThen(multiplyBy2);
        return sum;
    }

    private Function0<Integer> expensiveOperation() {
        return () -> {
            try {
                System.out.println("an expensive method");
                Thread.sleep(500);
            } catch (Exception ignored) {
            }
            return 2 + 2;
        };
    }

    @Test
    public void shouldExecuteSuccessfully1() {
        IO.IOMonad<Integer> operations = () -> 1 + 1;
        assertThat(operations.attempt().isRight()).isTrue();
        assertThat(operations.attempt().get()).isEqualTo(2);
        assertThat(operations.operation()).isEqualTo(2);

    }


    @Test
    public void shouldFailInCaseOfException1() {
        IO.IOMonad<String> boom = () -> {
            throw new RuntimeException("Boom");
        };
        assertThat(boom.attempt().isLeft()).isTrue();
    }


    @Test
    public void shouldExecuteAsyncOperation1() throws InterruptedException {
        IO.IOMonad<String> operation = () -> "a";
        operation.asyncInvoke(s -> {
            System.out.println("operation finished with result " + s);
            assertThat(s).isEqualTo("aa");
        });

        System.out.println("Waiting result..");
        Thread.sleep(1000);

    }

    @Test
    public void shouldGetFutureOfAsyncOperation1() {
        IO.IOMonad<String> operation = () -> "a";

        Future<Either<Throwable, String>> future = operation.asyncInvoke();
        future.await();
        assertThat(future.isCompleted()).isTrue();
        assertThat(future.get().isRight()).isTrue();
    }
}
