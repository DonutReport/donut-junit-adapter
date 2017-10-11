package report.donut.adapters;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import report.donut.gherkin.model.Element;
import report.donut.gherkin.model.Feature;
import report.donut.gherkin.model.Step;
import report.donut.jaxb.XmlUtils;
import report.donut.junit.model.Testcase;
import report.donut.junit.model.Testsuite;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class JUnitAdapterTest {

    private JUnitAdapter jUnitAdapter;
    private XmlUtils xmlUtils;
    private static final String SINGLE_RESULT_FOLDER_PATH = FileUtils.toFile(JUnitAdapterTest.class.getResource("/junit/single_result")).getAbsolutePath();
    private static final String MULTIPLE_RESULTS_FOLDER_PATH = FileUtils.toFile(JUnitAdapterTest.class.getResource("/junit/multiple_results")).getAbsolutePath();
    private static final String SINGLE_RESULT_WITH_FAILURE_FOLDER_PATH = FileUtils.toFile(JUnitAdapterTest.class.getResource("/junit/single_result_with_failure")).getAbsolutePath();
    private static final String SINGLE_RESULT_WITH_NO_TEST_CASES = FileUtils.toFile(JUnitAdapterTest.class.getResource("/junit/single_result_with_no_test_cases")).getAbsolutePath();
    private static final String RSPEC_SINGLE_RESULT_WITH_FAILURE_FOLDER_PATH = FileUtils.toFile(JUnitAdapterTest.class.getResource("/junit/serverspec_rspec_junit/single_result_with_failures")).getAbsolutePath();
    private static final String RSPEC_SINGLE_RESULT_WITH_NO_FAILURES_FOLDER_PATH = FileUtils.toFile(JUnitAdapterTest.class.getResource("/junit/serverspec_rspec_junit/single_result_no_failures")).getAbsolutePath();

    @Before
    public void setUp() {
        jUnitAdapter = new JUnitAdapter();
        xmlUtils = new XmlUtils();
    }

    // Behavior
    @Test
    public void shouldBeAbleToTransformTheTestSuiteToFeaturesList() throws Exception {
        List<Testsuite> testsuites = xmlUtils.unmarshal(SINGLE_RESULT_FOLDER_PATH);

        List<Feature> features = jUnitAdapter.transform(testsuites);
        assertTrue("One feature should be present.", features.size() == 1);
    }

    @Test
    public void shouldBeAbleToTransformMultipleTestSuitesToFeaturesList() throws Exception {
        List<Testsuite> testsuites = xmlUtils.unmarshal(MULTIPLE_RESULTS_FOLDER_PATH);

        List<Feature> features = jUnitAdapter.transform(testsuites);
        assertTrue("Two features should be present", features.size() == 2);
    }

    @Test
    public void shouldBeAbleToTransformResultXmlWithFailure() throws Exception {
        List<Testsuite> testsuites = xmlUtils.unmarshal(SINGLE_RESULT_WITH_FAILURE_FOLDER_PATH);
        List<Feature> features = jUnitAdapter.transform(testsuites);
        List<Element> scenarios = features.get(0).getElements();
        Element scenario = scenarios.get(0);
        Step step = scenario.getSteps().get(0);

        assertTrue(features.size() == 1);
        assertTrue(scenarios.size() == 1);
        assertTrue(scenario.getName().equals("shouldBeAbleToTransformTheTestSuiteToFeaturesList"));
        assertTrue(step.getResult().getErrorMessage().contains("Error message:"));
        assertTrue(step.getResult().getStatus().equals("failed"));
    }

    @Test
    public void shouldBeAbleToTransformRSpecJUnitResultXmlWithFailure() throws Exception {
        List<Testsuite> testsuites = xmlUtils.unmarshal(RSPEC_SINGLE_RESULT_WITH_FAILURE_FOLDER_PATH);
        List<Feature> features = jUnitAdapter.transform(testsuites);
        List<Element> scenarios = features.get(0).getElements();
        Element scenario = scenarios.get(0);
        Step step = scenario.getSteps().get(0);

        assertTrue(features.size() == 1);
        assertTrue(scenarios.size() == 7);
        assertTrue(scenario.getName().equals("MyApp Container installed Apps Apache HTTP should have copied my app files"));
        assertTrue(step.getResult().getErrorMessage().contains("Error message:"));
        assertTrue(step.getResult().getStatus().equals("failed"));
    }

    @Test
    public void shouldBeAbleToTransformRSpecJUnitResultXmlWithNoFailures() throws Exception {
        List<Testsuite> testsuites = xmlUtils.unmarshal(RSPEC_SINGLE_RESULT_WITH_NO_FAILURES_FOLDER_PATH);
        List<Feature> features = jUnitAdapter.transform(testsuites);
        List<Element> scenarios = features.get(0).getElements();
        Element scenario = scenarios.get(0);
        Step step = scenario.getSteps().get(0);

        assertTrue(features.size() == 1);
        assertTrue(scenarios.size() == 6);
        assertTrue(scenario.getName().equals("MyApp Container installed Apps Apache HTTP should have copied my app files"));
        assertTrue(step.getResult().getStatus().equals("passed"));
    }

    @Test
    public void shouldThrowAnExceptionWhenATestSuiteHasNoTestCases() throws Exception {

        List<Testsuite> testsuites = xmlUtils.unmarshal(SINGLE_RESULT_WITH_NO_TEST_CASES);

        try {
            jUnitAdapter.transform(testsuites);
            fail("Exception wasn't thrown.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("The test suite doesn't have any test cases."));
        }
    }

    // Units

    @Test
    public void shouldMakeElementsWithPredefinedValues() throws Exception {
        Testcase testCase = buildTestCase();
        String name = testCase.getName();
        assertNotNull(name);

        List<Testcase> testCases = Collections.singletonList(testCase);
        List<Element> elements = jUnitAdapter.makeElements(testCases);

        assertTrue(elements.size() == 1);
        Element element = elements.get(0);
        assertTrue(name.equals(element.getName()));
        assertTrue(JUnitAdapter.UNIT_TEST_KEYWORD.equals(element.getKeyword()));
        assertTrue(JUnitAdapter.UNIT_TEST_TYPE.equals(element.getType()));
    }

    /**
     * Returns the first test case node from a sample xml document kept at SINGLE_RESULT_FOLDER_PATH.
     *
     * @return TestCase
     * @throws Exception Thrown if the path provided is that of a file or not an absolute path
     */
    private Testcase buildTestCase() throws Exception {

        List<Testsuite> testsuites = xmlUtils.unmarshal(SINGLE_RESULT_FOLDER_PATH);
        return testsuites.get(0).getTestcase().get(0);
    }
}
