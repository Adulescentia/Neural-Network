class ActivationFunction {
    String name;
    float run (float value, int type) {
        return switch (type) {
            case 1 -> value;                                // 1:linear
            case 2 -> Math.max(0,value);                    // 2:ReLU
            case 3 -> (float) Math.max(0.1*value,value);    // 3:leaky ReLU
            case 4 -> (float) (1/(1+Math.exp(-value)));     // 4:sigmoid
            case 5 -> (float) Math.tanh(value);             // 5:tanh
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}

