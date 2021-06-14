plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    jcenter() // Warning: this repository is going to shut down soon
}

dependencies {
    implementation("com.android.tools.ddms:ddmlib:27.1.2")
    implementation("com.android.tools:sdk-common:27.1.2")
    implementation("com.android.tools.build:builder-test-api:4.1.2")
    implementation("com.google.code.gson:gson:2.8.6")
}