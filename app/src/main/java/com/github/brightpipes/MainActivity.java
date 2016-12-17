package com.github.brightpipes;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;

public class MainActivity extends Activity {

    private Thread gameThread;
    private SurfaceHolder surfaceHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        SurfaceView gameSurface = (SurfaceView) findViewById(R.id.gameSurface);
        gameThread = new Thread(new GameLoop());
        gameThread.start();


        gameSurface.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                surfaceHolder = holder;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                surfaceHolder = holder;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                surfaceHolder = null;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameThread.interrupt();
    }

    private final class GameLoop implements Runnable {
        private void draw(Canvas c) {
            Paint background = new Paint();
            background.setARGB(255, 255, 0, 0);
            c.drawRect(new Rect(0, 0, c.getWidth(), c.getHeight()), background);
        }

        private void update(long deltaTime) {

        }

        @Override
        public void run() {
            long lastUpdate = System.currentTimeMillis();
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    long current = System.currentTimeMillis();

                    update(current - lastUpdate);

                    if (surfaceHolder != null) {
                        Canvas c = surfaceHolder.lockCanvas();
                        if (c != null) {
                            draw(c);
                            surfaceHolder.unlockCanvasAndPost(c);
                        }
                    }

                    lastUpdate = current;

                    Thread.sleep(30);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
