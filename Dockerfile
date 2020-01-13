###
# Copyright © 2019  Atos Spain SA. All rights reserved.
# This file is part of SEAL Configuration Manager (SEAL ConfMngr).
# SEAL ConfMngr is free software: you can redistribute it and/or modify it under the terms of EUPL 1.2.
# THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT ANY WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
# INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT, 
# IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
# DAMAGES OR OTHER LIABILITY, WHETHER IN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
# See README file for the full disclaimer information and LICENSE file for full license information in the project root.
###

FROM openjdk:8-jdk-alpine
MAINTAINER Atos
VOLUME /tmp
#COPY ./resources/* /resources/
#COPY ./resources/attributeLists/* /resources/attributeLists/
#COPY ./resources/externalEntities/* /resources/externalEntities/
#COPY ./resources/internal/* /resources/internal/
ADD ./target/cm-0.0.1.jar seal-cm.jar
RUN sh -c 'touch /seal-cm.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /seal-cm.jar" ]
#COPY ./scripts/commands.sh /scripts/commands.sh
#RUN ["chmod", "+x", "/scripts/commands.sh"]
#ENTRYPOINT ["/scripts/commands.sh"]
EXPOSE 8083
#EXPOSE 8080
