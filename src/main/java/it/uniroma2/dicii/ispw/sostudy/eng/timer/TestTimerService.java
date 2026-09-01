package it.uniroma2.dicii.ispw.sostudy.eng.timer;

import it.uniroma2.dicii.ispw.sostudy.eng.timer.observer.TimerSubject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class TestTimerService extends TimerSubject {
    private final LocalDateTime deadline;
    private ScheduledExecutorService scheduler;
    private final Duration totalDuration;
    private volatile Duration remaining;

    public TestTimerService(LocalDateTime startInstant, Duration duration) {
        this.totalDuration = duration;
        this.deadline = startInstant.plus(duration);
    }

    public void start() {
        ThreadFactory daemonThreadFactory = runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.setName("TestTimer-Background-Thread");
            return thread;
        };

        scheduler = Executors.newSingleThreadScheduledExecutor(daemonThreadFactory);
        scheduler.scheduleAtFixedRate(this::tick, 0, 1, TimeUnit.SECONDS);
    }

    private void tick() {
        remaining = getRemaining();
        if (remaining.isNegative() || remaining.isZero()) {
            notifyTimeExpired();
            stop();
        } else {
            notifyRemaningTime();
        }
    }

    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }

    public Duration getTotalDuration() { return totalDuration; }

    public Duration getRemaining() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime zonedDeadline = deadline.atZone(ZoneId.systemDefault());
        Duration timeLeft = Duration.between(now, zonedDeadline);
        return timeLeft.isNegative() ? Duration.ZERO : timeLeft;
    }
}