package com.yandex.mobile.realty.testing.reporter;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Custom ClassTestResults based on Gradle's ClassTestResults
 */
class ClassTestResults extends CompositeTestResults {

    private final String name;
    private final PackageTestResults packageResults;
    private final Set<TestResult> results = new TreeSet<TestResult>();
    private final StringBuilder standardOutput = new StringBuilder();
    private final StringBuilder standardError = new StringBuilder();

    public ClassTestResults(String name, PackageTestResults packageResults) {
        super(packageResults);
        this.name = name;
        this.packageResults = packageResults;
    }

    @Override
    public String getTitle() {
        return String.format("Class %s", name);
    }

    @Override
    public String getName() {
        return name;
    }

    public String getSimpleName() {
        int pos = name.lastIndexOf(".");
        if (pos != -1) {
            return name.substring(pos + 1);
        }
        return name;
    }

    public PackageTestResults getPackageResults() {
        return packageResults;
    }

    public Map<String, Map<String, TestResult>> getTestResultsMap() {
        Map<String, Map<String, TestResult>> map = Maps.newHashMap();
        for (TestResult result : results) {
            String device = result.getDevice();

            Map<String, TestResult> deviceMap = map.get(device);
            if (deviceMap == null) {
                deviceMap = Maps.newHashMap();
                map.put(device, deviceMap);
            }

            deviceMap.put(result.getName(), result);
        }

        return map;
    }

    public CharSequence getStandardError() {
        return standardError;
    }

    public CharSequence getStandardOutput() {
        return standardOutput;
    }

    public TestResult addTest(String testName, long duration,
        String device, String project, String flavor) {
        TestResult test = new TestResult(testName, duration, device, project, flavor, this);
        results.add(test);

        addDevice(device, test);
        addVariant(project, flavor, test);

        return addTest(test);
    }

    public void addStandardOutput(String textContent) {
        standardOutput.append(textContent);
    }

    public void addStandardError(String textContent) {
        standardError.append(textContent);
    }
}