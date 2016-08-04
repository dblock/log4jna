# CI With AppVeyor and Maven

This article shows how to configure AppVeyor as a Continuous Integration server for Java projects needing to access Windows libraries or APIs.

There are better CI solutions out there if you don't need Windows DLL's and APIs.

We use 3 configurations for CI, SNAPSHOT deployments to Sonatype's repository and Releases deployments to Maven Central via Sonatype's OSS.

1. [Getting Maven for AppVeyor](#maven) (Tahnks to [Yegor Bugayenko's post](http://www.yegor256.com/2015/01/10/windows-appveyor-maven.html) that quick started all this.)
2. [Configuring The Default Build](#b1).
3. [Configuring The Deploy Build](#b2).
4. [Configuring The Release Build](#b3).

## <a name="maven"></a>Getting Maven for AppVeyor

First we need some variables to use Maven in the build scripts.

```yaml
...

environment:
  MAVEN_HOME: C:\maven\apache-maven-3.3.9
  PATH: C:\maven\apache-maven-3.3.9\bin;$(JAVA_HOME)\bin;C:\Program Files (x86)\Windows Kits\10\bin\x64;$(PATH)
  MAVEN_OPTS: -Xmx4g
  JAVA_OPTS: -Xmx4g
  M2: $(USERPROFILE)\.m2

...

```

We download and install maven

```yaml

...

install:

  # Install Maven if not present
  - ps: |
      Add-Type -AssemblyName System.IO.Compression.FileSystem
      if (!(Test-Path -Path "C:\maven" )) {
        (new-object System.Net.WebClient).DownloadFile(
          'http://www.us.apache.org/dist/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.zip',
          'C:\maven-bin.zip'
        )
        [System.IO.Compression.ZipFile]::ExtractToDirectory("C:\maven-bin.zip", "C:\maven")

      }

...

```

And we save Maven and the local .m2 directory for next build.

```yaml
...

cache:
  # Cache Maven and m.2
  - '%MAVEN_HOME%'
  - '%M2%'

...
``` 

 ## <a name="b1"></a>Log4JNA Default Build

 This is a very straight forward build, we build whenever te code or other files changes in the `master` branch.
 
 This is the project that we monitor to see that nothing is broken on a Pull Request or branch merge.
  
 Create a new AppVeyor project and name it Log4JNA Default.
 
 
 