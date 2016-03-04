package com.kermitlin.yowoo2048;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class AnimLayer extends FrameLayout {

    //資源回收桶
    private List<Card> cards = new ArrayList<Card>();

    public AnimLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayer();
    }

    public AnimLayer(Context context) {
        super(context);
        initLayer();
    }

    private void initLayer() {
    }

    public void createMoveAnim(final Card from, final Card to, int fromX, int toX, int fromY, int toY) {
        final Card card = getCard(from.getNum());

        LayoutParams lp = new LayoutParams(Config.CARD_WIDTH, Config.CARD_WIDTH);
        //用margin距離定位到要移動的格子
        lp.leftMargin = fromX * Config.CARD_WIDTH;
        lp.topMargin = fromY * Config.CARD_WIDTH;
        card.setLayoutParams(lp);

        //先隱藏目的位置的數字，避免還沒移動過去就有數字的狀況產生
        if (to.getNum() <= 0) {
            to.getLabel().setVisibility(View.INVISIBLE);
        }

        TranslateAnimation ta = new TranslateAnimation(0, Config.CARD_WIDTH * (toX - fromX), 0, Config.CARD_WIDTH * (toY - fromY));
        ta.setDuration(1000);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                to.getLabel().setVisibility(View.VISIBLE);
                recycleCard(card);
            }
        });
        card.startAnimation(ta);
    }

    private Card getCard(int num) {
        Card card;

        if (cards.size() > 0) {
            card = cards.remove(0);
        } else {
            card = new Card(getContext());
            addView(card);
        }

        //直接新增卡片使用（記憶體會吃光！）
//        card = new Card(getContext());
//        addView(card);

        card.setVisibility(View.VISIBLE);
        card.setNum(num);

        return card;
    }

    private void recycleCard(Card card) {
        //回收是以一個手勢為單位，不是以單一動畫為單位
        card.setVisibility(View.INVISIBLE);
        card.setAnimation(null);
        cards.add(card);
    }

    public void createScaleTo1(Card target) {
        //由小變大
        ScaleAnimation sa = new ScaleAnimation(0.1f, 1, 0.1f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        //慢一點有開花的感覺>///<
        sa.setDuration(1000);
        target.setAnimation(null);
        target.getLabel().startAnimation(sa);
    }

}
