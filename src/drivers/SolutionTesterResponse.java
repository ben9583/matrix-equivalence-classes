package drivers;

public record SolutionTesterResponse<InputClass, OutputClass>(boolean success, InputClass input,
                                                              OutputClass expectedOutput, OutputClass actualOutput) { }
