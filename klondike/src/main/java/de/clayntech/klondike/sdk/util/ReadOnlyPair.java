package de.clayntech.klondike.sdk.util;

import java.util.Objects;

public class ReadOnlyPair<F,S> extends Pair<F,S>{

    public ReadOnlyPair(F first, S second) {
        super(Objects.requireNonNull(first), Objects.requireNonNull(second));
    }

    @Override
    public void setFirst(F first) {

    }

    @Override
    public void setSecond(S second) {
    }
}
