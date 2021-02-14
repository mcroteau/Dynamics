package dynamics.gain.repository;

import dynamics.gain.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class LocationRepo {

    private static final Logger log = Logger.getLogger(LocationRepo.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public long getId() {
        String sql = "select max(id) from locations";
        long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
        return id;
    }

    public Long getCount() {
        String sql = "select count(*) from locations";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public Location get(long id){
        String sql = "select * from locations where id = ?";
        Location location = jdbcTemplate.queryForObject(sql, new Object[] { id },
                new BeanPropertyRowMapper<>(Location.class));
        return location;
    }


    public List<Location> getList(){
        String sql = "select * from locations";
        List<Location> locations = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Location.class));
        return locations;
    }

    public Location save(Location location){
        String sql = "insert into locations (name, location_uri, description, needs, count, town_id) values (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[] {
                location.getName(), location.getLocationUri(), location.getDescription(), location.getNeeds(), location.getCount(),  location.getTownId()
        });
        Long id = getId();
        Location savedLocation = get(id);
        return savedLocation;
    }

    public boolean delete(long id){
        String sql = "delete from locations where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public List<Location> getList(long id) {
        String sql = "select * from locations where town_id = ?";
        List<Location> locations = jdbcTemplate.query(sql, new Object[]{ id }, new BeanPropertyRowMapper<>(Location.class));
        return locations;
    }

    public Location get(String uri) {
        String sql = "select * from locations where location_uri = ?";
        Location location = jdbcTemplate.queryForObject(sql, new Object[] { uri },
                new BeanPropertyRowMapper<>(Location.class));
        return location;
    }

    public boolean deleteLocations(Long id) {
        String sql = "delete from locations where town_id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public boolean update(Location location) {
        String sql = "update locations set name = ?, location_uri = ?, description = ?, needs = ?, town_id = ? where id = ?";
        jdbcTemplate.update(sql, new Object[] { location.getName(), location.getLocationUri(), location.getDescription(), location.getNeeds(), location.getTownId(), location.getId() });
        return true;
    }

    public String getDevKey(Long id) {
        String sql = "select dev_key from locations where id = ?";
        String apiKey = jdbcTemplate.queryForObject(sql, new Object[] { id },
                new BeanPropertyRowMapper<>(String.class));
        return apiKey;
    }

    public String getLiveKey(Long id) {
        String sql = "select live_key from locations where id = ?";
        String apiKey = jdbcTemplate.queryForObject(sql, new Object[] { id },
                new BeanPropertyRowMapper<>(String.class));
        return apiKey;
    }
}
