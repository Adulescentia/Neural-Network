import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int activationFunctionType;
        int inputPerceptronCount;
        int[] hiddenPerceptronCount;
        int outputPerceptronCount;
        float learningRate;
        Data dummyInputData = new Data(new Float[]{0F, 1F});
        Data dummyTargetData = new Data(new Float[]{1F});

        //set perception's activation function
        System.out.println("""
                Please Enter the number of the desired activation function
                1: Linear
                2: ReLU
                3: Leaky ReLU
                4: Sigmoid
                5: Tanh
                >>>""");
        activationFunctionType = scanner.nextInt();

        //set learning rate
        System.out.println("Please enter learning rate (float)");
        learningRate = scanner.nextFloat();

        //make inputLayer's perceptron
        System.out.println("Please enter the number of input Perceptron >>>");
        inputPerceptronCount = scanner.nextInt();

        //make hiddenLayer
        System.out.println("Please enter the number of hidden layers >>>");
        hiddenPerceptronCount = new int[scanner.nextInt()];
        for(int i = 0; i <= hiddenPerceptronCount.length-1; i++) {
            System.out.println("Please enter the number of perceptron of hidden layer");
            hiddenPerceptronCount[i] = scanner.nextInt();
        }

        //make outputLayer's perceptron
        System.out.println("Please enter the number of output Perceptron >>>");
        outputPerceptronCount = scanner.nextInt();

        Net net = new Net(activationFunctionType, learningRate, inputPerceptronCount, hiddenPerceptronCount, outputPerceptronCount, new ArrayList<Float>(List.of(dummyInputData.getElement())), new ArrayList<Float>(List.of(dummyTargetData.getElement())));
        net.activate();
        net.printResult();
    }
}