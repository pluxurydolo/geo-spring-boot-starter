package com.pluxurydolo.geo.configuration;

import com.graphhopper.GraphHopper;
import com.graphhopper.config.Profile;
import com.graphhopper.json.Statement;
import com.graphhopper.util.CustomModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class GraphHopperConfiguration {

    @Bean
    public GraphHopper graphHopper() throws IOException {
        File osmFile = getOrCreateOsmFile();
        String osmFileAbsolutePath = osmFile.getAbsolutePath();

        GraphHopper graphHopper = new GraphHopper();
        graphHopper.setOSMFile(osmFileAbsolutePath);
        graphHopper.setGraphHopperLocation("graph-cache");
        graphHopper.setEncodedValuesString("bike_access, road_class, max_speed, surface, foot_access");
        graphHopper.setProfiles(footProfile(), bikeProfile());
        graphHopper.importOrLoad();

        return graphHopper;
    }

    private static File getOrCreateOsmFile() throws IOException {
        Path osmDir = Paths.get("osm");

        if (!Files.exists(osmDir)) {
            Files.createDirectories(osmDir);
        }

        File osmFile = osmDir.resolve("central-russia.osm.pbf")
            .toFile();

        if (osmFile.exists()) {
            return osmFile;
        }

        InputStreamSource resource = new ClassPathResource("osm/central-russia.osm.pbf");

        try (InputStream is = resource.getInputStream()) {
            Files.copy(is, osmFile.toPath());
        }

        return osmFile;
    }

    private static Profile footProfile() {
        CustomModel customModel = new CustomModel();
        customModel.addToSpeed(Statement.If("true", Statement.Op.LIMIT, "4.0"));
        customModel.addToPriority(Statement.If("true", Statement.Op.MULTIPLY, "1.0"));
        customModel.addToPriority(Statement.If("!foot_access", Statement.Op.MULTIPLY, "0.0"));
        customModel.addToPriority(Statement.If("road_class == PEDESTRIAN", Statement.Op.MULTIPLY, "5.5"));
        customModel.addToPriority(Statement.If("road_class == FOOTWAY", Statement.Op.MULTIPLY, "6.0"));
        customModel.addToPriority(Statement.If("road_class == PATH", Statement.Op.MULTIPLY, "4.0"));
        customModel.addToPriority(Statement.If("road_class == MOTORWAY", Statement.Op.MULTIPLY, "0.0"));
        customModel.addToPriority(Statement.If("road_class == TRUNK", Statement.Op.MULTIPLY, "0.0"));
        customModel.addToPriority(Statement.If("road_class == PRIMARY", Statement.Op.MULTIPLY, "0.5"));
        customModel.setDistanceInfluence(0.0);

        Profile profile = new Profile("foot");
        profile.setCustomModel(customModel);
        profile.setTurnCostsConfig(null);

        return profile;
    }

    private static Profile bikeProfile() {
        CustomModel customModel = new CustomModel();
        customModel.addToSpeed(Statement.If("true", Statement.Op.LIMIT, "15.0"));
        customModel.addToSpeed(Statement.If("road_class == CYCLEWAY", Statement.Op.LIMIT, "20.0"));
        customModel.addToSpeed(Statement.If("max_speed < 30", Statement.Op.LIMIT, "18.0"));
        customModel.addToSpeed(Statement.If("surface == UNPAVED", Statement.Op.LIMIT, "8.0"));
        customModel.addToPriority(Statement.If("true", Statement.Op.MULTIPLY, "1.0"));
        customModel.addToPriority(Statement.If("!bike_access", Statement.Op.MULTIPLY, "0.0"));
        customModel.addToPriority(Statement.If("bike_access", Statement.Op.MULTIPLY, "4.0"));
        customModel.addToPriority(Statement.If("road_class == CYCLEWAY", Statement.Op.MULTIPLY, "5.0"));
        customModel.addToPriority(Statement.If("road_class == PATH", Statement.Op.MULTIPLY, "1.2"));
        customModel.addToPriority(Statement.If("max_speed < 30", Statement.Op.MULTIPLY, "3.0"));
        customModel.addToPriority(Statement.If("surface == UNPAVED", Statement.Op.LIMIT, "0.7"));
        customModel.addToPriority(Statement.If("road_class == MOTORWAY", Statement.Op.MULTIPLY, "0.0"));
        customModel.addToPriority(Statement.If("road_class == TRUNK", Statement.Op.MULTIPLY, "0.0"));
        customModel.setDistanceInfluence(0.7);

        Profile profile = new Profile("bike");
        profile.setCustomModel(customModel);
        profile.setTurnCostsConfig(null);

        return profile;
    }
}
