# JAXB-Facets

This is a fork of jaxb-facets from http://www.infosys.tuwien.ac.at/staff/hummer/tools/jaxb-facets.html

It aims to automate the creation of facets specific versions of the jaxb-api and jaxb-impl using maven.

A jaxb jira is outstanding to integrate this stuff directly into JAXB RI. 
If and when that happens this project will be obselete (which we are looking forward to!).

http://java.net/jira/browse/JAXB-917

## Maven Repository

The jaxb-api and jaxb-impl JARs are deployed to a maven repo located here:

https://github.com/whummer/mvn

E.g., see:

https://raw.github.com/whummer/mvn/master/releases/javax/xml/bind/jaxb-api/2.2.7-facets-1.0.5/jaxb-api-2.2.7-facets-1.0.5.jar
https://raw.github.com/whummer/mvn/master/releases/com/sun/xml/bind/jaxb-impl/2.2.6-facets-1.2.0/jaxb-impl-2.2.6-facets-1.2.0.jar


## Compile & Build

To compile, test and package the code, run a maven build from the project's root directory:

```
$ mvn clean install
```

## Maven Integration

To integrate JAXB-Facets into your Maven project, simply add the following repository and dependencies. You need to ensure that there are no other versions of jaxb-api and jaxb-impl on the CLASSPATH.

```xml
<project ...>
...
    <dependencies>
        <dependency>
            <groupId>javax.xml.bind</groupId>
            <artifactId>jaxb-api</artifactId>
            <version>2.2.7-facets-1.0.5</version>
        </dependency>
        <dependency>
            <groupId>com.sun.xml.bind</groupId>
            <artifactId>jaxb-impl</artifactId>
            <version>2.2.6-facets-1.2.0</version>
        </dependency>
        ...
    </dependencies>

    <repositories>
        <repository>
            <id>github-repo-releases</id>
            <url>https://raw.github.com/whummer/mvn/master/releases</url>
        </repository>
    </repositories>
...
</project>
```

## JAXB Schemagen Maven Integration

To integrate JAXB-Facets with the schemagen facility of jaxb2-maven-plugin, use the following configuration:

```xml
<project ...>
...
	<build>
		<plugins>
			<plugin>
	    		<groupId>org.codehaus.mojo</groupId>
	    		<artifactId>jaxb2-maven-plugin</artifactId>
	    		<version>1.5</version>
	    		<dependencies>
	    			<dependency>
	    				<groupId>com.sun.xml.bind</groupId>
	    				<artifactId>jaxb-impl</artifactId>
	    				<version>2.2.6-facets-1.2.0</version>
					</dependency>
					<dependency>
	    				<groupId>javax.xml.bind</groupId>
	        			<artifactId>jaxb-api</artifactId>
	        			<version>2.2.7-facets-1.0.5</version>
	    			</dependency> 
                    <dependency>
                        <groupId>com.sun.xml.bind</groupId>
                        <artifactId>jaxb-xjc</artifactId>
                        <version>2.2.6</version>
                        <exclusions>
                        	<exclusion>
			    				<groupId>javax.xml.bind</groupId>
			        			<artifactId>jaxb-api</artifactId>
                        	</exclusion>
                        	<exclusion>
			    				<groupId>com.sun.xml.bind</groupId>
			    				<artifactId>jaxb-impl</artifactId>
                        	</exclusion>
                        </exclusions>
                	</dependency>
				</dependencies>
				<executions>
	        		<execution>
	            		<goals>
	                		<goal>schemagen</goal>
	            		</goals>
	            		<phase>generate-resources</phase>
	            		<configuration>
	            			...
	            		</configuration>
	            	</execution>
	            </executions>
			</plugin>
		</plugins>
	</build>
...
</project>
```

## Wsimport Integration (WSDL-First Schema Generation)

Starting with version 2.2.6-facets-1.1.0, JAXB-facets supports also WSDL-first generation 
using 'wsimport'. JAXB-Facets can easily be hooked as a plugin into wsimport in order to
include the specific annotations (@Facets, @Documentation, @Annotation, ...) in the 
generated Java code.

To activate the plugin, use the wsimport wrapper script with the "-jaxb-facets" switch as follows:
```
./bin/wsimport.sh -keep -B-jaxb-facets -d <target_dir> <source_wsdl>
```
... and for Windows:
```
./bin/wsimport.bat -keep -B-jaxb-facets -Xendorsed -d <target_dir> <source_wsdl>
```

## Maven Endorsed Integration

In some situations in order to make use of the new jaxb facet in your maven project, you may need to make use of the endorsed 
strategy with the maven compiler and surefire plugins.

```xml
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-dependency-plugin</artifactId>
	<configuration>
		<outputDirectory>${project.build.directory}/endorsed</outputDirectory>
	</configuration>
	<executions>
		<execution>
			<id>copy-endorsed</id>
			<phase>generate-sources</phase>
			<goals>
				<goal>copy</goal>
			</goals>
			<configuration>
				<artifactItems>
					<artifactItem>
						<groupId>javax.xml.bind</groupId>
						<artifactId>jaxb-api</artifactId>
						<version>2.2.6-facets-1.0.5</version>
						<overWrite>true</overWrite>
						<destFileName>jaxb-api.jar</destFileName>
					</artifactItem>
				</artifactItems>
			</configuration>
		</execution>
	</executions>
</plugin>
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-compiler-plugin</artifactId>
	<configuration>
		<compilerArguments>
			<endorseddirs>${project.build.directory}/endorsed</endorseddirs>
		</compilerArguments>
	</configuration>
</plugin>
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-surefire-plugin</artifactId>
	<configuration>
		<systemPropertyVariables>
			<java.endorsed.dirs>${project.build.directory}/endorsed</java.endorsed.dirs>
		</systemPropertyVariables>
	</configuration>
</plugin>
```


## Change Log

- jaxb-impl:2.2.6-facets-1.2.0
    * Initial support for xs:assert elements (defined in XML Schema 1.1).
    * Refactorings due to newly introduced XML Schema 1.1 features.
- jaxb-api:2.2.7-facets-1.0.5
	* Added @Assert annotation to define xs:assert elements.
- jaxb-impl:2.2.6-facets-1.1.0
    * Added support for "wsimport" WSDL-first JAXB generation.
- jaxb-impl:2.2.6-facets-1.0.11
	* add location() parameter to @Annotation (INSIDE_ELEMENT, 
	  OUTSIDE_ELEMENT): For XSD groups (in particular <choice>), 
	  this allows to place <annotation> either inside the children 
	  of the group, or into the group itself (as sibling of 
	  the children of the group). This can also be used to place
	  an annotation inside an @XmlElementWrapper, instead of placing
	  it inside the element which is wrapped.
- jaxb-api:2.2.7-facets-1.0.4
	* Added AnnotationLocation class to specify the location of 
	  generated XSD annotations.
- jaxb-impl:2.2.6-facets-1.0.10
	* support jaxb-facets for source code based schemagen
	  (e.g., used for "mvn generate-resources)
	* remaining limitation: package-level annotations do not (yet)
	  work with the schemagen based generation approach. This is
	  planned for a future release.
- jaxb-impl:2.2.6-facets-1.0.9
	* support additional facets (e.g., maxLength) on enumeration types
- jaxb-impl:2.2.6-facets-1.0.8
	* compulsory namespace for xsd:annotation attributes
	* improved schema validation in unit tests
- jaxb-impl:2.2.6-facets-1.0.7
	* support for custom attributes in xsd:annotation
	* support for XML content in xsd:annotation
	* test classes moved back to jaxb-impl project
- jaxb-api:2.2.7-facets-1.0.3
	* minor (changed versions, updated pom.xml file metadata)
- jaxb-impl:2.2.7-facets-1.0.6
	* removed com.sun.xml.internal.* references
	* moved jaxb-impl test classes into separate maven project
	* code refactoring; removed code duplication in XmlSchemaGenerator
- jaxb-impl:2.2.7-facets-1.0.5
	* fixed pom.xml metadata and JAR manifest info
	* Handling of com.sun.xml.internal.* classes in separate XmlSchemaGenerator
- jaxb-impl:2.2.7-facets-1.0.4
	* test utility classes
	* updated CXF dependency version
- jaxb-impl:2.2.7-facets-1.0.3
	* test cases for enum literals support
- jaxb-api:2.2.7-facets-1.0.2
	* initial github version

Older versions (legacy from http://dsg.tuwien.ac.at/staff/hummer/tools/jaxb-facets.html):

- 2012-11-09: version 1.0 
	Minor bug fixes and API changes in @Facets. The type of minInclusive/maxInclusive/minExclusive/maxExclusive has 
	been changed from long to String, which is needed, e.g., to represent min/max for dates. This version is not API
	compatible with 0.x versions!
- 2012-09-05: version 0.4 
	Support for xsd:annotation, xsd:appinfo, and xsd:documentation
- 2012-05-27: version 0.3 
	fixed classloading-related issues; ready for deployment in JBoss
- 2012-04-15: version 0.2 
	supports Facets for XML attributes; support for Java 1.7
- 2011-12-04: version 0.1
	initial release

