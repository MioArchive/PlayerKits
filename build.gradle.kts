plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
}

repositories {
    mavenLocal()
    mavenCentral()
    //PaperMC
    maven("https://repo.papermc.io/repository/maven-public/")
    //InvUI
    maven("https://repo.xenondevs.xyz/releases")
}

java {
    disableAutoTargetJvm()
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

dependencies {
    implementation(libs.paper.api)
    implementation(libs.commandapi)
    implementation(libs.invui)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

group = "net.javamio.playerkits"
version = "1.0"
description = "A personal user kit's plugin."

tasks {
    jar {
        enabled = false
    }

    shadowJar {
        archiveFileName = "${rootProject.name}-${project.version}.jar"
        archiveClassifier = null

        relocate("dev.jorel.commandapi", "net.javamio.playerkits.shaded.commandapi")
        relocate("xyz.xenondevs.invui", "net.javamio.playerkits.shaded.invui")

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

    val version = "1.21.1"
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
            url("https://github.com/JorelAli/CommandAPI/releases/download/9.5.3/CommandAPI-9.5.3.jar")
        }

        jvmArgs = jvmArgsExternal
    }
}