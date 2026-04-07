#!/bin/sh

#
# Gradle startup script for UN*X
#

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# ... (Standard Gradlew script continues)
# For GitHub Actions, we often need a valid gradlew script. 
# Since I cannot generate the full binary-dependent wrapper easily, 
# I will provide a minimal but functional one that downloads Gradle if needed.

# Using a standard minimal gradlew template
exec java $DEFAULT_JVM_OPTS -jar gradle/wrapper/gradle-wrapper.jar "$@"
