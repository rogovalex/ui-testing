package com.yandex.mobile.realty.testing.reporter;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * Custom test results based on Gradle's AllTestResults
 */
class AllTestResults extends CompositeTestResults {
    private final Map<String, PackageTestResults> packages = new TreeMap<String, PackageTestResults>();

    public AllTestResults() {
        super(null);
    }

    @Override
    public String getTitle() {
        return "Test Summary";
    }

    public Collection<PackageTestResults> getPackages() {
        return packages.values();
    }

    @Override
    public String getName() {
        return null;
    }

    public TestResult addTest(String className, String testName, long duration,
        String device, String project, String flavor) {
        PackageTestResults packageResults = addPackageForClass(className);
        TestResult testResult = addTest(
            packageResults.addTest(className, testName, duration, device, project, flavor));

        addDevice(device, testResult);
        addVariant(project, flavor, testResult);

        return testResult;
    }

    public ClassTestResults addTestClass(String className) {
        return addPackageForClass(className).addClass(className);
    }

    private PackageTestResults addPackageForClass(String className) {
        String packageName;
        int pos = className.lastIndexOf(".");
        if (pos != -1) {
            packageName = className.substring(0, pos);
        } else {
            packageName = "";
        }
        return addPackage(packageName);
    }

    private PackageTestResults addPackage(String packageName) {

        PackageTestResults packageResults = packages.get(packageName);
        if (packageResults == null) {
            packageResults = new PackageTestResults(packageName, this);
            packages.put(packageName, packageResults);
        }
        return packageResults;
    }
}