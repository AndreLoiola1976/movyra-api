package ai.movyra.domain;

import ai.movyra.domain.model.valueobject.TimeRange;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

class TimeRangeTest {
    
    @Test
    void shouldCreateValidTimeRange() {
        Instant start = Instant.parse("2024-01-15T10:00:00Z");
        Instant end = Instant.parse("2024-01-15T11:00:00Z");
        
        TimeRange timeRange = TimeRange.of(start, end);
        
        assertThat(timeRange.startAt()).isEqualTo(start);
        assertThat(timeRange.endAt()).isEqualTo(end);
        assertThat(timeRange.duration()).isEqualTo(Duration.ofHours(1));
    }
    
    @Test
    void shouldRejectEndTimeBeforeStartTime() {
        Instant start = Instant.parse("2024-01-15T11:00:00Z");
        Instant end = Instant.parse("2024-01-15T10:00:00Z");
        
        assertThatThrownBy(() -> TimeRange.of(start, end))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("End time must be after start time");
    }
    
    @Test
    void shouldRejectEqualStartAndEndTimes() {
        Instant time = Instant.parse("2024-01-15T10:00:00Z");
        
        assertThatThrownBy(() -> TimeRange.of(time, time))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("End time must be after start time");
    }
    
    @Test
    void shouldDetectOverlappingRanges() {
        TimeRange range1 = TimeRange.of(
            Instant.parse("2024-01-15T10:00:00Z"),
            Instant.parse("2024-01-15T11:00:00Z")
        );
        
        TimeRange range2 = TimeRange.of(
            Instant.parse("2024-01-15T10:30:00Z"),
            Instant.parse("2024-01-15T11:30:00Z")
        );
        
        assertThat(range1.overlaps(range2)).isTrue();
        assertThat(range2.overlaps(range1)).isTrue();
    }
    
    @Test
    void shouldDetectNonOverlappingRanges() {
        TimeRange range1 = TimeRange.of(
            Instant.parse("2024-01-15T10:00:00Z"),
            Instant.parse("2024-01-15T11:00:00Z")
        );
        
        TimeRange range2 = TimeRange.of(
            Instant.parse("2024-01-15T11:00:00Z"),
            Instant.parse("2024-01-15T12:00:00Z")
        );
        
        assertThat(range1.overlaps(range2)).isFalse();
        assertThat(range2.overlaps(range1)).isFalse();
    }
    
    @Test
    void shouldCreateRangeFromDuration() {
        Instant start = Instant.parse("2024-01-15T10:00:00Z");
        Duration duration = Duration.ofMinutes(30);
        
        TimeRange timeRange = TimeRange.ofDuration(start, duration);
        
        assertThat(timeRange.startAt()).isEqualTo(start);
        assertThat(timeRange.endAt()).isEqualTo(start.plus(duration));
        assertThat(timeRange.duration()).isEqualTo(duration);
    }
}
