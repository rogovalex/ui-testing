package com.yandex.mobile.realty.testing.reporter;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Custom PackageTestResults based on Gradle's PackageTestResults
 */
class PackageTestResults extends CompositeTestResults {

    private static final String DEFAULT_PACKAGE = "default-package";
    private final String name;
    private final Map<String, ClassTestResults> classes = new TreeMap<String, ClassTestResults>();

    public PackageTestResults(String name, AllTestResults model) {
        super(model);
        this.name = name.length() == 0 ? DEFAULT_PACKAGE : name;
    }

    @Override
    public String getTitle() {
        return name.equals(DEFAULT_PACKAGE) ? "Default package" : String.format("Package %s", name);
    }

    @Override
    public String getName() {
        return name;
    }

    public Collection<ClassTestResults> getClasses() {
        return classes.values();
    }

    public TestResult addTest(String className, String testName, long duration,
        String device, String project, String flavor) {
        ClassTestResults classResults = addClass(className);
        TestResult testResult = addTest(
            classResults.addTest(testName, duration, device, project, flavor));

        addDevice(device, testResult);
        addVariant(project, flavor, testResult);

        return testResult;
    }

    public ClassTestResults addClass(String className) {
        ClassTestResults classResults = classes.get(className);
        if (classResults == null) {
            classResults = new ClassTestResults(className, this);
            classes.put(className, classResults);
        }
        return classResults;
    }
}