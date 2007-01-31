<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet [
<!ENTITY nl "&#xd;&#xa;">
<!ENTITY pad2 "  ">
<!ENTITY pad4 "    ">
<!ENTITY pad6 "      ">
]>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text"/>

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

  <xsl:text>&nl;Errors Found:&nl;</xsl:text>
  <xsl:for-each select="log/record [level='SEVERE']">
    <xsl:sort select="message" />
    <xsl:variable name="errorMessage" select="message" />
    
    <xsl:if test="not(preceding-sibling::record[message=$errorMessage])">
      <xsl:variable name="record" select="//record[message=$errorMessage]" />
      <xsl:text>&pad2;ERROR&pad2;</xsl:text><xsl:value-of select="$errorMessage" /><xsl:text>&nl;</xsl:text>
      
      <xsl:if test="$record[file]">
        <xsl:text>&pad2;</xsl:text>Example: <xsl:if test="$record/line">line <xsl:value-of select="$record/line" /> of </xsl:if><xsl:value-of select="$record/file" /><xsl:text>&nl;</xsl:text>
      </xsl:if>
      
      <xsl:text>&pad2;</xsl:text><xsl:value-of select="count(//record[message=$errorMessage and level='SEVERE'])" /><xsl:text> occurrence(s)&nl;</xsl:text>
    </xsl:if>
  </xsl:for-each>
  
  <xsl:text>&nl;Warnings Found:&nl;</xsl:text>
  <xsl:for-each select="log/record [level='WARNING']">
    <xsl:sort select="message" />
    <xsl:variable name="warningMessage" select="message" />
    
    <xsl:if test="not(preceding-sibling::record[message=$warningMessage])">
      <xsl:variable name="record" select="//record[message=$warningMessage]" />
      <xsl:text>&pad2;WARNING&pad2;</xsl:text><xsl:value-of select="$warningMessage" /><xsl:text>&nl;</xsl:text>
      
      <xsl:if test="$record[file]">
        <xsl:text>&pad2;</xsl:text>Example: <xsl:if test="$record/line">line <xsl:value-of select="$record/line" /> of </xsl:if><xsl:value-of select="$record/file" /><xsl:text>&nl;</xsl:text>
      </xsl:if>
      
      <xsl:text>&pad2;</xsl:text><xsl:value-of select="count(//record[message=$warningMessage and level='WARNING'])" /><xsl:text> occurrence(s)&nl;</xsl:text>
    </xsl:if>
  </xsl:for-each>
  
  <xsl:text>&nl;Info Found:&nl;</xsl:text>
  <xsl:for-each select="log/record [level='INFO']">
    <xsl:sort select="message" />
    <xsl:variable name="infoMessage" select="message" />
    
    <xsl:if test="not(preceding-sibling::record[message=$infoMessage])">
      <xsl:variable name="record" select="//record[message=$infoMessage]" />
      <xsl:text>&pad2;INFO&pad2;</xsl:text><xsl:value-of select="$infoMessage" /><xsl:text>&nl;</xsl:text>
      
      <xsl:if test="$record[file]">
        <xsl:text>&pad2;</xsl:text>Example: <xsl:if test="$record/line">line <xsl:value-of select="$record/line" /> of </xsl:if><xsl:value-of select="$record/file" /><xsl:text>&nl;</xsl:text>
      </xsl:if>
      
      <xsl:text>&pad2;</xsl:text><xsl:value-of select="count(//record[message=$infoMessage and level='INFO'])" /><xsl:text> occurrence(s)&nl;&nl;</xsl:text>
    </xsl:if>
  </xsl:for-each>
  
  <xsl:text>End Of Report&nl;</xsl:text>
</xsl:template>

</xsl:stylesheet>