package org.monarchinitiative.phenol.io.obo.go;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import org.monarchinitiative.phenol.formats.go.GoOntology;
import org.monarchinitiative.phenol.graph.IdLabeledEdge;
import org.monarchinitiative.phenol.io.utils.ResourceUtils;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedSet;

/**
 * Testcases that verify whether obo-formatted Go ontology is properly parsed and loaded.
 *
 * @author <a href="mailto:HyeongSikKim@lbl.gov">HyeongSik Kim</a>
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public class GoOboParserTest {
  @Rule public TemporaryFolder tmpFolder = new TemporaryFolder();
  private File goHeadFile;

  private GoOntology ontology;

  @Before
  public void setUp() throws IOException {
    goHeadFile = tmpFolder.newFile("go_head.obo");
    ResourceUtils.copyResourceToFile("/go_head.obo", goHeadFile);
    GoOboParser parser = new GoOboParser(goHeadFile);
    Optional<GoOntology> optOnto = parser.parse();
    this.ontology=optOnto.get();
  }

  @Test
  public void testCanParseGo() {
    GoOboParser parser = new GoOboParser(goHeadFile);
    Optional<GoOntology> optOnto = parser.parse();
    assertTrue(optOnto.isPresent());
  }


  /** The obo file has the three top-level GO terms and one child each (i.e., three asserted is_a links).
   * The input of the GO file should create a new artificial root and should attach each of the top level terms,
   * and thus we expect 3+3=6 edges.
   */
  @Test
  public void testEdgeSetSize() {
    int expected=6;
    final DefaultDirectedGraph<TermId, IdLabeledEdge> graph = ontology.getGraph();
    System.err.println(graph.toString());
    assertEquals(expected, graph.edgeSet().size());
  }

  @Test
  public void testArtificialRootTerm() {
    TermId tid = ontology.getRootTermId();
    Term root = ontology.getTermMap().get(tid);
    TermId expected = TermId.constructWithPrefix("GO:0000000");
    assertEquals(expected,tid);
  }



  @Test
  public void testParseHpoHead() throws IOException {
//    final GoOboParserOLD parser = new GoOboParserOLD(goHeadFile, true);
//    final GoOntology ontology = parser.parse();
    final DefaultDirectedGraph<TermId, IdLabeledEdge> graph = ontology.getGraph();



//
//    assertEquals(
//        "[TermId [prefix=TermPrefix [value=GO], id=0000000], TermId [prefix=TermPrefix [value=GO], id=0000004], TermId [prefix=TermPrefix [value=GO], id=0003674], TermId [prefix=TermPrefix [value=GO], id=0005554], TermId [prefix=TermPrefix [value=GO], id=0005575], TermId [prefix=TermPrefix [value=GO], id=0007582], TermId [prefix=TermPrefix [value=GO], id=0008150], TermId [prefix=TermPrefix [value=GO], id=0008372]]",
//        ImmutableSortedSet.copyOf(ontology.getAllTermIds()).toString());
//
//    assertThat(
//        ImmutableSortedMap.copyOf(ontology.getTermMap()).toString(),
//        startsWith("{TermId"));
//
//    assertThat(
//        ImmutableSortedMap.copyOf(ontology.getTermMap()).toString(),
//        endsWith("description=null, trailingModifiers=null]]]}"));
//
//    assertEquals(
//        "{1=Relationship [source=TermId [prefix=TermPrefix [value=GO], id=0003674], dest=TermId [prefix=TermPrefix [value=GO], id=0000000], id=1, relationshipType=IS_A], 2=Relationship [source=TermId [prefix=TermPrefix [value=GO], id=0005575], dest=TermId [prefix=TermPrefix [value=GO], id=0000000], id=2, relationshipType=IS_A], 3=Relationship [source=TermId [prefix=TermPrefix [value=GO], id=0008150], dest=TermId [prefix=TermPrefix [value=GO], id=0000000], id=3, relationshipType=IS_A]}",
//        ImmutableSortedMap.copyOf(ontology.getRelationMap()).toString());
//
//
//
//    assertEquals(
//        "{data-version=releases/2017-06-16, remark=Includes Ontology(OntologyID(OntologyIRI(<http://purl.obolibrary.org/obo/go/never_in_taxon.owl>))) [Axioms: 18 Logical Axioms: 0]}",
//        ontology.getMetaInfo().toString());
  }
}
