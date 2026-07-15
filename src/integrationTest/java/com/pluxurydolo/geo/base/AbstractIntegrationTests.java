package com.pluxurydolo.geo.base;

import com.pluxurydolo.geo.TestApplication;
import com.pluxurydolo.geo.configuration.CoordinatesProviderTestConfiguration;
import com.pluxurydolo.geo.configuration.GraphHopperTestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest(classes = {
    TestApplication.class,
    CoordinatesProviderTestConfiguration.class,
    GraphHopperTestConfiguration.class
})
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public abstract class AbstractIntegrationTests {
}
