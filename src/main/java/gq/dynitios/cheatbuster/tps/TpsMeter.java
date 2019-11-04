package gq.dynitios.cheatbuster.tps;

import org.bukkit.scheduler.BukkitRunnable;

public class TpsMeter extends BukkitRunnable {

    private static double tps = -1;

    private static long currentSec = 0;
    private static int ticks = 0;

    public static double getTps() {
        return tps;
    }

    @Override
    public void run() {
        long sec = (System.currentTimeMillis() / 1000);

        if (currentSec == sec) {
            ticks++;
        } else {
            currentSec = sec;
            tps = (tps == 0 ? ticks : ((tps + ticks) / 2));
            ticks = 0;
        }
    }
}
