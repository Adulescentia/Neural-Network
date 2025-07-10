import java.util.ArrayList;
public class Net {
    Perceptron[][] perceptronNet;

    ArrayList<Float> inputValue;
    ArrayList<Float> outputValue;
    ArrayList<Float> targetOutputValue;

    ArrayList<Float> errorSignal;
    ArrayList<Float>[] errorGradient;
    float[][][] weightOfWeight;

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
        errorGradient = new ArrayList[hiddenPerceptronCount.length + 1];



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

        //weight grid initializer
        weightOfWeight = new float[1 + hiddenPerceptronCount.length][][];
        for (int i = 0; i <= perceptronNet.length - 2; i++) {
            weightOfWeight[i] = new float[perceptronNet[i].length][];
            for (int j = 0; j <= perceptronNet[i].length-1; j++) {
                weightOfWeight[i][j] = new float[perceptronNet[i+1].length];
            }
        }
    }


//    void transmission () { //??????????? wtf
//        for (int i = 0; i < perceptronNet.length; i++) {
//            for (Perceptron perceptron : perceptronNet[i]) {
//                if (i == 0) {
//                    for (float input : inputValue) {
//                        perceptron.setInput(input);
//                    }
//                }else if (i < perceptronNet.length-1) {
//                    for (int j = 0; j < perceptronNet[i+1].length; j++) {
//                        System.out.println(i+"  "+j);
//                        perceptronNet[i+1][j].setInput(perceptron.getOutput().get(j));
//                    }
//                } else {
//                    for (int j = 0; j <= perceptronNet[i].length; j++) {
//                        outputValue.addAll(perceptron.getOutput());
//                    }
//                }
//            }
//        }
//    }

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

    void updateOutputLayerErrorGradient() {
        for(int i = 0; i<=perceptronNet[perceptronNet.length-1].length-1; i++) {
            errorGradient[perceptronNet.length-2].add(
                    (float) (1/(1+Math.exp(-perceptronNet[perceptronNet.length-1][i].sumOfInputs))*
                    (1-(1/(1+Math.exp(-perceptronNet[perceptronNet.length-1][i].sumOfInputs))))*errorSignal.get(i)));
        }
    }

    void updateOutputLayerWeight() {
        for(int i = 0; i<= perceptronNet[perceptronNet.length-2].length-1; i++) {
            for(int j = 0; i<= perceptronNet[perceptronNet.length-1].length-1; i++) {
                weightOfWeight[perceptronNet.length-2][i][j] = (Perceptron.learningRate * perceptronNet[perceptronNet.length - 2][i].output * errorGradient[perceptronNet.length-2].get(j));
            }
        }
        for(int i = 0; i<=perceptronNet[perceptronNet.length-2].length-1; i++) {
            perceptronNet[perceptronNet.length-2][i].updateWeight(weightOfWeight[perceptronNet.length-2][i]);
        }

    }

    void updateHiddenLayerWeight() {
        //todo
    }

    void updateHiddenLayerErrorGradient() {
        //todo
    }

    void activate() {
        transmission();
        updateErrorSignal();
        updateOutputLayerErrorGradient();
        updateOutputLayerWeight();
        updateHiddenLayerErrorGradient();
        updateHiddenLayerWeight();
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
