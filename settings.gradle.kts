/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
val snapshotVersion : String? = System.getenv("COMPOSE_SNAPSHOT_ID")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        /*maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            // Do not change the username below. It should always be "mapbox" (not your username).
            credentials {
                username = "mapbox"
                // Use the secret token stored in gradle.properties as the password
                password = "sk.eyJ1Ijoibm9mZmVwZXJmZWt0IiwiYSI6ImNtMWo3ZHozeDAxYXcycXM4ZDY5dDFmemcifQ.13Sk4w5Py1TAmt6DyeXEzg"
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }*/
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            // Do not change the username below. It should always be "mapbox" (not your username).
            credentials.username = "mapbox"
            // Use the secret token stored in gradle.properties as the password
            credentials.password = "sk.eyJ1Ijoibm9mZmVwZXJmZWt0IiwiYSI6ImNtMWo3ZHozeDAxYXcycXM4ZDY5dDFmemcifQ.13Sk4w5Py1TAmt6DyeXEzg"
            authentication.create<BasicAuthentication>("basic")
        }
        
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        snapshotVersion?.let {
            println("https://androidx.dev/snapshots/builds/$it/artifacts/repository/") 
            maven { url = uri("https://androidx.dev/snapshots/builds/$it/artifacts/repository/") }
        }

        google()
        mavenCentral()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                username = "mapbox"
                password = "sk.eyJ1Ijoibm9mZmVwZXJmZWt0IiwiYSI6ImNtMWo3ZHozeDAxYXcycXM4ZDY5dDFmemcifQ.13Sk4w5Py1TAmt6DyeXEzg"
            }
        }
        
    }
}
rootProject.name = "Jetchat"
include(":app")

