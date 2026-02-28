package com.jjcommands;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TechApplier {

    private static boolean ready = false;
    private static boolean tried = false;

    private static Capability<Object> CAP;
    private static Class<?> VARS_CLASS;
    private static Method SYNC;
    private static Field F_TECHNIQUE;
    private static Field F_TECHNIQUE2;
    private static Field F_SELECT;
    private static Field F_SELECT_NAME;
    private static Field F_POWER;
    private static Field F_POWER_MAX;
    private static Field F_POWER_FORMER;

    @SuppressWarnings("unchecked")
    private static boolean init() {
        if (tried) return ready;
        tried = true;
        try {
            Class<?> modVars = Class.forName("net.mcreator.jujutsucraft.network.JujutsucraftModVariables");
            VARS_CLASS = Class.forName("net.mcreator.jujutsucraft.network.JujutsucraftModVariables$PlayerVariables");

            Field capField = modVars.getDeclaredField("PLAYER_VARIABLES_CAPABILITY");
            capField.setAccessible(true);
            CAP = (Capability<Object>) capField.get(null);

            SYNC = VARS_CLASS.getDeclaredMethod("syncPlayerVariables", net.minecraft.world.entity.Entity.class);
            SYNC.setAccessible(true);

            F_TECHNIQUE    = getField("PlayerCurseTechnique");
            F_TECHNIQUE2   = getField("PlayerCurseTechnique2");
            F_SELECT       = getField("PlayerSelectCurseTechnique");
            F_SELECT_NAME  = getField("PlayerSelectCurseTechniqueName");
            F_POWER        = getField("PlayerCursePower");
            F_POWER_MAX    = getField("PlayerCursePowerMAX");
            F_POWER_FORMER = getField("PlayerCursePowerFormer");

            ready = true;
        } catch (Exception e) {
            ready = false;
        }
        return ready;
    }

    private static Field getField(String name) throws Exception {
        Field f = VARS_CLASS.getDeclaredField(name);
        f.setAccessible(true);
        return f;
    }

    public static boolean apply(ServerPlayer player, Techniques.Technique t) {
        if (!init()) return false;
        try {
            var lazy = player.getCapability(CAP);
            boolean[] ok = {false};
            lazy.ifPresent(vars -> {
                try {
                    F_TECHNIQUE.setDouble(vars, t.id());
                    F_TECHNIQUE2.setDouble(vars, t.id());
                    F_SELECT.setDouble(vars, t.id());
                    F_SELECT_NAME.set(vars, t.name());
                    F_POWER.setDouble(vars, t.power());
                    F_POWER_MAX.setDouble(vars, t.power());
                    F_POWER_FORMER.setDouble(vars, t.power() / 4.0);
                    SYNC.invoke(vars, player);
                    ok[0] = true;
                } catch (Exception ignored) {}
            });
            return ok[0];
        } catch (Exception e) {
            return false;
        }
    }
}
