package com.github.brightpipes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainActivity extends AppCompatActivity {

    private Thread gameThread;
    private SurfaceView gameSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameSurface = (SurfaceView) findViewById(R.id.gameSurface);
        gameThread = new Thread(new GameLoop());

        gameSurface.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        gameThread.interrupt();
    }

    private final class GameLoop implements Runnable {
        private void draw(Canvas c) {
            Paint background = new Paint();
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

                    Canvas c = gameSurface.getHolder().lockCanvas();

                    update(current - lastUpdate);
                    draw(c);

                    gameSurface.getHolder().unlockCanvasAndPost(c);

                    lastUpdate = current;

                    Thread.sleep(30);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
