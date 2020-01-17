FROM openjdk:8-jdk-alpine
MAINTAINER Atos
VOLUME /tmp
COPY ./resources/* /resources/
COPY ./resources/attributeLists/* /resources/attributeLists/
# Temporary: without EWP integration
COPY ./resources/externalEntities/* /resources/externalEntities/
ADD cm-0.0.7p.jar cm.jar
RUN sh -c 'touch /cm.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /cm.jar" ]
EXPOSE 8080
