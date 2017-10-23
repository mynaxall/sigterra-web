package itomy.sigterra.service.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatUtil {

    private DateFormatUtil() {
    }

    public static String format(Timestamp date) {
        LocalDateTime dateTime = date.toLocalDateTime();

        return toFormatString(dateTime, 0);

    }

    public static String format(Timestamp date, String offsetStr) {
        LocalDateTime dateTime = date.toLocalDateTime();
        int offset;
        try {
            offset = Integer.parseInt(offsetStr);
        } catch (NullPointerException | NumberFormatException ex) {
            offset = 0;
        }
        if (-12 <= offset && offset <= 14) {
            dateTime = dateTime.plusHours(offset);
        }

        return toFormatString(dateTime, offset);
    }

    private static String toFormatString(LocalDateTime dateTime, int offset) {
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mm a ");
        DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern("dd/MM/yy");
        String s = timeFormat.format(dateTime);
        if (isToday(dateTime, offset)) {
            s += "Today";
        } else {
            if (isYesterday(dateTime, offset)) {
                s += "Yesterday";
            } else {
                s += dayFormat.format(dateTime);
            }
        }
        return s;
    }

    private static boolean isToday(LocalDateTime dateTime, int offset) {
        LocalDate today = LocalDateTime.now().plusHours(offset).toLocalDate();
        LocalDate date = dateTime.toLocalDate();

        return today.isEqual(date);
    }

    private static boolean isYesterday(LocalDateTime dateTime, int offset) {
        LocalDate yesterday = LocalDateTime.now().minusDays(1).plusHours(offset).toLocalDate();
        LocalDate date = dateTime.toLocalDate();

        return yesterday.isEqual(date);
    }
}
