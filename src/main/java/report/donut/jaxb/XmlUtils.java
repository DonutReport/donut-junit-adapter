package report.donut.jaxb;

import org.apache.commons.io.FileUtils;
import report.donut.junit.model.ObjectFactory;
import report.donut.junit.model.Testsuite;
import report.donut.junit.model.Testsuites;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XmlUtils {

    private final ObjectFactory factory = new ObjectFactory();

    public List<Testsuite> unmarshal(String folderPath) throws Exception {

        File file = new File(folderPath);
        if (file.isFile()) {
            throw new Exception("Provided path is that of a file. Please provide path of the folder containing result xmls.");
        }

        if (file.isDirectory()) {
            List<File> files = (List<File>) FileUtils.listFiles(file, new String[]{"xml"}, true);
            List<Testsuite> testsuites = new ArrayList<>();
            for (File resultFile : files) {

                Object obj = unmarshal(resultFile);

                if (obj != null) {
                    testsuites.addAll((List<Testsuite>) obj);
                }else{
                    throw new Exception("Unmarshalling returned null.");
                }
            }
            return testsuites;
        }

        throw new Exception("Provided path isn't a directory or the path isn't absolute.");
    }

    private Object unmarshal(File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(factory.getClass());
        Unmarshaller unmarshaller = context.createUnmarshaller();

        Object obj = unmarshaller.unmarshal(file);

        if (obj instanceof Testsuite) {
            return Collections.singletonList(obj);
        }

        if (obj instanceof Testsuites) {
            return ((Testsuites) obj).getTestsuite();
        }
        return null;
    }
}
