import java.util.Objects;

public class MyDate implements Comparable {
    private int day;
    private int month;
    private int year;

    public MyDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyDate myDate = (MyDate) o;
        return day == myDate.day &&
                month == myDate.month &&
                year == myDate.year;
    }

    @Override
    public int hashCode() {

        return Objects.hash(day, month, year);
    }

    @Override
    public String toString(){
        return String.format("%04d-%02d-%02d",year,month,day);
    }

    @Override
    public int compareTo(Object o) {
        MyDate obj = (MyDate) o;
        if(obj.year!=this.year) return this.year-obj.year;
        if(obj.month!=this.month) return this.month-obj.month;
        if(obj.day!=this.day) return this.day-obj.day;
        return 0;
    }
}
