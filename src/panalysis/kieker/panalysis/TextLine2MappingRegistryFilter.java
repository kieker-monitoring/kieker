package kieker.panalysis;

import java.util.Map;

import kieker.common.util.filesystem.FSUtil;
import kieker.panalysis.base.AbstractSink;

public class TextLine2MappingRegistryFilter extends AbstractSink<TextLine2MappingRegistryFilter.INPUT_PORT> {

	static public enum INPUT_PORT {
		TEXT_LINE
	}

	private final Map<Integer, String> stringRegistry;

	public TextLine2MappingRegistryFilter(final Map<Integer, String> stringRegistry) {
		super(INPUT_PORT.class);
		this.stringRegistry = stringRegistry;
	}

	public void execute() {
		final String textLine = (String) this.take(INPUT_PORT.TEXT_LINE);

		final int split = textLine.indexOf('=');
		if (split == -1) {
			this.logger.error("Failed to find character '=' in line: {" + textLine + "}. It must consist of a ID=VALUE pair.");
			return;
		}
		final String key = textLine.substring(0, split);
		// BETTER execute split instead of checking it before with multiple string operations
		final String value = FSUtil.decodeNewline(textLine.substring(split + 1));
		// the leading $ is optional
		final Integer id;
		try {
			id = Integer.valueOf((key.charAt(0) == '$') ? key.substring(1) : key); // NOCS
		} catch (final NumberFormatException ex) {
			this.logger.error("Error reading mapping file, id must be integer", ex);
			return; // continue on errors
		}
		final String prevVal = this.stringRegistry.put(id, value);
		if (prevVal != null) {
			this.logger.error("Found addional entry for id='" + id + "', old value was '" + prevVal + "' new value is '" + value + "'");
		}
	}

}
