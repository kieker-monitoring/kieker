/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.livedemo.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.examples.livedemo.analysis.LiveDemoAnalysis;
import kieker.examples.livedemo.analysis.sink.CPUUtilizationDisplayFilter;
import kieker.examples.livedemo.analysis.sink.ClassLoadingDisplayFilter;
import kieker.examples.livedemo.analysis.sink.CompilationDisplayFilter;
import kieker.examples.livedemo.analysis.sink.ComponentFlowDisplayFilter;
import kieker.examples.livedemo.analysis.sink.GCCountDisplayFilter;
import kieker.examples.livedemo.analysis.sink.GCTimeDisplayFilter;
import kieker.examples.livedemo.analysis.sink.JVMHeapDisplayFilter;
import kieker.examples.livedemo.analysis.sink.JVMNonHeapDisplayFilter;
import kieker.examples.livedemo.analysis.sink.MemoryDisplayFilter;
import kieker.examples.livedemo.analysis.sink.MethodFlowDisplayFilter;
import kieker.examples.livedemo.analysis.sink.MethodResponsetimeDisplayFilter;
import kieker.examples.livedemo.analysis.sink.SwapDisplayFilter;
import kieker.examples.livedemo.analysis.sink.ThreadsStatusDisplayFilter;
import kieker.examples.livedemo.common.EnrichedOperationExecutionRecord;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Bjoern Weissenfels, Nils Christian Ehmke
 * 
 * @since 1.9
 */
@ManagedBean(name = "analysisBean", eager = true)
@ApplicationScoped
public class AnalysisBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisBean.class);

	public AnalysisBean() {
		try {
			LiveDemoAnalysis.getInstance().initializeAnalysis();
		} catch (final IllegalStateException ex) {
			AnalysisBean.LOGGER.error("Could not initialize analysis", ex);
		} catch (final AnalysisConfigurationException ex) {
			AnalysisBean.LOGGER.error("Could not initialize analysis", ex);
		}
	}

	@PostConstruct
	protected void startThreads() {
		LiveDemoAnalysis.getInstance().startAnalysis();
	}

	public SystemModelRepository getSystemModelRepository() {
		return LiveDemoAnalysis.getInstance().getSystemModelRepository();
	}

	public CPUUtilizationDisplayFilter getCPUUtilizationDisplayFilter() {
		return LiveDemoAnalysis.getInstance().getCPUUtilizationDisplayFilter();
	}

	public MemoryDisplayFilter getMemoryDisplayFilter() {
		return LiveDemoAnalysis.getInstance().getMemoryDisplayFilter();
	}

	public SwapDisplayFilter getSwapDisplayFilter() {
		return LiveDemoAnalysis.getInstance().getSwapDisplayFilter();
	}

	public ListCollectionFilter<EnrichedOperationExecutionRecord> getRecordListFilter() {
		return LiveDemoAnalysis.getInstance().getRecordListFilter();
	}

	public MethodResponsetimeDisplayFilter getMethodResponsetimeDisplayFilter() {
		return LiveDemoAnalysis.getInstance().getMethodResponsetimeDisplayFilter();
	}

	public MethodFlowDisplayFilter getMethodFlowDisplayFilter() {
		return LiveDemoAnalysis.getInstance().getMethodFlowDisplayFilter();
	}

	public ComponentFlowDisplayFilter getComponentFlowDisplayFilter() {
		return LiveDemoAnalysis.getInstance().getComponentFlowDisplayFilter();
	}

	public ClassLoadingDisplayFilter getClassLoadingDisplayFilter() {
		return LiveDemoAnalysis.getInstance().getClassLoadingDisplayFilter();
	}

	public ThreadsStatusDisplayFilter getThreadsStatusDisplayFilter() {
		return LiveDemoAnalysis.getInstance().getThreadsStatusDisplayFilter();
	}

	public CompilationDisplayFilter getJitCompilationDisplayFilter() {
		return LiveDemoAnalysis.getInstance().getJitCompilationDisplayFilter();
	}

	public GCCountDisplayFilter getGcCountDisplayFilter() {
		return LiveDemoAnalysis.getInstance().getGcCountDisplayFilter();
	}

	public GCTimeDisplayFilter getGcTimeDisplayFilter() {
		return LiveDemoAnalysis.getInstance().getGcTimeDisplayFilter();
	}

	public JVMHeapDisplayFilter getJvmHeapMemoryDisplayFilter() {
		return LiveDemoAnalysis.getInstance().getJvmHeapMemoryDisplayFilter();
	}

	public JVMNonHeapDisplayFilter getJvmNonHeapMemoryDisplayFilter() {
		return LiveDemoAnalysis.getInstance().getJvmNonHeapMemoryDisplayFilter();
	}

}
