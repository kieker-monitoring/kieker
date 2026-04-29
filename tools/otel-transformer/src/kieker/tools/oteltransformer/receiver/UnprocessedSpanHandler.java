package kieker.tools.oteltransformer.receiver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.io.BaseEncoding;

import io.opentelemetry.proto.trace.v1.Span;

public class UnprocessedSpanHandler {
	private Map<String, List<Span>> unprocessedSpans = new HashMap<>();
	
	public void addUnprocessedSpan(Span span) {
		final String parentSpanId = BaseEncoding.base16().lowerCase().encode(span.getParentSpanId().toByteArray());
		List<Span> spans = unprocessedSpans.get(parentSpanId);
		if (spans == null) {
			spans = new LinkedList<Span>();
			unprocessedSpans.put(parentSpanId, spans);
		}
		spans.add(span);
	}
	
	public List<Span> getUnprocessedSpans(String parentId) {
		List<Span> spans = unprocessedSpans.get(parentId);
		if (spans != null) {
			unprocessedSpans.put(parentId, null);
			return spans;
		}
		return null;
	}
}
