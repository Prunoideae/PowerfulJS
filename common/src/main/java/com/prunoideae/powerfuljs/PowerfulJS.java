package com.prunoideae.powerfuljs;

import com.prunoideae.powerfuljs.proxy.PowerfulJSClient;
import com.prunoideae.powerfuljs.proxy.PowerfulJSCommon;
import dev.architectury.utils.EnvExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class PowerfulJS {
    public static final String MOD_ID = "powerfuljs";
    public static final Logger LOGGER = LogManager.getLogger("PowerfulJS");
    public static PowerfulJSCommon PROXY;

    public static void init() {
        PROXY = EnvExecutor.getEnvSpecific(() -> PowerfulJSClient::new, () -> PowerfulJSCommon::new);
    }
}