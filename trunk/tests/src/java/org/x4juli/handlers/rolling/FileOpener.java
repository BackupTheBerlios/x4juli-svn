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
package org.x4juli.handlers.rolling;

import java.io.FileInputStream;
import java.io.InputStream;


/**
 * Testcase for x4juli.
 * @since 0.5
 */
public class FileOpener {

    /**
     * 
     */
    public FileOpener() {
        super();
    }

    public static void main(String[] args) {
         FileOpener fo = new FileOpener();
         fo.openFile();
    }

    public void openFile() {
//        System.out.println("Entering FileOpener");
//        System.out.flush();
        try {
            InputStream is = new FileInputStream("output/testren.log");
            is.read();
            Thread.sleep(10000);
            is.close();
        } catch (Exception e) {
//            System.err.println("Error in FileOpener:["+e+"]");
//            e.printStackTrace();
//            System.err.flush();
        }
//        System.out.println("Exiting FileOpener");
//        System.out.flush();
    }

}

// EOF FileOpener.java
