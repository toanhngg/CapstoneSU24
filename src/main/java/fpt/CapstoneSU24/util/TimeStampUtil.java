package fpt.CapstoneSU24.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

@Component
public class TimeStampUtil {
    public String convertTimestampToDate(Long timestamp){
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        Instant instant = Instant.ofEpochSecond(timestamp);
        Date date = Date.from(instant.atZone(zoneId).toInstant());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    public long convertTo10Digit(long timestamp13Digit) {
        return timestamp13Digit / 1000;
    }
    public static long convertTo13Digit(long timestamp10Digit) {
        return timestamp10Digit * 1000;
    }

}
