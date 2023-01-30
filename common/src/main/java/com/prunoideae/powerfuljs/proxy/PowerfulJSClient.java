package com.prunoideae.powerfuljs.proxy;

public class PowerfulJSClient extends PowerfulJSCommon {
    @Override
    public void runOnClient(Runnable run) {
        run.run();
    }
}
