/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package bookstoreTracing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.analysis.plugin.MonitoringRecordConsumerException;
import kieker.analysis.reader.MonitoringReaderException;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

public class BookstoreHostnameRewriter {

    public static void main(final String[] args)
            throws MonitoringReaderException, MonitoringRecordConsumerException {

        if (args.length == 0) {
            return;
        }

        /* Create Kieker.Analysis instance */
        final AnalysisController analysisInstance = new AnalysisController();
        analysisInstance.registerPlugin(new HostNameRewriterPlugin());

        /* Set filesystem monitoring log input directory for our analysis */
        final String inputDirs[] = {args[0]};
        analysisInstance.setReader(new FSReader(inputDirs));

        /* Start the analysis */
        analysisInstance.run();
    }
}

class HostNameRewriterPlugin implements IMonitoringRecordConsumerPlugin {

    private static final IMonitoringController MONITORING_CTRL =
            MonitoringController.getInstance();
    private static final Collection<Class<? extends IMonitoringRecord>> RECORDTYPE_SUBSCR_LIST =
            new ArrayList<Class<? extends IMonitoringRecord>>();

    static {
        HostNameRewriterPlugin.RECORDTYPE_SUBSCR_LIST.add(OperationExecutionRecord.class);
    }

    @Override
    public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
        return HostNameRewriterPlugin.RECORDTYPE_SUBSCR_LIST;
    }
    private static final String BOOKSTORE_HOSTNAME = "SRV0";
    private static final Random rnd = new Random();
    private static final int RND_PERCENTILE_HOST_IDX_1 = 34;
    private static final String[] CATALOG_HOSTNAMES = {"SRV0", "SRV1"};
    private static final String CRM_HOSTNAME = "SRV0";

    @Override
    public boolean newMonitoringRecord(final IMonitoringRecord record) {
        if (!(record instanceof OperationExecutionRecord)) {
            return true;
        }

        final OperationExecutionRecord execution =
                (OperationExecutionRecord) record;

        if (execution.className.equals(Bookstore.class.getName())) {
            execution.hostName = HostNameRewriterPlugin.BOOKSTORE_HOSTNAME;
        } else if (execution.className.equals(CRM.class.getName())) {
            execution.hostName = HostNameRewriterPlugin.CRM_HOSTNAME;
        } else if (execution.className.equals(Catalog.class.getName())) {
            if (HostNameRewriterPlugin.rnd.nextInt(99) < HostNameRewriterPlugin.RND_PERCENTILE_HOST_IDX_1) {
                execution.hostName = HostNameRewriterPlugin.CATALOG_HOSTNAMES[0];
            } else {
                execution.hostName = HostNameRewriterPlugin.CATALOG_HOSTNAMES[1];
            }
        }
        HostNameRewriterPlugin.MONITORING_CTRL.newMonitoringRecord(record);

        return true;
    }

    @Override
    public boolean execute() {
        return true; // do nothing
    }

    @Override
    public void terminate(final boolean error) {
        return; // do nothing
    }
}
