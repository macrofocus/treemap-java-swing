plugins {
    java
    application
}

tasks.compileJava {
    options.release.set(11)
}

repositories {
    maven { url = uri("https://www.macrofocus.com/archiva/repository/public/") }
    maven { url = uri("https://www.macrofocus.com/archiva/repository/snapshots/") }
    mavenCentral()
}

val frameworkAttribute = Attribute.of("mkui", String::class.java)
configurations.all {
    afterEvaluate {
        attributes.attribute(frameworkAttribute, "swing")
    }
}

dependencies {
    val kotlinVersion: String by project
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    val macrofocusVersion: String by project
//    implementation("org.macrofocus", "macrofocus-common", "$macrofocusVersion", "jvmRuntime")
//    implementation("org.molap", "molap", "$macrofocusVersion", "jvmRuntime")
//    implementation("org.macrofocus", "mkui", "$macrofocusVersion", "swingRuntime")
//    implementation("com.treemap", "treemap", "$macrofocusVersion", "swingRuntime")

    implementation("org.macrofocus:macrofocus-common:$macrofocusVersion")
    implementation("org.molap:molap:$macrofocusVersion")
    implementation("org.macrofocus:mkui:$macrofocusVersion")
    implementation("com.treemap:treemap:$macrofocusVersion")
}

application {
    mainClass.set("Demo")
}