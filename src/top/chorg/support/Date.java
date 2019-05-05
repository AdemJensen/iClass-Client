package top.chorg.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Date {
    protected int year, month, day;

    public Date() {
        this.currentTime();
    }

    public Date(String str) {
        this.assign(str);
    }

    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    public static int getMonthDay(int year, int month) {
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12: return 31;
            case 2: return Date.isLeapYear(year) ? 29 : 28;
            case 4: case 6: case 9: case 11: return 30;
        }
        return -1;
    }

    public void reconstruct() {
        this.month--;
        while (this.month < 0) {
            this.month += 12;
            this.year--;
        }
        this.year += this.month / 12;
        this.month %= 12;
        this.month++;
        while (this.day > Date.getMonthDay(this.year, this.month)) {
            this.day -= Date.getMonthDay(this.year, this.month);
            this.month++;
            if (this.month > 12) {
                this.month -= 12;
                this.year++;
            }
        }
        while (this.day <= 0) {
            this.month--;
            if (this.month <= 0) {
                this.month += 12;
                this.year--;
            }
            this.day += Date.getMonthDay(this.year, this.month);
        }
    }

    public void assign(String string) throws IllegalArgumentException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); //创建要显示的日期格式
        java.util.Date date;      //将从数据库读出来的 timestamp 类型的时间转换为java的Date类型
        try {
            date = fmt.parse(string);
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
        string = fmt.format(date);
        var result1 = string.split("-");
        this.year = Integer.parseInt(result1[0]);
        this.month = Integer.parseInt(result1[1]);
        this.day = Integer.parseInt(result1[2]);
    }

    public void assign(int y, int M, int d) {
        this.year = y;
        this.month = M;
        this.day = d;
    }

    public void currentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        this.assign(df.format(new java.util.Date()));
    }

    public void plus(int d) {
        this.day += d;
        this.reconstruct();
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    public void setYear(int year) {
        this.year = year;
        this.reconstruct();
    }

    public void setMonth(int month) {
        this.month = month;
        this.reconstruct();
    }

    public void setDay(int day) {
        this.day = day;
        this.reconstruct();
    }

    public void plusOneYear() {
        this.year++;
        this.day = Math.min(this.day, Date.getMonthDay(this.year, this.month));
    }

    public void minusOneYear() {
        this.year--;
        this.day = Math.min(this.day, Date.getMonthDay(this.year, this.month));
    }

    public void plusOneMonth() {
        this.month++;
        if (this.month > 12) {
            this.month = 1;
            this.year++;
        }
        this.day = Math.min(this.day, Date.getMonthDay(this.year, this.month));
    }

    public void minusOneMonth() {
        this.month--;
        if (this.month < 1) {
            this.month = 12;
            this.year++;
        }
        this.day = Math.min(this.day, Date.getMonthDay(this.year, this.month));
    }

    public boolean smallerThan(Date alter) {
        if (this.year > alter.year) return false;
        if (this.year < alter.year) return true;
        if (this.month > alter.month) return false;
        if (this.month < alter.month) return true;
        return this.day < alter.day;
    }

    public int compareTo(Date alter) {
        if (this.equals(alter)) return 0;
        if (this.smallerThan(alter)) return -1;
        return 1;
    }

    public boolean equals(Date alter) {
        return this.year == alter.year && this.month == alter.month && this.day == alter.day;
    }

    @Override
    public String toString() {
        return String.format("%d-%02d-%02d", year, month, day);
    }
}
