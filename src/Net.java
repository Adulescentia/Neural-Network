import java.util.ArrayList;
public class Net {
    Perceptron[][] perceptronNet;

    ArrayList<Float> inputValue;
    ArrayList<Float> outputValue;
    ArrayList<Float> targetOutputValue;

    ArrayList<Float> errorSignal;
    float[][] errorGradient;
    float[][][] weightOfWeight;
    float hiddenErrorGradientXweight;

    Float sumOfTheSquaredErrors;
    int epoch = 0;
    Net(int functionType, float learningRate, int inputPerceptronCount, int[] hiddenPerceptronCount, int outputPerceptronCount, ArrayList<Float> inputValue, ArrayList<Float> targetOutputValue) {

        perceptronNet = new Perceptron[1 + hiddenPerceptronCount.length + 1][]; //input, hidden, output

        Perceptron.functionType = functionType;
        Perceptron.learningRate = learningRate;

        this.inputValue = inputValue;
        this.outputValue = new ArrayList<>();
        this.targetOutputValue = targetOutputValue;

        errorSignal = new ArrayList<>();
        errorGradient = new float[hiddenPerceptronCount.length + 1][]; // hidden + output



        //make input layer
        perceptronNet[0] = new Perceptron[inputPerceptronCount];
        for (int i = 0; i <= inputPerceptronCount-1; i++) { //make input layer
            perceptronNet[0][i] = new Perceptron(1, hiddenPerceptronCount[0], false);
        }

        //make hidden layers
        for (int i = 0; i <= hiddenPerceptronCount.length-1; i++) {
            perceptronNet[i+1] = new Perceptron[hiddenPerceptronCount[i]];
            if (perceptronNet.length == 3) {
                for (int j = 0; j <= hiddenPerceptronCount[i] - 1; j++) {
                    perceptronNet[i + 1][j] = new Perceptron(inputPerceptronCount, outputPerceptronCount, false);
                }
            } else {
                for (int j = 0; j <= hiddenPerceptronCount[i] - 1; j++) {
                    if (i == 0) {
                        perceptronNet[i + 1][j] = new Perceptron(inputPerceptronCount, hiddenPerceptronCount[i + 1], false);
                    } else if (i == hiddenPerceptronCount.length - 1) {
                        perceptronNet[i + 1][j] = new Perceptron(hiddenPerceptronCount[i - 1], outputPerceptronCount, false);
                    } else {
                        perceptronNet[i + 1][j] = new Perceptron(hiddenPerceptronCount[i - 1], hiddenPerceptronCount[i + 1], false);
                    }
                }
            }
        }

        //make output layer
        perceptronNet[perceptronNet.length - 1] = new Perceptron[outputPerceptronCount];
        for (int i = 0; i <= outputPerceptronCount-1; i++) { //make output layer
            perceptronNet[perceptronNet.length - 1][i] = new Perceptron((hiddenPerceptronCount[hiddenPerceptronCount.length - 1]), 1, true);
        }

        //error gradient initializer
        for (int i = 0; i <= errorGradient.length-1; i++) {
            errorGradient[i] = new float[perceptronNet[i+1].length];
        }

        //weight grid initializer
        weightOfWeight = new float[1 + hiddenPerceptronCount.length][][];
        for (int i = 0; i <= perceptronNet.length - 2; i++) {
            weightOfWeight[i] = new float[perceptronNet[i].length][];
            for (int j = 0; j <= perceptronNet[i].length-1; j++) {
                weightOfWeight[i][j] = new float[perceptronNet[i+1].length];
            }
        }
    }

    void transmission () {
        for (int i = 0; i <= perceptronNet[0].length-1; i++) {
            for (float input : inputValue) {
                perceptronNet[0][i].setInput(input);
            }
        }
        for (int i = 1; i <= perceptronNet.length-2; i++) {
            for (int j = 0; j <= perceptronNet[i].length-1; j++ ) {
                for (int k = 0; k <= perceptronNet[i - 1].length - 1; k++) {
                    perceptronNet[i][j].setInput(perceptronNet[i - 1][k].getOutput().get(j));
                }
            }
        }
        for (int i = 0; i<= perceptronNet[perceptronNet.length-1].length-1; i++) {
            outputValue.addAll(perceptronNet[perceptronNet.length-1][i].getOutput());
        }
    }

    void updateErrorSignal() {
        for (float i : outputValue) {
            System.out.println(i);
        }
        for (float i : targetOutputValue) {
            System.out.println(i);
        }
        for(int i = 0; i<=perceptronNet[perceptronNet.length-1].length-1; i++) {
            errorSignal.add(targetOutputValue.get(i) - outputValue.get(i));
        }
    }

    void updateHiddenErrorGradient() {
        for (int i = errorGradient.length-2; i >= 0; i--) {
            for (int j = 0; j<= errorGradient[i+1].length-1; j++) {
                for (int k = 0; k <= perceptronNet[i+1].length-1; k++) {
                    hiddenErrorGradientXweight += (errorGradient[i+1][j]
                            * perceptronNet[i+1][k]
                            .weightList.get(j));
                }
            }
            for (int j = 0; j<= errorGradient[i].length-1; j++) {
                errorGradient[i][j] =
                        (float) ((1 / (1 + Math.exp(-1 * (perceptronNet[i][j].getSumOfInputs())))) * (1 - (1 / (1 + Math.exp(-1 * (perceptronNet[i][j].getSumOfInputs()))))) * hiddenErrorGradientXweight);
            }
        }
    }
    void updateOutputErrorGradient() {
        for(int i = 0; i<=perceptronNet[perceptronNet.length-1].length-1; i++) {
            errorGradient[errorGradient.length-1][i] =
                    (float) (1/(1+Math.exp(-perceptronNet[perceptronNet.length-1][i].sumOfInputs))*
                    (1-(1/(1+Math.exp(-perceptronNet[perceptronNet.length-1][i].sumOfInputs))))*errorSignal.get(i));
        }
    }

    void updateWeight() {
        for(int i = 0; i<= perceptronNet[perceptronNet.length-2].length-1; i++) {
            for(int j = 0; i<= perceptronNet[perceptronNet.length-1].length-1; i++) {
                weightOfWeight[perceptronNet.length-2][i][j] =
                        (Perceptron.learningRate * perceptronNet[perceptronNet.length - 2][i].output * errorGradient[perceptronNet.length-2][j]);
            }
        }
        for(int i = 0; i<=perceptronNet[perceptronNet.length-2].length-1; i++) {
            perceptronNet[perceptronNet.length-2][i].updateWeight(weightOfWeight[perceptronNet.length-2][i]);
        }

    }


    void activate() {
        transmission();
        updateErrorSignal();
        updateOutputErrorGradient();
        updateHiddenErrorGradient();
        updateWeight();
        epoch++;
    }

    void printResult() { //dummy?
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
