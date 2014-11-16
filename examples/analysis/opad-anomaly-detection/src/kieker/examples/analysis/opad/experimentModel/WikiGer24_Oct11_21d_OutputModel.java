/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.analysis.opad.experimentModel;

/**
 * @author duelle
 *
 * @since 1.11
 */
public class WikiGer24_Oct11_21d_OutputModel {
	private long pageRequests;
	private long forecast;
	private double confidence;

	public long getForecast() {
		return this.forecast;
	}

	public void setForecast(final long forecast) {
		this.forecast = forecast;
	}

	public double getConfidence() {
		return this.confidence;
	}

	public void setConfidence(final double confidence) {
		this.confidence = confidence;
	}

	public long getPageRequests() {
		return this.pageRequests;
	}

	public void setPageRequests(final long pageRequests) {
		this.pageRequests = pageRequests;
	}
}
