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

package kieker.tools.dataexchange;

import kieker.common.util.signature.ClassOperationSignaturePair;
import kieker.common.util.signature.Signature;
import kieker.tools.traceAnalysis.systemModel.Execution;

/**
 * Data class to store an operation signature in a handy format.
 * 
 * @author Dominik Olp, Yannic Noller, Thomas Düllmann
 * @version 0.4
 * @since 1.10
 */
public class OperationSignature {

	/**
	 * Kieker Signature object which contains operation name, modifier list, return type and parameter type list.
	 */
	protected Signature signature;

	/**
	 * Name of the corresponding host.
	 */
	private String hostName;

	/**
	 * Name of the corresponding application.
	 */
	private String applicationName;

	/**
	 * Ordered list of package names in the package path for that operation.
	 */
	private String[] packagePath;

	/**
	 * Name of the related class.
	 */
	private String className;

	private String stringSignature;

	/**
	 * Creates an OperationSignature object from an Execution object.
	 * 
	 * @param exec
	 *            - Execution object which should parsed into an OperationSignature object
	 */
	public OperationSignature(final Execution exec) {
		this.signature = exec.getOperation().getSignature();
		this.hostName = exec.getAllocationComponent().getExecutionContainer().getName().split("\\+")[0];
		final String applicationNameFull = exec.getAllocationComponent().getExecutionContainer().getName();
		if (applicationNameFull.contains("+")) {
			this.applicationName = applicationNameFull.split("\\+")[1];
		} else {
			this.applicationName = "<not-set>";
		}

		final String[] fullQualifiedName = exec.getOperation().getComponentType().getFullQualifiedName().split("\\.");
		this.packagePath = new String[fullQualifiedName.length - 1];
		System.arraycopy(fullQualifiedName, 0, this.packagePath, 0, this.packagePath.length);
		this.className = exec.getOperation().getComponentType().getTypeName();
	}

	/**
	 * Creates an OperationSignature object from host name, application name and the full qualified operation signature as String.
	 * 
	 * @param hostname
	 *            - name of the host in which the operation is executed
	 * @param applicationName
	 *            - name of the application in which the operation is executed
	 * @param fqOperationSignature
	 *            - full qualified signature of an operation, e.g. package+.class.method(param*)
	 */
	public OperationSignature(final String hostname, final String applicationName, final String fqOperationSignature) {
		this.init(hostname, applicationName, fqOperationSignature);
	}

	/**
	 * Creates an OperationSignature object from String which was created by local toString method.
	 * 
	 * @param inputData
	 *            - String of the data created by toString() in form: host;application;package+.class.method(param*)
	 */
	public OperationSignature(final String inputData) {
		final String[] splittedInputData = inputData.split(";");
		this.init(splittedInputData[0], splittedInputData[1], splittedInputData[2]);
	}

	private void init(final String hostname, final String appName, final String fqOperationSignature) {
		final ClassOperationSignaturePair cosp = ClassOperationSignaturePair.splitOperationSignatureStr(fqOperationSignature);
		this.signature = cosp.getSignature();

		final String[] fqClass = cosp.getFqClassname().split("\\.");

		this.className = cosp.getSimpleClassname();

		this.packagePath = new String[fqClass.length - 1];
		System.arraycopy(fqClass, 0, this.packagePath, 0, fqClass.length - 1);

		this.hostName = hostname;
		this.applicationName = appName;
		this.stringSignature = fqOperationSignature;
	}

	public String getApplicationName() {
		return this.applicationName;
	}

	public String getClassName() {
		return this.className;
	}

	public String getHostName() {
		return this.hostName;
	}

	/**
	 * Returns this object as String for database inserts.
	 * 
	 * @return String in form: host.application.package+.class.method(param*)
	 */
	public String getFullQualifiedOperationString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.hostName);
		sb.append('.');
		sb.append(this.applicationName);
		sb.append('.');
		for (final String s : this.packagePath) {
			sb.append(s);
			sb.append('.');
		}
		sb.append(this.className);
		sb.append('.');
		sb.append(this.getName());
		sb.append('(');
		final String[] paramTypeList = this.getParamTypeList();
		for (int i = 0; i < paramTypeList.length; i++) {
			sb.append(paramTypeList[i]);
			if (i < (paramTypeList.length - 1)) {
				sb.append(',');
			}
		}
		sb.append(')');

		return sb.toString();
	}

	public String[] getModifierList() {
		return this.signature.getModifier();
	}

	public String getName() {
		return this.signature.getName();
	}

	public String[] getPackagePath() {
		return this.packagePath; // NOPMD returning String[] is intended
	}

	public String[] getParamTypeList() {
		return this.signature.getParamTypeList();
	}

	public String getReturnType() {
		return this.signature.getReturnType();
	}

	public String getStringSignature() {
		return this.stringSignature;
	}

	public void setApplicationName(final String applicationName) {
		this.applicationName = applicationName;
	}

	public void setClassName(final String className) {
		this.className = className;
	}

	public void setHostName(final String hostName) {
		this.hostName = hostName;
	}

	public void setPackagePath(final String[] packagePath) { // NOPMD directly storing is intended
		this.packagePath = packagePath;
	}

	public void setStringSignature(final String stringSignature) {
		this.stringSignature = stringSignature;
	}

	/**
	 * Returns this object as String.
	 * 
	 * @return String in form: host;application;fullqualifiedSignature
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.hostName);
		sb.append(';');
		sb.append(this.applicationName);
		sb.append(';');
		sb.append(this.stringSignature);
		return sb.toString();
	}
}
