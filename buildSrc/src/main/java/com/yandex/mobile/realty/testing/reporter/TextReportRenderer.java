package com.yandex.mobile.realty.testing.reporter;

import org.gradle.internal.ErroringAction;
import org.gradle.internal.IoActions;

import java.io.File;
import java.io.Writer;

/**
 * Custom TextReportRenderer based on Gradle's TextReportRenderer
 */
public abstract class TextReportRenderer<T> {
    /**
     * Renders the report for the given model to a writer.
     */
    protected abstract void writeTo(T model, Writer out) throws Exception;

    /**
     * Renders the report for the given model to a file.
     */
    public void writeTo(final T model, File file) {
        IoActions.writeTextFile(file, "utf-8", new ErroringAction<Writer>() {
            @Override
            protected void doExecute(Writer writer) throws Exception {
                writeTo(model, writer);
            }
        });
    }
}