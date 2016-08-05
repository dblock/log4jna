# Maven, Ivy, and Gradle Artifacts

Configure your build system to get dependencies from Maven Central.

### Apache Maven

Add the following to your `pom.xml` file.

```xml
<dependencies>
  <dependency>
    <groupId>org.dblock.log4jna</groupId>
    <artifactId>log4jna-api</artifactId>
    <version>2.0</version>
  </dependency>
</dependencies>
```

### Apache Ivy

Add the following to your `ivy.xml` file.

```xml
<dependencies>
  <dependency org="org.dblock.log4jna" name="log4jna-api"  rev="2.0" />
</dependencies>
```

### Gradle

Add the following to your `build.gradle` file

```gradle
dependencies {
  compile group: 'org.dblock.log4jna', name: 'log4jna-api', version: '2.0'
}
```