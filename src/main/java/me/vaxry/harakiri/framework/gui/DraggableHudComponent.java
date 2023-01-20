package me.vaxry.harakiri.framework.gui;

import me.vaxry.harakiri.Harakiri;
import me.vaxry.harakiri.framework.util.RenderUtil;
import me.vaxry.harakiri.impl.gui.hud.GuiHudEditor;
import me.vaxry.harakiri.framework.gui.anchor.AnchorPoint;
import me.vaxry.harakiri.impl.gui.hud.component.special.HubComponent;
import me.vaxry.harakiri.impl.gui.hud.component.special.ModuleListComponent;
import me.vaxry.harakiri.impl.gui.hud.component.special.ModuleSearchComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;

public class DraggableHudComponent extends HudComponent {

    private boolean snappable;
    private boolean dragging;
    private boolean locked;
    private float deltaX;
    private float deltaY;

    private AnchorPoint anchorPoint;

    private DraggableHudComponent glued;
    private GlueSide glueSide;
    private boolean parent;

    private static final double ANCHOR_THRESHOLD = 80;

    protected final Minecraft mc = Minecraft.getMinecraft();

    private float SCALING = 1f;

    public DraggableHudComponent(String name) {
        this.setName(name);
        this.setVisible(false);
        this.setSnappable(true);
        this.setLocked(false);
        this.setX(Minecraft.getMinecraft().displayWidth / 2.0f);
        this.setY(Minecraft.getMinecraft().displayHeight / 2.0f);
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int button) {
        if (this.isMouseInside(mouseX, mouseY)) {
            if (button == 0) {
                this.setDragging(true);
                this.setDeltaX(mouseX - this.getX());
                this.setDeltaY(mouseY - this.getY());
                Harakiri.get().getHudManager().moveToTop(this);
                this.anchorPoint = null;
                this.glued = null;
                this.glueSide = null;
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        if(this instanceof ModuleListComponent || this instanceof HubComponent || this instanceof ModuleSearchComponent) {
            mouseX /= SCALING;
            mouseY /= SCALING;
        }

            boolean isHudEditor = Minecraft.getMinecraft().currentScreen instanceof GuiHudEditor;

        if (this.isDragging()) {
            this.setX(mouseX - this.getDeltaX());
            this.setY(mouseY - this.getDeltaY());
            this.clamp();
        } else if (this.isMouseInside(mouseX, mouseY)) {
            if(this instanceof ModuleListComponent || this instanceof HubComponent || this instanceof ModuleSearchComponent) {
                GlStateManager.scale(SCALING, SCALING, SCALING);
                RenderUtil.drawRoundedRect(this.getX(), this.getY(), this.getW(), this.getH(), 5, 0x11FFFFFF); //0x45
                GlStateManager.scale(1.f/SCALING, 1.f/SCALING, 1.f/SCALING);
            }
            else
                RenderUtil.drawRect(this.getX(), this.getY(), this.getX() + this.getW(), this.getY() + this.getH(), 0x11FFFFFF); //0x45
        }

        if (isHudEditor) {
            if(this instanceof HubComponent || this instanceof ModuleListComponent || this instanceof ModuleSearchComponent) {
                GlStateManager.scale(SCALING, SCALING, SCALING);
                RenderUtil.drawRoundedRect(this.getX(), this.getY(), this.getW(), this.getH(), 5, 0x75101010);
                GlStateManager.scale(1.f / SCALING, 1.f / SCALING, 1.f / SCALING);
            }
            else
                RenderUtil.drawRect(this.getX(), this.getY(), this.getX() + this.getW(), this.getY() + this.getH(), 0x75101010);
            if (this.isLocked()) {
                // why??? its ugly
                //RenderUtil.drawBorderedRect(this.getX() - 1, this.getY() - 1, this.getX() + this.getW() + 1, this.getY() + this.getH() + 1, 0.5f, 0x00000000, 0x75FFFFFF);
            }
        }

        if (this.glued != null) {
            if (this.glued.getAnchorPoint() == null) {
                if (this.anchorPoint != null) {
                    this.anchorPoint = null;
                }
            } else {
                this.anchorPoint = this.glued.getAnchorPoint();
            }
        }

        if (this.anchorPoint == null && this.glued != null) {
            this.setX(this.glued.getX());
            if (this.glueSide != null) {
                switch (this.glueSide) {
                    case TOP:
                        // math... am i right?
                        if (!isHudEditor && this.glued.getH() <= 0 && this.getH() <= 0) {
                            this.setY((this.glued.getY() - this.getEmptyH()) + this.glued.getEmptyH());
                        } else if (!isHudEditor && this.glued.getH() <= 0 && this.getH() > 0) {
                            this.setY((this.glued.getY() + this.glued.getEmptyH()) - this.getH());
                        } else if (!isHudEditor && this.glued.getH() > 0 && this.getH() <= 0) {
                            this.setY(this.glued.getY() - this.getEmptyH());
                        } else {
                            this.setY(this.glued.getY() - this.getH());
                        }
                        break;
                    case BOTTOM:
                        this.setY(this.glued.getY() + this.glued.getH());
                        break;
                }
            }
        }

        if (!this.isDragging()) {
            if (this.anchorPoint != null && this.glued != null) {
                switch (this.anchorPoint.getPoint()) {
                    case TOP_LEFT:
                        this.setX(this.anchorPoint.getX());
                        break;
                    case BOTTOM_LEFT:
                        this.setX(this.anchorPoint.getX());
                        break;
                    case TOP_RIGHT:
                        this.setX(this.anchorPoint.getX() - this.getW());
                        break;
                    case BOTTOM_RIGHT:
                        this.setX(this.anchorPoint.getX() - this.getW());
                        break;
                    case TOP_CENTER:
                        this.setX(this.anchorPoint.getX() - (this.getW() / 2.0f));
                        break;
                    case BOTTOM_CENTER:
                        this.setX(this.anchorPoint.getX() - (this.getW() / 2.0f));
                        break;
                }
                if (this.glueSide != null) {
                    switch (this.glueSide) {
                        case TOP:
                            this.setY(this.glued.getY() - this.getH());
                            break;
                        case BOTTOM:
                            this.setY(this.glued.getY() + this.glued.getH());
                            break;
                    }
                }
            } else if (this.anchorPoint != null) {
                switch (this.anchorPoint.getPoint()) {
                    case TOP_LEFT:
                        this.setX(this.anchorPoint.getX());
                        this.setY(this.anchorPoint.getY());
                        break;
                    case BOTTOM_LEFT:
                        this.setX(this.anchorPoint.getX());
                        this.setY(this.anchorPoint.getY() - this.getH());
                        break;
                    case TOP_RIGHT:
                        this.setX(this.anchorPoint.getX() - this.getW());
                        this.setY(this.anchorPoint.getY());
                        break;
                    case BOTTOM_RIGHT:
                        this.setX(this.anchorPoint.getX() - this.getW());
                        this.setY(this.anchorPoint.getY() - this.getH());
                        break;
                    case TOP_CENTER:
                        this.setX(this.anchorPoint.getX() - (this.getW() / 2.0f));
                        this.setY(this.anchorPoint.getY());
                        break;
                    case BOTTOM_CENTER:
                        this.setX(this.anchorPoint.getX() - (this.getW() / 2.0f));
                        this.setY(this.anchorPoint.getY() - this.getH());
                        break;
                }
            }
        }

        this.clamp();
    }

    @Override
    public void mouseRelease(int mouseX, int mouseY, int button) {
        super.mouseRelease(mouseX, mouseY, button);

        if (button == 0) {
            if (this.isDragging()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || !this.isSnappable()) {
                    this.setDragging(false);
                    return;
                }

                this.anchorPoint = this.findClosest(mouseX, mouseY);

                for (HudComponent component : Harakiri.get().getHudManager().getComponentList()) {
                    if (component instanceof DraggableHudComponent) {
                        DraggableHudComponent draggable = (DraggableHudComponent) component;
                        if (draggable != this && draggable.isVisible() && draggable.isSnappable()) {
                            if (this.collidesWith(draggable)) {
                                if ((this.getY() + (this.getH() / 2.0f)) < (draggable.getY() + (draggable.getH() / 2.0f))) { // top
                                    this.setY(draggable.getY() - this.getH());
                                    this.glueSide = GlueSide.TOP;
                                    this.glued = draggable;
                                    draggable.setParent(true);
                                    if (draggable.getAnchorPoint() != null) {
                                        this.anchorPoint = draggable.getAnchorPoint();
                                    }
                                } else if ((this.getY() + (this.getH() / 2.0f)) > (draggable.getY() + (draggable.getH() / 2.0f))) { // bottom
                                    this.setY(draggable.getY() + draggable.getH());
                                    this.glueSide = GlueSide.BOTTOM;
                                    this.glued = draggable;
                                    draggable.setParent(true);
                                    if (draggable.getAnchorPoint() != null) {
                                        this.anchorPoint = draggable.getAnchorPoint();
                                    }
                                }
                            } else {
                                AnchorPoint draggableClosest = draggable.getAnchorPoint();
                                AnchorPoint myClosest = this.findClosest(mouseX, mouseY);
                                if (draggableClosest != null && myClosest != null) {
                                    boolean sameAnchor = draggableClosest.getPoint().equals(myClosest.getPoint());
                                    if (sameAnchor) {
                                        this.anchorPoint = null;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            this.setDragging(false);
        } else if (button == 2) {
            if (isMouseInside(mouseX, mouseY)) {
                this.setLocked(!this.isLocked());
            }
        }
    }

    public AnchorPoint findClosest(float x, float y) {
        AnchorPoint ret = null;
        double max = ANCHOR_THRESHOLD;
        for (AnchorPoint point : Harakiri.get().getHudManager().getAnchorPoints()) {
            final double deltaX = x - point.getX();
            final double deltaY = y - point.getY();

            final double dist = MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY);
            if (dist <= max) {
                max = dist;
                ret = point;
            }
        }

        return ret;
    }

    public AnchorPoint findClosest() {
        return findClosest(this.getX(), this.getY());
    }

    public void clamp() {
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        /* off screen clamp bypass check */
        if (this.isLocked()/*this.isVisible() && this.getAnchorPoint() == null*/) {
            return;
            /*if (this.getX() > sr.getScaledWidth() || this.getY() > sr.getScaledHeight()) { // off the screen
                return;
            }*/
        }

        if (this.getX() <= 0) {
            this.setX(2);
        }

        if (this.getY() <= 0) {
            this.setY(2);
        }

        if (this.getX() + this.getW() >= sr.getScaledWidth() - 2) {
            this.setX(sr.getScaledWidth() - 2 - this.getW());
        }

        if (this.getY() + this.getH() >= sr.getScaledHeight() - 2) {
            this.setY(sr.getScaledHeight() - 2 - this.getH());
        }
    }

    public boolean isSnappable() {
        return snappable;
    }

    public void setSnappable(boolean snappable) {
        this.snappable = snappable;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public float getDeltaX() {
        return deltaX;
    }

    public void setDeltaX(float deltaX) {
        this.deltaX = deltaX;
    }

    public float getDeltaY() {
        return deltaY;
    }

    public void setDeltaY(float deltaY) {
        this.deltaY = deltaY;
    }

    public AnchorPoint getAnchorPoint() {
        return anchorPoint;
    }

    public void setAnchorPoint(AnchorPoint anchorPoint) {
        this.anchorPoint = anchorPoint;
    }

    public HudComponent getGlued() {
        return glued;
    }

    public void setGlued(DraggableHudComponent glued) {
        this.glued = glued;
    }

    public GlueSide getGlueSide() {
        return glueSide;
    }

    public void setGlueSide(GlueSide glueSide) {
        this.glueSide = glueSide;
    }

    public enum GlueSide {
        TOP, BOTTOM
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }
}
