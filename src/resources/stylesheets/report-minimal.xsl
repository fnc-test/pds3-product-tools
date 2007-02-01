<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" encoding="UTF-8"/>
<xsl:param name="level" select="'INFO'" />
<xsl:variable name="pad2" select="'  '" />
<xsl:variable name="pad4" select="'    '" />
<xsl:variable name="pad5" select="'     '" />
<xsl:variable name="pad6" select="'      '" />
<xsl:variable name="numErrors" select="count(log/record [level='SEVERE']/file)" />
<xsl:variable name="numWarnings" select="count(log/record [level='WARNING']/file)" />
<xsl:variable name="numInfo" select="count(log/record [level='INFO']/file)" />

<xsl:template match="/">
  <xsl:text>PDS Validation Tool Report&#xd;&#xa;</xsl:text>
  <xsl:for-each select="log/record [level='CONFIG']">
    <xsl:value-of select="$pad2" /><xsl:value-of select="message"/><xsl:text>&#xd;&#xa;</xsl:text>
  </xsl:for-each>
  
  <xsl:text>&#xd;&#xa;Parameter Settings:&#xd;&#xa;</xsl:text>
  <xsl:for-each select="log/record [level='PARAMETER']">
    <xsl:value-of select="$pad2" /><xsl:value-of select="message"/><xsl:text>&#xd;&#xa;</xsl:text>
  </xsl:for-each>
  
  <xsl:text>&#xd;&#xa;Summary:&#xd;&#xa;</xsl:text>
  <xsl:value-of select="$pad2" /><xsl:value-of select="count(log/record [level='NOTIFICATION' and message!='SKIP'])" /> of <xsl:value-of select="count(log/record [level='NOTIFICATION' and message!='SKIP'])" /> validated, <xsl:value-of select="count(log/record [level='NOTIFICATION' and message='SKIP'])" /><xsl:text> skipped&#xd;&#xa;</xsl:text>
  <xsl:value-of select="$pad2" /><xsl:value-of select="count(log/record [level='NOTIFICATION' and message='PASS'])" /> of <xsl:value-of select="count(log/record [level='NOTIFICATION' and (message='PASS' or message='FAIL')])" /> passed<xsl:text>&#xd;&#xa;</xsl:text>

  <xsl:text>&#xd;&#xa;Total Messages:&#xd;&#xa;</xsl:text>
  <xsl:choose>
    <xsl:when test="$level='INFO'">
      <xsl:text>&#xd;&#xa;ERROR</xsl:text><xsl:value-of select="$pad2" /><xsl:text>WARN</xsl:text><xsl:value-of select="$pad2" /><xsl:text>INFO&#xd;&#xa;</xsl:text>
    </xsl:when>
    
    <xsl:when test="$level='WARNING'">
      <xsl:text>&#xd;&#xa;ERROR<xsl:value-of select="$pad2" />WARN&#xd;&#xa;</xsl:text>
    </xsl:when>
    
    <xsl:when test="$level='SEVERE'">
      <xsl:text>&#xd;&#xa;ERROR&#xd;&#xa;</xsl:text>
    </xsl:when>
  </xsl:choose>
  
  <xsl:if test="$level='SEVERE' or $level='WARNING' or $level='INFO'">
    <xsl:value-of select="substring($pad5, string-length($numErrors) + 1)" /><xsl:value-of select="$numErrors" />
  </xsl:if>
  <xsl:if test="$level='WARNING' or $level='INFO'">
    <xsl:value-of select="substring($pad6, string-length($numWarnings) + 1)" /><xsl:value-of select="$numWarnings" />
  </xsl:if>
  <xsl:if test="$level='INFO'">
    <xsl:value-of select="substring($pad6, string-length($numInfo) + 1)" /><xsl:value-of select="$numInfo" />
  </xsl:if>

  <xsl:text>&#xd;&#xa;&#xd;&#xa;Message Counts by File:&#xd;&#xa;</xsl:text>
  <xsl:choose>
    <xsl:when test="$level='INFO'">
      <xsl:text>&#xd;&#xa;ERROR</xsl:text><xsl:value-of select="$pad2" /><xsl:text>WARN</xsl:text><xsl:value-of select="$pad2" /><xsl:text>INFO</xsl:text><xsl:value-of select="$pad2" /><xsl:text>FILE&#xd;&#xa;</xsl:text>
    </xsl:when>
    
    <xsl:when test="$level='WARNING'">
      <xsl:text>&#xd;&#xa;ERROR</xsl:text><xsl:value-of select="$pad2" /><xsl:text>WARN</xsl:text><xsl:value-of select="$pad2" /><xsl:text>FILE&#xd;&#xa;</xsl:text>
    </xsl:when>
    
    <xsl:when test="$level='SEVERE'">
      <xsl:text>&#xd;&#xa;ERROR</xsl:text><xsl:value-of select="$pad2" /><xsl:text>FILE&#xd;&#xa;</xsl:text>
    </xsl:when>
  </xsl:choose>
  
  <xsl:for-each select="log/record[level='NOTIFICATION']">
    <xsl:sort select="file" />
    <xsl:variable name="file" select="file" />
    <xsl:if test="$level='SEVERE' or $level='WARNING' or $level='INFO'">
      <xsl:variable name="numFileErrors" select="count(//record [level='SEVERE' and file=$file])" />
      <xsl:value-of select="substring($pad5, string-length($numFileErrors) + 1)" /><xsl:value-of select="$numFileErrors" />
    </xsl:if>
    <xsl:if test="$level='WARNING' or $level='INFO'">
      <xsl:variable name="numFileWarnings" select="count(//record [level='WARNING' and file=$file])" />
      <xsl:value-of select="substring($pad6, string-length($numFileWarnings) + 1)" /><xsl:value-of select="$numFileWarnings" />
    </xsl:if>
    <xsl:if test="$level='INFO'">
      <xsl:variable name="numFileInfo" select="count(//record [level='INFO' and file=$file])" />
      <xsl:value-of select="substring($pad6, string-length($numFileInfo) + 1)" /><xsl:value-of select="$numFileInfo" />
    </xsl:if>
    <xsl:value-of select="$pad2" /><xsl:value-of select="$file" /><xsl:text>&#xd;&#xa;</xsl:text>
  </xsl:for-each>
  
  <xsl:text>&#xd;&#xa;End Of Report&#xd;&#xa;</xsl:text>
</xsl:template>

</xsl:stylesheet>