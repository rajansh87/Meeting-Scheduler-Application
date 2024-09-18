plugins {
	java
	id("org.springframework.boot") version "2.5.12"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")

	//	for redis support
	implementation("org.springframework.boot:spring-boot-starter-data-redis")

	// for kafka support
	implementation("org.springframework.kafka:spring-kafka")

//	implementation("org.springframework.kafka:spring-kafka:2.7.12")
//	implementation("org.apache.kafka:kafka-clients:2.7.2")


	//	for webclients support
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("io.netty:netty-all")



	implementation("com.mysql:mysql-connector-j:8.0.31")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
