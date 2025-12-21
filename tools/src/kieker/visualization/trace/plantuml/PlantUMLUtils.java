/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.visualization.trace.plantuml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for PlantUML sequence diagram generation.
 *
 * @author Yorrick Josuttis
 */
@SuppressWarnings("PMD.TooManyMethods")
public final class PlantUMLUtils {

    public static final String ENCODING = "UTF-8";

    public static final String START_PUML = "@startuml";
    public static final String END_PUML = "@enduml";

    public static final String PLANTUML_PREAMBLE;

    private static final String PLANTUML_PREAMBLE_PATH = "sequence-preamble.puml";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantUMLUtils.class);

    static {
        final StringBuilder sb = new StringBuilder();
        boolean error = true;

        try (final InputStream is = PlantUMLSequenceDiagramFilter.class.getClassLoader().getResourceAsStream(PLANTUML_PREAMBLE_PATH)) {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(is, ENCODING))) {
                String line = br.readLine();
                while (line != null) {
                    sb.append(line).append(System.lineSeparator());
                    line = br.readLine();
                }
                error = false;
            }
        } catch (final IOException exc) {
            LOGGER.error("Could not read PlantUML preamble from resource: {}", PLANTUML_PREAMBLE_PATH, exc);
        } finally {
            if (error) {
                sb.append("' Could not read PlantUML preamble from resource: ").append(PLANTUML_PREAMBLE_PATH)
                        .append(System.lineSeparator());
            }
            PLANTUML_PREAMBLE = sb.toString();
        }
    }

    private PlantUMLUtils() {
        // utility class
    }

    /**
     * Generates a PlantUML actor definition.
     * 
     * @param name  the internal name of the actor
     * @param label the label of the actor
     * 
     * @return the PlantUML actor definition
     */
    public static String actor(final String name, final String label) {
        return new StringBuilder("actor ").append(label).append(" as ").append(name).toString();
    }

    /**
     * Generates a PlantUML participant definition.
     * 
     * @param name  the internal name of the participant
     * @param label the label of the participant
     * 
     * @return the PlantUML participant definition
     */
    public static String participant(final String name, final String label) {
        return new StringBuilder("participant ").append(label).append(" as ").append(name).toString();
    }

    /**
     * Generates a PlantUML step definition.
     * 
     * @return the PlantUML step definition
     */
    public static String step() {
        return "|||";
    }

    /**
     * Generates a PlantUML activation definition.
     * 
     * @param name the name of the activation
     * 
     * @return the PlantUML activation definition
     */
    public static String activate(final String name) {
        return new StringBuilder("activate ").append(name).toString();
    }

    /**
     * Generates a PlantUML deactivation definition.
     * 
     * @param name the name of the deactivation
     * 
     * @return the PlantUML deactivation definition
     */
    public static String deactivate(final String name) {
        return new StringBuilder("deactivate ").append(name).toString();
    }

    /**
     * Generates a PlantUML message definition.
     * 
     * @param from  the sender of the message
     * @param to    the receiver of the message
     * @param label the label of the message
     * @param arrow the arrow style of the message
     * 
     * @return the PlantUML message definition
     */
    public static String message(final String from, final String to, final String label, final String arrow) {
        return new StringBuilder(from).append(' ').append(arrow).append(' ').append(to).append(" : ")
                .append(label.replace("_", "&#95;")).toString(); // replace underscore to avoid PlantUML parsing issues
    }

    /**
     * Generates a PlantUML synchronous message definition.
     * 
     * @param from  the sender of the message
     * @param to    the receiver of the message
     * @param label the label of the message
     * 
     * @return the PlantUML synchronous message definition
     */
    public static String synchronousMessage(final String from, final String to, final String label) {
        return message(from, to, label, "->");
    }

    /**
     * Generates a PlantUML asynchronous message definition.
     * 
     * @param from  the sender of the message
     * @param to    the receiver of the message
     * @param label the label of the message
     * 
     * @return the PlantUML asynchronous message definition
     */
    public static String asynchronousMessage(final String from, final String to, final String label) {
        return message(from, to, label, "->>");
    }

    /**
     * Generates a PlantUML asynchronous return message definition.
     * 
     * @param from  the sender of the message
     * @param to    the receiver of the message
     * @param label the label of the message
     * 
     * @return the PlantUML asynchronous return message definition
     */
    public static String asynchronousReturnMessage(final String from, final String to, final String label) {
        return message(from, to, label, "-->>");
    }

    /**
     * Generates a PlantUML dot identifier.
     * 
     * @param id the identifier number
     * 
     * @return the PlantUML dot identifier
     */
    public static String dotId(final int id) {
        return new StringBuilder("O").append(id).toString();
    }

    /**
     * Generates a PlantUML label.
     * 
     * @param names the parts of the label
     * 
     * @return the PlantUML label
     */
    public static String label(final String... names) {
        if (names == null || names.length == 0) {
            return " ";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append('"');
        for (int i = 0; i < names.length; i++) {
            // replace backslash with double backslash to avoid PlantUML parsing issues
            if (names[i] == null) {
                sb.append(" ");
            } else {
                sb.append(names[i].replace("\\", "\\\\"));
            }
            if (i < names.length - 1) {
                sb.append("\\n");
            }
        }
        sb.append('"');
        return sb.toString();
    }

}