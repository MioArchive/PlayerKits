plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
}

group = "net.javamio"
version = "1.0"
description = "PlayerKits offers multiple personal player kits for your server."

repositories {
    mavenLocal()
    mavenCentral()

    //PaperMC
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(libs.commandapi)
    compileOnly(libs.paper.api)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

tasks {
    jar {
        enabled = false
    }

    shadowJar {
        archiveFileName = "${rootProject.name}-${project.version}.jar"
        archiveClassifier = null


        manifest {
            attributes["Implementation-Version"] = rootProject.version
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
    }

    assemble {
        dependsOn(shadowJar)
    }

    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
    }

    withType<Javadoc>() {
        options.encoding = Charsets.UTF_8.name()
    }

    defaultTasks("build")

    val version = "1.21.4"
    val javaVersion = JavaLanguageVersion.of(21)

    val jvmArgsExternal = listOf(
        "-Dcom.mojang.eula.agree=true"
    )

    runServer {
        minecraftVersion(version)
        runDirectory = rootDir.resolve("run/paper/$version")

        javaLauncher = project.javaToolchains.launcherFor {
            languageVersion = javaVersion
        }

        downloadPlugins {
            url("https://github.com/ViaVersion/ViaVersion/releases/download/5.4.2/ViaVersion-5.4.2.jar")
            url("https://github.com/ViaVersion/ViaBackwards/releases/download/5.4.2/ViaBackwards-5.4.2.jar")
        }

        jvmArgs = jvmArgsExternal
    }
}