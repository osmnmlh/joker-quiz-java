package pt.uevora.joker.domain;

import java.util.Arrays;

public final class MoneyLevels {
    public static final int[] LEVELS = {0, 200, 500, 1000, 3000, 10000, 50000};

    private MoneyLevels() {
    }

    public static int maxIndex() {
        return LEVELS.length - 1;
    }

    public static int[] copyOfLevels() {
        return Arrays.copyOf(LEVELS, LEVELS.length);
    }
}
