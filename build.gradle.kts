plugins {
    java
    application
}

tasks.compileJava {
    options.release.set(21)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    maven { url = uri("https://maven.macrofocus.com/releases") }
    mavenCentral()

    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev/")

    google()
}

// Picks the swing variants of the multiplatform modules (see "Multiple Targets Plan.md")
val frameworkAttribute = Attribute.of("mkui", String::class.java)
configurations.all {
    attributes.attribute(frameworkAttribute, "swing")
}

dependencies {
    val localDependencies = project.findProperty("localDependencies") as String?
    if(localDependencies != null && localDependencies.toBoolean()) {
        implementation(project(":macrofocus-common"))
        implementation(project(":molap"))
        implementation(project(":mkui"))
        implementation(project(":treemap"))
    } else {
        val macrofocusVersion = project.property("macrofocusVersion") as String
        implementation("org.macrofocus:macrofocus-common:$macrofocusVersion")
        implementation("org.molap:molap:$macrofocusVersion")
        implementation("org.mkui:mkui-swing:$macrofocusVersion")
        implementation("com.treemap:treemap-swing:$macrofocusVersion")
    }
}

application {
    mainClass.set("Demo")
}

distributions {
    main {
//        distributionBaseName.set("someName")
        contents {
            from(".") {
                exclude(".gradle/**")
                exclude("build/**")
                // The application of an unzipped distribution, made again by the build
                exclude("bin/**", "lib/**")
            }
            // Gradle 9 normalizes the permissions in archives (0644): ./gradlew must stay executable
            filesMatching("**/gradlew") { permissions { unix("755") } }

//            from("TreeMap API for Java-Swing Developer Guide.pdf")

//            from("../treemap/build/dokka/html/") {
//                into("dokka")
//            }
        }
    }
}