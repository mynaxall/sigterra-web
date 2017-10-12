package itomy.sigterra.service.util;

import itomy.sigterra.domain.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.TreeMap;
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
        this.timeToLive = timeToLiveUnit.toMillis(timeToLive);
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
        Long fromKey = cachedRanges.floorKey(key);
        Long toKey = cachedRanges.higherKey(key);
        if (fromKey == null) {
            return null;
        }

        ExpirableEntry fromEntry = cachedRanges.get(fromKey);
        ExpirableEntry toEntry = cachedRanges.get(toKey);

        if (fromEntry != null) {
            LocalDateTime updatedTime = LocalDateTime.now().plusSeconds(timeToLiveUnit.toSeconds(timeToLive));
            fromEntry.expirationTime = updatedTime;
            toEntry.expirationTime = updatedTime;

            return fromEntry.getValue();
        } else {
            return null;
        }
    }

    private void removeExpired() {
        if (cachedRanges.isEmpty()) {
            return;
        }

        Iterator<Long> it = cachedRanges.keySet().iterator();
        while (it.hasNext()) {
            if (cachedRanges.get(it.next()).isExpired()) {
                synchronized (lock) {
                    it.remove();
                }
            }
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
            return expirationTime.isAfter(LocalDateTime.now());
        }

        public String toString() {
            return expirationTime + " " + value;
        }
    }
}
