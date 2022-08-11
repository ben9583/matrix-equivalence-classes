package drivers;

public class SolutionTesterResponse<InputClass, OutputClass> {
    public final boolean success;
    public final InputClass input;
    public final OutputClass expectedOutput;
    public final OutputClass actualOutput;

    public SolutionTesterResponse(boolean success, InputClass input, OutputClass expectedOutput, OutputClass actualOutput) {
        this.success = success;
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.actualOutput = actualOutput;
    }
}
