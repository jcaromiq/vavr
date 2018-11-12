package io.vavr.control;

import io.vavr.concurrent.Future;

import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

class IO<T> {
    private Supplier<? extends T> f;

    private IO(Supplier<? extends T> f) {
        this.f = f;
    }

    public static <T> IO<T> of(Supplier<? extends T> f) {
        return new IO<>(f);
    }

    public Either<Throwable, T> attempt() {
        try {
            return Either.right(f.get());
        } catch (Throwable e) {
            return Either.left(e);
        }
    }

    public void async(Consumer<Either<Throwable, T>> callback) {
        Future.of(Executors.newSingleThreadExecutor(), this::attempt)
                .onSuccess(callback);
    }
}
