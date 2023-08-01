<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >

    <xsl:output method="xml" indent="yes"/>
    <xsl:param name="datamapper-step1-body" />
    <xsl:param name="datamapper-step2-body" />

    <xsl:template match="/">
        <Third>
            <xsl:for-each select="$datamapper-step1-body/First/Items/Item">
                <Item>
                    <Name>
                        <xsl:value-of select="."/>
                    </Name>
                    <Price>
                        <xsl:choose>
                            <xsl:when test=". = 'Apple'">
                                <xsl:value-of select="$datamapper-step2-body/Second/Apple/UnitPrice"/>
                            </xsl:when>
                            <xsl:when test=". = 'Orange'">
                                <xsl:value-of select="$datamapper-step2-body/Second/Orange/UnitPrice"/>
                            </xsl:when>
                        </xsl:choose>
                    </Price>
                </Item>
            </xsl:for-each>

            <xsl:variable name="total-price">
                <xsl:for-each select="$datamapper-step1-body/First/Items/Item">
                    <xsl:choose>
                        <xsl:when test=". = 'Apple'">
                            <xsl:copy-of select="$datamapper-step2-body/Second/Apple/UnitPrice"/>
                        </xsl:when>
                        <xsl:when test=". = 'Orange'">
                            <xsl:copy-of select="$datamapper-step2-body/Second/Orange/UnitPrice"/>
                        </xsl:when>
                    </xsl:choose>
                </xsl:for-each>
            </xsl:variable>
            <TotalPrice>
                <xsl:value-of select='format-number(sum($total-price/UnitPrice), "#.00")'/>
            </TotalPrice>
        </Third>
    </xsl:template>

</xsl:stylesheet>