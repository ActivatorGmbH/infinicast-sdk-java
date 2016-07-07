package io.activator.infinicast;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<T, U, W> {

    void accept(T t, U u, W w);

    default TriConsumer<T, U, W> andThen(TriConsumer<? super T, ? super U, ? super W> after) {
        Objects.requireNonNull(after);

        return (a, b, c) -> {
            accept(a, b, c);
            after.accept(a, b, c);
        };
    }
}

