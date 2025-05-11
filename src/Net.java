import java.util.ArrayList;
public class Net {
    ArrayList<Perceptron>[] perceptronNet;
    ArrayList<Float> inputValue;
    ArrayList<Float> outputValue = new ArrayList<>();
    ArrayList<Float> targetOutputValue;
    Net(int functionType, float learningRate, int inputPerceptronCount, int[] hiddenPerceptronCount, int outputPerceptronCount, ArrayList<Float> inputValue, ArrayList<Float> targetOutputValue) {
        perceptronNet = new ArrayList[1 + hiddenPerceptronCount.length + 1]; //input, hiddens, output
        Perceptron.functionType = functionType;
        Perceptron.learningRate = learningRate;
        Perceptron.bias = 0.5F;
        this.inputValue = inputValue;
        this.targetOutputValue = targetOutputValue;


        for (int i = 0; i <= perceptronNet.length-1; i++) {//initializer
            perceptronNet[i] = new ArrayList<>();
        }

        for (int i = 0; i <= inputPerceptronCount-1; i++) { //make input layer
            perceptronNet[0].add(new Perceptron(1,hiddenPerceptronCount[0], false));
        }

        for (int i = 0; i <= hiddenPerceptronCount.length-1; i++) { //make hidden layer
            for (int j = 0; j <= hiddenPerceptronCount[i]-1; j++) {
                if (i == hiddenPerceptronCount.length-1) {
                    perceptronNet[i+1].add(new Perceptron(hiddenPerceptronCount[i], outputPerceptronCount, false));
                } else {
                    perceptronNet[i+1].add(new Perceptron(hiddenPerceptronCount[i], hiddenPerceptronCount[i + 1], false));
                }
            }
        }

        for(int i = 0; i <= outputPerceptronCount-1; i++) { //make output layer
            perceptronNet[1 + hiddenPerceptronCount.length].add(new Perceptron(outputPerceptronCount, 1, true)); //last-1
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
                    for (int j = 0; j < perceptronNet[i+1].size(); j++) {
                        perceptronNet[i+1].get(j).setInput(perceptron.getOutput().get(j));
                    }
                } else {
                    outputValue = perceptron.getOutput();
                }
            }
        }
    }
    void activate() {
        transmission();
    }

    void printResult() {
        System.out.print("Target output is:");
        for (Float targetOutput : targetOutputValue) {
            System.out.print(targetOutput + ", ");
        }
        System.out.print("\n-------output is:");
        for (Float output : outputValue) {
            System.out.print(output + ", ");
        }
    }
}
