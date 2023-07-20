pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven( "https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TravelBook"
include(":app")