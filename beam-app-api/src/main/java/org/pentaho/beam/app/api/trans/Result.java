package org.pentaho.beam.app.api.trans;

import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import org.pentaho.beam.app.api.step.Step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ccaspanello on 3/28/18.
 */
public class Result {

  private Map<Step, PCollection<Row>> datasets;
  private List<String> resultFiles;
  private Metrics metrics;

  public Result( List<Step> executionPlan ) {
    datasets = new HashMap<>();
    resultFiles = new ArrayList<>();

    // Results
    executionPlan.stream().filter( step -> step.isTerminating() ).forEach( step -> datasets.put( step, step.getIncoming().stream().findFirst().get().getData() ));

    // Result Files
    executionPlan.stream().forEach( step -> resultFiles.addAll(step.getResultFiles()) );

    // Metrics
    metrics = new Metrics();
  }

  public Map<Step, PCollection<Row>> getDatasets() {
    return datasets;
  }

  public List<String> getResultFiles() {
    return resultFiles;
  }

  public Metrics getMetrics() {
    return metrics;
  }

  // Dummy Placehold for concept
  public static class Metrics {

  }
}
