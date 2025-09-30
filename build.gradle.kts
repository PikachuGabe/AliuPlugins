import com.aliucord.gradle.AliucordExtension
import com.android.build.gradle.BaseExtension

buildscript {
    repositories {
        google()
        mavenCentral()
        // Aliucords Maven repo which contains our tools and dependencies
        maven("https://maven.aliucord.com/snapshots")
        // Shitpack which still contains some Aliucord dependencies for now. TODO: Remove
        maven("https://jitpack.io")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        // Aliucord gradle plugin which makes everything work and builds plugins
        classpath("com.aliucord:gradle:main-SNAPSHOT")
        classpath("com.aliucord:jadb:1.2.1-SNAPSHOT")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.aliucord.com/snapshots")
    }
}

fun Project.aliucord(configuration: AliucordExtension.() -> Unit) = extensions.getByName<AliucordExtension>("aliucord").configuration()

fun Project.android(configuration: BaseExtension.() -> Unit) = extensions.getByName<BaseExtension>("android").configuration()

subprojects {
    apply(plugin = "com.android.library")
    apply(plugin = "com.aliucord.gradle")

    // Fill out with your info
    aliucord {
        author("pikachugabe", 701095070870274139L)
        updateUrl.set("https://raw.githubusercontent.com/PikachuGabe/AliuPlugins/builds/updater.json")
        buildUrl.set("https://raw.githubusercontent.com/PikachuGabe/AliuPlugins/builds/%s.zip")
    }

    android {
        compileSdkVersion(30)

        defaultConfig {
            minSdk = 24
            targetSdk = 30
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

    }

    dependencies {
        val discord by configurations
        val compileOnly by configurations

        discord("com.discord:discord:aliucord-SNAPSHOT")
        compileOnly("com.aliucord:Aliucord:main-SNAPSHOT")
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
