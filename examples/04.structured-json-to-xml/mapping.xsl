<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xj="http://camel.apache.org/component/xj"
                exclude-result-prefixes="xj">

    <xsl:output omit-xml-declaration="no"  method="xml" indent="yes"/>
    <xsl:param name="datamapper-step1-body" />

    <xsl:template match="/">
        <Person>
            <firstName><xsl:value-of select="$datamapper-step1-body/Account/FirstName"/></firstName>
            <lastName><xsl:value-of select="$datamapper-step1-body/Account/LastName"/></lastName>
            <email><xsl:value-of select="$datamapper-step1-body/Account/Contact/Email" /></email>
            <address>
                <streetAddress><xsl:value-of select="$datamapper-step1-body/Account/Contact/Address/Street" /></streetAddress>
                <city><xsl:value-of select="$datamapper-step1-body/Account/Contact/Address/City" /></city>
                <state><xsl:value-of select="$datamapper-step1-body/Account/Contact/Address/State" /></state>
                <postalCode xj:type="int"><xsl:value-of select="$datamapper-step1-body/Account/Contact/Address/Zip" /></postalCode>
            </address>
        </Person>
    </xsl:template>

</xsl:stylesheet>