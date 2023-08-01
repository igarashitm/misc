<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >

    <xsl:output method="xml" indent="yes"/>
    <xsl:param name="datamapper-step1-header-step1-header1" />
    <xsl:param name="datamapper-step1-header-global-header1" />
    <xsl:param name="datamapper-step2-header-step2-header1" />
    <xsl:param name="datamapper-step2-header-global-header1" />
    <xsl:param name="datamapper-step3-header-global-header1" />

    <xsl:template match="/">
        <Headers>
            <Global>
                <Step1><xsl:value-of select="$datamapper-step1-header-global-header1" /></Step1>
                <Step2><xsl:value-of select="$datamapper-step2-header-global-header1" /></Step2>
                <Step3><xsl:value-of select="$datamapper-step3-header-global-header1" /></Step3>
            </Global>
            <Local>
                <Step1><xsl:value-of select="$datamapper-step1-header-step1-header1"/></Step1>
                <Step2><xsl:value-of select="$datamapper-step2-header-step2-header1"/></Step2>
            </Local>
        </Headers>
    </xsl:template>

</xsl:stylesheet>