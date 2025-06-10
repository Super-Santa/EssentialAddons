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

val modVersion = "2.0.0"
val releaseVersion = "${modVersion}+${libs.versions.minecraft.get()}"
version = releaseVersion
group = "me.supersanta"

dependencies {
    minecraft(libs.minecraft)
    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.parchment.get()}@zip")
    })

    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)
    modImplementation(libs.fabric.kotlin)

    modImplementation(libs.carpet)
    modCompileOnly(libs.lithium) {
        exclude(group = "com.github.2No2Name", module = "McTester")
    }

    include(modImplementation(libs.arcade.commands.get())!!)
    include(modImplementation(libs.arcade.event.registry.get())!!)
    include(modImplementation(libs.arcade.events.server.get())!!)
    include(modImplementation(libs.arcade.extensions.get())!!)
    include(modImplementation(libs.arcade.utils.get())!!)
    include(modImplementation(libs.permissions.get())!!)
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
                "minecraft_dependency" to libs.versions.minecraft.get().replaceAfterLast('.', "x"),
                "fabric_loader_dependency" to libs.versions.fabric.loader.get(),
            ))
        }
    }

    jar {
        from("LICENSE")
    }

    publishMods {
        file = remapJar.get().archiveFile
        changelog = """
        - Rewrote the entire mod, with plenty of bug fixes and feature improvements
        - Reworked the command system - commands and now registered separately so disabling a conflicting command
        will not allow the other command to work properly
          - As a result of this rework the previous camera command name config has removed and replaced
          with a carpet rule, `cameraModeCommandName` - changing this no longer requires a restart
        - Reworked `reloadFakePlayerActions` into `savePlayerActions` which now saves and restarts player
        actions upon logging off and logging in, not just for fake players on server restart anymore
        - Reworked `commandWarp` to allow setting multiple named warps which are now saved between sessions
        - Reworked all the configs - unfortunately this will break previous configs, but the newer
        system is much nicer and robust
        - Implemented `sensitiveBamboo` and `sensitiveSugarCane` to make the respective plants behave like cactus,
        making it break next to solid blocks
        - Updated some of the rule descriptions to be more descriptive and accurate
        - Fixed `combinePotionDuration` not working correctly
        - Fixed `commandFly` not allowing flight
        - Fixed `cakeAlwaysEat` not making the players hand swing
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

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(project.components.getByName("java"))
        }
    }
}