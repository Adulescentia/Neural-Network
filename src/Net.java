import java.util.ArrayList;
public class Net {
    Perceptron[][] perceptronNet;

    ArrayList<Float> inputValue;
    ArrayList<Float> outputValue;
    ArrayList<Float> targetOutputValue;

    ArrayList<Float> errorSignal;
    float[][] errorGradient;
    float weightOfWeight;
    float hiddenErrorGradientXweight;
    float learningRate;

    Float sumOfTheSquaredErrors;
    int epoch = 1;
    Net(int functionType, float learningRate, int inputPerceptronCount, int[] hiddenPerceptronCount, int outputPerceptronCount, ArrayList<Float> inputValue, ArrayList<Float> targetOutputValue) {

        perceptronNet = new Perceptron[1 + hiddenPerceptronCount.length + 1][]; //input, hidden, output

        Perceptron.functionType = functionType;
        this.learningRate = learningRate;

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

//        //weight grid initializer
//        weightOfWeight = new float[1 + hiddenPerceptronCount.length][][];
//        for (int i = 0; i <= perceptronNet.length - 2; i++) {
//            weightOfWeight[i] = new float[perceptronNet[i].length][];
//            for (int j = 0; j <= perceptronNet[i].length-1; j++) {
//                weightOfWeight[i][j] = new float[perceptronNet[i+1].length];
//            }
//        }
    }

    void transmission () {
        for (int i = 0; i <= perceptronNet[0].length-1; i++) {
            for (float input : inputValue) {
                System.out.println(input);
                perceptronNet[0][i].setInput(input);
                perceptronNet[0][i].calculate();
            }
        }
        for (int i = 1; i <= perceptronNet.length-1; i++) {
            for (int j = 0; j <= perceptronNet[i].length-1; j++ ) {
                for (int k = 0; k <= perceptronNet[i - 1].length - 1; k++) {
                    System.out.println(perceptronNet[i - 1][k].getOutput().get(j));
                    perceptronNet[i][j].setInput(perceptronNet[i - 1][k].getOutput().get(j));
                    perceptronNet[i][j].calculate();
                }
            }
        }

//        for (int i = 0; i<= perceptronNet[perceptronNet.length-1].length-1; i++) {
//            System.out.println(perceptronNet[perceptronNet.length-1][i].calculatedOutputList);
//            perceptronNet[perceptronNet.length-1][i].calculate();
//            outputValue.addAll(perceptronNet[perceptronNet.length-1][i].calculatedOutputList);
//        }
    }

    void updateErrorSignal() {
        for (float i : outputValue) {
//            System.out.println(i);
        }
        for (float i : targetOutputValue) {
//            System.out.println(i);
        }
        for(int i = 0; i<=perceptronNet[perceptronNet.length-1].length-1; i++) {
            errorSignal.add(targetOutputValue.get(i) - perceptronNet[perceptronNet.length-1][i].calculatedOutputList.getFirst());
        }
    }

    void updateHiddenErrorGradient() {

        for (int i = errorGradient.length-2; i >= 0; i--) {
            for (int j = 0; j<= errorGradient[i+1].length-1; j++) {
                for (int k = 0; k <= perceptronNet[i+1].length-1; k++) {
                    hiddenErrorGradientXweight = 0;
                    hiddenErrorGradientXweight += (errorGradient[i+1][j]
                            * perceptronNet[i+1][k]
                            .weightList.get(j));
                }
            }
            for (int j = 0; j<= errorGradient[i].length-1; j++) { //todo 은닉층 2개 이상이면 에러뜸
                errorGradient[i]
                        [j] = //todo Index 1 out of bounds for length 1
                        // 이번엔 또 오류 안뜨네???????????????????? 뭐가 문제인거임
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

    void updateHiddenWeight() {
        for(int i = 0; i <= perceptronNet.length-3; i++) {
            for(int j = 0; j <= perceptronNet[i].length-1; j++) {
                for(int k = 0; k <= perceptronNet[i+1].length-1; k++) {
                    weightOfWeight = perceptronNet[i][j].getWeight(k) + learningRate*perceptronNet[i][j].calculatedOutputList.get(k) * errorGradient[i][k];
                    System.out.println("{}{}"+perceptronNet[i][j].output);
                    perceptronNet[i][j].updateWeight(k, weightOfWeight);
               }
            }
        }
    }

    void updateOutputWeight() {
        for(int j = 0; j <= perceptronNet[perceptronNet.length-2].length-1; j++) {
            for(int k = 0; k <= perceptronNet[perceptronNet.length-1].length-1; k++) {
                System.out.println("{}{}{}"+(perceptronNet[perceptronNet.length-2][j].calculatedOutputList.get(k) ));//이새끼 0이네?
                weightOfWeight = perceptronNet[perceptronNet.length-2][j].getWeight(k) + (learningRate*perceptronNet[perceptronNet.length-2][j].calculatedOutputList.get(k) * errorGradient[perceptronNet.length-2][k]);

                perceptronNet[perceptronNet.length-2][j].updateWeight(k, weightOfWeight);
            }
        }
    }

    void updateBias () {
        for(int i = 0; i <= perceptronNet.length-2; i++) {
            for(int j = 0; j <= perceptronNet[i].length-1; j++) {
                for(int k = 0; k <= perceptronNet[i+1].length-1; k++) {
                    perceptronNet[i][j].updateBias(learningRate*-1*errorGradient[i][k]);
                }
            }
        }
    }

    void printResult() { //dummy?
        System.out.printf("-------epoch:%d-------\n",epoch);
        System.out.print("Target output is:");
        for (Float targetOutput : targetOutputValue) {
            System.out.print(targetOutput + ", ");
        }
        System.out.print("\n-------output is:");
        for (Perceptron output : perceptronNet[perceptronNet.length-1]) {
            System.out.print(output.calculatedOutputList.getFirst() + ", ");
        }
        System.out.println();
    }

    void activate() {

//        while (true) {
//            if (true) { //허용범위
////                break;
//                transmission();
//                updateErrorSignal();
//                updateOutputErrorGradient();
//                updateHiddenErrorGradient();
//                updateWeight();
//                printResult();
//
//                epoch++;
//                outputValue.clear();
//                for (Perceptron[] layer : perceptronNet) {
//                    for (Perceptron perceptron : layer) {
//                        perceptron.reset();
//                    }
//                }
//            } else {
//
//            }
//        }
        for(int i = 0; i<=3000; i++) {
            transmission();
            updateErrorSignal();
            updateOutputErrorGradient();
            updateOutputWeight();
            updateHiddenErrorGradient();
            updateHiddenWeight();
            updateBias();
            printResult();

            epoch++;
            outputValue.clear();
            for (Perceptron[] layer : perceptronNet) {
                for (Perceptron perceptron : layer) {
                    perceptron.reset();
                }
            }
        }
    }
}
