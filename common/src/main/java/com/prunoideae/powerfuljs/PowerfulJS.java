package com.prunoideae.powerfuljs;

import com.prunoideae.powerfuljs.proxy.PowerfulJSClient;
import com.prunoideae.powerfuljs.proxy.PowerfulJSCommon;
import dev.architectury.utils.EnvExecutor;

public class PowerfulJS {
    public static final String MOD_ID = "powerfuljs";
    public static PowerfulJSCommon PROXY;

    public static void init() {
        PROXY = EnvExecutor.getEnvSpecific(() -> PowerfulJSClient::new, () -> PowerfulJSCommon::new);
    }
}