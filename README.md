# user-bll


## quick start

1. clone
    ```
    git clone --recurse-submodules <url>
    mvn clean generate-sources
    ```

2. run

    - maven
    ```
    mvn spring-boot:run -Dspring-boot.run.arguments="--spring.config.additional-location=src/main/resources/application-local.yml --spring.profiles.active=local"
    ```
    - java
    ```
    mvn package -Dmaven.test.skip=true
    java -XX:+UseG1GC -jar -Dspring.config.additional-location=src/main/resources/application-local.yml -Dspring.profiles.active=local target/order-base-0.0.1-SNAPSHOT.jar
    ```
    - intellij
      `edit configuration` -> `environment variables` set as
   ```
   spring.profiles.active=local;spring.config.additional-location=src/main/resources/application-local.yml
   ```

## other

mvn clean generate-sources

加入這行
```
            <plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-resources-plugin</artifactId>
				<version>3.3.1</version>
				<configuration>
					<resources>
						<resource>
							<directory>${project.basedir}/target/generated-sources/openapi/src/main/java</directory>
							<includes>**/*</includes>
						</resource>
						<resource>
							<directory>${project.basedir}/target/generated-sources/openapi/src/test/java</directory>
							<includes>**/*</includes>
						</resource>
					</resources>
				</configuration>
			</plugin>
```


