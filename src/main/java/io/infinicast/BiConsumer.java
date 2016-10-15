package io.infinicast;

//
public interface BiConsumer<T, U> {

    void accept(T t, U u);

  /*  default QuadConsumer<T, U, W, X> andThen(QuadConsumer<? super T, ? super U, ? super W, ? super X> after) {
        Objects.requireNonNull(after);

        return (a, b, c, d) -> {
            accept(a, b, c, d);
            after.accept(a, b, c, d);
        };
    }*/
}

