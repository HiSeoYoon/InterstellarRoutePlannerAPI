package org.interstella.mapper;

import org.interstella.model.AcceleratorRoute;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AcceleratorRouteRowMapper implements RowMapper<AcceleratorRoute> {
    @Override
    public AcceleratorRoute mapRow(ResultSet rs, int rowNum) throws SQLException {
        AcceleratorRoute acceleratorRoutes = new AcceleratorRoute();
        acceleratorRoutes.setSourceAcceleratorId(rs.getString("source_accelerator_id"));
        acceleratorRoutes.setTargetAcceleratorId(rs.getString("target_accelerator_id"));
        acceleratorRoutes.setJourneyFee(rs.getDouble("journey_fee"));

        Array journeyRoutesArray = rs.getArray("journey_routes");
        if (journeyRoutesArray != null) {
            String[] journeyRoutes = (String[]) journeyRoutesArray.getArray();
            List<String> journeyRoutesList = new ArrayList<>();
            for (String route : journeyRoutes) {
                journeyRoutesList.add(route);
            }
            acceleratorRoutes.setJourneyRoute(journeyRoutesList);
        }

        return acceleratorRoutes;
    }
}