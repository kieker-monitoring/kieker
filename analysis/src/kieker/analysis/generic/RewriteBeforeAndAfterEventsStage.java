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
package kieker.analysis.generic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kieker.analysis.code.CodeUtils;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Rewrite logging information collected by the kieker-lang-pack-c and resolve function pointer
 * references to actual function calls and files.
 *
 * @author Reiner Jung
 * @since 1.0
 */
public class RewriteBeforeAndAfterEventsStage extends AbstractConsumerStage<IMonitoringRecord> {

	private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort(IMonitoringRecord.class);

	private final File addrlineExecutable;
	private final Map<String, AddrOutput> addressMap = new HashMap<>();
	private final File modelExecutable;

	private final boolean caseInsensitive;

	public RewriteBeforeAndAfterEventsStage(final File addrLineExecutable, final File modelExecutable,
			final boolean caseInsensitive) {
		this.addrlineExecutable = addrLineExecutable;
		this.modelExecutable = modelExecutable;
		this.caseInsensitive = caseInsensitive;
	}

	@Override
	protected void execute(final IMonitoringRecord element) throws Exception {
		if (element instanceof BeforeOperationEvent) {
			final BeforeOperationEvent before = (BeforeOperationEvent) element;
			final AddrOutput rewriteInfo = this.findRewriteInfo(before.getOperationSignature());
			this.outputPort.send(new BeforeOperationEvent(before.getTimestamp(), before.getTraceId(),
					before.getOrderIndex(), rewriteInfo.name, rewriteInfo.filename));
		} else if (element instanceof AfterOperationEvent) {
			final AfterOperationEvent before = (AfterOperationEvent) element;
			final AddrOutput rewriteInfo = this.findRewriteInfo(before.getOperationSignature());
			this.outputPort.send(new AfterOperationEvent(before.getTimestamp(), before.getTraceId(),
					before.getOrderIndex(), rewriteInfo.name, rewriteInfo.filename));
		} else {
			this.outputPort.send(element);
		}
	}

	private AddrOutput findRewriteInfo(final String address) throws IOException, InterruptedException {
		final AddrOutput addrOutput = this.addressMap.get(address);
		if (addrOutput == null) {
			final Process process = Runtime.getRuntime().exec(String.format("%s -e %s -p -C -f %s",
					this.addrlineExecutable.getCanonicalPath(), this.modelExecutable, address));
			process.waitFor();
			new BufferedReader(new InputStreamReader(process.getErrorStream())).lines().forEach(new Consumer<String>() {
				@Override
				public void accept(final String arg0) {
					RewriteBeforeAndAfterEventsStage.this.logger.error("Error output from addr2line {}", arg0);
				}
			});

			new BufferedReader(new InputStreamReader(process.getInputStream())).lines().forEach(new Consumer<String>() {

				private final Pattern pattern = Pattern.compile("^([\\w.]+) at ([\\w\\?/\\.\\-]+):([\\d\\?]*)( .*)?$");

				@Override
				public void accept(final String string) {
					final Matcher matcher = this.pattern.matcher(string);
					if (matcher.find()) {
						final Integer linenumber = matcher.group(3).equals("?") ? null // NOCS
								: Integer.parseInt(matcher.group(3));
						final AddrOutput addrOutput = RewriteBeforeAndAfterEventsStage.this.caseInsensitive
								? new AddrOutput(matcher.group(1).toLowerCase(Locale.ROOT), // NOCS
										matcher.group(2).toLowerCase(Locale.ROOT), linenumber)
								: new AddrOutput(matcher.group(1), matcher.group(2), linenumber);
						RewriteBeforeAndAfterEventsStage.this.addressMap.put(address, addrOutput);
					} else if ("?? ??:0".equals(string)) {
						RewriteBeforeAndAfterEventsStage.this.addressMap.put(address,
								new AddrOutput(address, CodeUtils.NO_FILE, 0));
					} else {
						RewriteBeforeAndAfterEventsStage.this.logger.error("Cannot process result '{}' for address {}",
								string, address);
					}
				}

			});
			return this.addressMap.get(address);
		} else {
			return addrOutput;
		}
	}

	public OutputPort<IMonitoringRecord> getOutputPort() {
		return this.outputPort;
	}

	private class AddrOutput {

		private final String name;
		private final String filename;
		private final Integer linenumber;

		public AddrOutput(final String name, final String filename, final Integer linenumber) {
			this.name = name;
			this.filename = filename;
			this.linenumber = linenumber;
		}

		@Override
		public String toString() {
			return String.format("%s:%d -- %s", this.filename, this.linenumber, this.name);
		}
	}

}
