package dynamics.gain.repository;

import dynamics.gain.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class DonationRepo {

    private static final Logger log = Logger.getLogger(DonationRepo.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public long getId() {
        String sql = "select max(id) from donations";
        long id = jdbcTemplate.queryForObject(sql, new Object[]{}, Long.class);
        return id;
    }

    public Long getCount() {
        String sql = "select count(*) from donations";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count;
    }

    public Donation get(long id){
        String sql = "select * from donations where id = ?";
        Donation donation = jdbcTemplate.queryForObject(sql, new Object[] { id },
                new BeanPropertyRowMapper<>(Donation.class));
        return donation;
    }

    public Donation get(String subscriptionId){
        String sql = "select * from donations where subscription_id = ?";
        Donation donation = jdbcTemplate.queryForObject(sql, new Object[] { subscriptionId },
                new BeanPropertyRowMapper<>(Donation.class));
        return donation;
    }

    public Donation save(Donation donation){
        String sql = "insert into donations (amount, charge_id, subscription_id, user_id, location_id, donation_date, processed) values (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[] {
                donation.getAmount(), donation.getChargeId(), donation.getSubscriptionId(), donation.getUserId(), donation.getLocationId(), donation.getDonationDate(), donation.getProcessed()
        });
        Long id = getId();
        Donation savedDonation = get(id);
        return savedDonation;
    }

    public List<Donation> getList(){
        String sql = "select * from donations";
        List<Donation> donations = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Donation.class));
        return donations;
    }

    public List<Donation> getList(long userId) {
        String sql = "select * from donations where user_id = ?";
        List<Donation> donations = jdbcTemplate.query(sql, new Object[]{ userId }, new BeanPropertyRowMapper<>(Donation.class));
        return donations;
    }

    public List<Donation> getListLocation(long locationId) {
        String sql = "select * from donations where location_id = ?";
        List<Donation> donations = jdbcTemplate.query(sql, new Object[]{ locationId }, new BeanPropertyRowMapper<>(Donation.class));
        return donations;
    }

    public boolean update(Donation donation) {
        String sql = "update donations set amount = ?, processed = ?, charge_id = ?, subscription_id = ?, cancelled = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{
            donation.getAmount(), donation.getProcessed(), donation.getChargeId(), donation.getSubscriptionId(), donation.isCancelled(), donation.getId()
        });
        return true;
    }
}
