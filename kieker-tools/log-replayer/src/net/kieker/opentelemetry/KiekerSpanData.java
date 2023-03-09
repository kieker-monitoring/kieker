package net.kieker.opentelemetry;

import java.util.LinkedList;
import java.util.List;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.sdk.common.InstrumentationLibraryInfo;
import io.opentelemetry.sdk.common.InstrumentationScopeInfo;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.data.EventData;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.data.StatusData;

public class KiekerSpanData implements SpanData {

	private final String name;
	private final SpanContext context;
	private final long startEpoch, endEpoch;
	private final Resource resource;
	private final InstrumentationLibraryInfo library;
	private final SpanContext parentContext;
		
	public KiekerSpanData(String name, SpanContext context, SpanContext parentContext, long startEpoch, long endEpoch, Resource resource,
			InstrumentationLibraryInfo library) {
		this.name = name;
		this.context = context;
		this.parentContext = parentContext;
		this.startEpoch = startEpoch;
		this.endEpoch = endEpoch;
		this.resource = resource;
		this.library = library;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public SpanKind getKind() {
		return SpanKind.CLIENT;
	}

	@Override
	public SpanContext getSpanContext() {
		return context;
	}

	@Override
	public SpanContext getParentSpanContext() {
		return parentContext;
	}

	@Override
	public StatusData getStatus() {
		return new StatusData() {
			
			@Override
			public StatusCode getStatusCode() {
				return StatusCode.OK;
			}
			
			@Override
			public String getDescription() {
				return "Ok";
			}
		};
	}

	@Override
	public long getStartEpochNanos() {
		return startEpoch;
	}

	@Override
	public Attributes getAttributes() {
		return resource.getAttributes();
	}

	@Override
	public List<EventData> getEvents() {
		return new LinkedList<>();
	}

	@Override
	public List<LinkData> getLinks() {
		return new LinkedList<>();
	}

	@Override
	public long getEndEpochNanos() {
		return endEpoch;
	}

	@Override
	public boolean hasEnded() {
		return true;
	}

	@Override
	public int getTotalRecordedEvents() {
		return 1;
	}

	@Override
	public int getTotalRecordedLinks() {
		return 1;
	}

	@Override
	public int getTotalAttributeCount() {
		return 1;
	}

	@Override
	public InstrumentationLibraryInfo getInstrumentationLibraryInfo() {
		return library;
	}

	@Override
	public Resource getResource() {
		return resource;
	}

}
