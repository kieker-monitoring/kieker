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
package org.oceandsl.tools.fxca.utils;

import java.util.regex.Pattern;

/**
 * Process path uris.
 *
 *
 * @author Reiner Jung
 *
 * @since 1.3.0
 */
public class PatternUriProcessor implements IUriProcessor {

    private final Pattern regex;
    private final String replacement;

    public PatternUriProcessor(final String pattern, final String replacement) {
        this.regex = Pattern.compile(pattern);
        this.replacement = replacement;
    }

    @Override
    public String process(final String uri) {
        return this.regex.matcher(uri).replaceFirst(this.replacement);
    }

}
