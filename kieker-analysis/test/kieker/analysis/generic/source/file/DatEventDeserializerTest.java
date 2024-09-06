/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.generic.source.file;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.mockito.Mockito.mock; // NOCS
import teetime.framework.OutputPort;

import kieker.analysis.plugin.reader.newio.deserializer.DeserializerStringRegistry;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Clemens Kurz
 *
 * @since 2.0.0
 */
public class DatEventDeserializerTest {

    /**
     * A simple record with '\r' at the end.
     */
    private final static String RECORD = "$0;1693267380061362600;1.15-SNAPSHOT;KIEKER;6d672498b3fb;1;false;0;NANOSECONDS;0\r";

    /**
     * Create test object.
     */
    public DatEventDeserializerTest() {
        // nothing to do
    }

    /**
     * Buffer Size is chosen by the length of the first event + \r
     * this case ensures that the second trace can only be read if (and only if) the edge case is handled correctly
     * The Edge case is that the buffer lands between \r\n and tries to recognize \n and
     * does not run into an ArrayIndexOutOfBoundsException
      * @throws IOException
     */
    @Test
    public void testCarriageReturnLineFeedIsNoteSeperatedByTheBufferSize() throws IOException {
        // setup
        final int bufferSize = RECORD.getBytes().length;

        final List<String> values = new ArrayList<>();
        values.add("myClass"); // is required for the record to find a class and the Charset to get cleared.
        final DeserializerStringRegistry registry = new DeserializerStringRegistry(values);
        final DatEventDeserializer datEventDeserializer = new DatEventDeserializer(bufferSize, registry);

        final OutputPort<IMonitoringRecord> outputPort = mock(OutputPort.class); // NOCS

        // test
        // The record is applied twice since the read bytes are read twice and the end od the array must be reached.
        try (final InputStream is = new ByteArrayInputStream((RECORD + RECORD).getBytes())) {
            datEventDeserializer.processDataStream(is, outputPort);
        }
        // TODO: verify a bit unclear how to do since we could only check side effects and
        //  internal code of the class does not reach the point where the OutputPort.send Method is called.
    }

}
