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

    Net(int functionType, int inputPerceptronCount, int[] hiddenPerceptronCount, int outputPerceptronCount) {
        Perceptron.functionType = functionType;

        inputLayer = new Perceptron[inputPerceptronCount];
        for(int i = 1; i <= inputPerceptronCount; i++) {
            inputLayer[i-1] = new Perceptron();
        }

        hiddenLayers = new ArrayList[hiddenLayerCount];
        for(ArrayList hiddenLayer : hiddenLayers) {
            for (int PerceptronCount : hiddenPerceptronCount) {
                for(int i=0; i<= PerceptronCount; i++) {
                    hiddenLayer.add(new Perceptron());
                }
            }
        }

        outputLayer = new Perceptron[outputPerceptronCount];
        for (int i = 1; i <= outputPerceptronCount; i++) {
            outputLayer[i-1] = new Perceptron();
        }

    }

    void activate() {
        for(int i = 1; i <= inputPerceptronCount; i++) {

        }
    }
}
