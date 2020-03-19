package brave.grpc;

import brave.grpc.GrpcPropagation.Tags;
import brave.internal.HexCodec;
import brave.propagation.B3Propagation;
import brave.propagation.MutableTraceContext;
import brave.propagation.Propagation;
import brave.propagation.TraceContext;
import brave.propagation.TraceContext.Extractor;
import brave.propagation.TraceContext.Injector;
import brave.propagation.TraceContextOrSamplingFlags;
import io.grpc.Metadata;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@Measurement(iterations = 5, time = 1)
@Warmup(iterations = 10, time = 1)
@Fork(3)
@BenchmarkMode(Mode.SampleTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class GrpcPropagationBenchmarks {
  static final Propagation<Metadata.Key<String>> b3 =
      B3Propagation.FACTORY.create(AsciiMetadataKeyFactory.INSTANCE);
  static final Injector<Metadata> b3Injector = b3.injector(TracingClientInterceptor.SETTER);
  static final Extractor<Metadata> b3Extractor = b3.extractor(TracingServerInterceptor.GETTER);
  static final MutableTraceContext.Extractor<Metadata> b3MutableExtractor = B3Propagation.FACTORY
      .extractor(AsciiMetadataKeyFactory.INSTANCE, TracingServerInterceptor.GETTER);

  static final Propagation.Factory bothFactory = GrpcPropagation.newFactory(B3Propagation.FACTORY);
  static final Propagation<Metadata.Key<String>> both =
      bothFactory.create(AsciiMetadataKeyFactory.INSTANCE);
  static final Injector<Metadata> bothInjector = both.injector(TracingClientInterceptor.SETTER);
  static final Extractor<Metadata> bothExtractor = both.extractor(TracingServerInterceptor.GETTER);
  static final MutableTraceContext.Extractor<Metadata> bothMutableExtractor =
      bothFactory.extractor(AsciiMetadataKeyFactory.INSTANCE, TracingServerInterceptor.GETTER);

  static final TraceContext context = TraceContext.newBuilder()
      .traceIdHigh(HexCodec.lowerHexToUnsignedLong("67891233abcdef01"))
      .traceId(HexCodec.lowerHexToUnsignedLong("2345678912345678"))
      .spanId(HexCodec.lowerHexToUnsignedLong("463ac35c9f6413ad"))
      .sampled(true)
      .build();
  static final TraceContext contextWithTags;

  static final Metadata incomingB3 = new Metadata();
  static final Metadata incomingBoth = new Metadata();
  static final Metadata incomingBothNoTags = new Metadata();

  static {
    contextWithTags = bothFactory.decorate(context);
    contextWithTags.findExtra(Tags.class).put("method", "helloworld.Greeter/SayHello");

    b3Injector.inject(context, incomingB3);
    bothInjector.inject(contextWithTags, incomingBoth);
    bothInjector.inject(context, incomingBothNoTags);
  }

  static final Metadata nothingIncoming = new Metadata();

  Metadata carrier = new Metadata();
  MutableTraceContext extracted = new MutableTraceContext();

  @Benchmark public void inject_b3() {
    b3Injector.inject(context, carrier);
  }

  @Benchmark public TraceContextOrSamplingFlags extract_b3() {
    return b3Extractor.extract(incomingBoth);
  }

  @Benchmark public TraceContextOrSamplingFlags extract_b3_nothing() {
    return b3Extractor.extract(nothingIncoming);
  }

  @Benchmark public void mutable_extract_b3() {
    b3MutableExtractor.extract(incomingBoth, extracted);
  }

  @Benchmark public void mutable_extract_b3_nothing() {
    b3MutableExtractor.extract(nothingIncoming, extracted);
  }

  @Benchmark public void inject_both() {
    bothInjector.inject(contextWithTags, carrier);
  }

  @Benchmark public void inject_both_no_tags() {
    bothInjector.inject(context, carrier);
  }

  @Benchmark public TraceContextOrSamplingFlags extract_both() {
    return bothExtractor.extract(incomingBoth);
  }

  @Benchmark public TraceContextOrSamplingFlags extract_both_nothing() {
    return bothExtractor.extract(nothingIncoming);
  }

  @Benchmark public TraceContextOrSamplingFlags extract_both_no_tags() {
    return bothExtractor.extract(incomingBothNoTags);
  }

  @Benchmark public void mutable_extract_both() {
    bothMutableExtractor.extract(incomingBoth, extracted);
  }

  @Benchmark public void mutable_extract_both_nothing() {
    bothMutableExtractor.extract(nothingIncoming, extracted);
  }

  @Benchmark public void mutable_extract_both_no_tags() {
    bothMutableExtractor.extract(incomingBothNoTags, extracted);
  }

  // Convenience main entry-point
  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .addProfiler("gc")
        .include(".*" + GrpcPropagationBenchmarks.class.getSimpleName())
        .build();

    new Runner(opt).run();
  }
}
