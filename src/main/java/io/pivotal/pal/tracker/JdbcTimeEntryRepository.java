package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;
    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {

        final KeyHolder holder = new GeneratedKeyHolder();
        String query = "insert into time_entries (project_id, user_id, date, hours)  values(?, ?, ?, ?)";

        final PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                final PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                ps.setLong(1, timeEntry.getProjectId());
                ps.setLong(2, timeEntry.getUserId());
                ps.setDate(3, java.sql.Date.valueOf(timeEntry.getDate()));
                ps.setInt(4, timeEntry.getHours());

                return ps;

            }
        };
        jdbcTemplate.update(preparedStatementCreator, holder);
        final long newNameId = holder.getKey().longValue();
        return new TimeEntry(newNameId, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        TimeEntry timeEntry = null;
        String query = "select * from time_entries where id = ?";
        try {
            timeEntry = (TimeEntry) jdbcTemplate.queryForObject(query, new Object[]{timeEntryId}, new TimeEntryRowmapper());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return timeEntry;
    }

    @Override
    public List<TimeEntry> list() {
        String query = "select * from time_entries";
        return (List<TimeEntry>)jdbcTemplate.query(query, new Object[] {}, new TimeEntryRowmapper());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        String query = "update time_entries set project_id = ?, user_id = ?, date = ?, hours = ?  where id = ?";

        final PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                final PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                ps.setLong(1, timeEntry.getProjectId());
                ps.setLong(2, timeEntry.getUserId());
                ps.setDate(3, java.sql.Date.valueOf(timeEntry.getDate()));
                ps.setInt(4, timeEntry.getHours());
                ps.setLong(5, id);
                return ps;

            }
        };
        jdbcTemplate.update(preparedStatementCreator);
        return new TimeEntry(id, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
    }

    @Override
    public Boolean delete(long timeEntryId) {
        String query = "delete from time_entries where id = ?";

        final PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                final PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                ps.setLong(1, timeEntryId);
                return ps;

            }
        };
        return (jdbcTemplate.update(preparedStatementCreator) == 1) ? true : false;
    };

}
