/***************************************************************************
 * Copyright 2026 Kieker Project (http://kieker-monitoring.net)
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
import java.util.List;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
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
        } catch (UnsupportedOperationException e) {
            LOGGER.warn("PlantUML failed to generate {} for {}. Skipping this file.", format.getExtension(),
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

    /**
     * Merge all generated PDF files into a single PDF file.
     */
    private void mergePdfFiles() {
        final File outputFile = computeMergedOutputFile(generatedPdfFiles.get(0));
        final File tmpFile = new File(outputFile.getAbsolutePath() + ".tmp");

        try {
            Files.deleteIfExists(tmpFile.toPath());

            final PDFMergerUtility merger = new PDFMergerUtility();
            merger.setDestinationFileName(tmpFile.getAbsolutePath());

            for (final File src : generatedPdfFiles) {
                merger.addSource(src);
            }

            // tempfile-based approach using no main-memory; since there is no restricted
            // size it is safer for large PDFs
            merger.mergeDocuments(IOUtils.createTempFileOnlyStreamCache());

            Files.move(tmpFile.toPath(), outputFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);

            LOGGER.info("Merged {} PDF files into: {}", generatedPdfFiles.size(), outputFile.getAbsolutePath());
        } catch (final IOException e) {
            LOGGER.error("Failed to merge PDF files", e);
        }
    }

    private static File computeMergedOutputFile(final File file) {
        final String abs = file.getAbsolutePath();
        // foo-<fileCount>.pdf -> foos.pdf
        final String outputPath = abs.replaceAll("-\\d+\\.pdf$", "s.pdf");
        return new File(outputPath);
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