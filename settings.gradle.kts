pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            url = uri(
                "https://devrepo.kakao.com/nexus/repository/kakaomap-releases/"
            )
        }
    }
}

rootProject.name = "DayTodo"

include(":app")
include(":core")
include(":uikit")
include(":domain")
include(":data")

include(":feature:auth")
include(":feature:home")
include(":feature:magazine")
include(":feature:course")
include(":feature:save")
include(":feature:calendar")
include(":feature:today")
include(":feature:record")
include(":feature:mypage")
