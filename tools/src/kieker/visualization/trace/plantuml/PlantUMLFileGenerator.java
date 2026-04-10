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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.tools.common.TraceAnalysisParameters;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import teetime.framework.AbstractConsumerStage;

/**
 * Stage that generates image files (PNG, SVG, PDF) from PlantUML source files.
 * 
 * @author Yorrick Josuttis
 */
public class PlantUMLFileGenerator extends AbstractConsumerStage<File> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlantUMLFileGenerator.class);

	private final boolean generatePNG;
	private final boolean generateSVG;
	private final boolean generatePDF;

	/* List of generated PDF files to be merged. */
	private final List<File> generatedPdfFiles = new ArrayList<>();

	public PlantUMLFileGenerator(final TraceAnalysisParameters parameters) {
		boolean isPNG = parameters.isPng();
		final boolean isSVG = parameters.isSvg();
		final boolean isPDF = parameters.isPdf();

		if (!isPNG && !isSVG && !isPDF) {
			// If no specific format is requested, default to PNG
			isPNG = true;
		}

		this.generatePNG = isPNG;
		this.generateSVG = isSVG;
		this.generatePDF = isPDF;
	}

	@Override
	protected void execute(final File file) throws Exception {
		final String pumlSource = new String(Files.readAllBytes(file.toPath()));

		if (this.generatePNG) {
			this.generate(OutputFormat.PNG, pumlSource, file);
		}
		if (this.generateSVG) {
			this.generate(OutputFormat.SVG, pumlSource, file);
		}
		if (this.generatePDF) {
			final File pdfFile = this.generate(OutputFormat.PDF, pumlSource, file);
			if (pdfFile != null) {
				generatedPdfFiles.add(pdfFile);
			}
		}
	}

	private File generate(final OutputFormat format, final String pumlSource, final File file) {
		try {
			final byte[] imageBytes = generateImage(pumlSource, format.getFileFormat());
			final File outputFile = new File(file.getAbsolutePath().replaceAll("\\.puml$", format.getExtension()));
			try (final var fos = Files.newOutputStream(outputFile.toPath())) {

				fos.write(imageBytes);
			}
			return outputFile;
		} catch (OutOfMemoryError e) {
			LOGGER.warn("OutOfMemoryError generating {} for {} (we skip those large files)", format.getExtension(),
					file.getName());
			return null;
		} catch (IOException e) {
			LOGGER.warn("Failed to generate {} for {}: {}", format.getExtension(), file.getName(), e.getMessage());
			return null;
		}
	}

	private byte[] generateImage(final String pumlSource, final FileFormat format) throws IOException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final SourceStringReader reader = new SourceStringReader(pumlSource);
		reader.outputImage(out, new FileFormatOption(format));
		return out.toByteArray();
	}

	@Override
	protected void onTerminating() {
		super.onTerminating();
		if (!generatedPdfFiles.isEmpty()) {
			mergePdfFiles();
		}
	}

	private void mergePdfFiles() {
		// if only one PDF was generated, no merge needed
		if (generatedPdfFiles.size() <= 1) {
			return;
		}

		// filter out non-existing files and sort by name for deterministic merge order
		final List<File> sources = generatedPdfFiles.stream()
				.filter(f -> f != null && f.exists())
				.sorted(Comparator.comparing(File::getName))
				.collect(Collectors.toCollection(ArrayList::new));

		// if after filtering we have at most one file, no merge needed
		if (sources.size() <= 1) {
			return;
		}

		final File first = sources.get(0);
		final File outputFile = computeMergedOutputFile(first);
		final File tmpFile = new File(outputFile.getAbsolutePath() + ".tmp");

		// safety: if outputFile somehow appears in sources, filter it out
		sources.removeIf(f -> sameFile(f, outputFile) || sameFile(f, tmpFile));

		if (sources.size() <= 1) {
			return;
		}

		try {
			Files.deleteIfExists(tmpFile.toPath());

			final PDFMergerUtility merger = new PDFMergerUtility();
			merger.setDestinationFileName(tmpFile.getAbsolutePath());

			for (final File src : sources) {
				merger.addSource(src);
			}

			// tempfile-based approach using no main-memory; since there is no restricted
			// size it is safer for large PDFs
			merger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());

			Files.move(tmpFile.toPath(), outputFile.toPath(),
					StandardCopyOption.REPLACE_EXISTING,
					StandardCopyOption.ATOMIC_MOVE);

			LOGGER.info("Merged {} PDF files into: {}", sources.size(), outputFile.getAbsolutePath());
		} catch (final IOException e) {
			LOGGER.error("Failed to merge PDF files", e);
		}
	}

	private static boolean sameFile(final File a, final File b) {
		try {
			return a.getCanonicalFile().equals(b.getCanonicalFile());
		} catch (final IOException e) {
			// as a fallback, compare absolute paths
			return a.getAbsolutePath().equals(b.getAbsolutePath());
		}
	}

	private static File computeMergedOutputFile(final File firstSource) {
		final String abs = firstSource.getAbsolutePath();

		// foo-<fileCount>.pdf -> foos.pdf
		final String stripped = abs.replaceAll("-\\d+\\.pdf$", "s.pdf");
		if (!stripped.equals(abs)) {
			return new File(stripped);
		}

		// everything else: never write into the same file -> -merged.pdf
		if (abs.toLowerCase().endsWith(".pdf")) {
			return new File(abs.substring(0, abs.length() - 4) + "-merged.pdf");
		}
		return new File(abs + "-merged.pdf");
	}

	/**
	 * The supported output formats.
	 * 
	 * @author Yorrick Josuttis
	 */
	private enum OutputFormat {
		SVG(FileFormat.SVG, ".svg"),
		PNG(FileFormat.PNG, ".png"),
		PDF(FileFormat.PDF, ".pdf");

		private final FileFormat fileFormat;
		private final String extension;

		OutputFormat(final FileFormat fileFormat, final String extension) {
			this.fileFormat = fileFormat;
			this.extension = extension;
		}

		public FileFormat getFileFormat() {
			return this.fileFormat;
		}

		public String getExtension() {
			return this.extension;
		}
	}

}
