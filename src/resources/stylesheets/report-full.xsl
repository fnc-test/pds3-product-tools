<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet [
<!ENTITY nl "&#xd;&#xa;">
<!ENTITY pad2 "  ">
<!ENTITY pad4 "    ">
<!ENTITY pad6 "      ">
]>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text"/>
<xsl:param name="level" select="'INFO'" />

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
  <xsl:text>&pad2;</xsl:text><xsl:value-of select="count(log/record [level='NOTIFICATION' and message='PASS'])" /> of <xsl:value-of select="count(log/record [level='NOTIFICATION' and (message='PASS' or message='FAIL')])" /><xsl:text> passed&nl;</xsl:text>

  <xsl:text>&nl;Validation Details:&nl;</xsl:text>
  <xsl:for-each select="log/record[level='NOTIFICATION']">
    <xsl:sort select="file" />
    <xsl:variable name="file" select="file" /><xsl:text>&nl;</xsl:text>
    <xsl:text>&pad2;</xsl:text><xsl:value-of select="message" />: <xsl:value-of select="$file" /><xsl:text>&nl;</xsl:text>
    
    <xsl:choose>
      <xsl:when test="$level='INFO'">
        <xsl:for-each select="//record[file=$file and level!='NOTIFICATION' and (level='INFO' or level='WARNING' or level='SEVERE')]">
          <xsl:text>&pad6;</xsl:text><xsl:value-of select="level" /><xsl:text>&pad2;</xsl:text><xsl:value-of select="message" /><xsl:text>&nl;</xsl:text>
        </xsl:for-each>
      </xsl:when>
      
      <xsl:when test="$level='WARNING' or $level='INFO'">
        <xsl:for-each select="//record[file=$file and level!='NOTIFICATION' and (level='WARNING' or level='SEVERE')]">
          <xsl:text>&pad6;</xsl:text><xsl:value-of select="level" /><xsl:text>&pad2;</xsl:text><xsl:value-of select="message" /><xsl:text>&nl;</xsl:text>
        </xsl:for-each>
      </xsl:when>
      
      <xsl:when test="$level='SEVERE' or $level='WARNING' or $level='INFO'">
        <xsl:for-each select="//record[file=$file and level!='NOTIFICATION' and level='SEVERE']">
          <xsl:text>&pad6;</xsl:text><xsl:value-of select="level" /><xsl:text>&pad2;</xsl:text><xsl:value-of select="message" /><xsl:text>&nl;</xsl:text>
        </xsl:for-each>
      </xsl:when>
    </xsl:choose>
  </xsl:for-each>
  
  <xsl:text>&nl;End Of Report&nl;</xsl:text>
</xsl:template>

</xsl:stylesheet>