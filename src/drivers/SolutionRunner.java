package drivers;

import impl.ISolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SolutionRunner<InputClass, OutputClass> {
    private final ISolution<InputClass, OutputClass> solution;
    private final List<InputClass> inputs;

    public SolutionRunner(ISolution<InputClass, OutputClass> solution) {
        this.solution = solution;
        this.inputs = new ArrayList<>();
    }

    public OutputClass run(InputClass input) {
        return solution.solution(input);
    }

    public void addInput(InputClass input) {
        this.inputs.add(input);
    }

    public void addInputs(InputClass[] inputs) {
        Collections.addAll(this.inputs, inputs);
    }

    public OutputClass[] run() {
        List<OutputClass> outputs = new ArrayList<>(inputs.size());
        for (InputClass inputClass : inputs) {
            outputs.add(run(inputClass));
            inputs.remove(0);
        }
        return (OutputClass[]) outputs.toArray();
    }
}
