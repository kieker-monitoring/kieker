/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.livedemo.view.recordList;

import java.util.Collections;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import kieker.examples.livedemo.common.EnrichedOperationExecutionRecord;
import kieker.examples.livedemo.view.AnalysisBean;

/**
 * @author Bjoern Weissenfels, Nils Christian Ehmke
 * 
 * @since 1.9
 */
@ManagedBean(name = "recordBean", eager = true)
@ViewScoped
public class RecordListBean {

	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;

	private boolean freeze;
	private String freezeButton;
	private String updateForm;

	public RecordListBean() {
		this.freeze = false;
		this.freezeButton = "freeze";
		this.updateForm = "rec";
	}

	public void setAnalysisBean(final AnalysisBean analysisBean) {
		this.analysisBean = analysisBean;
	}

	public String freezeList() {
		if (this.freeze) {
			this.freeze = false;
			this.freezeButton = "freeze";
			this.updateForm = "rec";
		} else {
			this.freeze = true;
			this.freezeButton = "unfreeze";
			this.updateForm = "";
		}
		return "";
	}

	public String getUpdateForm() {
		return this.updateForm;
	}

	public String getFreezeButton() {
		return this.freezeButton;
	}

	public List<EnrichedOperationExecutionRecord> getRecords() {
		final List<EnrichedOperationExecutionRecord> list = this.analysisBean.getRecordListFilter().getList();
		Collections.reverse(list);
		return list;
	}

}
