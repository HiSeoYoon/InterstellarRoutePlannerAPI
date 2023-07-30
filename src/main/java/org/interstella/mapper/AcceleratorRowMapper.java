package org.interstella.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.interstella.model.Accelerator;
import org.interstella.model.AcceleratorConnection;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AcceleratorRowMapper implements RowMapper<Accelerator> {

    @Override
    public Accelerator mapRow(ResultSet rs, int rowNum) throws SQLException {
        Accelerator accelerator = new Accelerator();
        accelerator.setId(rs.getString("source_accelerator_name"));
        accelerator.setName(rs.getString("source_accelerator_id"));

        List<AcceleratorConnection> connections = new ArrayList<>();
        String jsonConnections = rs.getString("connections");
        if (jsonConnections != null) {
            JsonNode jsonNode = null;
            try {
                jsonNode = new ObjectMapper().readTree(jsonConnections);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            if (jsonNode.isArray()) {
                for (JsonNode node : jsonNode) {
                    String targetAcceleratorId = node.get("target_accelerator_id").asText();
                    int distance = node.get("distance").asInt();
                    AcceleratorConnection connection = new AcceleratorConnection();
                    connection.setId(targetAcceleratorId);
                    connection.setDistance(distance);
                    connections.add(connection);
                }
            }
        }
        accelerator.setConnections(connections);

        return accelerator;
    }
}
