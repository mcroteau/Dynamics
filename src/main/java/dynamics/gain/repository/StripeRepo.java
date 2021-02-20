package dynamics.gain.repository;

import dynamics.gain.common.Dynamics;
import dynamics.gain.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class StripeRepo {

    private static final Logger log = Logger.getLogger(StripeRepo.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public long getId() {
        String sql = "select max(id) from prices";
        long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
        return id;
    }

    public long getProductId() {
        String sql = "select max(id) from products";
        long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
        return id;
    }

    public long getPriceId() {
        String sql = "select max(id) from prices";
        long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
        return id;
    }


    public Integer getCount() {
        String sql = "select count(*) from prices";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count;
    }

    public DynamicsProduct getProduct(long id){
        String sql = "select * from products where id = ?";
        DynamicsProduct dynamicsProduct = jdbcTemplate.queryForObject(sql, new Object[] { id },
                new BeanPropertyRowMapper<>(DynamicsProduct.class));
        return dynamicsProduct;
    }

    public DynamicsPrice getPrice(long id){
        String sql = "select * from prices where id = ?";
        DynamicsPrice dynamicsPrice = jdbcTemplate.queryForObject(sql, new Object[] { id },
                new BeanPropertyRowMapper<>(DynamicsPrice.class));
        return dynamicsPrice;
    }

    public DynamicsPrice getPriceAmount(BigDecimal amount){
        try {
            String sql = "select * from prices where amount = ?";
            DynamicsPrice dynamicsPrice = jdbcTemplate.queryForObject(sql, new Object[]{amount},
                    new BeanPropertyRowMapper<>(DynamicsPrice.class));
            return dynamicsPrice;
        }catch(Exception e){}
        return null;
    }

    public List<DynamicsPrice> getList(){
        String sql = "select * from prices";
        List<DynamicsPrice> dynamicsPrices = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DynamicsPrice.class));
        return dynamicsPrices;
    }

    public DynamicsProduct saveProduct(DynamicsProduct dynamicsProduct){
        String sql = "insert into products (nickname, stripe_id) values (?, ?)";
        jdbcTemplate.update(sql, new Object[] {
                dynamicsProduct.getNickname(), dynamicsProduct.getStripeId()
        });
        Long id = getProductId();
        DynamicsProduct savedProduct = getProduct(id);
        return savedProduct;
    }

    public DynamicsPrice savePrice(DynamicsPrice dynamicsPrice){
        String sql = "insert into prices (amount, nickname, product_id, stripe_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[] {
                dynamicsPrice.getAmount(), dynamicsPrice.getNickname(), dynamicsPrice.getProductId(), dynamicsPrice.getStripeId()
        });
        Long id = getPriceId();
        DynamicsPrice savedPrice = getPrice(id);
        return savedPrice;
    }

    public boolean deleteProduct(long id){
        String sql = "delete from products where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public boolean deletePrice(long id){
        String sql = "delete from prices where id = ?";
        jdbcTemplate.update(sql, new Object[] {id });
        return true;
    }

    public DynamicsPrice getPriceProductId(Long id) {
        try{
            String sql = "select * from prices where product_id = ?";
            DynamicsPrice dynamicsPrice = jdbcTemplate.queryForObject(sql, new Object[] { id },
                    new BeanPropertyRowMapper<>(DynamicsPrice.class));
            return dynamicsPrice;
        }catch(Exception ex){ }
        return null;
    }


    public boolean updatePrice(DynamicsPrice dynamicsPrice) {
        String sql = "update prices set stripe_id = ? where id = ?";
        jdbcTemplate.update(sql, new Object[] {
                dynamicsPrice.getStripeId(), dynamicsPrice.getId()
        });
        return true;
    }


    public boolean updateProduct(DynamicsProduct dynamicsProduct) {
        String sql = "update products set stripe_id = ? where id = ?";
        jdbcTemplate.update(sql, new Object[] {
                dynamicsProduct.getStripeId(), dynamicsProduct.getId()
        });
        return true;
    }

    public DynamicsProduct getProductStripeId(String stripeId) {
        try {
            String sql = "select * from products where stripe_id = ?";
            DynamicsProduct dynamicsProduct = jdbcTemplate.queryForObject(sql, new Object[]{stripeId},
                    new BeanPropertyRowMapper<>(DynamicsProduct.class));
            return dynamicsProduct;
        }catch(Exception ex){ }
        return null;
    }

}
