import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Net {
    Scanner scanner = new Scanner(System.in);

    Perceptron[] inputLayer;
    int inputPerceptronCount;

    ArrayList<Perceptron>[] hiddenLayers;
    int hiddenLayerCount;
    int hiddenPerceptronCount;

    Perceptron[] outputLayer;
    int outputPerceptronCount;

    void makeNet() {
        //set perception's activation function
        System.out.println("Please Enter the number of the desired activation function\n" +
                           "1: Linear\n" +
                           "2: ReLU\n" +
                           "3: Leaky ReLU\n" +
                           "4: Sigmoid\n" +
                           "5: Tanh\n" +
                           ">>>");
        Perceptron.functionType = scanner.nextInt();

        //make inputLayer's perceptron
        System.out.println("Please enter the number of input Perceptron >>>");
        inputPerceptronCount = scanner.nextInt();
        inputLayer = new Perceptron[inputPerceptronCount];
        for(int i = 1; i <= inputPerceptronCount; i++) {
            inputLayer[i-1] = new Perceptron();
        }

        //make hiddenLayer
        System.out.println("Please enter the number of hidden layers >>>");
        hiddenLayerCount = scanner.nextInt();
        hiddenLayers = new ArrayList[hiddenLayerCount];
        for(int i = 1; i <= hiddenLayerCount; i++) { //make each hiddenLayer's perceptron
            ArrayList<Perceptron> hiddenLayer = new ArrayList<>();
            System.out.printf("Please enter the number of perceptron of hidden layer %d >>>\n",i);
            hiddenPerceptronCount = scanner.nextInt();
            for(int j = 1; j <= hiddenPerceptronCount; j++) {
                hiddenLayer.add(new Perceptron());
            }
            hiddenLayers[i-1] = hiddenLayer;
        }

        //make outputLayer's perceptron
        System.out.println("Please enter the number of output Perceptron >>>");
        outputPerceptronCount = scanner.nextInt();
        outputLayer = new Perceptron[outputPerceptronCount];
        for(int i = 1; i <= outputPerceptronCount; i++) {
            outputLayer[i-1] = new Perceptron();
        }


    }
    void activate() {
        for(int i = 1; i <= inputPerceptronCount; i++) {

        }
    }
}
