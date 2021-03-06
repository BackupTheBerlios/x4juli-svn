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

Configuring a standalone application to use x4juli with native Jakarta Commons Logging
and Simple Logging Facade for Java (slf4j) support

Pre-requisite
- Download Jakarta Commons Logging from http://jakarta.apache.org/commons/logging/
  or use the version from the lib directory. It is recommended to use the latest version.

1) You have to specify x4juli as logging system.
   This is done with the -Djava.util.logging.manager=org.x4juli.X4JuliLogManager option at the command line

2a) You have to specify x4juli as JCL Factory implementation
   This is done with the -Dorg.apache.commons.logging.LogFactory=org.x4juli.JCLFactory option at the command line

2b) For slf4j you have to do nothing. It is recommended to remove any other slf4j-xxxx.jar from the classpath to
    avoid confusion.

3) To start the application
${x4juli.jar.path} your absolute path to x4juli-x.y.jar, where x.y is the version. (i.E. /opt/libs/java/x4juli/x4juli-0.6.jar or c:\java\lib\x4juli\x4juli-0.6.jar )
${jcl.jar.path} your absolute path and filename to commons-logging.jar (i.E. /opt/libs/java/jcl/commons-logging.jar or c:\java\lib\jcl\commons-logging.jar )
${sample.logpath} absolute path where you want to store output generated by the sample (i.E. /var/log or c:\temp )
                  This is a feature of x4juli. You can specify a system property (here -Dsample.logpath)
                  and the LogManager will use its value for replacement. See logging.properties.

java -cp ${x4juli.jar.path};${jcl.jar.path};x4julisample-x.y.jar
     -Djava.util.logging.manager=org.x4juli.X4JuliLogManager
     -Dorg.apache.commons.logging.LogFactory=org.x4juli.JCLFactory
     -Dsample.logpath=${sample.logpath}
     -Djava.util.logging.config.file=.\logging.properties
     org.x4juli.sample.standalone.X4JuliDemo

4) Change level, handler, formatter parameters in the logging.properties to see changes.
   Another try: start the sample without -Dorg.apache.commons.logging.LogFactory=org.x4juli.JCLFactory 
   and see console output change: JclSample: Using class ???