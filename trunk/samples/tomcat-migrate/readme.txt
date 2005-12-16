# Copyright 2001-2005 The Apache Software Foundation.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

Configuring Tomcat to use x4juli with Jakarta Commons Logging per WebApplication

Pre-requisite
- This sample assumes a clean, new installation of Tomcat and is based on Tomcat 5.5.12,
  with no changes to ports or config.

${catalina.base} is your tomcat installation directory.

Server-Config:
1) Stop your server

2) Open ${catalina.base}/bin/catalina.sh or ${catalina.base}\bin\catalina.bat (Windows environment) in your favourite editor.
Search for tomcat-juli.jar. Replace String "org.apache.juli.ClassLoaderLogManager"
(without double quotes) with "org.x4juli.X4JuliLogManager" (without double quotes).

Result in catalina.bat
set JAVA_OPTS=%JAVA_OPTS% -Djava.util.logging.manager=org.x4juli.X4JuliLogManager -Djava.util.logging.config.file="${catalina.base}\conf\logging.properties" -Dorg.apache.commons.logging.LogFactory=org.x4juli.JCLFactory

Result in catalina.sh
JAVA_OPTS="$JAVA_OPTS "-Djava.util.logging.manager=org.x4juli.X4JuliLogManager" "-Djava.util.logging.config.file="$CATALINA_BASE/conf/logging.properties" "-Dorg.apache.commons.logging.LogFactory=org.x4juli.JCLFactory"

3) Rename ${catalina.base}/bin/tomcat-juli.jar to tomcat-juli.jar.initial
4) Rename x4juli-x.x.jar to tomcat-juli.jar and put it in ${catalina.base}/bin

5) Rename ${catalina.base}\conf\logging.properties to ${catalina.base}\conf\logging.properties.inital for later use
6) Copy logging.properties (provided in this directory) to ${catalina.base}\conf\logging.properties

7) Put the commons-logging.properties (provided in this directory) file in your ${catalina.base}/server/classes
    This will delegate Tomcats internal logging to the native x4juli JCL implementation.
    This will not touch JCL configuration of any included webapp (i.E. servlets-examples)

8) Start the server
