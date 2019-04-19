package top.chorg.support;

import top.chorg.system.Sys;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTime {
    private Date date;
    private int hour, minute, second;

    public DateTime() {
        this.currentTime();
    }

    public DateTime(String str) {
        this.assign(str);
    }

    public void reconstruct() {
        while (this.second < 0) {
            this.second += 60;
            this.minute--;
        }
        this.minute += this.second / 60;
        this.second %= 60;
        while (this.minute < 0) {
            this.minute += 60;
            this.hour--;
        }
        this.hour += this.minute / 60;
        this.minute %= 60;
        while (this.hour < 0) {
            this.hour += 24;
            this.date.plus(-1);
        }
        this.date.plus(this.hour / 24);
        this.hour %= 24;
        this.date.minusOneMonth();
    }

    public void assign(String string) throws IllegalArgumentException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //创建要显示的日期格式
        java.util.Date date = null;      //将从数据库读出来的 timestamp 类型的时间转换为java的Date类型
        try {
            date = fmt.parse(string);
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
        string = fmt.format(date);
        var middle = string.split(" ", 2);
        var result2 = middle[1].split(":");
        this.date = new Date(middle[0]);
        this.hour = Integer.parseInt(result2[0]);
        this.minute = Integer.parseInt(result2[1]);
        this.second = Integer.parseInt(result2[2]);
    }

    public void assign(int y, int M, int d, int h, int m, int s) {
        date.setYear(y);
        date.setMonth(M);
        date.setDay(d);
        this.hour = h;
        this.minute = m;
        this.second = s;
    }

    public void currentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.assign(df.format(new java.util.Date()));
    }

    public void plus(int d, int h, int m, int s) {
        this.date.plus(d);
        this.hour += h;
        this.minute += m;
        this.second += s;
        this.reconstruct();
    }

    public int getYear() {
        return this.date.year;
    }

    public int getMonth() {
        return this.date.month;
    }

    public int getDay() {
        return this.date.day;
    }

    public int getHour() {
        return this.hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public int getSecond() {
        return this.second;
    }

    public void setYear(int year) {
        this.date.setYear(year);
    }

    public void setMonth(int month) {
        this.date.setMonth(month);
    }

    public void setDay(int day) {
        this.date.setDay(day);
    }

    public void setHour(int hour) {
        this.hour = hour;
        this.reconstruct();
    }

    public void setMinute(int minute) {
        this.minute = minute;
        this.reconstruct();
    }

    public void setSecond(int second) {
        this.second = second;
        this.reconstruct();
    }

    public void plusOneYear() {
        date.plusOneYear();
    }

    public void minusOneYear() {
        date.minusOneYear();
    }

    public void plusOneMonth() {
        date.plusOneMonth();
    }

    public void minusOneMonth() {
        date.minusOneMonth();
    }

    public boolean smallerThan(DateTime alter) {
        if (this.date.smallerThan(alter.date)) return true;
        if (!this.date.equals(alter.date)) return false;
        if (this.hour > alter.hour) return false;
        if (this.hour < alter.hour) return true;
        if (this.minute > alter.minute) return false;
        if (this.minute < alter.minute) return true;
        return this.second < alter.second;
    }

    public int compareTo(DateTime alter) {
        if (this.equals(alter)) return 0;
        if (this.smallerThan(alter)) return -1;
        return 1;
    }

    public boolean equals(DateTime alter) {
        return this.date.equals(alter.date) &&
                this.hour == alter.hour &&
                this.minute == alter.minute &&
                this.second == alter.second;
    }

    @Override
    public String toString() {
        return String.format("%d-%02d-%02d %02d:%02d:%02d",
                this.getYear(), this.getMonth(), this.getDay(), hour, minute, second);
    }
}
