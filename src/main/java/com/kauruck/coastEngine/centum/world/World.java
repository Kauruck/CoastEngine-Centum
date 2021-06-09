package com.kauruck.coastEngine.centum.world;

import com.kauruck.coastEngine.centum.component.AbstractComponent;
import com.kauruck.coastEngine.centum.entity.GlobalEntity;
import com.kauruck.coastEngine.centum.system.AbstractSystem;

import java.util.ArrayList;
import java.util.List;

public class World {
    private int id;
    private boolean active = true;

    private final List<GlobalEntity> globalEntities = new ArrayList<GlobalEntity>();

    public World() {
    }

    public void processEntities(float deltaTime, AbstractSystem<?> system){
        for(GlobalEntity current : globalEntities){
            AbstractComponent component = current.getComponent(system.getComponentClass());
            if(component != null)
                system.process(component);
        }
    }

    public void addGlobalEntity(GlobalEntity entity){
        globalEntities.add(entity);
    }

    public void removeGlobalEntity(GlobalEntity entity){
        globalEntities.remove(entity);
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
