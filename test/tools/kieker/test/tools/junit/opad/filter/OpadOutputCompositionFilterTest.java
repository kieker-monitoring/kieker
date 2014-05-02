package kieker.test.tools.junit.opad.filter;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals; //NOCS

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;

import kieker.tools.opad.filter.OpadOutputCompositionFilter;
import kieker.tools.opad.record.ExtendedStorableDetectionResult;
import kieker.tools.opad.record.OpadOutputData;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Tests the composition of the OpadOutputCompositionFilter.
 * 
 * @author Thomas Düllmann
 * 
 */
public class OpadOutputCompositionFilterTest extends AbstractKiekerTest {

	private static final String HOSTNAME = "hostname";
	private static final String APPNAME = "appname";
	private static final String OPERATION = "public void doSomething(java.lang.String)";

	// ESDR: Variables that are transferred via the ExtendedStorableDetectionResult object which is initialized with those values.
	private static final String ESDR_HOST_APP_OPERATION_NAME = HOSTNAME + "+" + APPNAME + ":" + OPERATION;
	private static final long ESDR_LATENCY = 50L;
	private static final long ESDR_TIMESTAMP = 1337L;
	private static final double ESDR_FORECAST = 42d;
	private static final double ESDR_SCORE = 0.2d;
	private static final double ESDR_ANOMALY_THRESHOLD = 0.23d;

	private IAnalysisController controller;
	private ListReader<ExtendedStorableDetectionResult> listReader;
	private ListCollectionFilter<OpadOutputData> listCollectionFilter;
	private OpadOutputCompositionFilter ooCompFilter;
	private ExtendedStorableDetectionResult detectionResult;
	private OpadOutputData compositionResult;

	/**
	 * Constructor.
	 */
	public OpadOutputCompositionFilterTest() {}

	/**
	 * Set up the testing environment.
	 * Here we create the filters required for testing and connect them with each other. Finally the controller is run.
	 * 
	 * @throws Exception
	 *             exception that are thrown during the setup.
	 */
	@Before
	public void setUp() throws Exception {
		this.controller = new AnalysisController();
		this.listReader = new ListReader<ExtendedStorableDetectionResult>(new Configuration(), this.controller);
		this.ooCompFilter = new OpadOutputCompositionFilter(new Configuration(), this.controller);
		this.listCollectionFilter = new ListCollectionFilter<OpadOutputData>(new Configuration(), this.controller);

		this.controller.connect(this.listReader, ListReader.OUTPUT_PORT_NAME, this.ooCompFilter, OpadOutputCompositionFilter.INPUT_PORT_NAME_DETECTION_RESULTS);
		this.controller.connect(this.ooCompFilter, OpadOutputCompositionFilter.OUTPUT_PORT_OPAD_DATA,
				this.listCollectionFilter, ListCollectionFilter.INPUT_PORT_NAME);

		this.detectionResult = new ExtendedStorableDetectionResult(ESDR_HOST_APP_OPERATION_NAME, ESDR_LATENCY, ESDR_TIMESTAMP, ESDR_FORECAST, ESDR_SCORE,
				ESDR_ANOMALY_THRESHOLD);

		this.composeOutput();
	}

	/**
	 * All instances are reset to null to have a fresh environment for every test.
	 * 
	 * @throws Exception
	 *             exceptions that are thrown during teardown.
	 */
	@After
	public void tearDown() throws Exception {
		this.controller.terminate();
		this.controller = null;
		this.listReader = null;
		this.ooCompFilter = null;
		this.listCollectionFilter = null;
		this.detectionResult = null;
		this.compositionResult = null;
	}

	private void composeOutput() throws IllegalStateException, AnalysisConfigurationException {
		this.listReader.addObject(this.detectionResult);
		this.controller.run();
		this.compositionResult = this.listCollectionFilter.getList().get(0);
	}

	/**
	 * Test whether the hostname is extracted correctly.
	 */
	@Test
	public void testOpadOutputCompositionHostname() {
		assertEquals(this.compositionResult.getOperationSignature().getHostName(), HOSTNAME);
	}

	/**
	 * Test whether the appname is extracted correctly.
	 */
	@Test
	public void testOpadOutputCompositionAppname() {
		assertEquals(this.compositionResult.getOperationSignature().getApplicationName(), APPNAME);
	}

	/**
	 * Test whether the operation string is extracted correctly.
	 */
	@Test
	public void testOpadOutputCompositionOperation() {
		assertEquals(this.compositionResult.getOperationSignature().getStringSignature(), OPERATION);
	}

	/**
	 * Test whether the latency is correctly transferred to the new object.
	 */
	@Test
	public void testOpadOutputCompositionLatency() {
		assertEquals(this.compositionResult.getResponseTime(), ESDR_LATENCY);
	}

	/**
	 * Test whether the timestamp is correctly transferred to the new object.
	 */
	@Test
	public void testOpadOutputCompositionTimestamp() {
		assertEquals(this.compositionResult.getTimestamp(), ESDR_TIMESTAMP);
	}

	// @Test
	// public void testOpadOutputCompositionForecast() {
	// assertEquals(this.compositionResult.getForecast(), ESDR_FORECAST);
	// }

	/**
	 * Test whether the anomaly score is correctly transferred to the new object.
	 */
	@Test
	public void testOpadOutputCompositionScore() {
		assertEquals(this.compositionResult.getAnomalyScore(), ESDR_SCORE, 0.0d);
	}

	/**
	 * Test whether the anomaly threshold is correctly transferred to the new object.
	 */
	@Test
	public void testOpadOutputCompositionAnomalyThreshold() {
		assertEquals(this.compositionResult.getAnomalyThreshold(), ESDR_ANOMALY_THRESHOLD, 0.0d);
	}
}
