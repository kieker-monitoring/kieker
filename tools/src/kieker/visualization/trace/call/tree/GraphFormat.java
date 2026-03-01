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
package kieker.visualization.trace.call.tree;

import java.io.PrintStream;

import kieker.visualization.trace.plantuml.PlantUMLUtils;

/**
 * Strategy enum for graph output formats (DOT and PlantUML).
 * Encapsulates format-specific preamble and epilogue generation.
 *
 * @author Yorrick Josuttis
 */
public enum GraphFormat {
	/**
	 * Graphviz DOT format.
	 */
	DOT(".dot") {
		@Override
		public void writePreamble(final PrintStream ps) {
			ps.println("digraph G {");
		}

		@Override
		public void writeEpilogue(final PrintStream ps) {
			ps.println("}");
		}

		@Override
		public boolean hasOutputPort() {
			return false;
		}
	},

	/**
	 * PlantUML format (wraps DOT in @startuml/@enduml).
	 */
	PLANTUML(".puml") {
		@Override
		public void writePreamble(final PrintStream ps) {
			ps.println(PlantUMLUtils.START_PUML + System.lineSeparator());
			ps.println("digraph G {");
		}

		@Override
		public void writeEpilogue(final PrintStream ps) {
			ps.println("}");
			ps.println(PlantUMLUtils.END_PUML + System.lineSeparator());
		}

		@Override
		public boolean hasOutputPort() {
			return true;
		}
	};

	private final String extension;

	GraphFormat(final String extension) {
		this.extension = extension;
	}

	/**
	 * Write format-specific preamble to the print stream.
	 *
	 * @param ps the print stream
	 */
	public abstract void writePreamble(PrintStream ps);

	/**
	 * Write format-specific epilogue to the print stream.
	 *
	 * @param ps the print stream
	 */
	public abstract void writeEpilogue(PrintStream ps);

	/**
	 * Indicates whether this format requires an output port for file generation.
	 *
	 * @return true if output port is needed (e.g., for PlantUML image generation)
	 */
	public abstract boolean hasOutputPort();

	/**
	 * Get the file extension for this format.
	 *
	 * @return the file extension (e.g., ".dot" or ".puml")
	 */
	public String getExtension() {
		return this.extension;
	}
}
