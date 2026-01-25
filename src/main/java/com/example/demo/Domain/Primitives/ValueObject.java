package com.example.demo.Domain.Primitives;

import java.util.List;

// value objects are immutable and they are compared by their values not by id 
public abstract class ValueObject {
    protected abstract List<Object> getAtomicValues();

    @Override
    public boolean equals(Object obj) {
        if(obj==null || obj.getClass()!=getClass()) return false;
        ValueObject opt=(ValueObject) obj;
        return getAtomicValues().equals(opt.getAtomicValues());
    }

    @Override
    public int hashCode() {
        return getAtomicValues().hashCode();
    }


}
