pluginManagement {
    repositories {
        google()             // Repositório de plugins do Google (necessário para o plugin google-services)
        mavenCentral()       // Repositório Maven Central (para dependências gerais)
        gradlePluginPortal() // Repositório para plugins Gradle
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // Impede o uso de repositórios específicos em projetos
    repositories {
        google()       // Repositório do Google (necessário para dependências como Firebase)
        mavenCentral() // Repositório Maven Central
    }
}

rootProject.name = "My Application" // Nome do seu projeto
include(":app") // Inclui o módulo 'app' no projeto

 