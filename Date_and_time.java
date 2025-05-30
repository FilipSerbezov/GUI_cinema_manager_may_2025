package main_1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonPropertyOrder({"year", "month", "day_in_month", "hour", "minute", "second"})
public class Date_and_time {
    private Integer year;
    private Date_month month;
    private Integer day_in_month;
    private Integer hour;
    private Integer minute;
    private Integer second;

    public Date_and_time() {
        
    }

    public void set_year(int input_year) {
        this.year = input_year;
    }

    public void set_month(Date_month input_month) {
        this.month = input_month;
    } 

    public void set_day_in_month(int input_day_in_month) {
        if (input_day_in_month <= 0 || input_day_in_month > this.month.get_number_of_days()) {
            throw new IndexOutOfBoundsException("Invalid day number " + input_day_in_month + " for the month " + month.name() + ". ");
        }
        this.day_in_month = input_day_in_month;
    }

    public void set_hour(int input_hour) {
        this.hour = input_hour;
    }

    public void set_minute(int input_minute) {
        this.minute = input_minute;
    }

    @JsonProperty("year")
    public Integer get_year() {
        return this.year;
    }

    @JsonProperty("month")
    public Date_month get_month() {
        return this.month;
    }

    @JsonProperty("day_in_month")
    public Integer get_day_in_month() {
        return this.day_in_month;
    }

    @JsonProperty("hour")
    public Integer get_hour() {
        return this.hour;
    }

    @JsonProperty("minute")
    public Integer get_minute() {
        return this.minute;
    }

    @JsonProperty("second")
    public Integer get_second() {
        return this.second;
    }
}
