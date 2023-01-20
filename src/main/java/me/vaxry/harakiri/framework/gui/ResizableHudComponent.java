package me.vaxry.harakiri.framework.gui;

import me.vaxry.harakiri.Harakiri;
import me.vaxry.harakiri.framework.util.RenderUtil;
import me.vaxry.harakiri.impl.gui.hud.GuiHudEditor;
import net.minecraft.client.Minecraft;

public class ResizableHudComponent extends DraggableHudComponent {

    private boolean resizeDragging;
    private float resizeDeltaX;
    private float resizeDeltaY;

    private float initialWidth;
    private float initialHeight;
    private float maxWidth;
    private float maxHeight;

    protected final float CLICK_ZONE = 10;

    public ResizableHudComponent(String name, float initialWidth, float initialHeight, float maxWidth, float maxHeight) {
        super(name);
        this.initialWidth = initialWidth;
        this.initialHeight = initialHeight;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int button) {
        super.mouseClick(mouseX, mouseY, button);
        final boolean inside = mouseX >= this.getX() + this.getW() - CLICK_ZONE && mouseX <= this.getX() + this.getW() + CLICK_ZONE && mouseY >= this.getY() + this.getH() - CLICK_ZONE && mouseY <= this.getY() + this.getH() + CLICK_ZONE;

        if (inside) {
            if (button == 0) {
                this.setResizeDragging(true);
                this.setDragging(false);
                this.setResizeDeltaX(mouseX - this.getW());
                this.setResizeDeltaY(mouseY - this.getH());
                Harakiri.get().getHudManager().moveToTop(this);
            }
        }
    }


    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);

        if (this.isResizeDragging()) {
            this.setW(mouseX - this.getResizeDeltaX());
            this.setH(mouseY - this.getResizeDeltaY());
            this.clampMaxs();
        }

        if (Minecraft.getMinecraft().currentScreen instanceof GuiHudEditor) {
            RenderUtil.drawResizeAnchor(this.getX() + this.getW() - CLICK_ZONE, this.getY() + this.getH() - CLICK_ZONE, CLICK_ZONE, CLICK_ZONE, 0x80303030); //0x45
        }

        final boolean insideClickZone = mouseX >= this.getX() + this.getW() - CLICK_ZONE && mouseX <= this.getX() + this.getW() + CLICK_ZONE / 2.f && mouseY >= this.getY() + this.getH() - CLICK_ZONE && mouseY <= this.getY() + this.getH() + CLICK_ZONE / 2.f;
        if (insideClickZone) {
            RenderUtil.drawResizeAnchor(this.getX() + this.getW() - CLICK_ZONE, this.getY() + this.getH() - CLICK_ZONE, CLICK_ZONE, CLICK_ZONE, 0x22FFFFFF); //0x45
        }

        this.clampMaxs();
    }

    @Override
    public void mouseRelease(int mouseX, int mouseY, int button) {
        super.mouseRelease(mouseX, mouseY, button);

        if (button == 0) {
            if (this.isResizeDragging()) {
                this.setResizeDragging(false);
            }
        }
    }

    public void clampMaxs() {
        if (this.getW() <= this.getInitialWidth()) {
            this.setW(this.getInitialWidth());
        }

        if (this.getH() <= this.getInitialHeight()) {
            this.setH(this.getInitialHeight());
        }

        if (this.getW() >= this.getMaxWidth()) {
            this.setW(this.getMaxWidth());
        }

        if (this.getH() >= this.getMaxHeight()) {
            this.setH(this.getMaxHeight());
        }
    }

    public boolean isResizeDragging() {
        return resizeDragging;
    }

    public void setResizeDragging(boolean resizeDragging) {
        this.resizeDragging = resizeDragging;
    }

    public float getResizeDeltaX() {
        return resizeDeltaX;
    }

    public void setResizeDeltaX(float resizeDeltaX) {
        this.resizeDeltaX = resizeDeltaX;
    }

    public float getResizeDeltaY() {
        return resizeDeltaY;
    }

    public void setResizeDeltaY(float resizeDeltaY) {
        this.resizeDeltaY = resizeDeltaY;
    }

    public float getInitialWidth() {
        return initialWidth;
    }

    public void setInitialWidth(float initialWidth) {
        this.initialWidth = initialWidth;
    }

    public float getInitialHeight() {
        return initialHeight;
    }

    public void setInitialHeight(float initialHeight) {
        this.initialHeight = initialHeight;
    }

    public float getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(float maxWidth) {
        this.maxWidth = maxWidth;
    }

    public float getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(float maxHeight) {
        this.maxHeight = maxHeight;
    }
}
