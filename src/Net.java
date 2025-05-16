import java.util.ArrayList;
public class Net {
    ArrayList<Perceptron>[] perceptronNet;

    ArrayList<Float> inputValue;
    ArrayList<Float> outputValue;
    ArrayList<Float> targetOutputValue;

    ArrayList<Float> errorSignal;
    ArrayList<Float>[] errorGradient;
    ArrayList<ArrayList<Float>>[] weightOfWeight;

    Float sumOfTheSquaredErrors;
    int epoch = 0;
    Net(int functionType, float learningRate, int inputPerceptronCount, int[] hiddenPerceptronCount, int outputPerceptronCount, ArrayList<Float> inputValue, ArrayList<Float> targetOutputValue) {

        perceptronNet = new ArrayList[1 + hiddenPerceptronCount.length + 1]; //input, hidden, output

        Perceptron.functionType = functionType;
        Perceptron.learningRate = learningRate;

        this.inputValue = inputValue;
        this.outputValue = new ArrayList<>();
        this.targetOutputValue = targetOutputValue;

        errorSignal = new ArrayList<>();
        errorGradient = new ArrayList[hiddenPerceptronCount.length+1];
        weightOfWeight = new ArrayList[1 + hiddenPerceptronCount.length-1]; //input, hidden

        for (int i = 0; i <= perceptronNet.length-2; i++) {//initializer of weight
            weightOfWeight[i] = new ArrayList<>();
        }

        for (int i = 0; i <= perceptronNet.length-1; i++) {//initializer of net
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

    void updateErrorSignal() {
        for(int i = 0; i<=perceptronNet[perceptronNet.length-1].size()-1; i++) {
            errorSignal.add(targetOutputValue.get(i) - outputValue.get(i));
        }
    }

    void updateOutputLayerErrorGradient() {
        for(int i = 0; i<=perceptronNet[perceptronNet.length-1].size()-1; i++) {
            errorGradient[perceptronNet.length-2].add((float) (1/(1+Math.exp(-perceptronNet[perceptronNet.length-1].get(i).sumOfInputs))*(1-(1/(1+Math.exp(-perceptronNet[perceptronNet.length-1].get(i).sumOfInputs))))*errorSignal.get(i)));
        }
    }

    void updateOutputLayerWeight() {
        for(int i = 0; i<=perceptronNet[perceptronNet.length-2].size()-1; i++) {
            for(int j = 0; i<= perceptronNet[perceptronNet.length-1].size()-1; i++) {
                weightOfWeight[perceptronNet.length-2].get(i).add(Perceptron.learningRate * perceptronNet[perceptronNet.length - 2].get(i).output * errorGradient[perceptronNet.length-2].get(j));
            }
        }
        for(int i = 0; i<=perceptronNet[perceptronNet.length-2].size()-1; i++) {
            perceptronNet[perceptronNet.length-2].get(i).updateWeight(weightOfWeight[perceptronNet.length-2].get(i));
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
