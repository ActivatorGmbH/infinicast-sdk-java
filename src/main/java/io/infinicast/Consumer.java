package io.infinicast;

/**
 * Created by ASG on 15.10.2016.
 * Only used in java 1.7 version
 */
public interface Consumer<T> {

    void accept(T t);
}
