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

    public long getId() {
        String sql = "select max(id) from daily_counts";
        long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
        return id;
    }

    public DailyCount get(long id) {
        String sql = "select * from daily_counts where id = ?";
        DailyCount dailyCount = jdbcTemplate.queryForObject(sql, new Object[] { id },
                new BeanPropertyRowMapper<>(DailyCount.class));
        return dailyCount;
    }

    public DailyCount save(DailyCount count){
        String sql = "insert into daily_counts (user_id, location_id, count, date_entered) values(?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[] {
                count.getUserId(), count.getLocationId(), count.getCount(), count.getDateEntered()
        });
        Long id = getId();
        DailyCount savedCount = get(id);
        return savedCount;
    }

    public boolean update(DailyCount count) {
        String sql = "update daily_counts set user_id = ?, count = ?, date_entered = ? where id = ?";
        jdbcTemplate.update(sql, new Object[] {
                count.getUserId(), count.getCount(), count.getDateEntered(), count.getId()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete * from daily_counts where id = ?";
        jdbcTemplate.update(sql, new Object[] { id });
        return true;
    }

    public DailyCount getCount(long locationId, long date){
        try {
            String sql = "select * from daily_counts where location_id = ? and date_entered = ?";
            DailyCount count = jdbcTemplate.queryForObject(sql, new Object[]{locationId, date}, new BeanPropertyRowMapper<>(DailyCount.class));

            return count;
        }catch(Exception e){
            return null;
        }
    }

    public List<DailyCount> getCounts(long locationId){
        String sql = "select * from daily_counts where location_id = ?";
        List<DailyCount> counts = jdbcTemplate.query(sql, new Object[]{ locationId }, new BeanPropertyRowMapper<>(DailyCount.class));
        return counts;
    }
}
