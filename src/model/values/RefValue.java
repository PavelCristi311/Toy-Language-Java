package model.values;

import model.type.IType;
import model.type.RefType;

public class RefValue implements IValue {
    int address;
    IType locationType;

    public RefValue(int address, IType locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddress() {
        return address;
    }

    @Override
    public IType getType() {
        return new RefType(locationType);
    }

    public IType getLocationType() {
        return locationType;
    }

    @Override
    public IValue deepCopy() {
        return new RefValue(address, locationType.deepCopy());
    }

    public String toString() {
        return "RefValue(" + address + "," + locationType + ")";
    }
}
