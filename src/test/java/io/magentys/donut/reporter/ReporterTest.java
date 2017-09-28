package io.magentys.donut.reporter;

import io.magentys.donut.adapters.JUnitAdapter;
import io.magentys.donut.gherkin.model.Feature;
import io.magentys.donut.jaxb.XmlUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ReporterTest {

    private JUnitAdapter jUnitAdapter;
    private Reporter reporter;
    private XmlUtils xmlUtils;
    private static final String SINGLE_RESULT_FOLDER_PATH = FileUtils.toFile(ReporterTest.class.getResource("/junit/single_result")).getAbsolutePath();


    @Before
    public void setUp() {
        jUnitAdapter = new JUnitAdapter();
        reporter = new Reporter();
        xmlUtils = new XmlUtils();
    }

    @Test
    public void shouldGenerateSameNumberOfJsonFilesAsNumberOfFeatures() throws Exception {
        List<Feature> features = jUnitAdapter.transform(xmlUtils.unmarshal(SINGLE_RESULT_FOLDER_PATH));
        File reportDir = FileUtils.toFile(ReporterTest.class.getResource("/junit/"));
        reporter.writeJsons(features, reportDir.getAbsolutePath());

        assertTrue(features.size() == 1);
        assertTrue(FileUtils.listFiles(new File(reportDir, Reporter.OUTPUT_FOLDER_NAME), new String[]{"json"}, false).size() == 1);
    }

    @Test
    public void shouldGenerateJsonFilesInBuildDirIfOutputDirIsBlankIfTargetFolderIsPresent() throws Exception {
        List<Feature> features = jUnitAdapter.transform(xmlUtils.unmarshal(SINGLE_RESULT_FOLDER_PATH));
        reporter.writeJsons(features, "");

        assertTrue(features.size() == 1);
        File buildDir = new File("./target");
        assertTrue(FileUtils.listFiles(new File(buildDir, Reporter.OUTPUT_FOLDER_NAME), new String[]{"json"}, false).size() == 1);
    }
}
