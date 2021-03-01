package de.clayntech.klondike.sdk.util;

import java.util.Objects;
import java.util.Optional;

public class Pair<F,S> {
    protected F first;
    protected S second;

    public Pair() {
        this(null,null);
    }

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public Optional<F> getFirstOptional() {
        return Optional.ofNullable(getFirst());
    }

    public Optional<S> getSecondOptional() {
        return Optional.ofNullable(getSecond());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
