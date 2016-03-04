package com.kermitlin.yowoo2048;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {

    private TextView label;
    private View background;
    private int num = 0;

    public Card(Context context) {
        super(context);
        LayoutParams lp = null;

        background = new View(getContext());
        background.setBackgroundResource(R.color.cardBackground);

        //-1:fill_parent,match_parent; -2:wrap_content
        lp = new LayoutParams(-1, -1);
        //左,上,右,下
        lp.setMargins(10, 10, 0, 0);
        addView(background, lp);

        label = new TextView(getContext());
        label.setTextSize(28);
        label.setGravity(Gravity.CENTER);

        lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 0, 0);
        addView(label, lp);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;

        if (num <= 0) {
            label.setText("");
        } else {
            label.setText(String.valueOf(num));
        }

        switch (num) {
            case 0:
                label.setBackgroundResource(R.color.bgColorLabel0);
                break;
            case 2:
                label.setBackgroundResource(R.color.bgcolorLabel2);
                break;
            case 4:
                label.setBackgroundResource(R.color.bgcolorLabel4);
                break;
            case 8:
                label.setBackgroundResource(R.color.bgcolorLabel8);
                break;
            case 16:
                label.setBackgroundResource(R.color.bgcolorLabel16);
                break;
            case 32:
                label.setBackgroundResource(R.color.bgcolorLabel32);
                break;
            case 64:
                label.setBackgroundResource(R.color.bgcolorLabel64);
                break;
            case 128:
                label.setBackgroundResource(R.color.bgcolorLabel128);
                break;
            case 256:
                label.setBackgroundResource(R.color.bgcolorLabel256);
                break;
            case 512:
                label.setBackgroundResource(R.color.bgcolorLabel512);
                break;
            case 1024:
                label.setBackgroundResource(R.color.bgcolorLabel1024);
                break;
            case 2048:
                label.setBackgroundResource(R.color.bgcolorLabel2048);
                break;
            default:
                label.setBackgroundResource(R.color.bgcolorLabelDefault);
                break;
        }
    }

    public boolean equals(Card card) {
        return getNum() == card.getNum();
    }

    public TextView getLabel() {
        return label;
    }
}
