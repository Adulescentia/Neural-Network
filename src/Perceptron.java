import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Perceptron {
    Random random = new Random();
    static int functionType;
    static float learningRate;
    static float bias;
    float output;
    ArrayList<Float> inputList;
    ArrayList<Float> weightedOutputList;
    ArrayList<Float> weightList;
    Perceptron (int inputCount, int outputCount) {
        inputList = new ArrayList<>();
        weightedOutputList = new ArrayList<>();
        weightList = new ArrayList<>();
        for (int i = 0; i < outputCount; i ++) { //weight initializing
            // TO-DO 각 활성화 함수에 맞는 초기화 함수 사용 https://wikidocs.net/259052  - 현재는 He initializing
            weightList.add(random.nextFloat((float) (2* Math.sqrt((double) 6 /inputCount)))); //일단은 ReLU 용
        }
    }
    void calculate () {
        output = 0;
        for (float input : inputList) {
            output += input;
        }
        output -= bias;
        for (float weight : weightList) {
            weightedOutputList.add(output*weight);
        }
    }

    ArrayList<Float> getOutput () {
        return weightedOutputList;
    }

    void setInput (float inputValue) {
        inputList.add(inputValue);
    }


    float activate (float value) { //activation function
        return switch (functionType) {
            case 1 -> value;                                // 1:linear
            case 2 -> Math.max(0,value);                    // 2:ReLU
            case 3 -> (float) Math.max(0.1*value,value);    // 3:leaky ReLU
            case 4 -> (float) (1/(1+Math.exp(-value)));     // 4:sigmoid
            case 5 -> (float) Math.tanh(value);             // 5:tanh
            default -> throw new IllegalStateException("Unexpected value: " + functionType);
        };
    }
}
