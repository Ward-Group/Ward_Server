plugins {
	java
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.ward"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}

	all{
		exclude(group = "commons-logging", module= "commons-logging")
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	implementation("com.auth0:java-jwt:4.4.0")
	implementation("commons-validator:commons-validator:1.8.0")
	implementation ("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.1") // 쿼리 파라미터 로그 남기기, 배포용엔 제외
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.mysql:mysql-connector-j")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	//selenium
	implementation("org.seleniumhq.selenium:selenium-java:4.6.0")
	implementation("org.jsoup:jsoup:1.17.1")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
