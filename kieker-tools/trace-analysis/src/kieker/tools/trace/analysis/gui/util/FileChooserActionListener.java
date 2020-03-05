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

package kieker.tools.trace.analysis.gui.util;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.text.JTextComponent;

/**
 * An action listener which can be used to choose a file or a directory for a given text component.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public final class FileChooserActionListener implements ActionListener {

	private final JFileChooser fileChooser = new JFileChooser();
	private final JTextComponent textComponent;
	private final int selectionMode;
	private final Component parent;

	private FileChooserActionListener(final JTextComponent textComponent, final int selectionMode, final Component parent) {
		this.textComponent = textComponent;
		this.selectionMode = selectionMode;
		this.parent = parent;
	}

	public static FileChooserActionListener createFileChooserActionListener(final JTextComponent textComponent, final Component parent) {
		return new FileChooserActionListener(textComponent, JFileChooser.FILES_ONLY, parent);
	}

	public static FileChooserActionListener createDirectoryChooserActionListener(final JTextComponent textComponent, final Component parent) {
		return new FileChooserActionListener(textComponent, JFileChooser.DIRECTORIES_ONLY, parent);
	}

	@Override
	public void actionPerformed(final ActionEvent event) {
		this.initializeFileChooser();
		this.showFileChooser();
	}

	private void initializeFileChooser() {
		final String currentPath = this.textComponent.getText();
		if (null == currentPath) {
			this.fileChooser.setCurrentDirectory(null);
		} else {
			this.fileChooser.setCurrentDirectory(new File(currentPath));
		}
	}

	private void showFileChooser() {
		this.fileChooser.setFileSelectionMode(this.selectionMode);

		final int returnState = this.fileChooser.showOpenDialog(this.parent);
		if (JFileChooser.APPROVE_OPTION == returnState) {
			final File selectedFile = this.fileChooser.getSelectedFile();
			final String selectedFilePath = selectedFile.getAbsolutePath();
			this.textComponent.setText(selectedFilePath);
		}
	}

}
