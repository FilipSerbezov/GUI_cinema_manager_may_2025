package main_1;

import java.util.Comparator;

public class Date_time_comparator implements Comparator<Date_and_time> {

    @Override
    public int compare(Date_and_time date_time_1, Date_and_time date_time_2) {
        if (date_time_1.get_year() > date_time_2.get_year()) {
            return 1;
        } else if (date_time_1.get_year() < date_time_2.get_year()) {
            return -1;
        } else {
            if (date_time_1.get_month().get_number_in_year() > date_time_2.get_month().get_number_in_year()) {
                return 1;
            } else if (date_time_1.get_month().get_number_in_year() < date_time_2.get_month().get_number_in_year()) {
                return -1;
            } else {
                if (date_time_1.get_day_in_month() > date_time_2.get_day_in_month()) {
                    return 1;
                } else if (date_time_1.get_day_in_month() < date_time_2.get_day_in_month()) {
                    return -1;
                } else {
                    if (date_time_1.get_hour() > date_time_2.get_hour()) {
                        return 1;
                    } else if (date_time_1.get_hour() < date_time_2.get_hour()) {
                        return -1;
                    } else {
                        if (date_time_1.get_minute() > date_time_2.get_minute()) {
                            return 1;
                        } else if (date_time_1.get_minute() < date_time_2.get_minute()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                }
            }
        }
    }

}