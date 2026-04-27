plugins {
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(libs.guava)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

// Removemos a configuração global 'mainClass' pois agora temos vários pontos de entrada.

// --- TAREFAS CUSTOMIZADAS DE EXECUÇÃO ---

tasks.register<JavaExec>("runFabrica") {
    group = "Concorrente A2"
    description = "Inicia o Servidor da Fábrica (Porta 8080)"
    mainClass.set("org.concorrente.FabricaApp")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("runLoja1") {
    group = "Concorrente A2"
    description = "Inicia a Loja 1 (Porta 8081)"
    mainClass.set("org.concorrente.LojaApp")
    classpath = sourceSets["main"].runtimeClasspath
    args("1", "8081") // Passa os parâmetros: ID da Loja e Porta
}

tasks.register<JavaExec>("runLoja2") {
    group = "Concorrente A2"
    description = "Inicia a Loja 2 (Porta 8082)"
    mainClass.set("org.concorrente.LojaApp")
    classpath = sourceSets["main"].runtimeClasspath
    args("2", "8082")
}

tasks.register<JavaExec>("runLoja3") {
    group = "Concorrente A2"
    description = "Inicia a Loja 3 (Porta 8083)"
    mainClass.set("org.concorrente.LojaApp")
    classpath = sourceSets["main"].runtimeClasspath
    args("3", "8083")
}

tasks.register<JavaExec>("runClientes") {
    group = "Concorrente A2"
    description = "Inicia as 20 Threads de Clientes"
    mainClass.set("org.concorrente.ClienteApp")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}