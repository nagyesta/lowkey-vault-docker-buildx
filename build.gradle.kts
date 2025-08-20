plugins {
    id("java")
    id("io.toolebox.git-versioner") version "1.6.7"
}

group = "com.github.nagyesta"

buildscript {
    fun optionalPropertyString(name: String): String {
        return if (project.hasProperty(name)) {
            project.property(name) as String
        } else {
            ""
        }
    }

    fun dockerAbortGroups(name: String): String {
        return if (project.hasProperty(name)) {
            "all"
        } else {
            ""
        }
    }

    // Define versions in a single place
    extra.apply {
        set("gitToken", optionalPropertyString("githubToken"))
        set("gitUser", optionalPropertyString("githubUser"))
        set("repoUrl", "https://github.com/nagyesta/lowkey-vault-docker-buildx")
        set("licenseName", "MIT License")
        set("licenseUrl", "https://raw.githubusercontent.com/nagyesta/lowkey-vault-docker-buildx/main/LICENSE")
        set("maintainerId", "nagyesta")
        set("maintainerName", "Istvan Zoltan Nagy")
        set("maintainerUrl", "https://github.com/nagyesta/")
    }
}

versioner {
    startFrom {
        major = 0
        minor = 0
        patch = 1
    }
    match {
        major = "{major}"
        minor = "{minor}"
        patch = "{patch}"
    }
    pattern {
        //force using the version from the Lowkey Vault jar
        pattern = libs.versions.lowkeyVault.get()
    }
    git {
        authentication {
            https {
                token = project.extra.get("gitToken").toString()
            }
        }
    }
    tag {
        prefix = "v"
        useCommitMessage = true
    }
}

versioner.apply()

repositories {
    mavenCentral()
}

val lowkeyVault = configurations.create("lowkeyVault")

dependencies {
    lowkeyVault(libs.lowkey.vault.app)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.register<Copy>("copyDockerfile") {
    group = "Docker"
    description = "Copies the Dockerfile to the Docker build directory."
    inputs.file("src/docker/Dockerfile")
    outputs.file(layout.buildDirectory.file("docker/Dockerfile").get().asFile)
    from(file("src/docker/Dockerfile"))
    into(layout.buildDirectory.dir("docker").get().asFile)
    rename {
        "Dockerfile"
    }
    dependsOn(":build")
}

tasks.register<Copy>("prepareDocker") {
    group = "Docker"
    description = "Prepares the Docker build directory."
    inputs.file(lowkeyVault.asFileTree.singleFile)
    outputs.file(layout.buildDirectory.file("docker/lowkey-vault.jar").get().asFile)
    from(lowkeyVault.asFileTree.singleFile)
    into(layout.buildDirectory.dir("docker").get().asFile)
    rename {
        "lowkey-vault.jar"
    }
    dependsOn(":copyDockerfile")
}

tasks.register<Exec>("createDockerBuildx") {
    group = "Docker"
    description = "Creates a Docker Buildx instance."
    workingDir = layout.buildDirectory.dir("docker").get().asFile
    commandLine = listOf(
        "docker", "buildx",
        "create",
        "--use"
    )
    dependsOn(":prepareDocker")
}

tasks.register<Exec>("buildDocker") {
    group = "Docker"
    description = "Builds the Docker image."
    inputs.dir(layout.buildDirectory.dir("docker").get().asFile)
    workingDir = layout.buildDirectory.dir("docker").get().asFile
    commandLine = listOf(
        "docker", "buildx",
        "build",
        "--platform", "linux/arm64,linux/amd64",
        "--pull",
        "-t", "nagyesta/lowkey-vault:${libs.versions.lowkeyVault.get()}-ubi9-minimal",
        "."
    )
    dependsOn(":createDockerBuildx")
}

tasks.register<Exec>("buildDockerPush") {
    group = "Docker"
    description = "Builds and pushes the Docker image."
    inputs.dir(layout.buildDirectory.dir("docker").get().asFile)
    workingDir = layout.buildDirectory.dir("docker").get().asFile
    commandLine = listOf(
        "docker", "buildx",
        "build",
        "--platform", "linux/arm64,linux/amd64",
        "--push",
        "-t", "nagyesta/lowkey-vault:${libs.versions.lowkeyVault.get()}-ubi9-minimal",
        "."
    )
}
