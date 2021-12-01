package com.kauruck.coastEngine.centum.component;

import com.kauruck.coastEngine.centum.entity.Entity;

public abstract class AbstractComponent {
    private Entity entity;

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
