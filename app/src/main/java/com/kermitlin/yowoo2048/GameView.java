package com.kermitlin.yowoo2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class GameView extends LinearLayout {

    private Card[][] cardsMap = new Card[Config.LINES][Config.LINES];
    private List<Point> emptyPoints = new ArrayList<Point>();

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initGameView();
    }

    public GameView(Context context) {
        super(context);

        initGameView();
    }

    private void initGameView() {
        //監聽滑動
        setOnTouchListener(new View.OnTouchListener() {
            private float startX, startY, offsetX, offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                swipeLeft();
                            } else if (offsetX > 5) {
                                swipeRight();
                            }
                        } else {
                            if (offsetY < -5) {
                                swipeUp();
                            } else if (offsetY > 5) {
                                swipeDown();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    //螢幕尺寸計算結束後執行
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //依照設定行數計算格子大小並且畫出來,pixel
        Config.CARD_WIDTH = (Math.min(w, h) - 10) / Config.LINES;
        addCards(Config.CARD_WIDTH, Config.CARD_WIDTH);
        startGame();
    }

    private void addCards(int cardWidth, int cardHeight) {
        Card card;
        LinearLayout line;
        LinearLayout.LayoutParams lineLp;

        for (int y = 0; y < Config.LINES; y++) {
            line = new LinearLayout(getContext());
            lineLp = new LinearLayout.LayoutParams(-1, cardHeight);
            addView(line, lineLp);

            //種卡片
            for (int x = 0; x < Config.LINES; x++) {
                card = new Card(getContext());
                line.addView(card, cardWidth, cardHeight);

                cardsMap[x][y] = card;
            }
        }
    }

    public void startGame() {
        MainActivity mac = MainActivity.getMainActivity();
        mac.clearScore();
        mac.showBestScore(mac.getBestScore());

        //初始化
        for (int y = 0; y < Config.LINES; y++) {
            for (int x = 0; x < Config.LINES; x++) {
                cardsMap[x][y].setNum(0);
            }
        }

        //遊戲一開始隨機挑兩個位置放數字
        addRandomNum();
        addRandomNum();
    }

    private void addRandomNum() {
        //重新計算盤面空白位置，清空emptyPoints列表
        emptyPoints.clear();

        //計算空白位置並分配到emptyPoints內
        for (int y = 0; y < Config.LINES; y++) {
            for (int x = 0; x < Config.LINES; x++) {
                if (cardsMap[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        if (emptyPoints.size() > 0) {
            //隨機挑一個空白位置
            Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));

            //隨機產生0~1之間的數，產生之數字介於0~0.1之間的話輸出4，否則輸出2
            cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);

            //加入動畫
            MainActivity.getMainActivity().getAnimLayer().createScaleTo1(cardsMap[p.x][p.y]);
        }
    }

    private void swipeLeft() {
        boolean merge = false;

        for (int y = 0; y < Config.LINES; y++) {

            for (int x = 0; x < Config.LINES; x++) {
                for (int x1 = x + 1; x1 < Config.LINES; x1++) {
                    if (cardsMap[x1][y].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);

                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            //位置交換後重新比較
                            x--;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);

                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void swipeRight() {
        boolean merge = false;

        for (int y = 0; y < Config.LINES; y++) {

            for (int x = Config.LINES - 1; x >= 0; x--) {
                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (cardsMap[x1][y].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);

                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x++;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);

                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void swipeUp() {
        boolean merge = false;

        for (int x = 0; x < Config.LINES; x++) {

            for (int y = 0; y < Config.LINES; y++) {
                for (int y1 = y + 1; y1 < Config.LINES; y1++) {
                    if (cardsMap[x][y1].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);

                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y--;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);

                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void swipeDown() {
        boolean merge = false;

        for (int x = 0; x < Config.LINES; x++) {

            for (int y = Config.LINES - 1; y >= 0; y--) {
                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (cardsMap[x][y1].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y++;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);

                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void checkComplete() {
        boolean complete = true;

        //提升效率
        CHECK:
        for (int y = 0; y < Config.LINES; y++) {
            for (int x = 0; x < Config.LINES; x++) {
                if (cardsMap[x][y].getNum() == 0 || //還有空位
//                        (x > 0 && cardsMap[x][y].equals(cardsMap[x-1][y])) ||              //左邊有相同數字可相加
                        (x < Config.LINES - 1 && cardsMap[x][y].equals(cardsMap[x + 1][y])) || //右邊有相同數字可相加
//                        (y > 0 && cardsMap[x][y].equals(cardsMap[x][y-1]))) {              //上面有相同數字可相加
                        (y < Config.LINES - 1 && cardsMap[x][y].equals(cardsMap[x][y + 1]))) { //下面有相同數字可相加

                    complete = false;
                    break CHECK;
                }
            }
        }

        if (complete) {
            AlertDialog ad = new AlertDialog.Builder(getContext()).setTitle("太可惜拉～～～～～～").setMessage("GAME OVERRRRR")
                    .setPositiveButton("再來一局！", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame();
                        }
                    }).show();
            //不可藉由點擊dialog外部空間來消除dialog
            ad.setCanceledOnTouchOutside(false);
            //不可藉由點擊back按鈕來消除dialog
            ad.setCancelable(false);
        }
    }
}
