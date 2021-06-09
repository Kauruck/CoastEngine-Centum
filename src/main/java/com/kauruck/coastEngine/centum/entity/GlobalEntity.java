package com.kauruck.coastEngine.centum.entity;

import com.kauruck.coastEngine.centum.component.AbstractComponent;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class GlobalEntity {
    private int id;

    private final List<AbstractComponent> components = new ArrayList<AbstractComponent>();

    public void addComponent(AbstractComponent component){
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
