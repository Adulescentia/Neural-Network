import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int activationFunctionType;
        int inputPerceptronCount;
        int[] hiddenPerceptronCount;
        int outputPerceptronCount;
        float learningRate;

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
        for(int PerceptronCount : hiddenPerceptronCount) {
            System.out.println("Please enter the number of perceptron of hidden layer");
            PerceptronCount = scanner.nextInt();
        }

        //make outputLayer's perceptron
        System.out.println("Please enter the number of output Perceptron >>>");
        outputPerceptronCount = scanner.nextInt();

        Net net = new Net(activationFunctionType, learningRate, inputPerceptronCount, hiddenPerceptronCount, outputPerceptronCount, new ArrayList<Float>()/*todo - input*/, new ArrayList<Float>()/*todo - target output*/);
    }
}