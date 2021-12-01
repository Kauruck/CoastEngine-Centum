package com.kauruck.coastEngine.centum.entity;

import com.kauruck.coastEngine.centum.component.AbstractComponent;
import com.kauruck.coastEngine.core.math.Vector2;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Entity {
    private int id;

    private final List<AbstractComponent> components = new ArrayList<>();


    public void addComponent(AbstractComponent component){
        component.setEntity(this);
        this.components.add(component);
    }

    public boolean hasComponent(Class<? extends AbstractComponent> componentClass){
        for(AbstractComponent current : components){
            if(current.getClass() == componentClass)
                return true;
        }
        return false;
    }

    public AbstractComponent getComponent(Class<? extends AbstractComponent> componentClass){
        for(AbstractComponent current : components){
            if(current.getClass() == componentClass)
                return current;
        }
        return null;
    }
}
