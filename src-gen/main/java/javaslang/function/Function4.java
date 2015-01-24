/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
// @@ GENERATED FILE - DO NOT MODIFY @@
package javaslang.function;

import javaslang.Tuple4;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface Function4<T1, T2, T3, T4, R> extends λ<R> {

    R apply(T1 t1, T2 t2, T3 t3, T4 t4);

    @Override
    default int arity() {
        return 4;
    }

    @Override
    default Function1<T1, Function1<T2, Function1<T3, Function1<T4, R>>>> curried() {
        return t1 -> t2 -> t3 -> t4 -> apply(t1, t2, t3, t4);
    }

    @Override
    default Function1<Tuple4<T1, T2, T3, T4>, R> tupled() {
        return t -> apply(t._1, t._2, t._3, t._4);
    }

    @Override
    default Function4<T4, T3, T2, T1, R> reversed() {
        return (t4, t3, t2, t1) -> apply(t1, t2, t3, t4);
    }

    @Override
    default <V> Function4<T1, T2, T3, T4, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t1, t2, t3, t4) -> after.apply(apply(t1, t2, t3, t4));
    }

}