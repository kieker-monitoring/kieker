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
package org.oceandsl.tools.cmi.stages;

import teetime.framework.AbstractConsumerStage;

/**
 * @author Reiner Jung
 * @since 1.3.0
 */
public class PrintReportStage extends AbstractConsumerStage<Report> {

    @Override
    protected void execute(final Report report) throws Exception {
        System.out.println("+++++++++++++++++++");
        System.out.println(report.getName());
        System.out.println("+++++++++++++++++++");
        report.getMessages().forEach(message -> System.out.println(message));
        System.out.println();
    }

}
