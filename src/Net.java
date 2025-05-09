import java.util.ArrayList;
public class Net {
    ArrayList<Perceptron>[] perceptronNet;
    ArrayList<Float> inputValue = new ArrayList<>();
    ArrayList<Float> outputValue = new ArrayList<>();
    ArrayList<Float> targetOutputValue = new ArrayList<>();
    Net(int functionType, float learningRate, int inputPerceptronCount, int[] hiddenPerceptronCount, int outputPerceptronCount, ArrayList<Float> inputValue, ArrayList<Float> targetOutputValue) {
        perceptronNet = new ArrayList[1 + hiddenPerceptronCount.length + 1]; //input, hiddens, output
        Perceptron.functionType = functionType;
        Perceptron.learningRate = learningRate;
        Perceptron.bias = 0.5F;

        for (int i = 0; i <= perceptronNet.length-1; i++) {//initializer
            perceptronNet[i] = new ArrayList<>();
        }

        for (int i = 0; i <= inputPerceptronCount-1; i++) { //make input layer
            perceptronNet[0].add(new Perceptron(1,hiddenPerceptronCount[0]));
        }

        for (int i = 0; i <= hiddenPerceptronCount.length-1; i++) { //make hidden layer
            for (int j = 0; j <= hiddenPerceptronCount[i]; j++) {
                if (i == hiddenPerceptronCount.length-1) {
                    perceptronNet[i+1].add(new Perceptron(hiddenPerceptronCount[i], outputPerceptronCount));
                } else {
                    perceptronNet[i+1].add(new Perceptron(hiddenPerceptronCount[i], hiddenPerceptronCount[i + 1]));
                }
            }
        }

        for(int i = 0; i <= outputPerceptronCount-1; i++) { //make input layer
            perceptronNet[inputPerceptronCount + hiddenPerceptronCount.length -1 ].add(new Perceptron(outputPerceptronCount, 1)); //last-1
        }

    }
    void transmission () {
        for (int i = 0; i < perceptronNet.length; i++) {
            for (Perceptron perceptron : perceptronNet[i]) {
                if (i == 0) {
                    for (float input : inputValue) {
                        perceptron.setInput(input);
                    }
                }else if (i < perceptronNet.length-1) {
                    for (int j = 0; j < perceptronNet[i+1].size(); i++) {
                        perceptronNet[i+1].get(j).setInput(perceptron.getOutput().get(j));
                    }
                } else {
                    outputValue.add(perceptron.output);
                }
            }
        }
    }
    void activate() {
        for (ArrayList<Perceptron> layer : perceptronNet) {
            transmission();
        }
    }
}
