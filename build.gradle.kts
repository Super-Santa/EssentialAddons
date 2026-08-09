plugins {
    val jvmVersion = libs.versions.fabric.kotlin.get()
        .split("+kotlin.")[1]
        .split("+")[0]

    kotlin("jvm").version(jvmVersion)
    kotlin("plugin.serialization").version(jvmVersion)
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.mod.publish)
    `maven-publish`
    java
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://maven.parchmentmc.org/")
    maven("https://maven.supersanta.me/snapshots")
    maven("https://api.modrinth.com/maven")
    maven("https://jitpack.io")
}

val modVersion = "2.5.1"
val releaseVersion = "${modVersion}+${libs.versions.minecraft.get()}"
version = releaseVersion
group = "me.supersanta"

dependencies {
    minecraft(libs.minecraft)

    implementation(libs.fabric.loader)
    implementation(libs.fabric.api)
    implementation(libs.fabric.kotlin)

    implementation(libs.carpet)
    compileOnly(libs.lithium) {
        exclude(group = "com.github.2No2Name", module = "McTester")
    }

    implementation(libs.bundles.arcade)
    include(libs.bundles.arcade)
}

java {
    withSourcesJar()
}

tasks {
    processResources {
        inputs.property("version", releaseVersion)
        filesMatching("fabric.mod.json") {
            expand(mutableMapOf(
                "version" to releaseVersion,
                "minecraft_dependency" to "~${libs.versions.minecraft.get()}",
                "fabric_api_dependency" to libs.versions.fabric.api.get(),
                "fabric_kotlin_dependency" to libs.versions.fabric.kotlin.get(),
                "carpet_dependency" to libs.versions.carpet.get(),
            ))
        }
    }

    jar {
        from("LICENSE")
    }

    publishMods {
        file = jar.get().archiveFile
        changelog = """
        - Update dependencies
        """.trimIndent()
        type = STABLE
        modLoaders.add("fabric")

        displayName = "EssentialAddons $modVersion for ${libs.versions.minecraft.get()}"
        version = releaseVersion

        modrinth {
            accessToken = providers.environmentVariable("MODRINTH_API_KEY")
            projectId = "3WQaouc1"
            minecraftVersions.add(libs.versions.minecraft)

            requires {
                id = "P7dR8mSH"
            }
            requires {
                id = "Ha28R6CL"
            }
            requires {
                id = "TQTTVgYE"
            }
        }
    }
}

loom {
    runConfigs {
        getByName("server") {
            runDirectory.set(file("run/server"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(project.components.getByName("java"))
        }
    }
}