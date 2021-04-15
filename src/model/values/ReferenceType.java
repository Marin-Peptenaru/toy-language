package model.values;

public class ReferenceType extends Type {

    private final Type inner;

    public ReferenceType(Type innerType){
        super(TypeLabel.REFERENCE);
        inner = innerType;
    }

    public Type getInner(){
        return inner;
    }

    @Override
    public boolean equals(Type other){
        if(!(other instanceof ReferenceType))
            return false;
        return this.inner.equals(((ReferenceType)other).inner);
    }

    @Override
    public String toString() {
        return TypeLabel.REFERENCE + "(" + inner + ")";
    }
}
