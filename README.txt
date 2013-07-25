The Product Tools Library is a set of software classes for manipulating 
PDS product labels. The software is packaged in a JAR file.

The software can be compiled with the "mvn compile" command but in order 
to create the JAR file, you must execute the "mvn package" command. The 
documentation including release notes and installation of the software 
should be online at http://pds-cm.jpl.nasa.gov/product-tools/. 
If it is not accessible, you can execute the "mvn site:run" command and 
view the documentation locally at http://localhost:8080.

In order to create a complete package for distribution, execute the 
following commands: 

% mvn site
% mvn package

Note: The build in the Mac OS X environment is flakey and occasionally 
produces the following error when as the result of compiling the Antlr
output:

[INFO] Compilation failure
/Users/shardman/dev/pds/tools/product-tools/target/generated-sources/antlr3/gov/nasa/pds/tools/label/antlr/ODLParser.java:[7559,19] code too large

Upgrading to Antlr 3.3 did not resolve the issue. The good news is that 
it works just fine in a Linux environment. In addition, one of the unit tests 
fails (CharacterCheckerTest) in Mac OS X where it succeeds in Linux.
