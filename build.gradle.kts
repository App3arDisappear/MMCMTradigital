plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "edu.mmcm.tradigital"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    version = "23"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation("org.mongodb:mongodb-driver-sync:4.11.1")
    implementation("org.slf4j:slf4j-nop:2.0.12")
}

application {
    mainClass.set("edu.mmcm.tradigital.MainApp")
}