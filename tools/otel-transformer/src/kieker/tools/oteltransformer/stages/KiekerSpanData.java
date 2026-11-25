/***************************************************************************
 * Copyright (C) 2026 Kieker Project (https://kieker-monitoring.net)
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

package kieker.tools.oteltransformer.stages;

import java.util.Collections;
import java.util.List;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.sdk.common.InstrumentationScopeInfo;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.data.EventData;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.data.StatusData;

/**
 * An already finished span that was converted from a Kieker execution.
 *
 * @author Malte Hansen
 */
final class KiekerSpanData implements SpanData {

	private final String name;
	private final SpanContext spanContext;
	private final SpanContext parentSpanContext;
	private final long startEpochNanos;
	private final long endEpochNanos;
	private final Attributes attributes;
	private final Resource resource;
	private final InstrumentationScopeInfo instrumentationScopeInfo;

	KiekerSpanData(final String name, final SpanContext spanContext, final SpanContext parentSpanContext, final long startEpochNanos,
			final long endEpochNanos, final Attributes attributes, final Resource resource, final InstrumentationScopeInfo instrumentationScopeInfo) {
		this.name = name;
		this.spanContext = spanContext;
		this.parentSpanContext = parentSpanContext;
		this.startEpochNanos = startEpochNanos;
		this.endEpochNanos = endEpochNanos;
		this.attributes = attributes;
		this.resource = resource;
		this.instrumentationScopeInfo = instrumentationScopeInfo;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public SpanKind getKind() {
		return SpanKind.INTERNAL;
	}

	@Override
	public SpanContext getSpanContext() {
		return this.spanContext;
	}

	@Override
	public SpanContext getParentSpanContext() {
		return this.parentSpanContext;
	}

	@Override
	public StatusData getStatus() {
		// Kieker executions carry no error information
		return StatusData.unset();
	}

	@Override
	public long getStartEpochNanos() {
		return this.startEpochNanos;
	}

	@Override
	public Attributes getAttributes() {
		return this.attributes;
	}

	@Override
	public List<EventData> getEvents() {
		return Collections.emptyList();
	}

	@Override
	public List<LinkData> getLinks() {
		return Collections.emptyList();
	}

	@Override
	public long getEndEpochNanos() {
		return this.endEpochNanos;
	}

	@Override
	public boolean hasEnded() {
		return true;
	}

	@Override
	public int getTotalRecordedEvents() {
		return 0;
	}

	@Override
	public int getTotalRecordedLinks() {
		return 0;
	}

	@Override
	public int getTotalAttributeCount() {
		return this.attributes.size();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @deprecated required by the interface, use {@link #getInstrumentationScopeInfo()}
	 */
	@Deprecated
	@Override
	public io.opentelemetry.sdk.common.InstrumentationLibraryInfo getInstrumentationLibraryInfo() {
		return io.opentelemetry.sdk.common.InstrumentationLibraryInfo.create(this.instrumentationScopeInfo.getName(),
				this.instrumentationScopeInfo.getVersion(), this.instrumentationScopeInfo.getSchemaUrl());
	}

	@Override
	public InstrumentationScopeInfo getInstrumentationScopeInfo() {
		return this.instrumentationScopeInfo;
	}

	@Override
	public Resource getResource() {
		return this.resource;
	}
}
