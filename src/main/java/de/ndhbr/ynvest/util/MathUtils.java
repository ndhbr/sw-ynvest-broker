package de.ndhbr.ynvest.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {
    // Round double to number of places
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}