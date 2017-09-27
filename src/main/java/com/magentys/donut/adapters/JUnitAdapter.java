package com.magentys.donut.adapters;

import com.magentys.donut.gherkin.model.*;
import com.magentys.donut.junit.model.Failure;
import com.magentys.donut.junit.model.Testcase;
import com.magentys.donut.junit.model.Testsuite;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JUnitAdapter {

    private static final String PASSED = "passed";
    private static final String FAILED = "failed";
    static final String UNIT_TEST_TYPE = "unit-test";
    static final String UNIT_TEST_KEYWORD = "Unit Test";


    public List<Feature> transform(List<Testsuite> testsuites) throws Exception {

        List<Feature> features = new ArrayList<>();
        for (Testsuite testsuite : testsuites) {
            features.add(makeFeature(testsuite));
        }
        return features;
    }

    private Feature makeFeature(Testsuite testsuite) throws Exception {

        Feature feature = new Feature();
        feature.setName(StringUtils.defaultString(testsuite.getName(), "Without feature"));
        feature.setDescription("");
        feature.setId(StringUtils.defaultString(testsuite.getId(), ""));
        feature.setKeyword("Feature");
        feature.setLine(0);
        feature.setTags(Collections.singletonList(new Tag("@complete", 0)));
        feature.setUri("Test URI");
        feature.setElements(makeElements(testsuite.getTestcase()));

        return feature;
    }

    List<com.magentys.donut.gherkin.model.Element> makeElements(List<Testcase> testCases) throws Exception {
        List<com.magentys.donut.gherkin.model.Element> elements = new ArrayList<>();

        for (Testcase testCase : testCases) {
            com.magentys.donut.gherkin.model.Element element = new com.magentys.donut.gherkin.model.Element();

            element.setName(testCase.getName());

            element.setDescription("");
            element.setLine(0);
            element.setKeyword(UNIT_TEST_KEYWORD);

            element.setId(testCase.toString());
            element.setSteps(makeSteps(testCase));
            element.setType(UNIT_TEST_TYPE);

            elements.add(element);
        }

        if (elements.isEmpty()) {
            throw new Exception("The test suite doesn't have any test cases.");
        }
        return elements;
    }

    private List<Step> makeSteps(Testcase testCase) throws Exception {
        Step step = new Step();
        long duration = (long) (Double.valueOf(testCase.getTime()) * 1000);

        String status = makeStatus(testCase.getFailure());

        if (status.equals(PASSED)) {
            step.setResult(new Result(PASSED, duration, null));
        } else {
            step.setResult(new Result(FAILED, duration, makeErrorMessage(testCase.getFailure())));
        }

        step.setKeyword("");
        step.setLine(0);
        step.setName(testCase.getName());
        step.setMatch(new Match(testCase.getClassname()));

        return Collections.singletonList(step);
    }

    private String makeErrorMessage(List<Failure> failures) {
        try {
            StringBuilder builder = new StringBuilder();

            for (Failure failure : failures) {
                builder.append(" [");
                builder.append(failure.getContent());
                builder.append("] ");
            }

            return "Error message: " + StringUtils.normalizeSpace(builder.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private String makeStatus(List<Failure> failures) throws Exception {

        if (failures.isEmpty()) {
            return PASSED;
        }
        return FAILED;
    }

}
