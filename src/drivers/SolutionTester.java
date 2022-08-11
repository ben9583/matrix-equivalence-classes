package drivers;

import impl.ISolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SolutionTester<InputClass, OutputClass> {
    private final ISolution<InputClass, OutputClass> solution;
    private final List<InputClass> inputs;
    private final List<OutputClass> expectedOutputs;

    public SolutionTester(ISolution<InputClass, OutputClass> solution) {
        this.solution = solution;
        this.inputs = new ArrayList<>();
        this.expectedOutputs = new ArrayList<>();
    }

    public SolutionTesterResponse<InputClass, OutputClass> test(InputClass input, OutputClass expectedOutput) {
        OutputClass actualOutput = this.solution.solution(input);

        return new SolutionTesterResponse<InputClass, OutputClass>(expectedOutput.equals(actualOutput), input, expectedOutput, actualOutput);
    }

    public void add(InputClass input, OutputClass expectedOutput) {
        this.inputs.add(input);
        this.expectedOutputs.add(expectedOutput);
    }

    public void add(InputClass[] inputs, OutputClass[] expectedOutputs) {
        if(inputs.length != expectedOutputs.length) {
            throw new IllegalArgumentException("Inputs length does not match expected outputs length.");
        }

        Collections.addAll(this.inputs, inputs);
        Collections.addAll(this.expectedOutputs, expectedOutputs);
    }

    public List<SolutionTesterResponse<InputClass, OutputClass>> test() {
        List<SolutionTesterResponse<InputClass, OutputClass>> outputs = new ArrayList<>();
        for(int i = 0; i < inputs.size(); i++) {
            InputClass input = inputs.get(i);
            OutputClass expectedOutput = expectedOutputs.get(i);
            OutputClass actualOutput = solution.solution(input);
            outputs.add(new SolutionTesterResponse<>(actualOutput.equals(expectedOutput), input, expectedOutput, actualOutput));
        }

        inputs.clear();
        expectedOutputs.clear();

        return outputs;
    }
}
