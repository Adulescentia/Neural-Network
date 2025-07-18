import java.util.ArrayList;
public class Net {
    Perceptron[][] perceptronNet;

    ArrayList<Float> inputValue;
    ArrayList<Float> outputValue;
    ArrayList<Float> targetOutputValue;

    ArrayList<Float> errorSignal;
    float[][] errorGradient;
    float[][][] weightOfWeight;
    float[][] weightOfBias;
    float hiddenErrorGradientXweight;
    float learningRate;
    float SSE;

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
        for (int i = 0; i <= inputPerceptronCount - 1; i++) { //make input layer
            perceptronNet[0][i] = new Perceptron(1, hiddenPerceptronCount[0], false);
        }

        //make hidden layers
        for (int i = 0; i <= hiddenPerceptronCount.length - 1; i++) {
            perceptronNet[i + 1] = new Perceptron[hiddenPerceptronCount[i]];
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
        for (int i = 0; i <= outputPerceptronCount - 1; i++) { //make output layer
            perceptronNet[perceptronNet.length - 1][i] = new Perceptron((hiddenPerceptronCount[hiddenPerceptronCount.length - 1]), 1, true);
        }

        //error gradient initializer
        for (int i = 0; i <= errorGradient.length - 1; i++) {
            errorGradient[i] = new float[perceptronNet[i + 1].length];
        }

        //weight grid initializer
        weightOfWeight = new float[1 + hiddenPerceptronCount.length][][];
        for (int i = 0; i <= perceptronNet.length - 2; i++) {
            weightOfWeight[i] = new float[perceptronNet[i].length][];
            for (int j = 0; j <= perceptronNet[i].length - 1; j++) {
                weightOfWeight[i][j] = new float[perceptronNet[i + 1].length];
            }
        }

        //bias grid initializer
        weightOfBias = new float[hiddenPerceptronCount.length + 1][];
        for (int i = 0; i <= weightOfBias.length - 1; i++) {
            weightOfBias[i] = new float[perceptronNet[i + 1].length];
        }
    }

    void transmission() {
        for (int i = 0; i <= perceptronNet[0].length - 1; i++) {
            for (float input : inputValue) {
                System.out.println("input0," + i + ":" + input);
                perceptronNet[0][i].setInput(input);

            }
            perceptronNet[0][i].calculate();
        }
        for (int i = 1; i <= perceptronNet.length - 1; i++) {
            for (int j = 0; j <= perceptronNet[i].length - 1; j++) {
                for (int k = 0; k <= perceptronNet[i - 1].length - 1; k++) {
                    System.out.println("input" + i + "," + j + ":" + perceptronNet[i - 1][k].getOutput().get(j));
                    perceptronNet[i][j].setInput(perceptronNet[i - 1][k].getOutput().get(j));
                }
                perceptronNet[i][j].calculate();
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
        for (int i = 0; i <= perceptronNet[perceptronNet.length - 1].length - 1; i++) {
            errorSignal.add(targetOutputValue.get(i) - perceptronNet[perceptronNet.length - 1][i].calculatedOutputList.getFirst());

        }
    }

    void updateOutputErrorGradient() {
        for (int i = 0; i <= perceptronNet[perceptronNet.length - 1].length - 1; i++) {
            errorGradient[errorGradient.length - 1][i] =
                    (float) (1 / (1 + Math.exp(-perceptronNet[perceptronNet.length - 1][i].sumOfInputs)) *
                            (1 - (1 / (1 + Math.exp(-perceptronNet[perceptronNet.length - 1][i].sumOfInputs)))) * errorSignal.get(i));
        }
    }

    void updateHiddenErrorGradient() {
        for (int i = errorGradient.length - 2; i >= 0; i--) {
            for (int j = 0; j <= errorGradient[i + 1].length - 1; j++) {
                for (int k = 0; k <= perceptronNet[i + 1].length - 1; k++) {
                    hiddenErrorGradientXweight = 0;
                    hiddenErrorGradientXweight += (errorGradient[i + 1][j]
                            * perceptronNet[i + 1][k]
                            .weightList.get(j));
                }
            }
            for (int j = 0; j <= errorGradient[i].length - 1; j++) { //todo 은닉층 2개 이상이면 에러뜸
                errorGradient[i][j] = (float) ((1 / (1 + Math.exp(-1 * (perceptronNet[i][j].getSumOfInputs())))) * (1 - (1 / (1 + Math.exp(-1 * (perceptronNet[i][j].getSumOfInputs()))))) * hiddenErrorGradientXweight);
                //todo Index 1 out of bounds for length 1
                // 이번엔 또 오류 안뜨네???????????????????? 뭐가 문제인거임
            }
        }
    }

    void setOutputWeightOfWeight() {
        for (int i = 0; i <= weightOfWeight[weightOfWeight.length - 1].length - 1; i++) {
            for (int j = 0; j <= weightOfWeight[weightOfWeight.length - 1][i].length - 1; j++) {
                weightOfWeight[weightOfWeight.length - 1][i][j] = learningRate * perceptronNet[perceptronNet.length - 2][i].calculatedOutputList.get(j) * errorGradient[perceptronNet.length - 2][j];
            }
        }
    }

    void setHiddenWeightOfWeight() {
        for (int i = 0; i <= weightOfWeight.length - 2; i++) {
            for (int j = 0; j <= weightOfWeight[i].length - 1; j++) {
                for (int k = 0; k <= weightOfWeight[i + 1].length - 1; k++) {
                    weightOfWeight[i][j][k] = learningRate * perceptronNet[i][j].calculatedOutputList.get(k) * errorGradient[i][k];
//                    System.out.println("DDD"+weightOfWeight[i][j][k]);
                }
            }
        }
    }

    void setOutputBias() {
        for (int i = 0; i <= perceptronNet[perceptronNet.length - 1].length - 1; i++) {
            weightOfBias[weightOfBias.length - 1][i] = -1 * learningRate * errorGradient[perceptronNet.length - 2][i];
        }
    }

    void setHiddenBias() {
        for (int i = 1; i <= perceptronNet.length - 2; i++) {
            for (int j = 0; j <= perceptronNet[i].length - 1; j++) {
                weightOfBias[i - 1][j] = -1 * learningRate * errorGradient[i - 1][j];
            }
        }
    }


    //    void updateHiddenWeight() {
//        for(int i = 0; i <= perceptronNet.length-3; i++) {
//            for(int j = 0; j <= perceptronNet[i].length-1; j++) {
//                for(int k = 0; k <= perceptronNet[i+1].length-1; k++) {
//                    weightOfWeight = perceptronNet[i][j].getWeight(k) + learningRate*perceptronNet[i][j].calculatedOutputList.get(k) * errorGradient[i][k];
//                    System.out.println("{}{}"+perceptronNet[i][j].output);
//                    perceptronNet[i][j].updateWeight(k, weightOfWeight);
//               }
//            }
//        }
//    }
//
//    void updateOutputWeight() {
//        for(int j = 0; j <= perceptronNet[perceptronNet.length-2].length-1; j++) {
//            for(int k = 0; k <= perceptronNet[perceptronNet.length-1].length-1; k++) {
//                System.out.println("{}{}{}"+(perceptronNet[perceptronNet.length-2][j].calculatedOutputList.get(k) ));//이새끼 0이네?
//                weightOfWeight = perceptronNet[perceptronNet.length-2][j].getWeight(k) + (learningRate*perceptronNet[perceptronNet.length-2][j].calculatedOutputList.get(k) * errorGradient[perceptronNet.length-2][k]);
//
//                perceptronNet[perceptronNet.length-2][j].updateWeight(k, weightOfWeight);
//            }
//        }
//    }
    void updateWeight() {
        for (int i = 0; i <= weightOfWeight.length - 1; i++) {
            for (int j = 0; j <= weightOfWeight[i].length - 1; j++) {
                for (int k = 0; k <= weightOfWeight[i][j].length - 1; k++) {
                    perceptronNet[i][j].updateWeight(k, weightOfWeight[i][j][k]);
//                    System.out.println("ww "+weightOfWeight[i][j][k]);
//                    System.out.println("W  "+perceptronNet[i][j].weightList.get(k));
                }
            }
        }
    }

    void updateBias() {
        for (int i = 1; i <= perceptronNet.length - 1; i++) {
            for (int j = 0; j <= perceptronNet[i].length - 1; j++) {
                perceptronNet[i][j].updateBias(weightOfBias[i - 1][j]);
            }
        }
    }

    void printResult() { //dummy?
        System.out.printf("-------epoch:%d-------\n", epoch);
        System.out.print("Target output is:");
        for (Float targetOutput : targetOutputValue) {
            System.out.print(targetOutput + ", ");
        }
        System.out.print("\n-------output is:");
        for (Perceptron output : perceptronNet[perceptronNet.length - 1]) {
            System.out.print(output.calculatedOutputList.getFirst() + ", ");
        }

        System.out.println();
    }

    void activate() {

        while (true) {
            transmission();
            updateErrorSignal();

            updateOutputErrorGradient();
            setOutputWeightOfWeight();
            setOutputBias();

            updateHiddenErrorGradient();
            setHiddenWeightOfWeight();
            setHiddenBias();

            updateWeight();
            updateBias();

            printResult();



            for (float err : errorSignal) {
                SSE = 0;
                SSE += (float) Math.pow(err, 2);
            }
            if(SSE <= 0.001) {
                break;
            }
            outputValue.clear();
            for (Perceptron[] layer : perceptronNet) {
                for (Perceptron perceptron : layer) {
                    perceptron.reset();
                }
            }
            epoch++;
        }
        System.out.println("------------------Learning completed------------------");
        System.out.println("epoch: "+epoch);
        System.out.print("Target output is:");
        for (Float targetOutput : targetOutputValue) {
            System.out.print(targetOutput + ", ");
        }
        System.out.print("\n-------output is:");
        for (Perceptron output : perceptronNet[perceptronNet.length - 1]) {
            System.out.print(output.calculatedOutputList.getFirst() + ", ");
        }
        System.out.println();
        System.out.println("----weight----");
        for (int i = 0; i <= perceptronNet.length-2; i++) {
            for (int j = 0; j <= perceptronNet[i].length-1; j++) {
                for (int k = 0; k <= perceptronNet[i+1].length-1; k++) {
                    System.out.println("(" + i + ", " + j + ") -> " + "(" + (i + 1) + ", " + k + ") : " + perceptronNet[i][j].weightList.get(k));
                }
            }
        }
        System.out.println("-----bias-----");
        for (int i = 1; i <= perceptronNet.length-1; i++) {
            for (int j = 0; j <= perceptronNet[i].length-1; j++) {
                System.out.println("(" + i + ", " + j + ") : " + perceptronNet[i][j].bias);
            }
        }
    }
}