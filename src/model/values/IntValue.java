package model.values;

import exception.MyException;

public class IntValue implements Value<Integer>{

    private final Integer value;

    public IntValue(int value){this.value = value;}

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public Type getType() {
        return Type.Int;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    public boolean equals(IntValue another) {
        return this.value.equals(another.value);
    }



}
