package de.rayzs.monsterlab.plugin;

import de.rayzs.monsterlab.api.monster.MonsterManager;
import com.google.common.reflect.ClassPath;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.plugin.*;
import java.util.Arrays;

public class MonsterLabPlugin extends JavaPlugin {

    public static Plugin PLUGIN;

    @Override
    public void onEnable() {
        PLUGIN = this;
        PluginManager pluginManager = getServer().getPluginManager();

        // Register listeners
        Arrays.asList("monster", "player").forEach(packageName -> loadListenersFromPackage(pluginManager, packageName));
    }

    @Override
    public void onDisable() {
        MonsterManager.removeAll();
    }

    private void loadListenersFromPackage(final PluginManager pluginManager, final String packageName) {
        final ClassPath classPath;

        try {
            classPath = ClassPath.from(MonsterLabPlugin.class.getClassLoader());
            classPath.getTopLevelClasses("de.rayzs.monsterlab.plugin.events." + packageName).forEach(topLevelClass -> {
                try {
                    Class<?> clazz = Class.forName(topLevelClass.getName());
                    String className = clazz.getSimpleName();
                    Object clazzObj = clazz.newInstance();

                    if (clazzObj instanceof Listener) {
                        Listener listener = (Listener) clazzObj;
                        pluginManager.registerEvents(listener, this);
                    } else getLogger().warning("Class " + className + " is not a Listener-Class!");

                } catch (Throwable throwable) {
                    getLogger().warning("Failed to load class: " + topLevelClass.getSimpleName());
                    throwable.printStackTrace();
                }
            });
        } catch (Throwable throwable) {
            getLogger().warning("Failed to load ClassLoader!");
            throwable.printStackTrace();
        }
    }
}
