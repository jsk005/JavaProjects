package com.link2me.android.interfaces;

import com.link2me.android.interfaces.interfaces.RemoteControl;

public class Device {
    public void turnon(RemoteControl remoteControl){
        remoteControl.turnOn();
    }

    public void turnoff(RemoteControl remoteControl){
        remoteControl.turnOff();
    }
}
