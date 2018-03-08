package io.disc99.validation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.regex.Pattern;

import static io.disc99.validation.Validation.invalid;
import static io.disc99.validation.Validation.valid;

public interface Validator {

    default  <T> Validation<String, T> isNotNull(T target) {
        return target == null ? invalid("may not be null") : valid(target);
    }

    default Validation<String, String> equal(String target1, String target2) {
        return Objects.equals(target1, target2) ? valid(target1) : invalid("not equal");
    }

    default <T> Validation<String, T> required(T target) {
        return Objects.isNull(target) ? invalid("may not be empty") : valid(target);
    }

    default Validation<String, String> notEmpty(String target) {
        return Util.isEmpty(target) ? invalid("may not be empty") : valid(target);
    }

    default Validation<String, String> numeric(String target) {
        return Util.isNumeric(target) ? invalid("my not be number") : valid(target);
    }

    default Validation<String, String> pattern(String target, String regexp) {
        return Pattern.matches(regexp, target)
                ? valid(target)
                : invalid(String.format("must match \"%s\"", regexp));
    }

    default Validation<String, String> length(String target, Integer min, Integer max) {
        return target.length() >= min && target.length() <= max
                ? valid(target)
                : invalid(String.format("size must be between %s and %s", min, max));
    }

    default Validation<String, Integer> size(Integer target, Integer min, Integer max) {
        return target >= min && target <= max
                ? valid(target)
                : invalid(String.format("size must be between %s and %s", min, max));
    }

    default Validation<String, Long> size(Long target, Long min, Long max) {
        return target >= min && target <= max
                ? valid(target)
                : invalid(String.format("size must be between %s and %s", min, max));
    }

    default Validation<String, Double> size(Double target, Double min, Double max) {
        return target >= min && target <= max
                ? valid(target)
                : invalid(String.format("size must be between %s and %s", min, max));
    }

    default Validation<String, BigInteger> size(BigInteger target, BigInteger min, BigInteger max) {
        return target.compareTo(min) <= 0 && target.compareTo(max) >= 0
                ? valid(target)
                : invalid(String.format("size must be between %s and %s", min, max));
    }

    default Validation<String, BigDecimal> size(BigDecimal target, BigDecimal min, BigDecimal max) {
        return target.compareTo(min) <= 0 && target.compareTo(max) >= 0
                ? valid(target)
                : invalid(String.format("size must be between %s and %s", min, max));
    }
}
