package com.yandex.mobile.realty.testing.reporter;

import com.android.annotations.NonNull;

/**
 * VariantTestResults to accumulate results per variant
 */
class VariantTestResults extends CompositeTestResults {

    private final String name;

    public VariantTestResults(@NonNull String name, CompositeTestResults parent) {
        super(parent);
        this.name = name;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }
}