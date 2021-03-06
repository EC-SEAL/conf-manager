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
#
# Temporary: the EWP is NOT being used in SEAL by the moment. TO BE REMOVED.
###

### NOT BEING USED

#!/bin/bash

mvn install:install-file -Dfile=./libs/ewp-registry-client-1.6.1-SNAPSHOT.jar -DgroupId=eu.erasmuswithoutpaper -Dversion=1.6.1-SNAPSHOT -DartifactId=ewp-registry-client -Dpackaging=jar -DlocalRepositoryPath=/home/travis/.m2/repository

java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /seal-cm.jar      