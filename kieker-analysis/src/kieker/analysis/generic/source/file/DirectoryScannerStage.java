/***************************************************************************
 * Copyright (C) 2023 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.analysis.generic.source.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Predicate;

import teetime.stage.basic.AbstractFilter;

/**
 * Scan a directory for contained files.
 *
 * @author Reiner Jung
 * @since 1.3.0
 *
 */
public class DirectoryScannerStage extends AbstractFilter<Path> {

	private final boolean recursive;
	private final Predicate<Path> fileFilter;
	private final Predicate<Path> directoryFilter;

	public DirectoryScannerStage(final boolean recursive, final Predicate<Path> directoryFilter,
			final Predicate<Path> fileFilter) {
		this.recursive = recursive;
		this.directoryFilter = directoryFilter;
		this.fileFilter = fileFilter;
	}

	@Override
	protected void execute(final Path directory) throws Exception {
		if (this.recursive) {
			Files.walkFileTree(directory, this.createVisitor(directory));
		} else {
			for (final File file : directory.toFile().listFiles()) {
				if (this.fileFilter.test(file.toPath())) {
					this.outputPort.send(file.toPath());
				}
			}
		}
	}

	private FileVisitor<? super Path> createVisitor(final Path directory) {
		return new SimpleFileVisitor<>() {

			@Override
			public FileVisitResult visitFile(final Path filePath, final BasicFileAttributes attrs) {
				if (!Files.isDirectory(filePath) && DirectoryScannerStage.this.fileFilter.test(filePath)) {
					DirectoryScannerStage.this.outputPort.send(filePath);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
					throws IOException {
				if (dir.equals(directory) || DirectoryScannerStage.this.directoryFilter.test(dir)) {
					return super.preVisitDirectory(dir, attrs);
				} else {
					return FileVisitResult.SKIP_SUBTREE;
				}
			}

			@Override
			public FileVisitResult visitFileFailed(final Path filePath, final IOException exc) {
				DirectoryScannerStage.this.logger.warn("Could not visit {}: ", filePath.toString(), exc.getClass());
				return FileVisitResult.CONTINUE;
			}
		};
	}

}
