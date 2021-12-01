package com.kauruck.coastEngine.centum;

import com.kauruck.coastEngine.centum.system.AbstractSystem;
import com.kauruck.coastEngine.centum.world.World;
import com.kauruck.coastEngine.core.exception.NoSuchProcessException;
import com.kauruck.coastEngine.core.threding.Thread;
import com.kauruck.coastEngine.core.threding.ThreadManger;
import org.jetbrains.annotations.Nullable;
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
    private static final List<Thread> threadsToCreate = new ArrayList<>();

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

    public static void registerSystem(AbstractSystem<?> system, @Nullable OnStartExecutor onStart, @Nullable OnEndExecutor onEnd){
        systems.add(system);
        threadsToCreate.add(new SystemThread(system, onStart, onEnd));
    }

    public static void registerSystem(AbstractSystem<?> system, OnStartExecutor onStart) {
        registerSystem(system, onStart, null);
        
    }

    public static void registerSystem(AbstractSystem<?> system, OnEndExecutor onEnd) {
        registerSystem(system, null, onEnd);
    }

    public static void registerSystem(AbstractSystem<?> system) {
        registerSystem(system, null, null);
    }


    public static void removeSystem(AbstractSystem<?> system){
        systems.remove(system);
        for(Thread thread : threadsToCreate){
            if(thread instanceof SystemThread){
                if(((SystemThread)thread).system ==  system){
                    threadsToCreate.remove(thread);
                }
            }
        }
    }


    public static void startSystems(){
        for(Thread thread : threadsToCreate){
            int currentPID = ThreadManger.addThread(thread);
            try {
                ThreadManger.startTread(currentPID);
            } catch (NoSuchProcessException ignored) {

            }
            processes.add(currentPID);
        }
        threadsToCreate.clear();
    }


    @FunctionalInterface
    public interface OnStartExecutor {
        void execute();
    }

    @FunctionalInterface
    public interface OnEndExecutor {
        void execute();
    }

    private static class SystemThread extends Thread{

        private final AbstractSystem<?> system;
        private final OnStartExecutor onStart;
        private final OnEndExecutor onEnd;

        public SystemThread(AbstractSystem<?> system, @Nullable  OnStartExecutor onStart, @Nullable  OnEndExecutor onEnd) {
            super(system.getMaxFps());
            this.system = system;
            this.onStart = onStart;
            this.onEnd = onEnd;
        }

        public void onTick(float v) {
            worlds.stream().
                    filter(World::isActive)
                    .forEach(current -> current.processEntities(v, system));
        }

        public void onStart() {
            if(onStart != null)
                onStart.execute();
        }

        public void onEnd() {
            if(onEnd != null)
                onEnd.execute();
        }
    }
}
