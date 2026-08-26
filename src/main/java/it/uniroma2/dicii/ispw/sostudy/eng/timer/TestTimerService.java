package it.uniroma2.dicii.ispw.sostudy.eng.timer;

import it.uniroma2.dicii.ispw.sostudy.eng.timer.observer.TimerSubject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestTimerService extends TimerSubject {
    private final LocalDateTime deadline;
    private ScheduledExecutorService scheduler;
    private final Duration totalDuration;

    public TestTimerService(LocalDateTime startInstant, Duration duration) {
        this.totalDuration = duration;
        this.deadline = startInstant.plus(duration);
    }

    public void start() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::tick, 0, 1, TimeUnit.SECONDS);
    }

    private void tick() {
        Duration remaining = getRemaining();
        if (remaining.isNegative() || remaining.isZero()) {
            notifyTimeExpired();
            stop();
        } else {
            notifyRemaningTime();
        }
    }

    public void stop() {
        if (scheduler != null) scheduler.shutdown();
    }

    public Duration getTotalDuration() { return totalDuration; }

    public Duration getRemaining() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime zonedDeadline = deadline.atZone(ZoneId.systemDefault());
        Duration remaining = Duration.between(now, zonedDeadline);
        return remaining.isNegative() ? Duration.ZERO : remaining;
    }
}
