package model.values;

public class ReferenceValue implements Value<Integer> {

    private final int address;
    private final Type locationType;

    public ReferenceValue(int address, Type type) {
        this.address = address;
        this.locationType = type;
    }

    @Override
    public Type getType() {
        return new ReferenceType(locationType);
    }

    public Integer getAddress(){
        return address;
    }

    @Override
    public Integer getValue() {
        return address;
    }

    @Override
    public String toString() {
        return "@" + address;
    }
}
