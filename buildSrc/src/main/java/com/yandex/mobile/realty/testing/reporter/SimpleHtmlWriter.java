package com.yandex.mobile.realty.testing.reporter;

import java.io.IOException;
import java.io.Writer;

/**
 * <p>A streaming HTML writer.</p>
 */
public class SimpleHtmlWriter extends SimpleMarkupWriter {

    public SimpleHtmlWriter(Writer writer) throws IOException {
        this(writer, null);
    }

    public SimpleHtmlWriter(Writer writer, String indent) throws IOException {
        super(writer, indent);
        writeHtmlHeader();
    }

    private void writeHtmlHeader() throws IOException {
        writeRaw("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
    }
}