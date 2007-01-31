<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet [
<!ENTITY nl "&#xd;&#xa;">
<!ENTITY pad2 "  ">
<!ENTITY pad4 "    ">
<!ENTITY pad5 "     ">
<!ENTITY pad6 "      ">
]>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text"/>

<xsl:variable name="numErrors" select="count(log/record [level='SEVERE']/file)" />
<xsl:variable name="numWarnings" select="count(log/record [level='WARNING']/file)" />
<xsl:variable name="numInfo" select="count(log/record [level='INFO']/file)" />

<xsl:template match="/">
  <xsl:text>PDS Validation Tool Report&nl;</xsl:text>
  <xsl:for-each select="log/record [level='CONFIG']">
    <xsl:text>&pad2;</xsl:text><xsl:value-of select="message"/><xsl:text>&nl;</xsl:text>
  </xsl:for-each>
  
  <xsl:text>&nl;Parameter Settings:&nl;</xsl:text>
  <xsl:for-each select="log/record [level='PARAMETER']">
    <xsl:text>&pad2;</xsl:text><xsl:value-of select="message"/><xsl:text>&nl;</xsl:text>
  </xsl:for-each>
  
  <xsl:text>&nl;Summary:&nl;</xsl:text>
  <xsl:text>&pad2;</xsl:text><xsl:value-of select="count(log/record [level='NOTIFICATION' and message!='SKIP'])" /> of <xsl:value-of select="count(log/record [level='NOTIFICATION'])" /> validated, <xsl:value-of select="count(log/record [level='NOTIFICATION' and message='SKIP'])" /><xsl:text> skipped&nl;</xsl:text>
  <xsl:text>&pad2;</xsl:text><xsl:value-of select="count(log/record [level='NOTIFICATION' and message='PASS'])" /> of <xsl:value-of select="count(log/record [level='NOTIFICATION' and (message='PASS' or message='FAIL')])" /> passed<xsl:text>&nl;</xsl:text>

  <xsl:text>&nl;Total Messages:&nl;</xsl:text>
  <xsl:text>&nl;ERROR&pad2;WARN&pad2;INFO&nl;</xsl:text>
  <xsl:value-of select="substring('&pad5;', string-length($numErrors) + 1)" /><xsl:value-of select="$numErrors" />
  <xsl:value-of select="substring('&pad6;', string-length($numWarnings) + 1)" /><xsl:value-of select="$numWarnings" />
  <xsl:value-of select="substring('&pad6;', string-length($numInfo) + 1)" /><xsl:value-of select="$numInfo" />

  <xsl:text>&nl;&nl;Message Counts by File:&nl;</xsl:text>
  <xsl:text>&nl;ERROR&pad2;WARN&pad2;INFO&pad2;FILE&nl;</xsl:text>
  <xsl:for-each select="log/record[level='NOTIFICATION']">
    <xsl:sort select="file" />
    <xsl:variable name="file" select="file" />
    <xsl:variable name="numFileErrors" select="count(//record [level='SEVERE' and file=$file])" />
    <xsl:variable name="numFileWarnings" select="count(//record [level='WARNING' and file=$file])" />
    <xsl:variable name="numFileInfo" select="count(//record [level='INFO' and file=$file])" />
    <xsl:value-of select="substring('&pad5;', string-length($numFileErrors) + 1)" /><xsl:value-of select="$numFileErrors" />
    <xsl:value-of select="substring('&pad6;', string-length($numFileWarnings) + 1)" /><xsl:value-of select="$numFileWarnings" />
    <xsl:value-of select="substring('&pad6;', string-length($numFileInfo) + 1)" /><xsl:value-of select="$numFileInfo" />
    <xsl:text>&pad2;</xsl:text><xsl:value-of select="$file" /><xsl:text>&nl;</xsl:text>
  </xsl:for-each>
  
  <xsl:text>&nl;End Of Report&nl;</xsl:text>
</xsl:template>

</xsl:stylesheet>