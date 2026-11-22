# Release To Maven Central

To make a Log4JNA release:

1. Run the on-demand [Release GitHub Workflow](https://github.com/dblock/log4jna/actions/workflows/Release.yml).
2. Publish the validated deployment from the [Central Portal](https://central.sonatype.com/publishing/deployments).
3. Check whether the release made it to [Maven Central](https://central.sonatype.com/namespace/org.dblock.log4jna).
4. Date the released section of [CHANGELOG.md](CHANGELOG.md) and add a new *Next* section, then update the version in the Maven `<version>`, Ivy `rev` and Gradle examples and the `log4jna-win32dll` zip link in [README.md](README.md) and [MAVENIVYGRADLE.md](MAVENIVYGRADLE.md), their copies in `src/site/markdown`, and the sample `log4j2.xml` comments. Open a pull request with these changes.
5. Run the on-demand [Site GitHub Workflow](https://github.com/dblock/log4jna/actions/workflows/Site.yml).
