package model.values;

public class BoolValue implements Value<Boolean> {

    private final Boolean value;

    public BoolValue(boolean value){
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.Bool;
    }

    @Override
    public Boolean getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    public boolean equals(BoolValue another) {
        return this.value.equals(another.value);
    }

}
