package io.infinicast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ocean on 12.05.2017.
 */
public class IntervalChecker {
    private Timer timer;

    public void StopChecker() {
        Timer t = timer;
        timer = null;
        if (t != null) {
            t.cancel();
        }

    }

    public void StartChecker(final Action o, int timeBetweenChecks) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                o.accept();
            }
        }, timeBetweenChecks,timeBetweenChecks);
    }
}
