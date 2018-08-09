package main.glutil;

import main.jglm.Quat; 
import main.jglm.Vec3; 
 
/**
 * 
 * @author gbarbieri 
 */ 
public class ObjectData { 
 
    public Vec3 position; 
    private Quat orientation; 
 
    public ObjectData(Vec3 position, Quat orientation) { 
 
        this.position = position; 
        this.orientation = orientation; 
    } 
 
    public Vec3 getPosition() { 
        return position; 
    } 
 
    public Quat getOrientation() { 
        return orientation; 
    } 
 
    public void setOrientation(Quat orientation) { 
        this.orientation = orientation; 
    } 
}

