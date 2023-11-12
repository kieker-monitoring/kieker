/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
 ***************************************************************************/
package kieker.tools.dar.stages;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kieker.tools.dar.stages.SignatureProcessor;

/**
 * Testing signature processor.
 *
 * @author Reiner Jung
 * @since 1.3.0
 *
 */
class SignatureProcessorTest {

    private static final String TYPICAL_SIGNATURE = "public static java.util.List tools.descartes.teastore.registryclient.rest.LoadBalancedCRUDOperations.getEntities(tools.descartes.teastore.registryclient.Service, java.lang.String, java.lang.Class, int, int)";
    private static final String CLASS_SIGNATURE = "tools.descartes.teastore.registryclient.rest.LoadBalancedCRUDOperations";
    private static final String OPERATION_SIGNATURE = "public static java.util.List getEntities(tools.descartes.teastore.registryclient.Service, java.lang.String, java.lang.Class, int, int)";

    @Test
    void test() {
        final SignatureProcessor processor = new SignatureProcessor();

        processor.parse(SignatureProcessorTest.TYPICAL_SIGNATURE);

        Assertions.assertEquals(SignatureProcessorTest.CLASS_SIGNATURE, processor.getClassSignature());
        Assertions.assertEquals(SignatureProcessorTest.OPERATION_SIGNATURE, processor.getOperationSignature());
    }

}
