package kieker.monitoring.core.sampler;

import kieker.monitoring.core.ISamplingController;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
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
 * ==================================================
 */
/**
 * @author Andre van Hoorn
 */
public interface ISampler {

	/**
	 * Triggers this {@link ISampler} to perform a measurement and to pass the data
	 * to the given {@link SamplingController}.
	 * 
	 * @throws Exception thrown to indicate an error.
	 */
	public void sample(final ISamplingController samplingController) throws Exception;
}
