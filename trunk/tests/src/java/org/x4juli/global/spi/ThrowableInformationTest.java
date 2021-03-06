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

package org.x4juli.global.spi;

import org.x4juli.global.components.AbstractJuliTestCase;
import org.x4juli.global.spi.ThrowableInformation;

/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class ThrowableInformationTest extends AbstractJuliTestCase {

    /**
     * 
     */
    public ThrowableInformationTest() {
        super();
    }

    /**
     * @param name
     */
    public ThrowableInformationTest(String name) {
        super(name);
    }

    /*
     * Class to test for boolean equals(Object)
     */
    public void testEqualsObject() {
      Throwable e1 = new Exception("exeption 1");
      Throwable e2 = new Exception("exeption 2");
      
      ThrowableInformation te1 = new ThrowableInformation(e1);
      ThrowableInformation te2 = new ThrowableInformation(e2);
      
      assertEquals(te1, te1);
      assertEquals(te2, te2);
      
      boolean eq1 = te1.equals(te2);
      assertEquals(false, eq1);

      boolean eq2 = te1.equals(null);
      assertEquals(false, eq2);
    }

}

// EOF ThrowableInformationTest.java
