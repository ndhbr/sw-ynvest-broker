package de.ndhbr.ynvest.util;

import de.othr.sw.yetra.dto.MarketValueDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;

public class MathUtils {
    // Round double to number of places
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // Calculates the percent difference of a collection of markt values
    public static double calculateRoundedDifferenceOfMarktValues(Collection<MarketValueDTO> marketValues) {
        ArrayList<MarketValueDTO> marketValueDTO = new ArrayList<>(marketValues);
        double difference = 0.0;

        if (marketValueDTO.size() > 0) {
            difference = marketValueDTO.get(marketValueDTO.size() - 1).getUnitPrice() /
                    marketValueDTO.get(0).getUnitPrice();
            difference -= 1;
            difference *= 100;
            difference = MathUtils.round(difference, 2);
        }

        return difference;
    }

    // Calculates the percent difference of two values
    public static double calculateRoundedDifferenceBetweenTwoValues(double a, double b) {
        if (a <= 0 || b <= 0) return 0.0;
        double difference = b / a;
        difference -= 1;
        difference *= 100;
        difference = MathUtils.round(difference, 2);

        return difference;
    }
}
