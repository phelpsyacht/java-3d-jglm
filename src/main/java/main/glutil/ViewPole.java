package main.glutil;

import java.awt.event.MouseEvent; 
import java.awt.event.MouseWheelEvent; 
import javax.swing.SwingUtilities; 
import main.jglm.Jglm; 
import main.jglm.Mat4; 
import main.jglm.Quat; 
import main.jglm.Vec2; 
import main.jglm.Vec3; 
 
/**
 * 
 * @author gbarbieri 
 */ 
public class ViewPole { 
 
    private ViewData currView; 
    private ViewData initialView; 
    private ViewScale viewScale; 
    private boolean isDragging; 
    private RotatingMode rotatingMode; 
    private Vec2 startDragMouseLoc; 
    private float degStartDragSpin; 
    private Quat startDragOrient; 
 
    public ViewPole(ViewData viewData, ViewScale viewScale) { 
 
        this.currView = viewData; 
        this.initialView = viewData; 
        this.viewScale = viewScale; 
 
        isDragging = false; 
    } 
 
    public Mat4 calcMatrix() { 
 
        Mat4 mat = new Mat4(1.0f); 
 
        mat = Jglm.translate(mat, new Vec3(0.0f, 0.0f, -currView.getRadius())); 
//        System.out.println("currView.getRadius(): "+(-currView.getRadius())); 
//        mat.print("mat"); 
 
        Quat fullRotation = Jglm.angleAxis(currView.getDegSpinRotation(), new Vec3(0.0f, 0.0f, 1.0f)); 
        fullRotation = fullRotation.mult(currView.getOrient()); 
 
        mat = mat.mult(fullRotation.toMatrix()); 
 
        mat = Jglm.translate(mat, currView.getTargetPos().negated()); 
 
        return mat; 
    } 
 
    /**
     * @deprecated  
     * @param mouseEvent  
     */ 
    public void mousePressed(MouseEvent mouseEvent) { 
 
        if (!isDragging) { 
 
            if (SwingUtilities.isLeftMouseButton(mouseEvent)) { 
 
                Vec2 position = new Vec2(mouseEvent.getX(), mouseEvent.getY()); 
 
                if (mouseEvent.isControlDown()) { 
 
                    beginDragRotate(position, RotatingMode.BIAXIAL); 
 
                } else if (mouseEvent.isAltDown()) { 
 
                    beginDragRotate(position, RotatingMode.SPIN); 
 
                } else { 
 
                    beginDragRotate(position, RotatingMode.DUAL_AXIS); 
                } 
            } 
        } 
    } 
     
    public void mousePressed(com.jogamp.newt.event.MouseEvent mouseEvent) { 
 
        if (!isDragging) { 
 
//            System.out.println("mouseEvent.getButton() "+mouseEvent.getButton()); 
//            System.out.println("com.jogamp.newt.event.MouseEvent.BUTTON1 "+com.jogamp.newt.event.MouseEvent.BUTTON1); 
            if (mouseEvent.getButton() == com.jogamp.newt.event.MouseEvent.BUTTON1) { 
//                System.out.println("in"); 
                Vec2 position = new Vec2(mouseEvent.getX(), mouseEvent.getY()); 
 
                if (mouseEvent.isControlDown()) { 
 
                    beginDragRotate(position, RotatingMode.BIAXIAL); 
 
                } else if (mouseEvent.isAltDown()) { 
 
                    beginDragRotate(position, RotatingMode.SPIN); 
 
                } else { 
 
                    beginDragRotate(position, RotatingMode.DUAL_AXIS); 
                } 
            } 
        } 
    } 
 
    private void beginDragRotate(Vec2 position, RotatingMode rotatingMode) { 
 
        this.rotatingMode = rotatingMode; 
 
        startDragMouseLoc = position; 
 
        degStartDragSpin = currView.getDegSpinRotation(); 
 
        startDragOrient = currView.getOrient(); 
 
        isDragging = true; 
    } 
 
    /**
     * @deprecated  
     * @param mouseEvent  
     */ 
    public void mouseMove(MouseEvent mouseEvent) { 
 
        if (isDragging) { 
 
            onDragRotate(mouseEvent); 
        } 
    } 
     
    public void mouseMove(com.jogamp.newt.event.MouseEvent mouseEvent) { 
 
        if (isDragging) { 
 
            onDragRotate(mouseEvent); 
        } 
    } 
 
    /**
     * @deprecated  
     * @param mouseEvent  
     */ 
    private void onDragRotate(MouseEvent mouseEvent) { 
 
        Vec2 current = new Vec2(mouseEvent.getX(), mouseEvent.getY()); 
 
        current = current.minus(startDragMouseLoc); 
 
        switch (rotatingMode) { 
 
            case DUAL_AXIS: 
                processXYchange(current); 
                break; 
 
            case SPIN: 
                processSpinAxis(current); 
                break; 
        } 
    } 
     
    private void onDragRotate(com.jogamp.newt.event.MouseEvent mouseEvent) { 
 
        Vec2 current = new Vec2(mouseEvent.getX(), mouseEvent.getY()); 
 
        current = current.minus(startDragMouseLoc); 
 
        switch (rotatingMode) { 
 
            case DUAL_AXIS: 
                processXYchange(current); 
                break; 
 
            case SPIN: 
                processSpinAxis(current); 
                break; 
        } 
    } 
 
    private void processXYchange(Vec2 diff) { 
 
        diff = diff.times(viewScale.getRotationScale()); 
 
        Quat yWorldSpace = Jglm.angleAxis(diff.x, new Vec3(0.0f, 1.0f, 0.0f)); 
 
        currView.setOrient(startDragOrient.mult(yWorldSpace)); 
 
        Quat xLocalSpace = Jglm.angleAxis(diff.y, new Vec3(1.0f, 0.0f, 0.0f)); 
 
        currView.setOrient(xLocalSpace.mult(currView.getOrient())); 
    } 
 
    private void processSpinAxis(Vec2 diff) { 
 
        float degSpinDiff = diff.x * viewScale.getRotationScale(); 
 
        currView.setDegSpinRotation(degSpinDiff + degStartDragSpin); 
    } 
 
    /**
     * @deprecated  
     * @param mouseEvent  
     */ 
    public void mouseReleased(MouseEvent mouseEvent) { 
 
        if (isDragging) { 
 
            if (SwingUtilities.isLeftMouseButton(mouseEvent)) { 
 
                if (rotatingMode == RotatingMode.DUAL_AXIS || rotatingMode == RotatingMode.BIAXIAL || rotatingMode == RotatingMode.SPIN) { 
 
                    endDragRotate(mouseEvent); 
                } 
            } 
        } 
    } 
 
    public void mouseReleased(com.jogamp.newt.event.MouseEvent mouseEvent) { 
 
        if (isDragging) { 
 
            if (mouseEvent.getButton() == com.jogamp.newt.event.MouseEvent.BUTTON1) { 
 
                if (rotatingMode == RotatingMode.DUAL_AXIS ||  
                        rotatingMode == RotatingMode.BIAXIAL ||  
                        rotatingMode == RotatingMode.SPIN) { 
 
                    endDragRotate(mouseEvent); 
                } 
            } 
        } 
    } 
 
    private void endDragRotate(MouseEvent mouseEvent) { 
 
        onDragRotate(mouseEvent); 
 
        isDragging = false; 
    } 
     
    private void endDragRotate(com.jogamp.newt.event.MouseEvent mouseEvent) { 
 
        onDragRotate(mouseEvent); 
 
        isDragging = false; 
    } 
 
    /**
     * @deprecated  
     * @param mouseWheelEvent  
     */ 
    public void mouseWheel(MouseWheelEvent mouseWheelEvent) { 
 
        if (mouseWheelEvent.isShiftDown()) { 
 
            currView.setRadius(currView.getRadius() + mouseWheelEvent.getWheelRotation() * viewScale.getLargeRadiusDelta()); 
 
        } else { 
 
            currView.setRadius(currView.getRadius() + mouseWheelEvent.getWheelRotation() * viewScale.getSmallRadiusDelta()); 
        } 
 
        currView.setRadius(Jglm.clamp(currView.getRadius(), viewScale.getMinRadius(), viewScale.getMaxRadius())); 
    } 
     
    public void mouseWheel(com.jogamp.newt.event.MouseEvent mouseEvent) { 
 
        if (mouseEvent.isShiftDown()) { 
 
            currView.setRadius(currView.getRadius() + mouseEvent.getRotation()[0] * viewScale.getLargeRadiusDelta()); 
 
        } else { 
 
            currView.setRadius(currView.getRadius() + mouseEvent.getRotation()[1] * viewScale.getSmallRadiusDelta()); 
        } 
 
        currView.setRadius(Jglm.clamp(currView.getRadius(), viewScale.getMinRadius(), viewScale.getMaxRadius())); 
    } 
 
    public ViewData getCurrView() { 
        return currView; 
    } 
     
    public enum RotatingMode { 
 
        SPIN, 
        BIAXIAL, 
        DUAL_AXIS 
    } 
}

