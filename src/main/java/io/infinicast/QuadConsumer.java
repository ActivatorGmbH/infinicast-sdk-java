package io.infinicast;

import java.util.Objects;

@FunctionalInterface
public interface QuadConsumer<T, U, W, X> {

    void accept(T t, U u, W w, X x);

    default QuadConsumer<T, U, W, X> andThen(QuadConsumer<? super T, ? super U, ? super W, ? super X> after) {
        Objects.requireNonNull(after);

        return (a, b, c, d) -> {
            accept(a, b, c, d);
            after.accept(a, b, c, d);
        };
    }
}

