package com.yandex.mobile.realty.testing.reporter;

import org.gradle.api.tasks.testing.TestResult;

public abstract class TestResultModel {
    public static final DurationFormatter DURATION_FORMATTER = new DurationFormatter();

    public abstract TestResult.ResultType getResultType();

    public abstract long getDuration();

    public abstract String getTitle();

    public String getFormattedDuration() {
        return DURATION_FORMATTER.format(getDuration());
    }

    public String getStatusClass() {
        switch (getResultType()) {
            case SUCCESS:
                return "success";
            case FAILURE:
                return "failures";
            case SKIPPED:
                return "skipped";
            default:
                throw new IllegalStateException();
        }
    }

    public String getFormattedResultType() {
        switch (getResultType()) {
            case SUCCESS:
                return "passed";
            case FAILURE:
                return "failed";
            case SKIPPED:
                return "ignored";
            default:
                throw new IllegalStateException();
        }
    }
}