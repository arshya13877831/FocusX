package com.focusx.app;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import java.util.Calendar;
import java.util.List;

public class AppBlockerService extends AccessibilityService {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean lockScreenOpen = false;

    private final Runnable checker = new Runnable() {
        @Override public void run() {
            checkForegroundUsage();
            handler.postDelayed(this, 1000);
        }
    };

    @Override protected void onServiceConnected() {
        super.onServiceConnected();
        handler.post(checker);
    }

    @Override public void onAccessibilityEvent(AccessibilityEvent event) {
        checkForegroundUsage();
    }

    @Override public void onInterrupt() { }

    @Override public void onDestroy() {
        handler.removeCallbacks(checker);
        super.onDestroy();
    }

    private void checkForegroundUsage() {
        String exceeded = getExceededPackage();
        if (exceeded != null && !lockScreenOpen) {
            lockScreenOpen = true;
            Intent i = new Intent(this, LockActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.putExtra("blocked_package", exceeded);
            startActivity(i);
        }
        if (exceeded == null) lockScreenOpen = false;
    }

    private String getExceededPackage() {
        UsageStatsManager usm = (UsageStatsManager)getSystemService(USAGE_STATS_SERVICE);
        if (usm == null) return null;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long start = c.getTimeInMillis();
        long end = System.currentTimeMillis();
        List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end);
        if (stats == null) return null;

        String[] packages = {
            "com.instagram.android",
            "com.zhiliaoapp.musically",
            "com.google.android.youtube"
        };
        for (String pkg : packages) {
            long total = 0;
            for (UsageStats s : stats) {
                if (pkg.equals(s.getPackageName())) {
                    total = s.getTotalTimeInForeground();
                    break;
                }
            }
            if (total >= 20L * 60L * 1000L) return pkg;
        }
        return null;
    }
}
