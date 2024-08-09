package fpt.CapstoneSU24.service;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
@Service
public class EpochDate {
    /**
     * Chuyển đổi ngày tháng sang kiểu long int (epoch)
     * @param dateTime Ngày tháng cần chuyển đổi
     * @return Giá trị long int (epoch) tương ứng
     */
    public long dateToEpoch(LocalDateTime dateTime) {
        Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        return instant.getEpochSecond();
    }

    /**
     * Chuyển đổi giá trị long int (epoch) sang ngày tháng
     * @param epoch Giá trị long int (epoch) cần chuyển đổi
     * @return Ngày tháng tương ứng
     */
    public LocalDateTime epochToDate(long epoch) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault());
    }

    /**
     * Chuyển đổi ngày tháng sang chuỗi string theo định dạng được truyền vào
     * @param dateTime Ngày tháng cần chuyển đổi
     * @param format Định dạng chuỗi string cần chuyển đổi
     * @return Chuỗi string tương ứng
     */
    public String dateTimeToString(LocalDateTime dateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(formatter);
    }

}