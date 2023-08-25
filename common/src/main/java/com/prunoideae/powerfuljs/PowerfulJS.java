package com.prunoideae.powerfuljs;

import com.google.common.base.Suppliers;
import com.prunoideae.powerfuljs.proxy.PowerfulJSClient;
import com.prunoideae.powerfuljs.proxy.PowerfulJSCommon;
import dev.architectury.utils.EnvExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;


public class PowerfulJS {
    public static final String MOD_ID = "powerfuljs";
    public static Supplier<PowerfulJSCommon> PROXY = Suppliers.memoize(() -> EnvExecutor.getEnvSpecific(() -> PowerfulJSClient::new, () -> PowerfulJSCommon::new));

    public static void init() {

    }
}