package ai.movyra.domain.model.valueobject;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public record TimeRange(Instant startAt, Instant endAt) {
    
    public TimeRange {
        Objects.requireNonNull(startAt, "Start time cannot be null");
        Objects.requireNonNull(endAt, "End time cannot be null");
        
        if (!endAt.isAfter(startAt)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }
    
    public static TimeRange of(Instant startAt, Instant endAt) {
        return new TimeRange(startAt, endAt);
    }
    
    public static TimeRange ofDuration(Instant startAt, Duration duration) {
        return new TimeRange(startAt, startAt.plus(duration));
    }
    
    public Duration duration() {
        return Duration.between(startAt, endAt);
    }
    
    public boolean overlaps(TimeRange other) {
        return this.startAt.isBefore(other.endAt) && this.endAt.isAfter(other.startAt);
    }
    
    public boolean contains(Instant instant) {
        return !instant.isBefore(startAt) && instant.isBefore(endAt);
    }
    
    @Override
    public String toString() {
        return String.format("[%s - %s]", startAt, endAt);
    }
}
