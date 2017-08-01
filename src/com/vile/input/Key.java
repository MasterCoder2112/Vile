package com.vile.input;

/**
 * Created by Julian Nieto on 7/31/2017.
 */
public class Key  {

    private int keyCode;
    private boolean pressed = false;

    public Key(int keyCode)
    {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Key key = (Key) o;
        return keyCode == key.keyCode;
    }
}
