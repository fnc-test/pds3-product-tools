Although this document may contain more information in the future it currently only covers how to generate and incorporate the grammar output. The following command will generate the HTML output from the grammar source:

[node: ~] java -classpath antlr-2.7.6.jar antlr.Tool -html ./product-tools
/src/resources/grammar/odl.g

The above command will produce two HTML files:

ODLLexer.html
ODLParser.html

In order to provide a somewhat consistent look-and-feel with the rest of the documentation and to provide this content in the project's PDF version of the documentation, the content of the HTML files need to be copied and pasted into corresponding xdoc files. The corresponding xdoc files, respectively, are:

./product-tools/xdocs/grammar/index-lexer.xml
./product-tools/xdocs/grammar/index-parser.xml

Copy and paste the content of the *.html files into the <source> blocks of the corresponding *.xml files. The "maven site" command will generate the documentation including the corresponding PDF document.
