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
package kieker.analysis.behavior.signature.processor;

import kieker.analysis.behavior.events.EntryCallEvent;
import kieker.analysis.behavior.model.UserSession;

import teetime.stage.basic.AbstractFilter;

/**
 * Filter to rewrite and process class and operation signatures.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class TraceSignatureProcessorFilter extends AbstractFilter<UserSession> {

	private final ITraceSignatureProcessor processor;

	/**
	 * Create the cleanup.
	 *
	 * @param processor
	 *            rewrite rule class.
	 */
	public TraceSignatureProcessorFilter(final ITraceSignatureProcessor processor) {
		this.processor = processor;
	}

	@Override
	protected void execute(final UserSession session) throws Exception {
		for (final EntryCallEvent event : session.getEvents()) {
			event.setClassSignature(this.processor.rewriteClassSignature(event.getClassSignature()));
			event.setOperationSignature(this.processor.rewriteOperationSignature(event.getOperationSignature()));
		}

		this.outputPort.send(session);
	}

}
