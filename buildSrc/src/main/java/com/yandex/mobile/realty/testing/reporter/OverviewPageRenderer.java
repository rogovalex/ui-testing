package com.yandex.mobile.realty.testing.reporter;

import java.io.IOException;

/**
 * Custom OverviewPageRenderer based on Gradle's OverviewPageRenderer
 */
class OverviewPageRenderer extends PageRenderer<AllTestResults> {

    public OverviewPageRenderer(ReportType reportType) {
        super(reportType);
    }

    @Override
    protected void registerTabs() {
        addFailuresTab();
        if (!getResults().getPackages().isEmpty()) {
            addTab("Packages", new ErroringAction<SimpleHtmlWriter>() {
                @Override
                protected void doExecute(SimpleHtmlWriter writer) throws IOException {
                    renderPackages(writer);
                }
            });
        }
        addTab("Classes", new ErroringAction<SimpleHtmlWriter>() {
            @Override
            public void doExecute(SimpleHtmlWriter htmlWriter) throws IOException {
                renderClasses(htmlWriter);
            }
        });
    }

    @Override
    protected void renderBreadcrumbs(SimpleHtmlWriter htmlWriter) {
    }

    private void renderPackages(SimpleHtmlWriter htmlWriter) throws IOException {
        htmlWriter.startElement("table");
        htmlWriter.startElement("thead");
        htmlWriter.startElement("tr");
        htmlWriter.startElement("th").characters("Package").endElement();
        htmlWriter.startElement("th").characters("Tests").endElement();
        htmlWriter.startElement("th").characters("Failures").endElement();
        htmlWriter.startElement("th").characters("Duration").endElement();
        htmlWriter.startElement("th").characters("Success rate").endElement();
        htmlWriter.endElement();
        htmlWriter.endElement();
        htmlWriter.startElement("tbody");
        for (PackageTestResults testPackage : getResults().getPackages()) {
            htmlWriter.startElement("tr");
            htmlWriter.startElement("td").attribute("class", testPackage.getStatusClass());
            htmlWriter.startElement("a").attribute("href", String.format("%s.html", testPackage.getFilename(reportType))).characters(testPackage.getName()).endElement();
            htmlWriter.endElement();
            htmlWriter.startElement("td").characters(Integer.toString(testPackage.getTestCount())).endElement();
            htmlWriter.startElement("td").characters(Integer.toString(testPackage.getFailureCount())).endElement();
            htmlWriter.startElement("td").characters(testPackage.getFormattedDuration()).endElement();
            htmlWriter.startElement("td").attribute("class", testPackage.getStatusClass()).characters(testPackage.getFormattedSuccessRate()).endElement();
            htmlWriter.endElement();
        }
        htmlWriter.endElement();
        htmlWriter.endElement();
    }

    private void renderClasses(SimpleHtmlWriter htmlWriter) throws IOException {
        htmlWriter.startElement("table");
        htmlWriter.startElement("thead");
        htmlWriter.startElement("tr");
        htmlWriter.startElement("th").characters("Class").endElement();
        htmlWriter.startElement("th").characters("Tests").endElement();
        htmlWriter.startElement("th").characters("Failures").endElement();
        htmlWriter.startElement("th").characters("Duration").endElement();
        htmlWriter.startElement("th").characters("Success rate").endElement();
        htmlWriter.endElement();
        htmlWriter.endElement();
        htmlWriter.startElement("tbody");

        for (PackageTestResults testPackage : getResults().getPackages()) {
            for (ClassTestResults testClass : testPackage.getClasses()) {
                htmlWriter.startElement("tr");
                htmlWriter.startElement("td").attribute("class", testClass.getStatusClass()).endElement();
                htmlWriter.startElement("a").attribute("href", String.format("%s.html", testClass.getFilename(reportType))).characters(testClass.getName()).endElement();
                htmlWriter.startElement("td").characters(Integer.toString(testClass.getTestCount())).endElement();
                htmlWriter.startElement("td").characters(Integer.toString(testClass.getFailureCount())).endElement();
                htmlWriter.startElement("td").characters(testClass.getFormattedDuration()).endElement();
                htmlWriter.startElement("td").attribute("class", testClass.getStatusClass()).characters(testClass.getFormattedSuccessRate()).endElement();
                htmlWriter.endElement();
            }
        }
        htmlWriter.endElement();
        htmlWriter.endElement();
    }
}