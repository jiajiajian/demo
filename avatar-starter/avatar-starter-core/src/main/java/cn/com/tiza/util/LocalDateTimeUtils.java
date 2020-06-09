package cn.com.tiza.util;

/**
 * @author tz0920
 */

import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * 时间工具类
 */
public class LocalDateTimeUtils {

    /**
     * 日期格式：yyyy-MM-dd
     */
    public static String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 日期格式：yyyyMMdd
     */
    public static String DATE_PATTERN_A = "yyyyMMdd";

    /**
     * 日期时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式(24小时制)：HH:mm:ss
     */
    public static String TIME_PATTERN_24 = "HH:mm:ss";

    /**
     * 时间格式(12小时制)：hh:mm:ss
     */
    public static String TIME_PATTERN_12 = "hh:mm:ss";

    /**
     * 获取年份
     */
    public static int getYear() {
        LocalDateTime localTime = LocalDateTime.now();
        int year = localTime.get(ChronoField.YEAR);
        return year;
    }

    /**
     * 获取月份
     */
    public static int getMonth() {
        LocalDateTime localTime = LocalDateTime.now();
        int month = localTime.get(ChronoField.MONTH_OF_YEAR);
        return month;
    }

    /**
     * 解析字符串日期为Date
     *
     * @param dateStr 日期字符串
     * @param pattern 格式，如：yyyy-MM-dd
     * @return 时间戳
     */
    public static Long parse(String dateStr, String pattern) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }

    /**
     * 获取日期字符串 开始时间戳
     *
     * @param str     2020-03-02
     * @param pattern yyyy-MM-dd
     * @return
     */
    public static Long getDayStartMilli(String str, String pattern) {
        LocalDate localDate = LocalDate.parse(str, DateTimeFormatter.ofPattern(pattern));
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取当前月开始时间戳
     *
     * @return
     */
    public static Long getMonStartDayTime() {
        LocalDateTime localDateTime = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 获取当前月结束时间戳
     *
     * @return
     */

    public static Long getMonStartEndTime() {
        LocalDateTime localDateTime = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }


    /**
     * 获取日期字符串 结束时间戳
     *
     * @param str     2020-03-02
     * @param pattern yyyy-MM-dd
     * @return
     */
    public static Long getDayEndMilli(String str, String pattern) {
        LocalDate localDate = LocalDate.parse(str, DateTimeFormatter.ofPattern(pattern));
        LocalDateTime localDateTime = localDate.atTime(23, 59, 59, 999);
        return localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }


    /**
     * 格式化时间-默认yyyy-MM-dd HH:mm:ss格式
     *
     * @param dateTime LocalDateTime对象
     * @param pattern  要格式化的字符串
     * @return
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(StringUtils.isEmpty(pattern) ? DATE_TIME_PATTERN : pattern));
    }


    /**
     * 增加月份 例 20200131 增加1个月 返回 20200229
     *
     * @param num       增加月数
     * @param pattern   格式
     * @param startDate 开始日期
     * @return
     */

    public static String addMonth(int num, String pattern, String startDate) {
        LocalDate localDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern(pattern));
        return localDate.plusMonths(num).format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 计算俩个日期之间月数
     *
     * @param pattern
     * @param startDate
     * @param endDate
     * @return
     */
    public static int monthDuration(String pattern, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern(pattern));
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern(pattern));
        Period between = Period.between(start, end);
        return between.getMonths() + between.getYears() * 12;
    }


    /**
     * 获取当前日期
     *
     * @param pattern 格式，如：yyyy-MM-dd
     * @return 2019-01-01
     */
    public static String getCurrentDay(String pattern) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 根据时间戳获取指定格式化日期
     *
     * @param pattern 格式，如：yyyy-MM-dd
     * @param time    时间戳
     */
    public static String formatDay(Long time, String pattern) {
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(time / 1000, 0, ZoneOffset.ofHours(8));
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static long secondToTomorrow() {
        LocalDateTime now = LocalDateTime.now();
        Instant instant = now.toInstant(ZoneOffset.of("+8"));
        //获取第第二天零点时刻的实例
        LocalDateTime tomorrowTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                .plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        //ChronoUnit日期枚举类,between方法计算两个时间对象之间的时间量
        long seconds = ChronoUnit.SECONDS.between(now, tomorrowTime);
        return seconds;
    }
}

