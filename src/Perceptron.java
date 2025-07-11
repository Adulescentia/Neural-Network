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

    Perceptron(int inputCount, int outputCount, boolean isOutputLayer) {
        inputList = new ArrayList<>();
        calculatedOutputList = new ArrayList<>();
        if (!isOutputLayer) {
            for (int i = 0; i <= outputCount; i++) { //weight initializing
                // TO-DO 각 활성화 함수에 맞는 초기화 함수 사용 https://wikidocs.net/259052  - 현재는 He initializing
                weightList.add((float) (random.nextDouble((2 * Math.sqrt((double) 6 / inputCount))) - Math.sqrt((double) 6 / inputCount))); //일단은 ReLU 용
            }
        } else {
            for (int i = 0; i <= outputCount; i++) {
                weightList.add(1F);
            }
        }
        this.activationFunction = selectActivationFunction(functionType);
    }

    void calculate() {
        output = 0;
        for (float input : inputList) {
            output += input;
        }
        output -= bias;
        for (float weight : weightList) {
//            System.out.println(activate(output * weight));
            calculatedOutputList.add(activate(output * weight));
        }
    }

    ArrayList<Float> getOutput() {
        calculate();
        for (Float c : calculatedOutputList) {
//            System.out.println("-"+c);
        }
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
        for (float input : inputList) {
            sumOfInputs += input;
        }
        return sumOfInputs;
    }
    void updateWeight(int index, float weightOfWeight) {
        weightList.set(index, weightOfWeight);
    }
    float getWeight(int index) {
        return weightList.get(index);
    }
    public float activate(float value) {
        return activationFunction.apply(value);
    }
    void reset () {
        output = 0;
        inputList.clear();
        sumOfInputs = 0;
        calculatedOutputList.clear();
    }
}