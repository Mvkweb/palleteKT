plugins {
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    `maven-publish`
}

group = "com.palletekt"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Kotlin Standard Library
    implementation(kotlin("stdlib"))
    
    // Paper API
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    
    // Adventure API (included in Paper, but explicit for clarity)
    compileOnly("net.kyori:adventure-api:4.15.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.15.0")
    
    // Testing
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
}

kotlin {
    jvmToolchain(17)
}

tasks {
    test {
        useJUnitPlatform()
    }
    
    shadowJar {
        archiveBaseName.set("PalleteKT")
        archiveClassifier.set("")
        
        relocate("kotlin", "com.palletekt.palletekt.libs.kotlin")
    }
    
    build {
        dependsOn(shadowJar)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.palletekt"
            artifactId = "palletekt"
            version = project.version.toString()
            
            from(components["kotlin"])
            
            pom {
                name.set("PalleteKT")
                description.set("A powerful Kotlin library for creating smooth, animated gradient text in Minecraft plugins")
                url.set("https://github.com/palletekt/palletekt")
                
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                
                developers {
                    developer {
                        id.set("palletekt")
                        name.set("PalleteKT")
                    }
                }
            }
        }
    }
}
