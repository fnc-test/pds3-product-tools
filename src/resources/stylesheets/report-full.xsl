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
  <xsl:value-of select="$pad2" /><xsl:value-of select="count(log/record [level='NOTIFICATION' and message!='SKIP' and message!='UNKNOWN'])" /> of <xsl:value-of select="count(log/record [level='NOTIFICATION' and message!='SKIP'])" /> validated, <xsl:value-of select="count(log/record [level='NOTIFICATION' and message='SKIP'])" /><xsl:text> skipped&#xd;&#xa;</xsl:text>
  <xsl:value-of select="$pad2" /><xsl:value-of select="count(log/record [level='NOTIFICATION' and message='PASS'])" /> of <xsl:value-of select="count(log/record [level='NOTIFICATION' and (message='PASS' or message='FAIL')])" /><xsl:text> passed&#xd;&#xa;</xsl:text>

  <xsl:text>&#xd;&#xa;Validation Details:&#xd;&#xa;</xsl:text>
  <xsl:for-each select="log/record[level='NOTIFICATION']">
    <xsl:variable name="file" select="file" /><xsl:text>&#xd;&#xa;</xsl:text>
    <xsl:value-of select="$pad2" /><xsl:value-of select="message" />: <xsl:value-of select="$file" /><xsl:text>&#xd;&#xa;</xsl:text>
    
    <xsl:choose>
    
      <xsl:when test="$level='INFO'">
        <xsl:for-each select="//record[(file=$file or context=$file) and level!='NOTIFICATION' and (level='INFO' or level='WARNING' or level='ERROR')]">
          <xsl:variable name="currentContext" select="context" />
          <xsl:if test="not(preceding-sibling::record[context=$currentContext])">
            <xsl:choose>
              <xsl:when test="context">
                <xsl:value-of select="$pad4" /><xsl:text>Label Fragment: </xsl:text><xsl:value-of select="context" /><xsl:text>&#xd;&#xa;</xsl:text>
              </xsl:when>
            </xsl:choose>
          </xsl:if>
          <xsl:value-of select="$pad6" /><xsl:value-of select="level" /><xsl:value-of select="$pad2" />
          <xsl:if test="line">line <xsl:value-of select="line" />: </xsl:if>
          <xsl:value-of select="message" /><xsl:text>&#xd;&#xa;</xsl:text>
        </xsl:for-each>
      </xsl:when>
      
      <xsl:when test="$level='WARNING' or $level='INFO'">
        <xsl:for-each select="//record[(file=$file or context=$file) and level!='NOTIFICATION' and (level='WARNING' or level='ERROR')]">
          <xsl:variable name="currentContext" select="context" />
          <xsl:if test="not(preceding-sibling::record[context=$currentContext])">
            <xsl:choose>
              <xsl:when test="context">
                <xsl:value-of select="$pad4" /><xsl:text>Label Fragment: </xsl:text><xsl:value-of select="context" /><xsl:text>&#xd;&#xa;</xsl:text>
              </xsl:when>
            </xsl:choose>
          </xsl:if>
          <xsl:value-of select="$pad6" /><xsl:value-of select="level" /><xsl:value-of select="$pad2" />
          <xsl:if test="line">line <xsl:value-of select="line" />: </xsl:if>
          <xsl:value-of select="message" /><xsl:text>&#xd;&#xa;</xsl:text>
        </xsl:for-each>
      </xsl:when>
      
      <xsl:when test="$level='ERROR' or $level='WARNING' or $level='INFO'">
        <xsl:for-each select="//record[file=$file and level!='NOTIFICATION' and level='ERROR']">
          <xsl:variable name="currentContext" select="context" />
          <xsl:if test="not(preceding-sibling::record[context=$currentContext])">
            <xsl:choose>
              <xsl:when test="context">
                <xsl:value-of select="$pad4" /><xsl:text>Label Fragment: </xsl:text><xsl:value-of select="context" /><xsl:text>&#xd;&#xa;</xsl:text>
              </xsl:when>
            </xsl:choose>
          </xsl:if>
          <xsl:value-of select="$pad6" /><xsl:value-of select="level" /><xsl:value-of select="$pad2" />
          <xsl:if test="line">line <xsl:value-of select="line" />: </xsl:if>
          <xsl:value-of select="message" /><xsl:text>&#xd;&#xa;</xsl:text>
        </xsl:for-each>
      </xsl:when>
      
    </xsl:choose>
  </xsl:for-each>
  
  <xsl:text>&#xd;&#xa;End Of Report&#xd;&#xa;</xsl:text>
</xsl:template>

</xsl:stylesheet>