package com.kauruck.coastEngine.centum;

import com.kauruck.coastEngine.centum.component.AbstractComponent;
import com.kauruck.coastEngine.centum.system.AbstractSystem;
import com.kauruck.coastEngine.centum.world.World;
import com.kauruck.coastEngine.core.exception.NoSuchProcessException;
import com.kauruck.coastEngine.core.threding.Thread;
import com.kauruck.coastEngine.core.threding.ThreadManger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Centum {

    public static final Logger logger = LoggerFactory.getLogger("Centum");

    private static final List<World> worlds = new ArrayList<>();
    private static final List<AbstractSystem<?>> systems = new ArrayList<>();
    private static final List<Integer> processes = new ArrayList<>();

    public static void registerWorld(World world){
        worlds.add(world);
        world.create(worlds.size() - 1);
    }

    public static void removeWorld(World world){
        if(!worlds.contains(world))
            throw new NoSuchElementException("There is no world: " + world.getId());
        worlds.remove(world);
        world.delete();
    }

    public static boolean getStatusOfWorld(World world){
        if(!worlds.contains(world))
            throw new NoSuchElementException("There is no world: " + world.getId());
        return world.isActive();
    }

    public static void setStatusOfWorld(World world, boolean status){
        if(!worlds.contains(world))
            throw new NoSuchElementException("There is no world: " + world.getId());
        world.setActive(status);
    }

    public static void registerSystem(AbstractSystem<?> system){
        systems.add(system);
    }

    public static void removeSystem(AbstractSystem<?> system){
        systems.remove(system);
    }

    public static void startSystems(){
        for(AbstractSystem<?> current : systems){
            int currentPID = ThreadManger.addThread(new SystemThread(current));
            try {
                ThreadManger.startTread(currentPID);
            } catch (NoSuchProcessException ignored) {

            }
            processes.add(currentPID);
        }
    }

    private static class SystemThread extends Thread{

        private final AbstractSystem<?> system;

        public SystemThread(AbstractSystem<?> system) {
            super(system.getMaxFps());
            this.system = system;
        }

        public void onTick(float v) {
            worlds.stream().
                    filter(World::isActive)
                    .forEach(current -> current.processEntities(v, system));
        }

        public void onStart() {

        }

        public void onEnd() {

        }
    }
}
