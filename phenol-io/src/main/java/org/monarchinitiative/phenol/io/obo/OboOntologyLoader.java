package org.monarchinitiative.phenol.io.obo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;

import com.google.common.collect.*;
import org.geneontology.obographs.model.*;
import org.geneontology.obographs.model.meta.BasicPropertyValue;
import org.geneontology.obographs.owlapi.FromOwl;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.monarchinitiative.phenol.io.owl.OwlOntologyEntryFactory;
import org.monarchinitiative.phenol.io.owl.generic.Owl2OboTermFactory;
import org.monarchinitiative.phenol.ontology.data.*;
import org.prefixcommons.CurieUtil;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.monarchinitiative.phenol.graph.IdLabeledEdge;
import org.monarchinitiative.phenol.graph.util.CompatibilityChecker;
import org.monarchinitiative.phenol.io.utils.CurieMapGenerator;

import javax.swing.text.html.Option;

/**
 * This class loads an OBO ontology using the OWLAPI.
 * Load OWL into an {@link ImmutableOntology}.
 *
 * @author <a href="mailto:HyeongSikKim@lbl.gov">HyeongSik Kim</a>
 * @author <a href="mailto:peter.robinson@jax.org">Peter Robinson</a>
 */
public final class OboOntologyLoader {

  private static final Logger LOGGER = LoggerFactory.getLogger(OboOntologyLoader.class);
  private final CurieUtil curieUtil;
  private final File file;
  /** Term ids of non-obsolete Terms. */
  private Collection<TermId> nonDepreTermIdNodes = Sets.newHashSet();
  /** Term ids of obsolete Terms. */
  private Collection<TermId> depreTermIdNodes = Sets.newHashSet();


  /** Key: a TermId; value: corresponding Term object. */
  private SortedMap<TermId, Term> terms = Maps.newTreeMap();
  //private Collection<TermId> termIdNodes = Sets.newHashSet();
  /** The relations are numbered incrementally--this is the key, and the value is the corresponding relation.*/
  private Map<Integer, Relationship> relationMap = Maps.newHashMap();
  /** Factory object that adds OBO-typical data to each term. */
  private final OwlOntologyEntryFactory factory;

  /**
   * Construct an OWL loader that can load an OBO ontology.
   * @param file Path to the OBO file
   */
  public OboOntologyLoader(File file) {
    this.file = file;
    curieUtil = new CurieUtil(CurieMapGenerator.generate());
    this.factory = new Owl2OboTermFactory();
  }

  public Optional<Ontology> load() {
    Optional<Ontology> emptyOntology = Optional.empty();
    Map<TermId,TermId> old2newTermIdMap = new HashMap<>();

    // We first load ontologies expressed in owl using Obographs's FromOwl class.
    OWLOntologyManager m = OWLManager.createOWLOntologyManager();
    OWLOntology ontology;
    try {
      ontology = m.loadOntologyFromOntologyDocument(file);
    } catch (OWLOntologyCreationException e) {
      e.printStackTrace();
      return emptyOntology;
    }
    FromOwl fromOwl = new FromOwl();
    GraphDocument gd = fromOwl.generateGraphDocument(ontology);

    // We assume there is only one graph instance in the graph document instance.
    Graph obograph = gd.getGraphs().get(0);
    if (obograph == null) {
      LOGGER.warn("No graph in the loaded ontology.");
      return emptyOntology;
    }

    List<Node> gNodes = obograph.getNodes();
    if (gNodes == null) {
      LOGGER.warn("No nodes found in the loaded ontology.");
      return emptyOntology;
    }

    List<Edge> gEdges = obograph.getEdges();
    if (gEdges == null) {
      LOGGER.warn("No edges found in the loaded ontology.");
      return emptyOntology;
    }

    // Mapping edges in obographs to termIds in phenol
    int edgeId = 1;
    DefaultDirectedGraph<TermId, IdLabeledEdge> phenolGraph =
      new DefaultDirectedGraph<>(IdLabeledEdge.class);


    // Mapping nodes in obographs to termIds in phenol
    for (Node node : gNodes) {
      if (node.getType()==null || ! node.getType().equals(Node.RDFTYPES.CLASS)) {
        continue; // only take classes-- otherwise, we may get some OIO and IAO entities
      }
      String nodeId = node.getId();
      Optional<String> nodeCurie = curieUtil.getCurie(nodeId);
      if (! nodeCurie.isPresent() ) continue;
      TermId termId = TermId.constructWithPrefix(nodeCurie.get());
      Term term = factory.constructTerm(node, termId);
      if (term.isObsolete()) {
        depreTermIdNodes.add(termId);
      } else {
        nonDepreTermIdNodes.add(termId);
        phenolGraph.addVertex(termId);
        terms.put(termId, term);
        for (TermId alternateId : term.getAltTermIds()) {
          terms.put(alternateId,term);
        }
      }
    }
    // add the alt_ids to the map if possible
    for (Map.Entry<TermId, TermId> entry : old2newTermIdMap.entrySet()) {
      Term term = terms.get(entry.getValue());
      if (term != null) {
        terms.put(entry.getKey(),term);
      }
    }

    Set<TermId> rootCandSet = Sets.newHashSet();
    Set<TermId> removeMarkSet = Sets.newHashSet();


    for (Edge edge : gEdges) {
      String subId = edge.getSub();
      String objId = edge.getObj();

      Optional<String> subCurie = curieUtil.getCurie(subId);
      if (! subCurie.isPresent() ) {
        LOGGER.warn("No matching curie found for edge's subject: " + subId);
        continue;
      }

      Optional<String> objCurie = curieUtil.getCurie(objId);
      if (! objCurie.isPresent()) {
        LOGGER.warn("No matching curie found for edge's object: " + objId);
        continue;
      }

      String subCurieStr = subCurie.get();
      String objCurieStr = objCurie.get();
      TermId subTermId = TermId.constructWithPrefix(subCurieStr);
      TermId objTermId = TermId.constructWithPrefix(objCurieStr);

      // For each edge and connected nodes,
      // we add candidate obj nodes in rootCandSet, i.e. nodes that have incoming edges.
      // we then remove subj nodes from rootCandSet, i.e. nodes that have outgoing edges.
      rootCandSet.add(objTermId);
      removeMarkSet.add(subTermId);

      phenolGraph.addVertex(subTermId);
      phenolGraph.addVertex(objTermId);
      IdLabeledEdge e = new IdLabeledEdge();
      e.setId(edgeId);
      phenolGraph.addEdge(subTermId, objTermId, e);

      Relationship ctr = factory.constructRelationship(subTermId, objTermId, edgeId);
      relationMap.put(edgeId, ctr);

      edgeId += 1;
    }

    rootCandSet.removeAll(removeMarkSet);
    CompatibilityChecker.check(phenolGraph.vertexSet(), phenolGraph.edgeSet());

    // Metadata about the ontology
    Meta gMeta = obograph.getMeta();
    Map<String, String> metaInfo = new HashMap<>();
    String version = gMeta.getVersion()!=null ? gMeta.getVersion() : "";
    metaInfo.put("data-version", version);
    if (gMeta.getBasicPropertyValues()!=null) {
      for (BasicPropertyValue bpv: gMeta.getBasicPropertyValues()) {
        if (bpv.getPred().equalsIgnoreCase("date")) {
          String date=bpv.getVal().trim();
          metaInfo.put("date",date);
        }
      }
    }

    // A heuristic for determining root node(s).
    // If there are multiple candidate roots, we will just put owl:Thing as the root one.
    TermId rootId;
    if (rootCandSet.isEmpty()) {
      rootId = TermId.constructWithPrefix("owl:Thing");
      // TODO Exception/Optional.isEmpty()

    } if (rootCandSet.size() > 1 ) {
      TermPrefix prefix;
      Optional<TermId> firstId = rootCandSet.stream().findFirst();
      // getPrefix should always work actually, but if we cannot find a term for some reason, use Owl as the prefix
      prefix = firstId.map(TermId::getPrefix).orElseGet(() -> new TermPrefix("Owl"));
      // Assumption: "0000000" is not used for actual terms in any OBO ontology
      rootId = new TermId(prefix,"0000000");
      Term rootTerm = new Term();
      rootTerm.setId(rootId);
      phenolGraph.addVertex(rootId);
      nonDepreTermIdNodes.add(rootId);
      terms.put(rootId, rootTerm);

      for (TermId childOfNewRootTermId : rootCandSet) {
        IdLabeledEdge e = new IdLabeledEdge();
        e.setId(edgeId);
        edgeId++;
        phenolGraph.addEdge(childOfNewRootTermId, rootId, e);
        Relationship ctr = factory.constructRelationship(childOfNewRootTermId, rootId, edgeId);
        relationMap.put(edgeId, ctr);
      }
    } else { // if we get here, there is exactly one root candidate
      List<TermId> rootCandList = new ArrayList<>(rootCandSet);
      rootId = rootCandList.get(0);
    }

    ImmutableOntology ont=  new ImmutableOntology(
      ImmutableSortedMap.copyOf(metaInfo),
      phenolGraph,
      rootId,
      nonDepreTermIdNodes,
      depreTermIdNodes,
      ImmutableMap.copyOf(terms),
      ImmutableMap.copyOf(relationMap));
    return Optional.of(ont);
  }


}
