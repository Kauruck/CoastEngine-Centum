package com.kauruck.coastEngine.centum.system;

import com.kauruck.coastEngine.centum.component.AbstractComponent;

public abstract class AbstractSystem<Component extends AbstractComponent> {

    private final float maxFps;
    private final Class<Component> componentClass;

    public AbstractSystem(float maxFps, Class<Component> componentClass) {
        this.maxFps = maxFps;
        this.componentClass = componentClass;
    }

    public float getMaxFps() {
        return maxFps;
    }

    public Class<Component> getComponentClass() {
        return componentClass;
    }

    public abstract void process(AbstractComponent component, float deltaTime);
}
