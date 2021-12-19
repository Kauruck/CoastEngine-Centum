package com.kauruck.coastEngine.centum.componetns;

import com.kauruck.coastEngine.centum.component.AbstractComponent;
import com.kauruck.coastEngine.core.math.Vector3;

public class Transform extends AbstractComponent {

    private Vector3 position;
    private Vector3 rotation;
    private float scale;


    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public void setRotation(Vector3 rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
