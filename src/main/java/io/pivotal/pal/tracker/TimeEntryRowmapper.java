package io.pivotal.pal.tracker;


import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeEntryRowmapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        TimeEntry timeEntry = new TimeEntry(rs.getLong("id"), rs.getLong("project_id"), rs.getLong("user_id"), asLocalDate(rs.getDate("date")), rs.getInt("hours"));
        return timeEntry;
    }

    public static LocalDate asLocalDate(java.util.Date date) {
        return asLocalDate(date, ZoneId.systemDefault());
    }

    public static LocalDate asLocalDate(java.util.Date date, ZoneId zone) {
        if (date == null)
            return null;

        if (date instanceof java.sql.Date)
            return ((java.sql.Date) date).toLocalDate();
        else
            return Instant.ofEpochMilli(date.getTime()).atZone(zone).toLocalDate();
    }
}
