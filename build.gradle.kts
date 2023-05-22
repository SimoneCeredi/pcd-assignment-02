import java.nio.file.Files
import java.nio.file.Path

plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.vertx:vertx-core:4.4.1")
    implementation("io.vertx:vertx-web:4.4.1")
    implementation("io.vertx:vertx-web-client:4.4.1")
    implementation("io.reactivex.rxjava3:rxjava:3.1.4")
}

java {
    sourceCompatibility = JavaVersion.VERSION_19
    targetCompatibility = JavaVersion.VERSION_19
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--enable-preview")
}

tasks.withType<Test>().configureEach {
    jvmArgs("--enable-preview")
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs("--enable-preview")
}

tasks.register("genSmallFS") {
    doLast {
        genFileSystem(10, 5, 10, 10_000)
    }
}

tasks.register("genRegularFS") {
    doLast {
        genFileSystem(10, 5, 10, 15_000)
    }
}

tasks.register("genBigFS") {
    doLast {
        genFileSystem(5, 5, 15, 500000)
    }
}

task<Exec>("deleteFS") {
    doLast {
        Files.deleteIfExists(Path.of("./benchmarks/fs"))
    }
}

fun genFileSystem(maxDirs: Int, maxDepth: Int, maxFiles: Int, maxSize: Int) {
    project.exec {
        val fs = file("./benchmarks/fs")
        if (fs.exists()) {
            fs.delete()
        }
        Files.createDirectories(fs.toPath())
        setWorkingDir("./benchmarks/")
        commandLine("./gen_random_filesystem.sh", "./fs",
            maxDirs, maxDepth, maxFiles, maxSize)
    }
}