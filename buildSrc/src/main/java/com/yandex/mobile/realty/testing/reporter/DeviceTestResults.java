package com.yandex.mobile.realty.testing.reporter;

import com.android.annotations.NonNull;

/**
 * DeviceTestResults to accumulate results per device.
 */
class DeviceTestResults extends CompositeTestResults {

    private final String name;

    public DeviceTestResults(@NonNull String name, CompositeTestResults parent) {
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