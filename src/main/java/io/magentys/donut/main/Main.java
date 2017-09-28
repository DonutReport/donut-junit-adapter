package io.magentys.donut.main;

import io.magentys.donut.adapters.JUnitAdapter;
import io.magentys.donut.jaxb.XmlUtils;
import io.magentys.donut.reporter.Reporter;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;

public class Main {
    @Option(name = "-p", aliases = "--junit-result-dir-path", usage = "path of nunit result xml directory", required = true)
    private String resultXmlDirPath;
    @Option(name = "-o", aliases = "--outputdir", usage = "the directory for storing the json reports")
    private String outputDir;

    public static void main(
            String[] args) {

        new Main().doMain(args);
    }

    private void doMain(
            String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            ensureResultXmlDirectory();

            generateDonutJsonFiles();

        } catch (CmdLineException c) {
            System.err.println(c.getMessage());
            System.err.println("usage: Main [options ...]");
            parser.printUsage(System.err);
            System.exit(1);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private void generateDonutJsonFiles() throws Exception {
        XmlUtils xmlUtils = new XmlUtils();
        JUnitAdapter adapter = new JUnitAdapter();
        Reporter reporter = new Reporter();
        reporter.writeJsons(adapter.transform(xmlUtils.unmarshal(resultXmlDirPath)), outputDir);

        if (StringUtils.isBlank(outputDir)) {
            System.out.println("JSON reports saved at location: " + new File(".", Reporter.OUTPUT_FOLDER_NAME).getAbsolutePath());
        } else {
            System.out.println("JSON reports saved at location: " + outputDir);
        }
    }

    private void ensureResultXmlDirectory()
            throws IOException {
        File file = new File(resultXmlDirPath);
        if (!file.exists())
            throw new IOException("The test results xml directory [" + file.getAbsolutePath() + "] does not exist.");

        if (!file.isDirectory() || file.listFiles() == null || file.listFiles().length == 0)
            throw new IOException("The test results xml directory [" + file.getAbsolutePath() + "] is either not a directory or there are no files in it.");
    }
}