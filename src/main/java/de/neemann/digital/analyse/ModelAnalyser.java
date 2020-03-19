/*
 * Copyright (c) 2016 Helmut Neemann
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digital.analyse;

import de.neemann.digital.analyse.expression.BitSetter;
import de.neemann.digital.analyse.quinemc.BoolTableByteArray;
import de.neemann.digital.core.*;
import de.neemann.digital.core.basic.And;
import de.neemann.digital.core.basic.Not;
import de.neemann.digital.core.basic.Or;
import de.neemann.digital.core.element.ElementAttributes;
import de.neemann.digital.core.flipflops.FlipflopD;
import de.neemann.digital.core.flipflops.FlipflopJK;
import de.neemann.digital.core.flipflops.FlipflopT;
import de.neemann.digital.core.wiring.Clock;
import de.neemann.digital.core.wiring.Splitter;
import de.neemann.digital.draw.elements.PinException;
import de.neemann.digital.gui.Main;
import de.neemann.digital.lang.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Analyses a given model.
 * Calculates the truth table which is generated by the given model
 */
public class ModelAnalyser {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelAnalyser.class);
    private static final int MAX_INPUTS_ALLOWED = 24;

    private final Model model;
    private final ArrayList<Signal> inputs;
    private final ArrayList<Signal> outputs;
    private int uniqueIndex = 0;
    private ModelAnalyserInfo modelAnalyzerInfo;

    /**
     * Creates a new instance
     *
     * @param model the model
     * @throws AnalyseException AnalyseException
     */
    public ModelAnalyser(Model model) throws AnalyseException {
        this.model = model;

        try {
            replaceTFF();
            replaceJKFF();
        } catch (NodeException e) {
            throw new AnalyseException(e);
        }

        modelAnalyzerInfo = new ModelAnalyserInfo(model);

        inputs = checkBinaryInputs(model.getInputs());
        checkUnique(inputs);
        outputs = checkBinaryOutputs(model.getOutputs());

        modelAnalyzerInfo.setInOut(inputs, outputs);

        for (Node n : model)
            if (n.hasState() && !(n instanceof FlipflopD))
                throw new AnalyseException(Lang.get("err_cannotAnalyse_N", n.getClass().getSimpleName()));

        int i = 0;
        List<FlipflopD> flipflops = model.findNode(FlipflopD.class);
        flipflops = replaceMultiBitFlipflops(flipflops);
        for (FlipflopD ff : flipflops) {
            checkClock(ff);
            if (ff.getBits() != 1)
                throw new AnalyseException(Lang.get("err_MultiBitFlipFlopFound"));

            ff.getDInput().removeObserver(ff); // turn off flipflop
            String label = ff.getLabel();
            if (label.length() == 0)
                label = createUniqueName(ff);

            if (!label.endsWith("n"))
                label += "n";

            outputs.add(i++, new Signal(label + "+1", ff.getDInput()));

            ObservableValue q = ff.getOutputs().get(0);
            final Signal sig = new Signal(label, q);
            if (inputs.contains(sig))
                throw new AnalyseException(Lang.get("err_varName_N_UsedTwice", sig.getName()));
            inputs.add(sig);

            ObservableValue notQ = ff.getOutputs().get(1);
            q.addObserver(new NodeWithoutDelay(notQ) {
                @Override
                public void hasChanged() {
                    notQ.setValue(~q.getValue());
                }
            });
        }

        if (inputs.size() == 0)
            throw new AnalyseException(Lang.get("err_analyseNoInputs"));
        if (outputs.size() == 0)
            throw new AnalyseException(Lang.get("err_analyseNoOutputs"));
    }

    private ModelAnalyserInfo getModelAnalyzerInfo() {
        return modelAnalyzerInfo;
    }

    private String createUniqueName(FlipflopD ff) {
        ObservableValue q = ff.getOutputs().get(0);
        for (Signal o : outputs) {
            if (o.getValue() == q)
                return o.getName();
        }

        String name;
        do {
            name = "Z" + uniqueIndex;
            uniqueIndex++;
        } while (inputs.contains(new Signal(name, null)));
        return name;
    }

    private void checkUnique(ArrayList<Signal> signals) throws AnalyseException {
        for (int i = 0; i < signals.size() - 1; i++)
            for (int j = i + 1; j < signals.size(); j++)
                if (signals.get(i).equals(signals.get(j)))
                    throw new AnalyseException(Lang.get("err_varName_N_UsedTwice", signals.get(i).getName()));
    }

    private ArrayList<Signal> checkBinaryOutputs(ArrayList<Signal> list) throws AnalyseException {
        ArrayList<Signal> outputs = new ArrayList<>();
        for (Signal s : list) {
            final int bits = s.getValue().getBits();
            if (bits == 1)
                outputs.add(s);
            else {
                try {
                    Splitter sp = Splitter.createOneToN(bits);
                    sp.setInputs(s.getValue().asList());
                    SplitPinString pins = SplitPinString.create(s);

                    final ObservableValues spOutputs = sp.getOutputs();
                    for (int i = spOutputs.size() - 1; i >= 0; i--)
                        outputs.add(new Signal(s.getName() + i, spOutputs.get(i)).setPinNumber(pins.getPin(i)));

                    s.getValue().fireHasChanged();

                    ArrayList<String> names = new ArrayList<>(bits);
                    for (int i = 0; i < bits; i++)
                        names.add(s.getName() + i);

                    modelAnalyzerInfo.addOutputBus(s.getName(), names);

                } catch (NodeException e) {
                    throw new AnalyseException(e);
                }
            }
        }
        return outputs;
    }

    private ArrayList<Signal> checkBinaryInputs(ArrayList<Signal> list) throws AnalyseException {
        ArrayList<Signal> inputs = new ArrayList<>();
        for (Signal s : list) {
            final int bits = s.getValue().getBits();
            if (bits == 1)
                inputs.add(s);
            else {
                try {
                    Splitter sp = Splitter.createNToOne(bits);
                    final ObservableValue out = sp.getOutputs().get(0);
                    out.addObserver(new NodeWithoutDelay(s.getValue()) {
                        @Override
                        public void hasChanged() {
                            s.getValue().setValue(out.getValue());
                        }
                    });
                    out.fireHasChanged();

                    SplitPinString pins = SplitPinString.create(s);
                    ObservableValues.Builder builder = new ObservableValues.Builder();
                    for (int i = bits - 1; i >= 0; i--) {
                        ObservableValue o = new ObservableValue(s.getName() + i, 1);
                        builder.add(o);
                        inputs.add(new Signal(s.getName() + i, o).setPinNumber(pins.getPin(i)));
                    }
                    final ObservableValues inputsList = builder.reverse().build();
                    sp.setInputs(inputsList);

                    modelAnalyzerInfo.addInputBus(s.getName(), inputsList.getNames());

                } catch (NodeException e) {
                    throw new AnalyseException(e);
                }
            }
        }
        return inputs;
    }

    private void checkClock(Node node) throws AnalyseException {
        if (!getClock().hasObserver(node))
            throw new AnalyseException(Lang.get("err_ffNeedsToBeConnectedToClock"));
    }

    private ObservableValue getClock() throws AnalyseException {
        ArrayList<Clock> clocks = model.getClocks();
        if (clocks.size() != 1)
            throw new AnalyseException(Lang.get("err_aSingleClockNecessary"));
        return clocks.get(0).getClockOutput();
    }

    private List<FlipflopD> replaceMultiBitFlipflops(List<FlipflopD> flipflops) throws AnalyseException {
        ArrayList<FlipflopD> out = new ArrayList<>();
        for (FlipflopD ff : flipflops) {
            if (ff.getBits() == 1)
                out.add(ff);
            else {
                try {
                    model.removeNode(ff);
                    ff.getDInput().removeObserver(ff);
                    ff.getClock().removeObserver(ff);

                    Splitter insp = Splitter.createOneToN(ff.getBits());
                    insp.setInputs(new ObservableValues(ff.getDInput()));
                    ff.getDInput().fireHasChanged();

                    Splitter outsp = Splitter.createNToOne(ff.getBits());

                    ObservableValues.Builder spinput = new ObservableValues.Builder();
                    String label = ff.getLabel();
                    if (label.length() == 0)
                        label = createUniqueName(ff);
                    for (int i = ff.getBits() - 1; i >= 0; i--) {
                        ObservableValue qn = new ObservableValue("", 1);
                        ObservableValue nqn = new ObservableValue("", 1);
                        FlipflopD newff = new FlipflopD(label + i, qn, nqn);
                        spinput.addAtTop(qn);
                        model.add(newff);
                        newff.setInputs(new ObservableValues(insp.getOutputs().get(i), getClock()));
                        out.add(newff);
                    }
                    outsp.setInputs(spinput.build());
                    for (ObservableValue v : spinput)
                        v.fireHasChanged();

                    final ObservableValue qout = ff.getOutputs().get(0);
                    final ObservableValue nqout = ff.getOutputs().get(1);
                    ObservableValue spq = outsp.getOutputs().get(0);
                    spq.addObserver(new NodeWithoutDelay(qout, nqout) {
                        @Override
                        public void hasChanged() {
                            final long value = spq.getValue();
                            qout.setValue(value);
                            nqout.setValue(~value);
                        }
                    });
                    spq.fireHasChanged();

                } catch (NodeException e) {
                    throw new AnalyseException(e);
                }
            }
        }
        return out;
    }

    private void replaceJKFF() throws NodeException, AnalyseException {
        List<FlipflopJK> jkList = model.findNode(FlipflopJK.class);

        for (FlipflopJK jk : jkList) {
            checkClock(jk);

            jk.getClockVal().removeObserver(jk);
            jk.getjVal().removeObserver(jk);
            jk.getkVal().removeObserver(jk);

            // create d ff
            ObservableValue q = jk.getOutputs().get(0);
            ObservableValue qn = jk.getOutputs().get(1);
            FlipflopD d = new FlipflopD(jk.getLabel(), q, qn);

            And a1 = new And(new ElementAttributes());
            a1.setInputs(new ObservableValues(jk.getjVal(), qn));
            And a2 = new And(new ElementAttributes());
            Not nk = new Not(new ElementAttributes());
            nk.setInputs(jk.getkVal().asList());
            a2.setInputs(new ObservableValues(nk.getOutput(), q));

            Or or = new Or(new ElementAttributes());
            or.setInputs(new ObservableValues(a1.getOutput(), a2.getOutput()));

            d.setInputs(new ObservableValues(or.getOutputs().get(0), jk.getClockVal()));

            model.add(a1);
            model.add(a2);
            model.add(nk);
            model.add(or);
            model.replace(jk, d);
        }
    }

    private void replaceTFF() throws NodeException, AnalyseException {
        List<FlipflopT> tList = model.findNode(FlipflopT.class);

        for (FlipflopT tff : tList) {
            checkClock(tff);
            tff.getClockVal().removeObserver(tff);
            ObservableValue q = tff.getOutputs().get(0);
            ObservableValue qn = tff.getOutputs().get(1);

            ObservableValue enable = tff.getEnableVal();
            if (enable == null) {
                // create d ff
                FlipflopD d = new FlipflopD(tff.getLabel(), q, qn);
                d.setInputs(new ObservableValues(qn, getClock()));
                model.replace(tff, d);
            } else {
                // create jk ff
                enable.removeObserver(tff);
                FlipflopJK jk = new FlipflopJK(tff.getLabel(), q, qn);
                jk.setInputs(new ObservableValues(enable, getClock(), enable));
                model.replace(tff, jk);
            }
        }
    }

    /**
     * @return the models inputs
     */
    public ArrayList<Signal> getInputs() {
        return inputs;
    }

    /**
     * @return the models outputs
     */
    public ArrayList<Signal> getOutputs() {
        return outputs;
    }

    /**
     * Analyses the circuit
     *
     * @return the generated truth table
     * @throws NodeException      NodeException
     * @throws PinException       PinException
     * @throws BacktrackException BacktrackException
     * @throws AnalyseException   AnalyseException
     */
    public TruthTable analyse() throws NodeException, PinException, BacktrackException, AnalyseException {
        LOGGER.debug("start to analyse the model...");

        TruthTable tt = new TruthTable();
        tt.setModelAnalyzerInfo(getModelAnalyzerInfo());
        for (Signal s : inputs)
            tt.addVariable(s.getName());

        if (!Main.isExperimentalMode())
            CycleDetector.checkForCycles(inputs);

        DependencyAnalyser da = new DependencyAnalyser(this);
        long steps = da.getRequiredSteps(this);

        long tableRows = 1L << inputs.size();
        LOGGER.debug("analyse speedup: " + tableRows + " rows vs " + steps + " steps, speedup " + ((double) tableRows) / steps);

        long time = System.currentTimeMillis();


        if (tableRows <= steps || tableRows <= 128)
            simpleFiller(tt);
        else
            dependantFiller(tt, da);

        time = System.currentTimeMillis() - time;
        LOGGER.debug("model analysis: " + time / 1000.0 + " sec");

        return tt;
    }

    private void simpleFiller(TruthTable tt) throws NodeException, AnalyseException {
        if (inputs.size() > MAX_INPUTS_ALLOWED)
            throw new AnalyseException(Lang.get("err_toManyInputs_max_N0_is_N1", MAX_INPUTS_ALLOWED, inputs.size()));


        BitSetter bitsetter = new BitSetter(inputs.size()) {
            @Override
            public void setBit(int row, int bit, boolean value) {
                inputs.get(bit).getValue().setBool(value);
            }
        };

        int rows = 1 << inputs.size();
        ArrayList<BoolTableByteArray> data = new ArrayList<>();
        for (Signal s : outputs) {
            BoolTableByteArray e = new BoolTableByteArray(rows);
            data.add(e);
            tt.addResult(s.getName(), e);
        }

        model.init();
        for (int row = 0; row < rows; row++) {
            bitsetter.fill(row);
            model.doStep();
            for (int i = 0; i < outputs.size(); i++) {
                data.get(i).set(row, outputs.get(i).getValue().getBool());
            }
        }
    }

    private void dependantFiller(TruthTable tt, DependencyAnalyser da) throws NodeException, AnalyseException {
        model.init();
        for (Signal out : outputs) {

            ArrayList<Signal> ins = reorder(da.getInputs(out), inputs);
            if (ins.size() > MAX_INPUTS_ALLOWED)
                throw new AnalyseException(Lang.get("err_toManyInputs_max_N0_is_N1", MAX_INPUTS_ALLOWED, ins.size()));

            int rows = 1 << ins.size();
            BoolTableByteArray e = new BoolTableByteArray(rows);
            BitSetter bitsetter = new BitSetter(ins.size()) {
                @Override
                public void setBit(int row, int bit, boolean value) {
                    ins.get(bit).getValue().setBool(value);
                }
            };

            for (int row = 0; row < rows; row++) {
                bitsetter.fill(row);
                model.doStep();
                e.set(row, out.getValue().getBool());
            }

            tt.addResult(out.getName(), new BoolTableExpanded(e, ins, inputs));
        }
    }

    private ArrayList<Signal> reorder(ArrayList<Signal> ins, ArrayList<Signal> originalOrder) {
        ArrayList<Signal> newList = new ArrayList<>();
        for (Signal i : originalOrder)
            if (ins.contains(i))
                newList.add(i);
        return newList;
    }

}