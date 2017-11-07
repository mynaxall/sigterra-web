package itomy.sigterra.service.util;

import itomy.sigterra.domain.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class IpRangeCache {
    private static final Logger log = LoggerFactory.getLogger(IpRangeCache.class);

    private final NavigableMap<Long, ExpirableEntry> cachedRanges;
    private final TimeUnit timeToLiveUnit;
    private final long timeToLive;

    private final Object lock = new Object();
    private static final EnumSet<TimeUnit> notAllowedTimeUnits = EnumSet.range(TimeUnit.NANOSECONDS, TimeUnit.MICROSECONDS);

    private static final ThreadFactory THREAD_FACTORY = runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    };

    private IpRangeCache(TimeUnit timeToLiveUnit, long timeToLive) {
        this.cachedRanges = new TreeMap<>();
        this.timeToLiveUnit = timeToLiveUnit;
        this.timeToLive = timeToLive;
    }

    public static IpRangeCache newInstance(TimeUnit timeToLiveUnit, long timeToLive,
                                           TimeUnit cleanDelayTimeUnit, long cleanDelayTime) {

        IpRangeCache cache = new IpRangeCache(timeToLiveUnit, timeToLive);
        if (notAllowedTimeUnits.contains(timeToLiveUnit) || notAllowedTimeUnits.contains(cleanDelayTimeUnit)) {
            throw new IllegalArgumentException("Interval too short");
        }

        ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor(THREAD_FACTORY);
        cleaner.scheduleWithFixedDelay(cache::removeExpired, cleanDelayTime, cleanDelayTime, cleanDelayTimeUnit);
        return cache;
    }

    public void put(Long key, Location value) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entryExpirationTime = now.plusSeconds(timeToLiveUnit.toSeconds(timeToLive));

        synchronized (lock) {
            cachedRanges.put(key, new ExpirableEntry(value, entryExpirationTime));
        }
    }

    public Location get(Long key) {
        Map.Entry<Long, ExpirableEntry> fromEntry = cachedRanges.floorEntry(key);

        // entry not present
        if (fromEntry == null) {
            return null;
        }

        ExpirableEntry expirableEntry = fromEntry.getValue();

        // out of ranges, entry is closest [to] border
        if (expirableEntry.getValue() == null) {
            return null;
        }
        // in range
        refreshEntries(fromEntry);
        return expirableEntry.getValue();
    }

    private void refreshEntries(Map.Entry<Long, ExpirableEntry> fromEntry) {
        refreshEntry(fromEntry);
        // get current range [to] border
        Map.Entry<Long, ExpirableEntry> toEntry = cachedRanges.higherEntry(fromEntry.getKey());
        refreshEntry(toEntry);
    }

    private void refreshEntry(Map.Entry<Long, ExpirableEntry> entry) {
        if (entry != null) {
            LocalDateTime now = LocalDateTime.now();
            entry.getValue().expirationTime = now.plusSeconds(timeToLiveUnit.toSeconds(timeToLive));
        }
    }

    private void removeExpired() {
        if (cachedRanges.isEmpty()) {
            return;
        }

        synchronized (lock) {
            cachedRanges.entrySet().removeIf(entry -> {
                if (entry == null) {
                    return true;
                }
                ExpirableEntry expirableEntry = entry.getValue();
                return expirableEntry.isExpired();
            });
        }
    }

    public long size() {
        return cachedRanges == null ? -1 : cachedRanges.size();
    }

    private class ExpirableEntry {
        private final Location value;
        private LocalDateTime expirationTime;

        ExpirableEntry(Location value, LocalDateTime expirationTime) {
            this.value = value;
            this.expirationTime = expirationTime;
        }

        public Location getValue() {
            return value;
        }

        boolean isExpired() {
            return LocalDateTime.now().isAfter(expirationTime);
        }

        public String toString() {
            return expirationTime + " " + value;
        }
    }
}
