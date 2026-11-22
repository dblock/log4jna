# Release To Maven Central

To make a Log4JNA release:

1. Run the on-demand [Release GitHub Workflow](https://github.com/dblock/log4jna/actions/workflows/Release.yml).
2. Publish the validated deployment from the [Central Portal](https://central.sonatype.com/publishing/deployments).
3. Check whether the release made it to [Maven Central](https://central.sonatype.com/namespace/org.dblock.log4jna).
4. Update [CHANGELOG.md](CHANGELOG.md) and any `<version>x.y.z</version>` references with the newly released version number, commit and push your changes.
5. Run the on-demand [Site GitHub Workflow](https://github.com/dblock/log4jna/actions/workflows/Site.yml).
