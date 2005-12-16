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
package org.x4juli.handlers.rolling.helper;

import java.io.File;

import org.x4juli.global.components.AbstractJuliTestCase;
import org.x4juli.global.util.Compare;
import org.x4juli.handlers.rolling.helper.GZCompressAction;

/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class CompressTest extends AbstractJuliTestCase {
    // -------------------------------------------------------------- Variables


    // ----------------------------------------------------------- Constructors
    
    /**
     * 
     */
    public CompressTest() {
        super();
    }

    /**
     * @param name
     */
    public CompressTest(String name) {
        super(name);
    }

    // --------------------------------------------------------- Public Methods

    public void test1() throws Exception {
        GZCompressAction.execute(new File("input/compress1.copy"), new File(
                "output/compress1.txt.gz"), false, getLogger());
        File myFile = new File("demo.txt");
        getLogger().finer(myFile.getAbsolutePath());
        assertTrue(Compare.gzCompare("output/compress1.txt.gz", "witness/compress1.txt.gz"));
    }

    public void test2() throws Exception {
        GZCompressAction.execute(new File("input/compress2.copy"), new File(
                "output/compress2.txt.gz"), false, getLogger());
        assertTrue(Compare.gzCompare("output/compress2.txt.gz", "witness/compress2.txt.gz"));
    }

}

// EOF CompressTest.java
