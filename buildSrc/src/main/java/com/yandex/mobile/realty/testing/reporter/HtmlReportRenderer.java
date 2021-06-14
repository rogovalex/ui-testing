package com.yandex.mobile.realty.testing.reporter;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;

import org.gradle.reporting.ReportRenderer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

public class HtmlReportRenderer {
    private final Set<URL> resources = new HashSet<URL>();

    public void requireResource(URL resource) {
        resources.add(resource);
    }

    public <T> TextReportRenderer<T> renderer(final ReportRenderer<T, SimpleHtmlWriter> renderer) {
        return renderer(new TextReportRendererImpl<T>(renderer));
    }

    public <T> TextReportRenderer<T> renderer(final TextReportRendererImpl<T> renderer) {
        return new TextReportRenderer<T>() {
            @Override
            protected void writeTo(T model, Writer out) throws Exception {
                renderer.writeTo(model, out);
            }

            @Override
            public void writeTo(T model, File file) {
                super.writeTo(model, file);
                for (URL resource : resources) {
                    String name = substringAfterLast(resource.getPath(), "/");
                    String type = substringAfterLast(resource.getPath(), ".");
                    File destFile = new File(file.getParentFile(), String.format("%s/%s", type, name));
                    if (!destFile.exists()) {
                        destFile.getParentFile().mkdirs();
                        try {
                            URLConnection urlConnection = resource.openConnection();
                            urlConnection.setUseCaches(false);
                            InputStream inputStream = null;
                            try {
                                inputStream = urlConnection.getInputStream();
                                OutputStream outputStream = null;
                                try {
                                    outputStream = new BufferedOutputStream(
                                        new FileOutputStream(destFile));
                                    ByteStreams.copy(inputStream, outputStream);
                                } finally {
                                    if (outputStream != null) {
                                        outputStream.close();
                                    }
                                }
                            } finally {
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        };
    }

    private static class TextReportRendererImpl<T> extends TextReportRenderer<T> {
        private final ReportRenderer<T, SimpleHtmlWriter> delegate;

        private TextReportRendererImpl(ReportRenderer<T, SimpleHtmlWriter> delegate) {
            this.delegate = delegate;
        }

        @Override
        protected void writeTo(T model, Writer writer) throws Exception {
            SimpleHtmlWriter htmlWriter = new SimpleHtmlWriter(writer, "");
            htmlWriter.startElement("html");
            delegate.render(model, htmlWriter);
            htmlWriter.endElement();
        }
    }

    /**
     * Returns the substring of a string that follows the last
     * occurence of a separator.
     *
     * <p>Largely replicated and slightly updated from the
     * {@code apache.commons.lang.StringUtils} method of the same name.
     *
     * @param string    the String to get a substring from, may not be null
     * @param separator the String to search for, may not be null
     * @return the substring after the last occurrence of the separator or an
     * empty string if not found.
     */
    public static String substringAfterLast(String string, String separator) {
        Preconditions.checkNotNull(string);
        Preconditions.checkNotNull(separator);
        int pos = string.lastIndexOf(separator);
        if (pos == -1 || pos == (string.length() - separator.length())) {
            return "";
        }
        return string.substring(pos + separator.length());
    }

}