package io.infinicast;

//
public interface Action {
    void accept();

  /*  default Action andThen(Action after){
        Objects.requireNonNull(after);

        return () -> {
            accept();
            after.accept();
        };
    }*/
}
