package com.kauruck.coastEngine.centum.world;

import com.kauruck.coastEngine.centum.component.AbstractComponent;
import com.kauruck.coastEngine.centum.entity.Entity;
import com.kauruck.coastEngine.centum.system.AbstractSystem;

import java.util.ArrayList;
import java.util.List;

public class World {
    private int id;
    private boolean active = true;

    private final List<Entity> entities = new ArrayList<>();

    public World() {
    }

    public void processEntities(float deltaTime, AbstractSystem<?> system){
        for(Entity current : entities){
            AbstractComponent component = current.getComponent(system.getComponentClass());
            if(component != null)
                system.process(component, deltaTime);

        }
    }

    public void addEntity(Entity entity){
        entities.add(entity);
    }

    public void removeEntity(Entity entity){
        entities.remove(entity);
    }

    public void onCreate(){}
    public void onDelete(){}

    public void create(int id){
        this.id = id;
        onCreate();
    }

    public int getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void delete() {
        this.id = 0;
        onDelete();
    }
}
