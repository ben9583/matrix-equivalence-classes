import drivers.SolutionTester;
import drivers.SolutionTesterResponse;
import impl.Solution;
import impl.utils.DisorderlyEscapeSolutionInput;

import java.util.List;

public class DisorderlyEscape {
    public static final DisorderlyEscapeSolutionInput[] testingInputs = new DisorderlyEscapeSolutionInput[]{
            new DisorderlyEscapeSolutionInput(1, 1, 1),
            new DisorderlyEscapeSolutionInput(1, 1, 2),
            new DisorderlyEscapeSolutionInput(1, 1, 3),
            new DisorderlyEscapeSolutionInput(1, 1, 4),
            new DisorderlyEscapeSolutionInput(2, 1, 1),
            new DisorderlyEscapeSolutionInput(2, 1, 2),
            new DisorderlyEscapeSolutionInput(2, 1, 3),
            new DisorderlyEscapeSolutionInput(2, 2, 1),
            new DisorderlyEscapeSolutionInput(2, 2, 2),
            new DisorderlyEscapeSolutionInput(2, 2, 3),
            new DisorderlyEscapeSolutionInput(2, 2, 4),
            new DisorderlyEscapeSolutionInput(3, 2, 2),
            new DisorderlyEscapeSolutionInput(3, 2, 3),
            new DisorderlyEscapeSolutionInput(3, 2, 4),
            new DisorderlyEscapeSolutionInput(3, 3, 2),
            new DisorderlyEscapeSolutionInput(3, 3, 5),
            new DisorderlyEscapeSolutionInput(3, 3, 10),
            new DisorderlyEscapeSolutionInput(8, 3, 4),
            new DisorderlyEscapeSolutionInput(2, 9, 7),
            new DisorderlyEscapeSolutionInput(5, 5, 5),
            new DisorderlyEscapeSolutionInput(12, 12, 20),
    };

    public static final String[] testingExpectedOutputs = new String[]{
            "1",
            "2",
            "3",
            "4",
            "1",
            "3",
            "6",
            "1",
            "7",
            "27",
            "76",
            "13",
            "92",
            "430",
            "36",
            "57675",
            "27969700",
            "1774852035",
            "4498416692",
            "20834113243925",
            "97195340925396730736950973830781340249131679073592360856141700148734207997877978005419735822878768821088343977969209139721682171487959967012286474628978470487193051591840"
    };
    public static void main(String[] args) {
        SolutionTester<DisorderlyEscapeSolutionInput, String> tester = new SolutionTester<>(new Solution());
        tester.add(testingInputs, testingExpectedOutputs);

        long startTime = System.currentTimeMillis();
        List<SolutionTesterResponse<DisorderlyEscapeSolutionInput, String>> responses = tester.test();
        long endTime = System.currentTimeMillis();

        for(SolutionTesterResponse<DisorderlyEscapeSolutionInput, String> response : responses) {
            if(response.success()) {
                System.out.println("Success: (" + response.input().width() + "x" + response.input().height() + " " + response.input().states() + ")");
            } else {
                System.out.println("Failure: (" + response.input().width() + "x" + response.input().height() + " " + response.input().states() + "): Expected: " + response.expectedOutput() + ", Actual: " + response.actualOutput());
            }
        }

        System.out.println("Time taken: " + (endTime - startTime) + "ms");

        /*
        SolutionRunner<DisorderlyEscapeSolutionInput, String> runner = new SolutionRunner<>(new Solution());
        String out = runner.run(new DisorderlyEscapeSolutionInput(20, 20, 50)); // This took me a few seconds to run — the answer is 643 digits long.
        System.out.println(out);
        */
    }
}