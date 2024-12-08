package activeRecord;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDBConnection {

    @Test
    public void testDBConnection_OK() {
        Connection conn = DBConnection.getConnection();
        boolean res = true;
        for (int i = 0; i < 10; i++) {
            Connection conn2 = DBConnection.getConnection();
            if (conn != conn2) {
                res = false;
            }
        }
        assertTrue(res);
    }

    @Test
    public void testDBConnection_changerNom() {
        Connection conn = DBConnection.getConnection();
        DBConnection.setNomDB("test");
        boolean res = conn != DBConnection.getConnection();

        assertTrue(res);
    }
}
