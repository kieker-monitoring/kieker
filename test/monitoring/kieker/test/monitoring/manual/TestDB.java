package kieker.test.monitoring.manual;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.writer.database.DBHelper;

public class TestDB {
	private static final Log LOG = LogFactory.getLog(TestDB.class);

	private static final String dbDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String dbConnectionAddress = "jdbc:derby:tmp/KIEKER;user=DBUSER;password=DBPASS";
	private static final String dbTablePrefix = "kieker";

	public static void main(final String[] args) {
		try {
			Class.forName(TestDB.dbDriverClassname).newInstance();
		} catch (final Exception ex) { // NOPMD NOCS (MultiCatch)
			ex.printStackTrace();
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(TestDB.dbConnectionAddress + ";create=true");
			final DBHelper helper = new DBHelper(connection, TestDB.dbTablePrefix);
			helper.newMonitoringRecord(new OperationExecutionRecord("operationSignature", "sessionId", 1L, 10, 11, "hostname", 0, 0));
			helper.newMonitoringRecord(new OperationExecutionRecord("operationSignature", "sessionId", 1L, 10, 11, "hostname", 0, 0));
		} catch (final SQLException ex) {
			TestDB.LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (final SQLException ex) {
					TestDB.LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
				}
			}
		}
	}
}
