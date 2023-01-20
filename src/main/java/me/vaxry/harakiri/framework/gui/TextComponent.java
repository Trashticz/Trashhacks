package me.vaxry.harakiri.framework.gui;

import me.vaxry.harakiri.Harakiri;
import me.vaxry.harakiri.framework.Texture;
import me.vaxry.harakiri.framework.util.RenderUtil;
import me.vaxry.harakiri.framework.util.Timer;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.util.logging.Level;

public class TextComponent extends HudComponent {

    public String displayValue, selectedText;
    public boolean focused;
    public boolean digitOnly;
    public ComponentListener returnListener;
    public TextComponentListener textListener;

    protected Texture checkTexture;

    protected Timer backspaceTimer = new Timer(), backspaceWaitTimer = new Timer();
    protected boolean doBackspacing = false;

    public TextComponent(String name, String displayValue, boolean digitOnly) {
        super(name);

        this.displayValue = displayValue;
        this.selectedText = "";
        this.focused = false;
        this.digitOnly = digitOnly;

        this.checkTexture = new Texture("check.png");
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);

        if (isMouseInside(mouseX, mouseY))
            RenderUtil.drawGradientRect(this.getX(), this.getY(), this.getX() + this.getW(), this.getY() + this.getH(), 0x30909090, 0x30909090);

        RenderUtil.drawRect(this.getX(), this.getY(), this.getX() + this.getW(), this.getY() + this.getH(), 0x45303030);

        final String displayValueText = this.getName() + ": " + this.displayValue;
        Harakiri.get().getTTFFontUtil().drawString(displayValueText, (int) this.getX() + 1, (int) this.getY() + 1, this.focused ? 0xFFFFFFFF : 0xFFAAAAAA);

        if (this.focused) {
            if (!this.selectedText.equals("")) {
                RenderUtil.drawRect(this.getX() + Harakiri.get().getTTFFontUtil().getStringWidth(this.getName() + ": "), this.getY(), this.getX() + Harakiri.get().getTTFFontUtil().getStringWidth(displayValueText), this.getY() + this.getH(), 0x45FFFFFF);
            }

            float blockX = this.getX() + Harakiri.get().getTTFFontUtil().getStringWidth(this.getName() + ": " + this.displayValue) + 1;
            float blockY = this.getY() + 1;
            int blockWidth = 2;
            int blockHeight = Harakiri.get().getTTFFontUtil().FONT_HEIGHT - 2;
            RenderUtil.drawRect(blockX, blockY, blockX + blockWidth, blockY + blockHeight, 0xFFFFFFFF);

            // check
            RenderUtil.drawRect(this.getX() + this.getW() - 10, this.getY(), this.getX() + this.getW(), this.getY() + this.getH(), 0xFF101010);
            this.checkTexture.bind();
            this.checkTexture.render(this.getX() + this.getW() - 9, this.getY() + 0.5f, 8, 8);

            // handle holding backspace
            this.handleBackspacing();
        }
    }

    @Override
    public void mouseRelease(int mouseX, int mouseY, int button) {
        super.mouseRelease(mouseX, mouseY, button);

        if (this.isMouseInside(mouseX, mouseY) && button == 0) {
            this.focus();

            // check for clicking check
            if (!(this instanceof ColorComponent)) {
                this.onCheckButtonPress(mouseX, mouseY);
            }
        } else {
            this.focused = false;
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);

        if (this.focused) {
            // invoke text listener
            if (textListener != null) {
                textListener.onKeyTyped(keyCode);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                switch (keyCode) {
                    case Keyboard.KEY_A:
                        this.selectedText = this.displayValue;
                        return;
                    case Keyboard.KEY_V:
                        if (!this.digitOnly) {
                            this.displayValue += this.getClipBoard();
                        } else if (this.getClipBoard().matches("[0-9]+") /* is a number */) {
                            this.displayValue += this.getClipBoard();
                        }
                        return;
                    case Keyboard.KEY_X:
                    case Keyboard.KEY_C:
                        return;
                }
            }

            switch (keyCode) {
                case Keyboard.KEY_ESCAPE:
                    this.focused = false;
                    return;
                case Keyboard.KEY_RETURN:
                    this.enterPressed();
                    return;
                //case Keyboard.KEY_SPACE:
                //    if (!this.digitOnly) {
                //        this.displayValue += ' ';
                //    }
                //    break;
                case Keyboard.KEY_BACK:
                case Keyboard.KEY_DELETE:
                    this.backspaceWaitTimer.reset();
                    this.doBackspacing = true;
                    if (this.displayValue.length() > 0) {
                        if (!this.onRemoveSelectedText()) {
                            this.displayValue = this.displayValue.substring(0, this.displayValue.length() - 1);
                        }
                    }
                    return;
                case Keyboard.KEY_CLEAR:
                    if (this.displayValue.length() > 0) {
                        this.displayValue = "";
                    }
                    return;
                case Keyboard.KEY_LEFT:
                case Keyboard.KEY_RIGHT:
                case Keyboard.KEY_UP:
                case Keyboard.KEY_DOWN:
                case Keyboard.KEY_LSHIFT:
                case Keyboard.KEY_RSHIFT:
                case Keyboard.KEY_LCONTROL:
                case Keyboard.KEY_RCONTROL:
                case Keyboard.KEY_TAB:
                case Keyboard.KEY_CAPITAL:
                case Keyboard.KEY_FUNCTION:
                case Keyboard.KEY_LMENU:
                case Keyboard.KEY_RMENU:
                case Keyboard.KEY_LMETA:
                    return;
                case Keyboard.KEY_PERIOD:
                    if (this.digitOnly) {
                        this.displayValue += typedChar;
                    }
                    break;
                default:
                    break;
            }

            if (digitOnly && !Character.isDigit(typedChar))
                return;

            this.onRemoveSelectedText();

            //if (!digitOnly && !Character.isLetterOrDigit(typedChar))
            //    return;

            this.displayValue += typedChar;
        }
    }

    protected void enterPressed() {
        // invoke return listener
        if (returnListener != null)
            returnListener.onComponentEvent();

        this.focused = false;
    }

    protected boolean onCheckButtonPress(int mouseX, int mouseY) {
        if (mouseX >= this.getX() + this.getW() - 10 && mouseX <= this.getX() + this.getW() && mouseY >= this.getY() && mouseY <= this.getY() + this.getH()) {
            this.enterPressed();
            return true;
        }
        return false;
    }

    protected boolean onRemoveSelectedText() {
        if (!this.selectedText.equals("")) {
            this.displayValue = "";
            this.selectedText = "";
            return true;
        }
        return false;
    }

    protected void handleBackspacing() {
        if (Keyboard.isKeyDown(Keyboard.KEY_BACK) || Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
            if (this.doBackspacing && this.backspaceWaitTimer.passed(600)) {
                if (this.backspaceTimer.passed(75)) {
                    if (this.displayValue.length() > 0) {
                        this.displayValue = this.displayValue.substring(0, this.displayValue.length() - 1);
                    }
                    this.backspaceTimer.reset();
                }
            }
        } else {
            this.doBackspacing = false;
        }
    }

    public String getClipBoard() {
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            Harakiri.get().getLogger().log(Level.WARNING, "Error getting clipboard while using " + this.getName());
        }
        return "";
    }

    public void focus() {
        this.focused = true;
    }

    public interface TextComponentListener {
        void onKeyTyped(int keyCode);
    }
}
