package net.kunmc.lab.survivetimer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Optional;

public final class SurviveTimer extends JavaPlugin implements Listener {

    private Objective oj;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        Scoreboard sc = Bukkit.getScoreboardManager().getMainScoreboard();
        oj = Optional.ofNullable(sc.getObjective("survive"))
                .orElseGet(() -> sc.registerNewObjective("survive", "dummy", "生存時間"));

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().stream()
                        .filter(Entity::isValid)
                        .map(e -> oj.getScore(e.getName()))
                        .forEach(e -> e.setScore(e.getScore() + 1));
            }
        }.runTaskTimer(this, 0, 20);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        oj.getScore(e.getEntity().getName()).setScore(0);
    }

}
