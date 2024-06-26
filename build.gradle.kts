import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.8.0" // kotlin!!
    kotlin("kapt") version "1.7.10" // kotlin/annotation!!
    // if you are using libraries, but they return errors when loading ingame, you may want to use this
     id("com.github.johnrengelman.shadow") version "7.1.2"
    // and if you do, also build your jars like this ./gradle clean build shadowJar
}

group = "me.wanttobee"
version = "1.0"

// Ensures that both compileJava and compileKotlin tasks will be using Java 1.8,
// aligning Java versions with Kotlin. (because kotlin was automatically be 1.8, while java stayed 1.7 still)
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()

    maven {
        url = uri("https://jitpack.io") // Use JitPack as a resolver for GitHub releases
    }

    maven {
        name = "citizens-repo"
        url = uri("https://maven.citizensnpcs.co/repo")
    }

    maven {
        name = "spigotmc-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        // here you can find the latest's spigot versions
        // https://hub.spigotmc.org/nexus/content/repositories/snapshots/org/spigotmc/spigot-api/
    }
}

dependencies {
    // compileOnly() will only be used while compiling the jar but will not be included in the jar (for example the spigot api is not needed in the jar, de server already has that implemented)
    // inplementation() will also compile, but also include it in the jar you are creating
    implementation("com.github.WantToBeeMe:CommandTree:2.0.0")
    implementation("com.github.WantToBeeMe:EverythingItems:2.3.0")
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.22") // kotlin!!
    compileOnly("org.spigotmc:plugin-annotations:1.2.3-SNAPSHOT") // annotations!!
    kapt("org.spigotmc:plugin-annotations:1.2.3-SNAPSHOT") // annotations!!

    compileOnly("net.citizensnpcs:citizens-main:2.0.34-SNAPSHOT") {
        exclude(group = "*", module = "*")
    }
}

// Configures a task to process resources, such as a plugin.yml file,
// with version information for the project.
tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}


// // this piece of code makes it so when building a jar with ./gradle clean build shadowJar
// // that the 2 jars you will get are ProjectName.jar and ProjectName-1.0.jar
// // here the ProjectName.jar is the correct one which you should be using
tasks.withType<ShadowJar> {
    archiveClassifier.set("")
    archiveVersion.set("")
    archiveBaseName.set(project.name)
}

