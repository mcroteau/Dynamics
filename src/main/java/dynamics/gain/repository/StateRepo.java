package dynamics.gain.repository;

import dynamics.gain.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class StateRepo {

    private static final Logger log = Logger.getLogger(StateRepo.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public long getId() {
        String sql = "select max(id) from states";
        long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
        return id;
    }

    public Long getCount() {
        String sql = "select count(*) from states";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public State get(long id){
        String sql = "select * from states where id = ?";
        State state = jdbcTemplate.queryForObject(sql, new Object[] { id },
                new BeanPropertyRowMapper<>(State.class));
        return state;
    }


    public List<State> getList(){
        String sql = "select * from states";
        List<State> states = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(State.class));
        return states;
    }

    public State save(State state){
        String sql = "insert into states (name) values (?)";
        jdbcTemplate.update(sql, new Object[] {
                state.getName()
        });

        Long id = getId();
        State savedState = get(id);
        return savedState;
    }

    public boolean update(State state){
        String sql = "update states set name = ? where id = ?";
        jdbcTemplate.update(sql, new Object[] {
                state.getName()
        });
        return true;
    }

    public boolean delete(long id){
        String sql = "delete from states where id = ?";
        jdbcTemplate.update(sql, new Object[] {id});
        return true;
    }

}
