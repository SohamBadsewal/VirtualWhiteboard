<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.whiteboard</groupId>
    <artifactId>whiteboard-fx</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>Virtual Whiteboard JavaFX</name>
    <description>Real-time gesture-based drawing with OpenCV and JavaFX</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <javafx.version>21.0.1</javafx.version>
    </properties>

    <dependencies>
        <!-- JavaFX Dependencies -->
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-swing</artifactId>
            <version>${javafx.version}</version>
        </dependency>

        <!-- OpenCV - Local JAR -->
        <dependency>
            <groupId>org.opencv</groupId>
            <artifactId>opencv</artifactId>
            <version>4.12.0</version>
            <scope>system</scope>
            <systemPath>${project.basedir}/lib/opencv-4120.jar</systemPath>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- Maven Compiler Plugin -->
<!-- JavaFX Maven Plugin -->
<plugin>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-maven-plugin</artifactId>
    <version>0.0.8</version>
    <configuration>
        <mainClass>com.whiteboard.App</mainClass>
        <options>
            <option>-Djava.library.path=lib</option>
        </options>
    </configuration>
</plugin>
            <!-- JavaFX Maven Plugin -->
            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>0.0.8</version>
                <configuration>
                    <mainClass>com.whiteboard.App</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
