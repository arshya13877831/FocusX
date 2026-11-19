package com.focusx.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LockActivity extends Activity {
    private TextView countdown;
    private final android.os.Handler handler = new android.os.Handler();
    private final Runnable ticker = new Runnable() {
        @Override public void run() {
            long now = System.currentTimeMillis();
            Calendar midnight = Calendar.getInstance();
            midnight.add(Calendar.DAY_OF_YEAR, 1);
            midnight.set(Calendar.HOUR_OF_DAY, 0);
            midnight.set(Calendar.MINUTE, 0);
            midnight.set(Calendar.SECOND, 0);
            midnight.set(Calendar.MILLISECOND, 0);
            long left = Math.max(0, midnight.getTimeInMillis() - now);
            long h = left / 3600000;
            long m = (left / 60000) % 60;
            long s = (left / 1000) % 60;
            countdown.setText(String.format(Locale.US, "%02d:%02d:%02d", h, m, s));
            handler.postDelayed(this, 1000);
            if (left <= 0) finish();
        }
    };

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(0xFF090B12);
        getWindow().setNavigationBarColor(0xFF090B12);
        getWindow().getDecorView().setSystemUiVisibility(0);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(32, 32, 32, 32);
        root.setBackgroundColor(0xFF090B12);

        TextView symbol = new TextView(this);
        symbol.setText("✦");
        symbol.setTextColor(0xFFB79CFF);
        symbol.setTextSize(54);
        symbol.setGravity(Gravity.CENTER);
        root.addView(symbol);

        TextView title = new TextView(this);
        title.setText("وقت استراحتته");
        title.setTextColor(0xFFFFFFFF);
        title.setTextSize(27);
        title.setTypeface(null, 1);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 20, 0, 12);
        root.addView(title);

        TextView desc = new TextView(this);
        desc.setText("سهمیه امروز این برنامه تموم شده. فردا دوباره می‌تونی ازش استفاده کنی.");
        desc.setTextColor(0xFFD0CCDD);
        desc.setTextSize(16);
        desc.setGravity(Gravity.CENTER);
        root.addView(desc);

        countdown = new TextView(this);
        countdown.setTextColor(0xFFB79CFF);
        countdown.setTextSize(32);
        countdown.setGravity(Gravity.CENTER);
        countdown.setPadding(0, 34, 0, 12);
        root.addView(countdown);

        TextView label = new TextView(this);
        label.setText("زمان باقی‌مانده تا باز شدن");
        label.setTextColor(0xFF8E8B9D);
        label.setTextSize(13);
        label.setGravity(Gravity.CENTER);
        root.addView(label);
        setContentView(root);
        ticker.run();
    }

    @Override protected void onDestroy() {
        handler.removeCallbacks(ticker);
        super.onDestroy();
    }

    @Override public void onBackPressed() {
        // Deliberately remain on the lock screen.
    }
}
