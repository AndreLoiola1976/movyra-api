package ai.movyra.domain.model.valueobject;

import java.util.Objects;

public record MoneyCents(int value) {
    
    public MoneyCents {
        if (value < 0) {
            throw new IllegalArgumentException("Money amount cannot be negative");
        }
    }
    
    public static MoneyCents of(int cents) {
        return new MoneyCents(cents);
    }
    
    public static MoneyCents zero() {
        return new MoneyCents(0);
    }
    
    public static MoneyCents fromDollars(double dollars) {
        return new MoneyCents((int) Math.round(dollars * 100));
    }
    
    public double toDollars() {
        return value / 100.0;
    }
    
    public MoneyCents add(MoneyCents other) {
        return new MoneyCents(this.value + other.value);
    }
    
    public MoneyCents subtract(MoneyCents other) {
        return new MoneyCents(this.value - other.value);
    }
    
    @Override
    public String toString() {
        return String.format("$%.2f", toDollars());
    }
}
