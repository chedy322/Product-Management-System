package com.example.demo.Domain.Primitives;
import java.util.UUID;

public abstract class Entity {
    private final UUID id;
    protected Entity(UUID id){
        this.id=id;
    }
    public UUID getId() {return id;}
    // entity is equal if it has the same id
    @Override
    public boolean equals(Object obj) {
        if(obj==null || obj.getClass()!=getClass()) return false;
        Entity opt=(Entity) obj;
        return this.getId().equals(opt.getId());
    }
    // hash
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
