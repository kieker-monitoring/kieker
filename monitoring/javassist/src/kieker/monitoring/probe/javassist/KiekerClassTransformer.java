/***************************************************************************
 * Copyright 2024 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.monitoring.probe.javassist;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import kieker.monitoring.util.KiekerPattern;

public class KiekerClassTransformer implements ClassFileTransformer {

	private static final Logger LOGGER = LoggerFactory.getLogger(KiekerClassTransformer.class);

	private final List<KiekerPattern> patternObjects;

	public KiekerClassTransformer(final List<KiekerPattern> patternObjects) {
		this.patternObjects = patternObjects;
	}

	@Override
	public byte[] transform(final ClassLoader loader, final String className, final Class<?> classBeingRedefined,
			final ProtectionDomain protectionDomain, final byte[] classfileBuffer) throws IllegalClassFormatException {
		final String realClassName = className.replaceAll(File.separator, ".");
		for (final KiekerPattern pattern : patternObjects) {
			if (realClassName.equals(pattern.getOnlyClass())) {
				LOGGER.info("Instrumenting: {}", realClassName);
				final ClassPool cp = ClassPool.getDefault();
				try {
					final CtClass cc = cp.get(realClassName);
					if (!cc.isInterface()) {
						// TODO: Interfaces would also require instrumentation of default method; this
						// is left out for now because of the experimental structure

						final MethodInstrumenter instrumenter = new MethodInstrumenter(cp);
						instrumenter.instrumentAllMethods(cc);

						final byte[] byteCode = cc.toBytecode();
						cc.detach();

						return byteCode;
					}
				} catch (NotFoundException | CannotCompileException | IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
