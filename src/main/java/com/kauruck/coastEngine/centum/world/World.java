package com.kauruck.coastEngine.centum.world;

import com.kauruck.coastEngine.centum.Centum;
import com.kauruck.coastEngine.centum.component.AbstractComponent;
import com.kauruck.coastEngine.centum.entity.Entity;
import com.kauruck.coastEngine.centum.system.AbstractSystem;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class World {
    private int id;
    private boolean active = true;

    private final List<Entity> entities = new ArrayList<>();

    public World() {
    }

    public void processEntities(float deltaTime, AbstractSystem<?> system){
        //No enhanced for loop, hopefully no concurrency errors more but just to be sure, here is a try catch
        try {
            for (int i = 0; i < entities.size(); i++) {
                Entity current = entities.get(i);
                if(current != null) {
                    AbstractComponent component = current.getComponent(system.getComponentClass());
                    if (component != null)
                        system.process(component, deltaTime);
                }
            }
        }
        catch (ConcurrentModificationException ignored){

        }catch (Exception e){
            Centum.logger.warn("System: " + system.getID() + " had an error. Here it is: " + e);
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
