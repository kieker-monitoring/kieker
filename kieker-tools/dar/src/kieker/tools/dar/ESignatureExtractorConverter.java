/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.tools.dar;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

/**
 * Convert command line input for class and operation name conversion.
 *
 * @author Reiner Jung
 * @since 1.2
 */
public class ESignatureExtractorConverter implements IStringConverter<ESignatureExtractor> {

    @Override
    public ESignatureExtractor convert(final String value) {
        for (final ESignatureExtractor element : ESignatureExtractor.values()) {
            if (element.name().equalsIgnoreCase(value)) {
                return element;
            }
        }
        throw new ParameterException(String.format("%s is not a valued signature extractor.", value));
    }

}
