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
    private static final List<AbstractSystem<?>> toInit = new ArrayList<>();

    public static boolean isStarted(){
        return toInit.size() == 0;
    }

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

    public static void registerSystem(AbstractSystem<?> system, @Nullable OnStartExecutor onStart, @Nullable OnEndExecutor onEnd, @Nullable Centum.OnTickStartExecutor onTickStart, @Nullable Centum.OnTickEndExecutor onTickEnd){
        systems.add(system);
        threadsToCreate.add(new SystemThread(system, onStart, onEnd, onTickStart, onTickEnd));
    }

    public static void registerSystem(AbstractSystem<?> system, @Nullable OnStartExecutor onStart, @Nullable OnEndExecutor onEnd, @Nullable Centum.OnTickStartExecutor onTickStart){
        registerSystem(system, onStart, onEnd, onTickStart, null);
    }

    public static void registerSystem(AbstractSystem<?> system, @Nullable OnStartExecutor onStart, @Nullable OnEndExecutor onEnd, @Nullable Centum.OnTickEndExecutor onTickEnd){
        registerSystem(system, onStart, onEnd, null, onTickEnd);
    }

    public static void registerSystem(AbstractSystem<?> system, OnStartExecutor onStart) {
        registerSystem(system, onStart, null, null, null);
        
    }

    public static void registerSystem(AbstractSystem<?> system, OnEndExecutor onEnd) {
        registerSystem(system, null, onEnd ,null, null);
    }

    public static void registerSystem(AbstractSystem<?> system) {
        registerSystem(system, null, null, null, null);
    }



    public static void registerSystem(AbstractSystem<?> system, OnStartExecutor onStart, OnTickStartExecutor onTickStart) {
        registerSystem(system, onStart, null, onTickStart, null);

    }

    public static void registerSystem(AbstractSystem<?> system, OnEndExecutor onEnd, OnTickStartExecutor onTickStart) {
        registerSystem(system, null, onEnd ,onTickStart, null);
    }

    public static void registerSystem(AbstractSystem<?> system, OnTickStartExecutor onTickStart) {
        registerSystem(system, null, null, onTickStart, null);
    }



    public static void registerSystem(AbstractSystem<?> system, OnStartExecutor onStart, OnTickEndExecutor onTickEnd) {
        registerSystem(system, onStart, null, null, onTickEnd);

    }

    public static void registerSystem(AbstractSystem<?> system, OnEndExecutor onEnd, OnTickEndExecutor onTickEnd) {
        registerSystem(system, null, onEnd ,null, onTickEnd);
    }

    public static void registerSystem(AbstractSystem<?> system, OnTickEndExecutor onTickEnd) {
        registerSystem(system, null, null, null, onTickEnd);
    }



    public static void registerSystem(AbstractSystem<?> system, OnStartExecutor onStart, OnTickStartExecutor onTickStart, OnTickEndExecutor onTickEnd) {
        registerSystem(system, onStart, null, onTickStart, onTickEnd);

    }

    public static void registerSystem(AbstractSystem<?> system, OnEndExecutor onEnd, OnTickStartExecutor onTickStart, OnTickEndExecutor onTickEnd) {
        registerSystem(system, null, onEnd ,onTickStart, onTickEnd);
    }

    public static void registerSystem(AbstractSystem<?> system, OnTickStartExecutor onTickStart, OnTickEndExecutor onTickEnd) {
        registerSystem(system, null, null, onTickStart, onTickEnd);
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
            toInit.add(((SystemThread)thread).system);
            try {
                ThreadManger.startTread(currentPID);
            } catch (NoSuchProcessException ignored) {

            }
            processes.add(currentPID);
        }
        threadsToCreate.clear();
        while (!isStarted()){
            try {
                java.lang.Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void startSystemInDetach(){
        for(Thread thread : threadsToCreate){
            int currentPID = ThreadManger.addThread(thread);
            toInit.add(((SystemThread)thread).system);
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

    @FunctionalInterface
    public interface OnTickStartExecutor {
        void execute();
    }

    @FunctionalInterface
    public interface OnTickEndExecutor {
        void execute();
    }

    @FunctionalInterface
    public interface OnStartDown {
        void execute();
    }

    private static class SystemThread extends Thread{

        private final AbstractSystem<?> system;
        private final OnStartExecutor onStart;
        private final OnEndExecutor onEnd;
        private final OnTickStartExecutor onTickStart;
        private final OnTickEndExecutor onTickEnd;

        public SystemThread(AbstractSystem<?> system, @Nullable OnStartExecutor onStart, @Nullable OnEndExecutor onEnd, OnTickStartExecutor onTickStart, OnTickEndExecutor onTickEnd) {
            super(system.getMaxFps());
            this.system = system;
            this.onStart = onStart;
            this.onEnd = onEnd;
            this.onTickStart = onTickStart;
            this.onTickEnd = onTickEnd;
        }

        public void onTick(float v) {
            if(onTickStart != null)
                onTickStart.execute();
            worlds.stream().
                    filter(World::isActive)
                    .forEach(current -> current.processEntities(v, system));
            if(onTickEnd != null)
                onTickEnd.execute();
        }

        public void onStart() {
            if(onStart != null)
                onStart.execute();
            toInit.remove(system);
        }

        public void onEnd() {
            if(onEnd != null)
                onEnd.execute();
        }
    }
}
