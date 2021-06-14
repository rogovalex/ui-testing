package com.yandex.mobile.realty.testing.reporter;

import org.gradle.reporting.ReportRenderer;

import java.io.IOException;

public class CodePanelRenderer extends ReportRenderer<String, SimpleHtmlWriter> {
    @Override
    public void render(String text, SimpleHtmlWriter htmlWriter) throws IOException {
        // Wrap in a <span>, to work around CSS problem in IE
        htmlWriter.startElement("span").attribute("class", "code")
            .startElement("pre").characters(text).endElement()
            .endElement();
    }
}