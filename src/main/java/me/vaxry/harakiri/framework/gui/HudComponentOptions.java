package me.vaxry.harakiri.framework.gui;

import me.vaxry.harakiri.Harakiri;
import me.vaxry.harakiri.framework.util.RenderUtil;

public class HudComponentOptions extends HudComponent {

    private HudComponent parent;

    public HudComponentOptions(HudComponent parent) {
        this.parent = parent;
        this.setVisible(false);
    }

    @Override
    public void mouseClick(int mouseX, int mouseY, int button) {
        if (this.isMouseInside(mouseX, mouseY) && button == 0) {
            Harakiri.get().getHudManager().moveToTop(this);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);

        if (parent == null)
            return;

        this.setX(parent.getX() + parent.getW());
        this.setY(parent.getY());

        final int parentNameWidth = (int)Harakiri.get().getTTFFontUtil().getStringWidth(parent.getName());
        final int visibleStringWidth = (int)Harakiri.get().getTTFFontUtil().getStringWidth("Visible");
        int yOffset = 0;

        RenderUtil.drawRect(this.getX(), this.getY(), this.getX() + parentNameWidth, this.getY() + Harakiri.get().getTTFFontUtil().FONT_HEIGHT, 0x75505050);
        Harakiri.get().getTTFFontUtil().drawStringWithShadow(parent.getName(), this.getX(), this.getY(), 0xFFFFFFFF);

        yOffset += Harakiri.get().getTTFFontUtil().FONT_HEIGHT;

        RenderUtil.drawRect(this.getX(), this.getY() + yOffset, this.getX() + visibleStringWidth, this.getY() + yOffset + Harakiri.get().getTTFFontUtil().FONT_HEIGHT, parent.isVisible() ? 0x7550FF50 : 0x75FF5050);
        Harakiri.get().getTTFFontUtil().drawStringWithShadow("Visible", this.getX(), this.getY() + yOffset, 0xFFFFFFFF);

        yOffset += Harakiri.get().getTTFFontUtil().FONT_HEIGHT;
        this.setW(Math.max(parentNameWidth, visibleStringWidth));
        this.setH(yOffset);
    }

    @Override
    public void mouseRelease(int mouseX, int mouseY, int button) {
        if (button == 0) {
            if ((mouseX > this.getX()) && (mouseX < this.getX() + Harakiri.get().getTTFFontUtil().getStringWidth("Visible"))) {
                if (mouseY > (this.getY() + Harakiri.get().getTTFFontUtil().FONT_HEIGHT)) {
                    if (mouseY < (this.getY() + this.getH())) {
                        parent.setVisible(!parent.isVisible());
                    }
                }
            }
        }

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int button) {
        super.mouseClickMove(mouseX, mouseY, button);
    }

    public HudComponent getParent() {
        return parent;
    }

    public void setParent(HudComponent parent) {
        this.parent = parent;
    }
}
