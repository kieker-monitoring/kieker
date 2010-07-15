package kieker.tests.junit;

import junit.framework.TestCase;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */


/**
 *
 * @author avanhoorn
 */
public class TestTestCase extends TestCase {

    private int a, b;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        a = 1;
        b = 1;
    }

    public void testAddition() {
        assertEquals(a, b);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
