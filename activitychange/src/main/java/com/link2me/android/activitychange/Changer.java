package com.link2me.android.activitychange;

import android.content.Context;
import android.content.Intent;

public class Changer {
    private static ActivityChangListener activityChangListener; // 2. Interface 변수 선언

    private static void init(Context context) {
        if (activityChangListener == null) {
            activityChangListener = new TargetActivity(); // 구현 객체 생성
        }
    }

    public static void saveData(Context context, Class activityClassToGo) {
        init(context);
        activityChangListener.activityClass(activityClassToGo);
        Intent intent = new Intent(context, TargetActivity.class);
        context.startActivity(intent);
    }
}
