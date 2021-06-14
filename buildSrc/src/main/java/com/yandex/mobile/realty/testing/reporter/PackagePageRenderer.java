package com.yandex.mobile.realty.testing.reporter;

import java.io.IOException;

/**
 * Custom PackagePageRenderer based on Gradle's PackagePageRenderer
 */
public class PackagePageRenderer extends PageRenderer<PackageTestResults> {

    public PackagePageRenderer(ReportType reportType) {
        super(reportType);
    }

    @Override
    protected String getTitle() {
        return getModel().getTitle();
    }

    @Override
    protected void renderBreadcrumbs(SimpleHtmlWriter htmlWriter) throws IOException {
        htmlWriter.startElement("div").attribute("class", "breadcrumbs");
        htmlWriter.startElement("a").attribute("href", "index.html").characters("all").endElement();
        htmlWriter.characters(String.format(" > %s", getResults().getName()));
        htmlWriter.endElement();
    }

    private void renderClasses(SimpleHtmlWriter htmlWriter) throws IOException {
        htmlWriter.startElement("table");
        htmlWriter.startElement("thread");
        htmlWriter.startElement("tr");

        htmlWriter.startElement("th").characters("Class").endElement();
        htmlWriter.startElement("th").characters("Tests").endElement();
        htmlWriter.startElement("th").characters("Failures").endElement();
        htmlWriter.startElement("th").characters("Duration").endElement();
        htmlWriter.startElement("th").characters("Success rate").endElement();

        htmlWriter.endElement();
        htmlWriter.endElement();

        for (ClassTestResults testClass : getResults().getClasses()) {
            htmlWriter.startElement("tr");
            htmlWriter.startElement("td").attribute("class", testClass.getStatusClass());
            htmlWriter.startElement("a").attribute("href", String.format("%s.html", testClass.getFilename(reportType))).characters(testClass.getSimpleName()).endElement();
            htmlWriter.endElement();
            htmlWriter.startElement("td").characters(Integer.toString(testClass.getTestCount())).endElement();
            htmlWriter.startElement("td").characters(Integer.toString(testClass.getFailureCount())).endElement();
            htmlWriter.startElement("td").characters(testClass.getFormattedDuration()).endElement();
            htmlWriter.startElement("td").attribute("class", testClass.getStatusClass()).characters(testClass.getFormattedSuccessRate()).endElement();
            htmlWriter.endElement();
        }
        htmlWriter.endElement();
    }

    @Override
    protected void registerTabs() {
        addFailuresTab();
        addTab("Classes", new ErroringAction<SimpleHtmlWriter>() {
            @Override
            public void doExecute(SimpleHtmlWriter htmlWriter) throws IOException {
                renderClasses(htmlWriter);
            }
        });
    }
}