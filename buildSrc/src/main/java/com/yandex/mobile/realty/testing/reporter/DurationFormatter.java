package com.yandex.mobile.realty.testing.reporter;

import java.math.BigDecimal;

/**
 * @author rogovalex on 28/07/2020.
 */
public class DurationFormatter {
    public static final int MILLIS_PER_SECOND = 1000;
    public static final int MILLIS_PER_MINUTE = 60000;
    public static final int MILLIS_PER_HOUR = 3600000;
    public static final int MILLIS_PER_DAY = 86400000;

    public DurationFormatter() {
    }

    public String format(long duration) {
        if (duration == 0L) {
            return "0s";
        } else {
            StringBuilder result = new StringBuilder();
            long days = duration / MILLIS_PER_DAY;
            duration %= MILLIS_PER_DAY;
            if (days > 0L) {
                result.append(days);
                result.append('d');
            }

            long hours = duration / MILLIS_PER_HOUR;
            duration %= MILLIS_PER_HOUR;
            if (hours > 0L || result.length() > 0) {
                result.append(hours);
                result.append('h');
            }

            long minutes = duration / MILLIS_PER_MINUTE;
            duration %= MILLIS_PER_MINUTE;
            if (minutes > 0L || result.length() > 0) {
                result.append(minutes);
                result.append('m');
            }

            int secondsScale = result.length() > 0 ? 2 : 3;
            result.append(
                BigDecimal.valueOf(duration)
                    .divide(BigDecimal.valueOf(MILLIS_PER_SECOND))
                    .setScale(secondsScale, 4)
            );
            result.append('s');
            return result.toString();
        }
    }
}
