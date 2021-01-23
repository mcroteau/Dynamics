package dynamics.gain.repository;

import dynamics.gain.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class PlanRepo {

    private static final Logger log = Logger.getLogger(PlanRepo.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public long getId() {
        String sql = "select max(id) from plans";
        long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
        return id;
    }

    public long getProductId() {
        String sql = "select max(id) from products";
        long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
        return id;
    }

    public Integer getCount() {
        String sql = "select count(*) from plans";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count;
    }

    public DynamicsProduct getProduct(long id){
        String sql = "select * from products where id = ?";
        DynamicsProduct dynamicsProduct = jdbcTemplate.queryForObject(sql, new Object[] { id },
                new BeanPropertyRowMapper<>(DynamicsProduct.class));
        return dynamicsProduct;
    }

    public DynamicsPlan getPlan(long id){
        String sql = "select * from plans where id = ?";
        DynamicsPlan dynamicsPlan = jdbcTemplate.queryForObject(sql, new Object[] { id },
                new BeanPropertyRowMapper<>(DynamicsPlan.class));
        return dynamicsPlan;
    }

    public List<DynamicsPlan> getList(){
        String sql = "select * from plans";
        List<DynamicsPlan> dynamicsPlans = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DynamicsPlan.class));
        return dynamicsPlans;
    }

    public DynamicsProduct saveProduct(DynamicsProduct dynamicsProduct){
        String sql = "insert into products (nickname, stripe_id) values (?, ?)";
        jdbcTemplate.update(sql, new Object[] {
                dynamicsProduct.getNickname(), dynamicsProduct.getStripeId()
        });
        Long id = getProductId();
        DynamicsProduct savedDynamicsProduct = getProduct(id);
        return savedDynamicsProduct;
    }

    public boolean savePlan(DynamicsPlan dynamicsPlan){
        String sql = "insert into plans (amount, nickname, product_id, stripe_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[] {
                dynamicsPlan.getAmount(), dynamicsPlan.getNickname(), dynamicsPlan.getProductId(), dynamicsPlan.getStripeId()
        });
        return true;
    }

    public boolean deleteProduct(long id){
        String sql = "delete from products where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public boolean deletePlan(long id){
        String sql = "delete from plans where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public DynamicsPlan getPlanProductId(Long id) {
        String sql = "select * from plans where product_id = ?";
        DynamicsPlan dynamicsPlan = jdbcTemplate.queryForObject(sql, new Object[] { id },
                new BeanPropertyRowMapper<>(DynamicsPlan.class));
        return dynamicsPlan;
    }

    public DynamicsProduct getProductStripeId(String stripeId) {
        String sql = "select * from products where stripe_id = ?";
        DynamicsProduct dynamicsProduct = jdbcTemplate.queryForObject(sql, new Object[] { stripeId },
                new BeanPropertyRowMapper<>(DynamicsProduct.class));
        return dynamicsProduct;
    }
}
