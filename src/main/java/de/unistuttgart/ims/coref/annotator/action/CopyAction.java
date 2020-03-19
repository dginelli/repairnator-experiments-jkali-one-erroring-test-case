package de.unistuttgart.ims.coref.annotator.action;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import org.kordamp.ikonli.materialdesign.MaterialDesign;

import de.unistuttgart.ims.coref.annotator.Annotator;
import de.unistuttgart.ims.coref.annotator.Constants;
import de.unistuttgart.ims.coref.annotator.Span;
import de.unistuttgart.ims.coref.annotator.TextWindow;

public class CopyAction extends AnnotatorAction {

	private static final long serialVersionUID = 1L;

	TextWindow textWindow;

	public CopyAction(TextWindow dw, Annotator app) {
		super(app, Constants.Strings.ACTION_COPY, MaterialDesign.MDI_CONTENT_COPY);
		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Span span = textWindow.getSelection();
		textWindow.getText().substring(span.begin, span.end);

		Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(new StringSelection(textWindow.getText().substring(span.begin, span.end)), null);
	}

}
