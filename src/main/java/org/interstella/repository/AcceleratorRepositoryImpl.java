package org.interstella.repository;

import org.interstella.mapper.AcceleratorRowMapper;
import org.interstella.model.Accelerator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AcceleratorRepositoryImpl implements AcceleratorRepository {
    private JdbcTemplate jdbcTemplate;

    public AcceleratorRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Accelerator> findAll() {
        String sql = "SELECT a1.accelerator_name AS source_accelerator_name, " +
                "a2.source_accelerator_id AS source_accelerator_id, " +
                "JSON_AGG(JSON_BUILD_OBJECT('target_accelerator_id', a2.target_accelerator_id, 'distance', a2.distance)) AS connections " +
                "FROM accelerators a1 " +
                "JOIN accelerator_connections a2 ON a1.accelerator_id = a2.source_accelerator_id " +
                "GROUP BY a1.accelerator_name, a2.source_accelerator_id";
        return jdbcTemplate.query(sql, new AcceleratorRowMapper());
    }

    @Override
    public Accelerator findById(String acceleratorID) {
        String sql = "SELECT a1.accelerator_name AS source_accelerator_name, " +
                "a2.source_accelerator_id AS source_accelerator_id, " +
                "JSON_AGG(JSON_BUILD_OBJECT('target_accelerator_id', a2.target_accelerator_id, 'distance', a2.distance)) AS connections " +
                "FROM accelerators a1 " +
                "JOIN accelerator_connections a2 ON a1.accelerator_id = a2.source_accelerator_id and a1.accelerator_id = ? " +
                "GROUP BY a1.accelerator_name, a2.source_accelerator_id";
        return jdbcTemplate.queryForObject(sql, new AcceleratorRowMapper(), acceleratorID);
    }
}
