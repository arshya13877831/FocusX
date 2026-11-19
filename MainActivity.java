package com.focusx.app;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
    private TextView status;
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(0xFF090B12);
        getWindow().setNavigationBarColor(0xFF090B12);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(36, 54, 36, 30);
        root.setBackgroundColor(0xFF090B12);

        TextView title = new TextView(this);
        title.setText("FOCUSX");
        title.setTextColor(0xFFB79CFF);
        title.setTextSize(30);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, 1);
        root.addView(title, match());

        TextView sub = new TextView(this);
        sub.setText("کنترل زمان؛ بدون حواس‌پرتی");
        sub.setTextColor(0xFFE6E2F0);
        sub.setTextSize(16);
        sub.setGravity(Gravity.CENTER);
        sub.setPadding(0, 12, 0, 30);
        root.addView(sub, match());

        TextView rule = new TextView(this);
        rule.setText("محدودیت روزانه هر برنامه: ۲۰ دقیقه\\nپس از آن: قفل تا ساعت ۰۰:۰۰");
        rule.setTextColor(0xFFFFFFFF);
        rule.setTextSize(18);
        rule.setGravity(Gravity.CENTER);
        rule.setPadding(20, 24, 20, 24);
        rule.setBackgroundColor(0xFF171A27);
        root.addView(rule, match());

        Button usage = button("۱. فعال‌سازی دسترسی آمار استفاده");
        usage.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)));
        root.addView(usage, params());

        Button accessibility = button("۲. فعال‌سازی سرویس مسدودکننده");
        accessibility.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        root.addView(accessibility, params());

        status = new TextView(this);
        status.setTextColor(0xFFB8B5C6);
        status.setTextSize(14);
        status.setPadding(0, 24, 0, 0);
        root.addView(status, match());

        TextView note = new TextView(this);
        note.setText("برای عملکرد پایدار در MIUI، اجرای خودکار FocusX را مجاز کن و محدودیت باتری آن را روی بدون محدودیت بگذار.");
        note.setTextColor(0xFF8E8B9D);
        note.setTextSize(13);
        note.setPadding(0, 22, 0, 0);
        root.addView(note, match());

        setContentView(root);
    }
    private LinearLayout.LayoutParams match() {
        return new LinearLayout.LayoutParams(-1, -2);
    }
    private LinearLayout.LayoutParams params() {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.topMargin = 18;
        return p;
    }
    private Button button(String text) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(14);
        b.setTextColor(0xFFFFFFFF);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF6550B8));
        return b;
    }
    private boolean hasUsageAccess() {
        AppOpsManager a = (AppOpsManager)getSystemService(Context.APP_OPS_SERVICE);
        int mode = a.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }
    @Override protected void onResume() {
        super.onResume();
        if (status != null) status.setText("دسترسی آمار استفاده: " + (hasUsageAccess() ? "فعال ✓" : "نیاز به فعال‌سازی") +
                "\\nمسدودسازی: از تنظیمات Accessibility سرویس FocusX را روشن کن.");
    }
}
