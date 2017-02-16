package io.disc99.validation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Pattern;

import static io.disc99.validation.Validation.invalid;
import static io.disc99.validation.Validation.valid;

public final class Validations {

    private Validations() {}

    public static <T> Validation<String, T> isNotNull(T target) {
        return target == null ? invalid("may not be null") : valid(target);
    }

    public static Validation<String, String> isNotEmpty(String target) {
        return Util.isEmpty(target) ? invalid("may not be empty") : valid(target);
    }

    public static Validation<String, String> isNumeric(String target) {
        return Util.isNumeric(target) ? invalid("my not be number") : valid(target);
    }

    public static Validation<String, String> pattern(String target, String regexp) {
        return Pattern.matches(regexp, target)
                ? valid(target)
                : invalid(String.format("must match \"%s\"", regexp));
    }

    public static Validation<String, Integer> size(Integer target, Integer min, Integer max) {
        return target >= min && target <= max
                ? valid(target)
                : invalid(String.format("size must be between %s and %s", min, max));
    }

    public static Validation<String, Long> size(Long target, Long min, Long max) {
        return target >= min && target <= max
                ? valid(target)
                : invalid(String.format("size must be between %s and %s", min, max));
    }

    public static Validation<String, Double> size(Double target, Double min, Double max) {
        return target >= min && target <= max
                ? valid(target)
                : invalid(String.format("size must be between %s and %s", min, max));
    }

    public static Validation<String, BigInteger> size(BigInteger target, BigInteger min, BigInteger max) {
        return target.compareTo(min) <= 0 && target.compareTo(max) >= 0
                ? valid(target)
                : invalid(String.format("size must be between %s and %s", min, max));
    }

    public static Validation<String, BigDecimal> size(BigDecimal target, BigDecimal min, BigDecimal max) {
        return target.compareTo(min) <= 0 && target.compareTo(max) >= 0
                ? valid(target)
                : invalid(String.format("size must be between %s and %s", min, max));
    }
}
