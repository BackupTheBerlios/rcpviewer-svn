<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="html"/>

	<xsl:template match="beans">
		<html>
			<head>
				<title>
					Spring Configuration
				</title>				
				<link rel="stylesheet" type="text/css" href="spring-context.css" />
			</head>
			<body>
				<xsl:if test="description">
				<p>
					<i><xsl:apply-templates select="description"/></i>
				</p>
				</xsl:if>
				<xsl:apply-templates select="bean"/>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="beans/description">
		<p class="summary">
			<xsl:value-of select="."/>
		</p>
	</xsl:template>


	<xsl:template match="bean">
		<h1><a name="{@id}"><xsl:value-of select="@id"/></a></h1>
		<xsl:apply-templates select="description"/>
		<p><span class="implnote">Implementation: </span><span class="impl"><xsl:value-of select="@class"/></span>
		</p>
		<xsl:if test="property[count(@ref) > 0]">
		<p>
			References
		</p>
		<ul>
			<xsl:apply-templates select="property[@ref]"/>
		</ul>
		</xsl:if>
		<xsl:if test="property[count(@value) > 0]">
		<p>
			Values
		</p>
		<ul>
			<xsl:apply-templates select="property[@value]"/>
		</ul>
		</xsl:if>
		<xsl:if test="property[count(@value) + count(@ref) = 0]">
		<p>
			Inline Objects
		</p>
		<ul>
			<xsl:apply-templates select="property[count(@value) + count(@ref) = 0]"/>
		</ul>
		</xsl:if>
	</xsl:template>

	<xsl:template match="bean/description">
		<p>
			<xsl:value-of select="."/>
		</p>
	</xsl:template>
	
	<xsl:template match="property[@ref]">
			<li>
				<xsl:value-of select="@name"/>
				<xsl:text>: </xsl:text>
				<a href="#{@ref}"><xsl:value-of select="@ref"/></a>
				<xsl:apply-templates select="description"/>
			</li>
	</xsl:template>
	
	<xsl:template match="property[@value]">
			<li>
				<xsl:value-of select="@name"/>
				<xsl:text>: </xsl:text>
				<span class="value"><xsl:value-of select="@value"/></span>
				<xsl:apply-templates select="description"/>
			</li>
	</xsl:template>

	<xsl:template match="property[count(@value) + count(@ref) = 0]">
			<li>
				<xsl:value-of select="@name"/>
				<xsl:text>: </xsl:text>
				<xsl:apply-templates select="description"/>
				<xsl:apply-templates select="@*|node()"/>			
			</li>
	</xsl:template>

	<xsl:template match="property/description">
		<p>
			<xsl:apply-templates select="@*|node()"/>			
		</p>
	</xsl:template>
	
</xsl:stylesheet>