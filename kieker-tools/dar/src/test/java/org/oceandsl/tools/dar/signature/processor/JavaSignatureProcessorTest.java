/***************************************************************************
 * Copyright (C) 2023 OceanDSL (https://oceandsl.uni-kiel.de)
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
package org.oceandsl.tools.dar.signature.processor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Testing Java signature processor.
 *
 * @author Reiner Jung
 * @since 1.3.0
 *
 */
class JavaSignatureProcessorTest {

    private static final String COMPONENT_SIGNATURE = "tools.descartes.teastore.registryclient.rest.LoadBalancedCRUDOperations";
    private static final String OPERATION_SIGNATURE = "public static java.util.List getEntities(tools.descartes.teastore.registryclient.Service, java.lang.String, java.lang.Class, int, int)";

    private static final String REDUCED_SIGNATURE = "public static List getEntities(Service, String, Class, int, int)";

    @Test
    void test() {
        final JavaSignatureProcessor processor = new JavaSignatureProcessor(false);

        processor.processSignatures(JavaSignatureProcessorTest.COMPONENT_SIGNATURE,
                JavaSignatureProcessorTest.OPERATION_SIGNATURE);

        Assertions.assertEquals(JavaSignatureProcessorTest.REDUCED_SIGNATURE, processor.getOperationSignature());
        Assertions.assertEquals(JavaSignatureProcessorTest.COMPONENT_SIGNATURE, processor.getComponentSignature());
    }

}
