package com.link2me.android.interfaces;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.link2me.android.interfaces.interfaces.ActivityChangListener;

public class ActivityChanger {
    private final String TAG = this.getClass().getSimpleName();
    ActivityChangListener mListener; // 2. 인터페이스 변수 선언

    public ActivityChanger() {
        Log.e(TAG,"ActivityChanger Start. ");
        mListener = new TargetActivity(); // 구현 객체 생성
    }

    public void saveData(Context context, Class activityClassToGo){
        Log.e(TAG,"ActivityChanger Method :" + activityClassToGo);
        mListener.onActivityClass(activityClassToGo);
        Intent intent = new Intent(context, TargetActivity.class);
        context.startActivity(intent);
    }
}
