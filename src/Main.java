import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int activationFunctionType;
        int inputPerceptronCount;
        int[] hiddenPerceptronCount;
        int outputPerceptronCount;

        //set perception's activation function
        System.out.println("Please Enter the number of the desired activation function\n" +
                "1: Linear\n" +
                "2: ReLU\n" +
                "3: Leaky ReLU\n" +
                "4: Sigmoid\n" +
                "5: Tanh\n" +
                ">>>");
        activationFunctionType = scanner.nextInt();

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

        Net net = new Net(activationFunctionType, inputPerceptronCount, hiddenPerceptronCount, outputPerceptronCount);
    }
}