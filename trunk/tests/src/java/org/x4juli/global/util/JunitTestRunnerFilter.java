/*
 * Copyright 1999,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.x4juli.global.util;

import org.apache.oro.text.perl.Perl5Util;


/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class JunitTestRunnerFilter implements Filter {
  Perl5Util util = new Perl5Util();

  /**
   * Filter out stack trace lines coming from the various JUnit TestRunners.
   */
  public String filter(String in) {
    if (in == null) {
      return null;
    }

    if (
      util.match(
          "/at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner/", in)) {
      return null;
    } else if (
      util.match(
          "/at org.apache.tools.ant.taskdefs.optional.junit.JUnitTestRunner/",
          in)) {
      return null;
    } else {
      return in;
    }
  }
}
