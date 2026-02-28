package com.jjcommands;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("jjcommands")
public class JJMod {
    public JJMod() {
        MinecraftForge.EVENT_BUS.register(JJCommands.class);
        MinecraftForge.EVENT_BUS.register(JJJoinHandler.class);
    }
}
