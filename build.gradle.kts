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
        generateFileSystem(5, 5, 10, 100_000)
    }
}

tasks.register("genRegularFS") {
    doLast {
        generateFileSystem(5, 7, 50, 250_000)
    }
}

tasks.register("genBigFS") {
    doLast {
        generateFileSystem(6, 7, 100, 500_000)
    }
}

tasks.register("deleteFS") {
    doLast {
        file(Path.of("./benchmarks/fs")).deleteRecursively()
    }
}

fun generateFileSystem(maxDirs: Int, maxDepth: Int, maxFiles: Int, maxSize: Int) {
    project.exec {
        setWorkingDir("./benchmarks/")
        val fs = file(Path.of("./benchmarks/fs"))
        if (fs.exists()) {
            fs.deleteRecursively()
        }
        Files.createDirectories(fs.toPath())
        commandLine("./gen_random_filesystem.sh", "./fs",
            maxDirs, maxDepth, maxFiles, maxSize)
    }

}