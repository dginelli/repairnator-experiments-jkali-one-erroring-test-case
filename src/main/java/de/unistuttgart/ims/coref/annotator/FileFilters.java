package de.unistuttgart.ims.coref.annotator;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FileFilters {
	static public FileFilter xmi = new FileFilter() {

		@Override
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().endsWith(".xmi");
		}

		@Override
		public String getDescription() {
			return "UIMA Xmi Files";
		}
	};

	static public FileFilter txt = new FileFilter() {

		@Override
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().endsWith(".txt");
		}

		@Override
		public String getDescription() {
			return "Plain text files";
		}

	};
}
