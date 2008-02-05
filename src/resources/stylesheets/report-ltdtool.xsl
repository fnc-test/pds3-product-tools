<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" encoding="UTF-8"/>
<xsl:param name="level" select="'INFO'" />
<xsl:variable name="pad2" select="'  '" />
<xsl:variable name="numErrors" select="count(log/record [level='ERROR']/file)" />
<xsl:variable name="numWarnings" select="count(log/record [level='WARNING']/file)" />

<xsl:template match="/">
  
  <xsl:for-each select="log/record [level='CONFIG']">
    <xsl:value-of select="message"/><xsl:text>&#xd;&#xa;</xsl:text>
  </xsl:for-each>
  
  <xsl:for-each select="log/record[level!='NOTIFICATION' and (level='INFO' or level='WARNING' or level='ERROR')]">
    <xsl:if test="$level='INFO'">
      <xsl:value-of select="level" /><xsl:value-of select="$pad2" />
      <xsl:if test="line">line <xsl:value-of select="line" />: </xsl:if>
      <xsl:value-of select="message" /><xsl:text>&#xd;&#xa;</xsl:text>
    </xsl:if>
    <xsl:if test="$level='WARNING' and level!='INFO'">
      <xsl:value-of select="level" /><xsl:value-of select="$pad2" />
      <xsl:if test="line">line <xsl:value-of select="line" />: </xsl:if>
      <xsl:value-of select="message" /><xsl:text>&#xd;&#xa;</xsl:text>
    </xsl:if>
    <xsl:if test="$level='ERROR' and level!='INFO' and level!='WARNING'">
      <xsl:value-of select="level" /><xsl:value-of select="$pad2" />
      <xsl:if test="line">line <xsl:value-of select="line" />: </xsl:if>
      <xsl:value-of select="message" /><xsl:text>&#xd;&#xa;</xsl:text>
    </xsl:if>
  </xsl:for-each>
  
  <xsl:if test="$numErrors = 0 and $numWarnings = 0">
    <xsl:text>Validation completed with no errors or warnings.&#xd;&#xa;</xsl:text>
  </xsl:if>

</xsl:template>

</xsl:stylesheet>