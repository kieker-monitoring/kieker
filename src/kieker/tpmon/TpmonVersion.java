package kieker.tpmon;

import kieker.tpmon.annotations.TpmonInternal;

/**
 * kieker.tpmon.TpmonVersion
 * 
 * ==================LICENCE=========================
 * Copyright 2006-2008 Matthias Rohr and the Kieker Project 
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
 * ==================================================
 * 
 * This class provides the method getVersion which returns the version number 
 * set during build (String replacement task within build.xml).
 *
 * @author Andre van Hoorn
 */
public class TpmonVersion {

    public static final String ANNOTATIONS_PGK="kieker.tpmon.annotations";

	/*
	 * The VERSION string is updated by the Ant build file, which looks for the
	 * pattern: VERSION = <quote>.*<quote>
	 * 
	 * The string is made private so the compiler can't propagate it into
	 * JMeterUtils This ensures that JMeterUtils always gets the correct
	 * version, even if it is not re-compiled during the build.
	 */
	private static final String VERSION = "0.91-20090424";

	static final String COPYRIGHT = "Copyright (c) 2006-2009 Matthias Rohr and the Kieker Project";

    /**
     * Not instantiable.
     */
	private TpmonVersion() 
	{
		super();
	}

    /**
     * Returns the version String.
     *
     * @return the version String.
     */
    @TpmonInternal()
	public static final String getVERSION() {
		return VERSION;
	}
}
