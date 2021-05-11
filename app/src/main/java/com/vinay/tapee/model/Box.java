package com.vinay.tapee.model;

import android.graphics.Color;
import android.view.View;

import java.util.Objects;

public class Box {
    View boxView;
    String color;
    int tapScore = 1;

    public Box(View boxView, String color) {
        this.boxView = boxView;
        this.color = color;
    }

    public View getBoxView() {
        return boxView;
    }

    public void resetColor(){
        tapScore = 0;
        boxView.setBackgroundColor(Color.parseColor(color));
    }

    public void setSelected(){
        tapScore = 1;
        boxView.setBackgroundColor(Color.parseColor("#808080"));
    }

    public int redeemTapScore() {
        if(tapScore==1){
            tapScore--;
            return 1;
        } else
            return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Box box = (Box) o;
        return Objects.equals(boxView, box.boxView) &&
                Objects.equals(color, box.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boxView, color);
    }
}
