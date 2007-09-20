:: Copyright 2006-2007, by the California Institute of Technology.
:: ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
:: Any commercial use must be negotiated with the Office of Technology Transfer
:: at the California Institute of Technology.
::
:: This software is subject to U. S. export control laws and regulations
:: (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
:: is subject to U.S. export control laws and regulations, the recipient has
:: the responsibility to obtain export licenses or other export authority as
:: may be required before exporting such information to foreign countries or
:: providing access to foreign nationals.


:: Batch file that allows easy execution of the Validation Tool (VTool)
:: without the need to set the CLASSAPTH or having to type in that long java
:: command (java gov.nasa.pds.tools.VTool ...)

@ECHO OFF

:: Set to the correct location of the jar file
:: The equals symbol '=' should not be surrounded by blank spaces or the batch
:: file will not work

SET jarpath=c:\product-tools-X.X.X\bin\product-tools-X.X.X-app.jar

:: Executes the JVM to run VTool. Arguments are passed to VTool via
:: the '%*' symbol

java -jar %jarpath% %*
