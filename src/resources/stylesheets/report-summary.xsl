<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" encoding="UTF-8"/>
<xsl:param name="level" select="'INFO'" />
<xsl:variable name="pad2" select="'  '" />
<xsl:variable name="pad4" select="'    '" />
<xsl:variable name="pad6" select="'      '" />

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
  <xsl:value-of select="$pad2" /><xsl:value-of select="count(log/record [level='NOTIFICATION' and message!='SKIP' and message!='UNKNOWN'])" /><xsl:text> of </xsl:text><xsl:value-of select="count(log/record [level='NOTIFICATION' and message!='SKIP'])" /><xsl:text> validated, </xsl:text><xsl:value-of select="count(log/record [level='NOTIFICATION' and message='SKIP'])" /><xsl:text> skipped&#xd;&#xa;</xsl:text>
  <xsl:value-of select="$pad2" /><xsl:value-of select="count(log/record [level='NOTIFICATION' and message='PASS'])" /><xsl:text> of </xsl:text><xsl:value-of select="count(log/record [level='NOTIFICATION' and (message='PASS' or message='FAIL')])" /><xsl:text> passed&#xd;&#xa;&#xd;&#xa;</xsl:text>

  <xsl:if test="$level='ERROR' or $level='WARNING' or $level='INFO'">
    <xsl:text>Errors Found:&#xd;&#xa;&#xd;&#xa;</xsl:text>
    <xsl:for-each select="log/record [level='ERROR']">
      <xsl:sort select="message" />
      <xsl:variable name="errorMessage" select="message" />
    
      <xsl:if test="not(preceding-sibling::record[message=$errorMessage])">
        <xsl:variable name="record" select="//record[message=$errorMessage]" />
        <xsl:value-of select="$pad2" /><xsl:text>ERROR</xsl:text><xsl:value-of select="$pad2" /><xsl:value-of select="$errorMessage" /><xsl:text>&#xd;&#xa;</xsl:text>
      
        <xsl:if test="$record/file and $record/line">
          <xsl:value-of select="$pad2" /><xsl:text>Example: line </xsl:text><xsl:value-of select="$record/line" /><xsl:text> of </xsl:text><xsl:value-of select="$record/file" /><xsl:text>&#xd;&#xa;</xsl:text>
        </xsl:if>
      
        <xsl:value-of select="$pad2" /><xsl:value-of select="count(//record[message=$errorMessage and level='ERROR'])" /><xsl:text> occurrence(s)&#xd;&#xa;&#xd;&#xa;</xsl:text>
      </xsl:if>
    </xsl:for-each>
  </xsl:if>
  
  <xsl:if test="$level='WARNING' or $level='INFO'">
    <xsl:text>Warnings Found:&#xd;&#xa;&#xd;&#xa;</xsl:text>
    <xsl:for-each select="log/record [level='WARNING']">
      <xsl:sort select="message" />
      <xsl:variable name="warningMessage" select="message" />
    
      <xsl:if test="not(preceding-sibling::record[message=$warningMessage])">
        <xsl:variable name="record" select="//record[message=$warningMessage]" />
        <xsl:value-of select="$pad2" /><xsl:text>WARNING</xsl:text><xsl:value-of select="$pad2" /><xsl:value-of select="$warningMessage" /><xsl:text>&#xd;&#xa;</xsl:text>
      
        <xsl:if test="$record[file]">
          <xsl:value-of select="$pad2" /><xsl:text>Example: line </xsl:text><xsl:value-of select="$record/line" /><xsl:text> of </xsl:text><xsl:value-of select="$record/file" /><xsl:text>&#xd;&#xa;</xsl:text>
        </xsl:if>
      
        <xsl:value-of select="$pad2" /><xsl:value-of select="count(//record[message=$warningMessage and level='WARNING'])" /><xsl:text> occurrence(s)&#xd;&#xa;&#xd;&#xa;</xsl:text>
      </xsl:if>
    </xsl:for-each>
  </xsl:if>
  
  <xsl:if test="$level='INFO'">
    <xsl:text>Info Found:&#xd;&#xa;&#xd;&#xa;</xsl:text>
    <xsl:for-each select="log/record [level='INFO']">
      <xsl:sort select="message" />
      <xsl:variable name="infoMessage" select="message" />
    
      <xsl:if test="not(preceding-sibling::record[message=$infoMessage])">
        <xsl:variable name="record" select="//record[message=$infoMessage]" />
        <xsl:value-of select="$pad2" /><xsl:text>INFO</xsl:text><xsl:value-of select="$pad2" /><xsl:value-of select="$infoMessage" /><xsl:text>&#xd;&#xa;</xsl:text>
      
        <xsl:if test="$record[file]">
          <xsl:value-of select="$pad2" /><xsl:text>Example: line </xsl:text><xsl:value-of select="$record/line" /><xsl:text> of </xsl:text><xsl:value-of select="$record/file" /><xsl:text>&#xd;&#xa;</xsl:text>
        </xsl:if>
      
        <xsl:value-of select="$pad2" /><xsl:value-of select="count(//record[message=$infoMessage and level='INFO'])" /><xsl:text> occurrence(s)&#xd;&#xa;&#xd;&#xa;</xsl:text>
      </xsl:if>
    </xsl:for-each>
  </xsl:if>
  
  <xsl:text>End Of Report&#xd;&#xa;</xsl:text>
</xsl:template>

</xsl:stylesheet>