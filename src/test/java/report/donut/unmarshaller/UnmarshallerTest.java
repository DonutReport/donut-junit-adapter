package report.donut.unmarshaller;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import report.donut.jaxb.XmlUtils;
import report.donut.junit.model.Testsuite;

import java.util.List;

public class UnmarshallerTest {

    private static final String MULTIPLE_RESULTS_FOLDER_ABS = FileUtils.toFile(UnmarshallerTest.class.getResource("/junit/multiple_results")).getAbsolutePath();
    private static final String MULTIPLE_RESULTS_FOLDER_REL = "src/test/resources/junit/multiple_results";
    private final String sample1Path = FileUtils.toFile(UnmarshallerTest.class.getResource("/junit/single_result/TestResult.xml")).getAbsolutePath();
    private final XmlUtils xmlUtils = new XmlUtils();


    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenFilePathIsProvided() throws Exception {
        xmlUtils.unmarshal(sample1Path);
    }

    @Test
    public void shouldReturnTestsuitesWhenAFolderAbsolutePathIsProvided() throws Exception {
        List<Testsuite> testSuites = xmlUtils.unmarshal(MULTIPLE_RESULTS_FOLDER_ABS);
        Assert.assertTrue(testSuites.size() == 2);
    }

    @Test
    public void shouldReturnTestsuitesWhenProvidedARelativePathToResultFolder() throws Exception {
        List<Testsuite> testSuites = xmlUtils.unmarshal(MULTIPLE_RESULTS_FOLDER_REL);
        Assert.assertTrue(testSuites.size() == 2);
    }

}
