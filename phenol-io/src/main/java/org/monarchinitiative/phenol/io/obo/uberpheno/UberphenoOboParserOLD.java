package org.monarchinitiative.phenol.io.obo.uberpheno;

import java.io.File;
import java.io.IOException;

import org.monarchinitiative.phenol.ontology.data.Relationship;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.formats.uberpheno.UberphenoOntology;


import org.monarchinitiative.phenol.io.base.OntologyOboParser;
import org.monarchinitiative.phenol.io.obo.OboImmutableOntologyLoader;
import org.monarchinitiative.phenol.ontology.data.ImmutableOntology;
import org.monarchinitiative.phenol.ontology.data.TermId;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;

/**
 * Facade class for parsing the Uberpheno from an OBO file.
 *
 * <h5>Usage Example</h5>
 *
 * <pre>
 * String fileName = "crossSpeciesOntology.obo";
 * UberphenoOBOParser parser = new UberphenoOBOParser(new File(fileName));
 * Uberphenontology Uberpheno;
 * try {
 *   Uberpheno = parser.parse();
 * } catch (IOException e) {
 *   System.err.println("Problem reading file " + fileName + ": " + e.getMessage());
 * }
 * </pre>
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public final class UberphenoOboParserOLD implements OntologyOboParser<UberphenoOntology> {

  /** Path to the OBO file to parse. */
  private final File oboFile;

  /** Whether debugging is enabled or not. */
  private final boolean debug;

  /**
   * Constructor.
   *
   * @param oboFile The OBO file to read.
   * @param debug Whether or not to enable debugging.
   */
  public UberphenoOboParserOLD(File oboFile, boolean debug) {
    this.oboFile = oboFile;
    this.debug = debug;
  }

  /**
   * Constructor, disabled debugging.
   *
   * @param oboFile The OBO file to read.
   */
  public UberphenoOboParserOLD(File oboFile) {
    this(oboFile, false);
  }

  /**
   * Parse OBO file into {@link UberphenoOntology} object.
   *
   * @return {@link UberphenoOntology} from parsing OBO file.
   * @throws IOException In case of problems with file I/O.
   */
  @Override
  public UberphenoOntology parse() throws IOException {
    final OboImmutableOntologyLoader loader =
        new OboImmutableOntologyLoader(oboFile, debug);
    final UberphenoOboFactoryOLD factory = new UberphenoOboFactoryOLD();
    final ImmutableOntology o = loader.load(factory);

    // Convert ImmutableOntology into Uberphenontology. The casts here are ugly and require the
    // @SuppressWarnings above but this saves us one factory layer of indirection.
    return new UberphenoOntology(
        (ImmutableSortedMap<String, String>) o.getMetaInfo(),
        o.getGraph(),
        o.getRootTermId(),
        o.getNonObsoleteTermIds(),
        o.getObsoleteTermIds(),
        (ImmutableMap<TermId, Term>) o.getTermMap(),
        (ImmutableMap<Integer, Relationship>) o.getRelationMap());
  }

  /** @return The OBO file to parse. */
  @Override
  public File getOboFile() {
    return oboFile;
  }
}
