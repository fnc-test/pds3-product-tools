# PDS3 Product Tools Library
Java library of software classes for manipulating PDS product labels. The software is packaged in a JAR file.

# Documentation
The documentation for the latest release of the PDS3 Product Tools, including release notes, installation and operation of the software are online at https://nasa-pds-incubator.github.io/pds3-product-tools/.

If you would like to get the latest documentation, including any updates since the last release, you can execute the "mvn site:run" command and view the documentation locally at http://localhost:8080.

# Build
The software can be compiled and built with the "mvn compile" command but in order 
to create the JAR file, you must execute the "mvn compile jar:jar" command. 

In order to create a complete distribution package, execute the 
following commands: 

```
% mvn site
% mvn package
```
# Operational Release

A release candidate should be created after the community has determined that a release should occur. These steps should be followed when generating a release candidate and when completing the release.

## Update Version Numbers

Update pom.xml for the release version or use the Maven Versions Plugin, e.g.:
```
VERSION=4.1.0
mvn versions:set -DnewVersion=$VERSION
```

## Update Changelog
Update Changelog using [Github Changelog Generator](https://github.com/github-changelog-generator/github-changelog-generator). Note: Make sure you set `$CHANGELOG_GITHUB_TOKEN` in your `.bash_profile` or use the `--token` flag.
```
github_changelog_generator --future-release v$VERSION
```

## Commit Changes
Commit changes using following template commit message:
```
git commit -m "[RELEASE] PDS3 Product Tools v$VERSION"
```

## Build and Deploy Software to [Sonatype Maven Repo](https://repo.maven.apache.org/maven2/gov/nasa/pds/).

```
mvn clean site deploy -P release
```

Note: If you have issues with GPG, be sure to make sure you've created your GPG key, sent to server, and have the following in your `~/.m2/settings.xml`:
```
<profiles>
  <profile>
    <activation>
      <activeByDefault>true</activeByDefault>
    </activation>
    <properties>
      <gpg.executable>gpg</gpg.executable>
      <gpg.keyname>KEY_NAME</gpg.keyname>
      <gpg.passphrase>KEY_PASSPHRASE</gpg.passphrase>
    </properties>
  </profile>
</profiles>

```

## Push Tagged Release
```
git tag v$VERSION
git push --tags
```

## Deploy Site to Github Pages

From cloned repo:
```
git checkout gh-pages

# Create specific version site
mv target/site $VERSION
rm -fr target

# Sync latest version to ops 
rsync -av $VERSION/* .
git add .
git commit -m "Deploy v$VERSION docs"
git push origin gh-pages
```

## Update Versions For Development

Update `pom.xml` with the next SNAPSHOT version either manually or using Github Versions Plugin, e.g.:
```
git checkout master
VERSION=4.2.0-SNAPSHOT
mvn versions:set -DnewVersion=$VERSION
git add pom.xml
git commit -m "Update version for $VERSION development"
git push -u origin master
```

## Complete Release in Github
Currently the process to create more formal release notes and attach Assets is done manually through the [Github UI](https://github.com/NASA-PDS-Incubator/pds3-product-tools/releases/new) but should eventually be automated via script.


# Snapshot Release

Deploy software to Sonatype SNAPSHOTS Maven repo:

```
# Operational release
mvn clean site deploy
```

# Maven JAR Dependency Reference

## Operational Releases
https://search.maven.org/search?q=g:gov.nasa.pds%20AND%20a:pds3-product-tools&core=gav

## Snapshots
https://oss.sonatype.org/content/repositories/snapshots/gov/nasa/pds/pds3-product-tools/

If you want to access snapshots, add the following to your `~/.m2/settings.xml`:
```
<profiles>
  <profile>
     <id>allow-snapshots</id>
     <activation><activeByDefault>true</activeByDefault></activation>
     <repositories>
       <repository>
         <id>snapshots-repo</id>
         <url>https://oss.sonatype.org/content/repositories/snapshots</url>
         <releases><enabled>false</enabled></releases>
         <snapshots><enabled>true</enabled></snapshots>
       </repository>
     </repositories>
   </profile>
</profiles>
```
