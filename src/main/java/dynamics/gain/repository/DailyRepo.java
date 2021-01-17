package dynamics.gain.repository;

import dynamics.gain.model.DailyCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DailyRepo {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    public DailyCount get(long id) {
        String sql = "select * from daily_counts where id = ?";
        DailyCount dailyCount = jdbcTemplate.queryForObject(sql, new Object[] { id },
                new BeanPropertyRowMapper<>(DailyCount.class));
        return dailyCount;
    }

    public boolean save(DailyCount count){
        String sql = "insert into daily_counts (account_id, shelter_id, count, notes, date_entered) values(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[] {
                count.getAccountId(), count.getShelterId(), count.getCount(), count.getNotes(), count.getDateEntered()
        });
        return true;
    }

    public boolean update(DailyCount count) {
        String sql = "update daily_counts set count = ?, notes = ?, date_entered = ? where id = ?";
        jdbcTemplate.update(sql, new Object[] {
                count.getCount(), count.getNotes(), count.getDateEntered(), count.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete * from daily_counts where id = ?";
        jdbcTemplate.update(sql, new Object[] { id });
        return true;
    }

    public DailyCount getCount(long shelterId, long date){
        try {
            String sql = "select * from daily_counts where shelter_id = ? and date_entered = ?";
            DailyCount count = jdbcTemplate.queryForObject(sql, new Object[]{shelterId, date}, new BeanPropertyRowMapper<>(DailyCount.class));

            return count;
        }catch(Exception e){
            return null;
        }
    }

    public List<DailyCount> getCounts(long shelterId){
        String sql = "select * from daily_counts where shelter_id = ?";
        List<DailyCount> counts = jdbcTemplate.query(sql, new Object[]{ shelterId }, new BeanPropertyRowMapper<>(DailyCount.class));
        return counts;
    }
}
