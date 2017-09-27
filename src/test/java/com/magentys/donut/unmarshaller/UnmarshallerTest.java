package com.magentys.donut.unmarshaller;

import com.magentys.donut.jaxb.XmlUtils;
import com.magentys.donut.junit.model.Testsuite;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class UnmarshallerTest {

    private static final String MULTIPLE_RESULTS_FOLDER_ABS = FileUtils.toFile(UnmarshallerTest.class.getResource("/junit/multiple_results")).getAbsolutePath();
    private static final String MULTIPLE_RESULTS_FOLDER_REL = "junit/multiple_results";
    private final String sample1Path = FileUtils.toFile(UnmarshallerTest.class.getResource("/junit/single_result/TestResult.xml")).getAbsolutePath();
    private final XmlUtils xmlUtils = new XmlUtils();


    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenFilePathIsProvided() throws Exception {
        xmlUtils.unmarshal(sample1Path);
    }

    @Test
    public void shouldReturnTestsuitesWhenAFolderAbsolutePathIsProvided() throws Exception {
        Object object = xmlUtils.unmarshal(MULTIPLE_RESULTS_FOLDER_ABS);

        Assert.assertTrue(object instanceof List);

        List<Testsuite> testsuites = (List<Testsuite>) object;
        Assert.assertTrue(testsuites.size() == 2);
    }

    @Test
    public void shouldThrowAnExceptionWhenProvidedARelativePathToResultFolder() throws Exception {
        try {

            xmlUtils.unmarshal(MULTIPLE_RESULTS_FOLDER_REL);

        } catch (Exception e) {
            assertTrue("", e.getMessage().contains("Provided path isn't a directory or the path isn't absolute."));
        }

    }

}
