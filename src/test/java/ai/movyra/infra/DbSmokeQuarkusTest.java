package ai.movyra.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class DbSmokeQuarkusTest {

    @Inject
    DataSource dataSource;

    @Test
    void should_connect_and_read_seeded_catalog() throws Exception {
        try (Connection c = dataSource.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("select count(*) from app.service_catalog")) {

            assertTrue(rs.next());
            int count = rs.getInt(1);

            // V2 seeds 12 services
            assertEquals(12, count);
        }
    }
}
