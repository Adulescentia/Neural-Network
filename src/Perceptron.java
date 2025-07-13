import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

interface ActivationFunction {
    float apply(float x);
}
public class Perceptron {
    Random random = new Random();
    static int functionType;
    private final ActivationFunction activationFunction;
    float bias;
    float output;
    ArrayList<Float> inputList;
    float sumOfInputs = 0;
    ArrayList<Float> calculatedOutputList;
    ArrayList<Float> weightList = new ArrayList<>();;
    boolean isOutPut;
    Perceptron(int inputCount, int outputCount, boolean isOutputLayer) {
        inputList = new ArrayList<>();
        calculatedOutputList = new ArrayList<>();
        bias = random.nextFloat()-1;
        for (int i = 0; i <= outputCount-1; i++) { //weight initializing
            // TO-DO 각 활성화 함수에 맞는 초기화 함수 사용 https://wikidocs.net/259052  - 현재는 He initializing
//                weightList.add((float) (random.nextDouble((2 * Math.sqrt((double) 6 / inputCount))) - Math.sqrt((double) 6 / inputCount))); //일단은 ReLU 용
//                weightList.add((float) (random.nextDouble() * Math.sqrt(2.0 / inputCount)));
            weightList.add(random.nextFloat()*2-1);
        }

        this.activationFunction = selectActivationFunction(functionType);
        this.isOutPut = isOutputLayer;
    }

    void calculate() {
        for (float input : inputList) {
            output += input;
        }
        output -= bias;
        if (isOutPut) {
            System.out.println(output);
        }
        for (float weight : weightList) {
            if (isOutPut) {
                calculatedOutputList.add(activate(output));
            } else {
                calculatedOutputList.add(activate(output) * weight);
            }
        }
    }
    ArrayList<Float> getOutput() {
        return calculatedOutputList;
    }

    void setInput(float inputValue) {
        inputList.add(inputValue);
    }

    private ActivationFunction selectActivationFunction(int functionType) {
        return switch (functionType) {
            case 1 -> (x -> x); //linear
            case 2 -> (x -> Math.max(0, x)); //ReLU
            case 3 -> (x -> Math.max(0.1f * x, x)); //Leaky_ReLU
            case 4 -> (x -> (float) (1 / (1 + Math.exp(-x)))); //sigmoid
            case 5 -> (x -> (float) Math.tanh(x)); //tanh
            default -> throw new IllegalArgumentException("Invalid function type: " + functionType);
        };
    }

    float getSumOfInputs() {
        sumOfInputs = 0;
        for (float input : inputList) {
            sumOfInputs += input;
        }
        return sumOfInputs;
    }

    void updateWeight(int index, float weightOfWeight) {
        weightList.set(index, weightList.get(index) + weightOfWeight);
//        System.out.println("uPW  "+weightList.get(index) + weightOfWeight);
    }
    void updateBias(float Bias) {
        bias += Bias;
    }

    float getWeight(int index) {
        System.out.println("{"+weightList.get(index));
        return weightList.get(index);

    }
    public float activate(float value) {
//        return activationFunction.apply(value);
        return (float) (1 / (1 + Math.exp(-value)));
    }
    void reset () {
        output = 0;
        inputList.clear();
        sumOfInputs = 0;
        calculatedOutputList.clear();
    }
}