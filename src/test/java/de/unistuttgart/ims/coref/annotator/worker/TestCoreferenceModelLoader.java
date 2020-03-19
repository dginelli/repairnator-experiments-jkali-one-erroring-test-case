package de.unistuttgart.ims.coref.annotator.worker;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.prefs.Preferences;

import org.apache.uima.UIMAException;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import de.unistuttgart.ims.coref.annotator.CoreferenceModelListener;
import de.unistuttgart.ims.coref.annotator.api.Entity;
import de.unistuttgart.ims.coref.annotator.api.EntityGroup;
import de.unistuttgart.ims.coref.annotator.document.CoreferenceModel;

public class TestCoreferenceModelLoader {
	JCas jcas;
	CoreferenceModelListener modelListener;

	@Before
	public void setUp() {
		modelListener = new CoreferenceModelListener() {

			@Override
			public void annotationEvent(Event event, Annotation annotation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void entityEvent(Event event, Entity entity) {
				// TODO Auto-generated method stub

			}

			@Override
			public void entityGroupEvent(Event event, EntityGroup entity) {
				// TODO Auto-generated method stub

			}

			@Override
			public void annotationMovedEvent(Annotation annotation, Object from, Object to) {
				// TODO Auto-generated method stub

			}
		};
	}

	@Test
	public void testLoading() throws UIMAException, SAXException, IOException {
		testResource("/rjmw.0.xmi");
		testResource("/Text_1804.xmi");
		testResource("/DieLeidenDesJungenWerther.xmi");
		testResource("/Annika+Bearbeitung+4.xmi");
		testResource("/Rowlandson_Tanja.xmi");
	}

	public void testResource(String s) throws UIMAException, SAXException, IOException {
		jcas = JCasFactory.createJCas();
		XmiCasDeserializer.deserialize(getClass().getResourceAsStream(s), jcas.getCas(), true);
		DocumentModelLoader cml = new DocumentModelLoader(cm -> {
		}, jcas);
		CoreferenceModel model = cml.load(Preferences.userRoot()).getCoreferenceModel();
		assertNotNull(model);
		assertNotNull(model.getMentions(20));
	}
}
