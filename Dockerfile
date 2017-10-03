FROM openjdk:8

# FIXME - issue #3
#FROM openjdk:9

LABEL maintainer="tim.myerscough@mechanicalrock.io"

ENV DONUT_JUNIT_VERSION 0.2.1

WORKDIR /app

ADD "http://repo1.maven.org/maven2/io/magentys/donut-junit-adapter/${DONUT_JUNIT_VERSION}/donut-junit-adapter-${DONUT_JUNIT_VERSION}-jar-with-dependencies.jar" "/app/donut-junit-adapter.jar"

VOLUME /source/
VOLUME /output/

ENTRYPOINT ["java", "-jar", "donut-junit-adapter.jar", "-p", "/source/", "-o", "/output/"]
