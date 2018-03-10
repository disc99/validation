package io.disc99.validation;


import io.disc99.function.*;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static io.disc99.validation.Builders.*;
import static java.util.stream.Collectors.toList;

public interface Validation<E, T> {

    /**
     * Creates a {@link Valid} that contains the given {@code value}.
     *
     * @param <E>   type of the violation
     * @param <T>   type of the given {@code value}
     * @param value A value
     * @return {@code Valid(value)}
     */
    static <E, T> Validation<E, T> valid(T value) {
        return new Valid<>(value);
    }

    /**
     * Creates an {@link Invalid} that contains the given {@code violations}.
     *
     * @param <E>   type of the given {@code violations}
     * @param <T>   type of the value
     * @param violations An violations
     * @return {@code Invalid(violations)}
     * @throws NullPointerException if violations is null
     */
    static <E, T> Validation<E, T> invalid(List<E> violations) {
        Objects.requireNonNull(violations, "violations is null");
        return new Invalid<>(violations);
    }

    /**
     * Creates an {@link Invalid} that contains the given {@code violations}.
     *
     * @param <E>   type of the given {@code violations}
     * @param <T>   type of the value
     * @param violations An violations
     * @return {@code Invalid(violations)}
     * @throws NullPointerException if violations is null
     */
    @SafeVarargs
    static <E, T> Validation<E, T> invalid(E... violations) {
        return invalid(Arrays.asList(violations));
    }

    static <E, T1, T2, U> Validation<E, U> zip(Validation<E, T1> validation1, Validation<E, T2> validation2, BiFunction<T1, T2, Validation<E, U>> zipper) {
        return combine(validation1, validation2).apply(zipper).flatMap(identity());
    }

    default <U> Validation<E, U> apply(Validation<E, ? extends Function<? super T, ? extends U>> validation) {
        Objects.requireNonNull(validation, "validation is null");
        if (isValid()) {
            if (validation.isValid()) {
                Function<? super T, ? extends U> f = validation.get();
                U u = f.apply(this.get());
                return valid(u);
            } else {
                return invalid(validation.getViolations());
            }
        } else {
            if (validation.isValid()) {
                List<E> violations = new ArrayList<>(this.getViolations());
                return invalid(violations);
            } else {
                List<E> violations = validation.getViolations();
                List<E> violation = this.getViolations();
                violations.addAll(violation);
                return invalid(violations);
            }
        }
    }

    /**
     * Check whether this is of type {@code Valid}
     *
     * @return true if is a Valid, false if is an Invalid
     */
    boolean isValid();

    /**
     * Check whether this is of type {@code Invalid}
     *
     * @return true if is an Invalid, false if is a Valid
     */
    boolean isInvalid();

    /**
     * Gets the value of this Validation if is a Valid or throws if this is an Invalid
     *
     * @return The value of this Validation
     * @throws NoSuchElementException if this is an Invalid
     */
    T get();

    /**
     * Maps the underlying value to a different component type.
     *
     * @param mapper A mapper
     * @param <U>    The new component type
     * @return A new value
     */
    default <U> Validation<E, U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isInvalid()) {
            return invalid(this.getViolations());
        } else {
            T value = this.get();
            return valid(mapper.apply(value));
        }
    }

    @SuppressWarnings("unchecked")
    default <U> Validation<E, U> flatMap(Function<? super T, ? extends Validation<E, ? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return isInvalid() ? (Validation<E, U>) this : (Validation<E, U>) mapper.apply(get());
    }

    /**
     * FlatMaps the errors if this {@code Validation} is an {@code Invalid}, otherwise does nothing.
     *
     * @param <U> type of the violations resulting from the mapping
     * @param f   a function that maps violations to sequences
     * @return an instance of {@code Validation<U, T>}
     * @throws NullPointerException if the given function {@code f} is null
     */
    @SuppressWarnings("unchecked")
    default <U> Validation<U, T> flatMapValidations(Function<? super E, ? extends List<? extends U>> f) {
        Objects.requireNonNull(f, "f is null");
        if (isValid()) {
            return (Validation<U, T>) this;
        } else {
            return invalid(getViolations().stream().flatMap(e -> f.apply(e).stream()).collect(toList()));
        }
    }

    /**
     * Whereas map only performs a mapping on a valid Validation, and mapViolations performs a mapping on an invalid
     * Validation, bimap allows you to provide mapping actions for both, and will give you the result based
     * on what type of Validation this is. Without this, you would have to do something like:
     *
     * validation.map(...).mapViolations(...);
     *
     * @param <E2>        type of the mapping result if this is an invalid
     * @param <T2>        type of the mapping result if this is a valid
     * @param violationMapper the invalid mapping operation
     * @param valueMapper the valid mapping operation
     * @return an instance of Validation&lt;U,R&gt;
     * @throws NullPointerException if invalidMapper or validMapper is null
     */
    default <E2, T2> Validation<E2, T2> bimap(Function<List<E>, List<E2>> violationMapper, Function<? super T, ? extends T2> valueMapper) {
        Objects.requireNonNull(violationMapper, "violationMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        if (isInvalid()) {
            List<E> violation = this.getViolations();
            return invalid(violationMapper.apply(violation));
        } else {
            T value = this.get();
            return valid(valueMapper.apply(value));
        }
    }

    /**
     * Gets the value if it is a Valid or an value calculated from the violation
     *
     * @param other a function which converts an violation to an alternative value
     * @return the value, if the underlying Validation is a Valid, or else the alternative value
     * provided by {@code other} by applying the violation.
     */
    default T orInvalidGet(Function<List<E>, ? extends T> other) {
        Objects.requireNonNull(other, "other is null");
        if (isValid()) {
            return get();
        } else {
            return other.apply(getViolations());
        }
    }

    default  <X extends Throwable> T orInvalidThrow(Function<List<E>, ? extends X> exceptionMapper) throws X {
        if (isValid()) {
            return get();
        } else {
            throw exceptionMapper.apply(getViolations());
        }
    }

    /**
     * Performs the given action for the value contained in {@code Valid}, or do nothing
     * if this is an Invalid.
     *
     * @param action the action to be performed on the contained value
     * @throws NullPointerException if action is null
     */
    default void ifValid(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (isValid()) {
            action.accept(get());
        }
    }

    /**
     * Gets the violation of this Validation if is an Invalid or throws if this is a Valid
     *
     * @return The violation of this Invalid
     * @throws RuntimeException if this is a Valid
     */
    List<E> getViolations();

    /**
     * Applies a function mapper to the violation of this Validation if this is an Invalid. Otherwise does nothing
     * if this is a Valid.
     *
     * @param <U> type of the violation resulting from the mapping
     * @param mapper   a function that maps the violation in this Invalid
     * @return an instance of Validation&lt;U,T&gt;
     * @throws NullPointerException if mapping operation mapper is null
     */
    default <U> Validation<U, T> mapViolations(Function<List<E>, List<U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isInvalid()) {
            List<E> violation = this.getViolations();
            return invalid(mapper.apply(violation));
        } else {
            return valid(this.get());
        }
    }

    /**
     * A valid Validation
     *
     * @param <E> type of the violation of this Validation
     * @param <T> type of the value of this Validation
     */
    final class Valid<E, T> implements Validation<E, T>, Serializable {

        private static final long serialVersionUID = 1L;

        private final T value;

        /**
         * Construct a {@code Valid}
         *
         * @param value The value of this success
         */
        private Valid(T value) {
            this.value = value;
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return false;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public List<E> getViolations() throws RuntimeException {
            throw new NoSuchElementException("violation of 'valid' Validation");
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Valid && Objects.equals(value, ((Valid<?, ?>) obj).value));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return "Valid(" + value + ")";
        }

    }

    /**
     * An invalid Validation
     *
     * @param <E> type of the violation of this Validation
     * @param <T> type of the value of this Validation
     */
    final class Invalid<E, T> implements Validation<E, T>, Serializable {

        private static final long serialVersionUID = 1L;

        private final List<E> violation;

        /**
         * Construct an {@code Invalid}
         *
         * @param violations The value of this violations
         */
        private Invalid(List<E> violations) {
            this.violation = violations;
        }

        @Override
        public boolean isValid() {
            return false;
        }

        @Override
        public boolean isInvalid() {
            return true;
        }

        @Override
        public T get() throws RuntimeException {
            throw new NoSuchElementException("get of 'invalid' Validation");
        }

        @Override
        public List<E> getViolations() {
            return violation;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Invalid && Objects.equals(violation, ((Invalid<?, ?>) obj).violation));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(violation);
        }

        @Override
        public String toString() {
            return "Invalid(" + violation + ")";
        }
    }

    static <E, R, T1, T2> Validation<E, R> compose(Validation<E, T1> v1, Validation<E, T2> v2, BiFunction<T1, T2, R> f) {
        return v2.apply(v1.apply(valid(
                t1 -> t2 -> f.apply(t1, t2)
        )));
    }

    default <R, T2> Validation<E, R> accumulate(Validation<E, T2> v2, BiFunction<T, T2, R> f) {
        return v2.apply(this.apply(valid(
                t1 -> t2 -> f.apply(t1, t2)
        )));
    }

    default <R, T2, T3> Validation<E, R> accumulate(Validation<E, T2> v2, Validation<E, T3> v3, Function3<T, T2, T3, R> f) {
        return v3.apply(v2.apply(this.apply(valid(
                t1 -> t2 -> t3 -> f.apply(t1, t2, t3)
        ))));
    }

    default <R, T2, T3, T4> Validation<E, R> accumulate(Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Function4<T, T2, T3, T4, R> f) {
        // TODO
        return null;
    }

    default <R, T2, T3, T4, T5> Validation<E, R> accumulate(Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Function5<T, T2, T3, T4, T5, R> f) {
        // TODO
        return null;
    }

    default <R, T2, T3, T4, T5, T6> Validation<E, R> accumulate(Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Function6<T, T2, T3, T4, T5, T6, R> f) {
        // TODO
        return null;
    }

    default <R, T2, T3, T4, T5, T6, T7> Validation<E, R> accumulate(Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Function7<T, T2, T3, T4, T5, T6, T7, R> f) {
        // TODO
        return null;
    }

    default <R, T2, T3, T4, T5, T6, T7, T8> Validation<E, R> accumulate(Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8, Function8<T, T2, T3, T4, T5, T6, T7, T8, R> f) {
        // TODO
        return null;
    }

    default <R, T2, T3, T4, T5, T6, T7, T8, T9> Validation<E, R> accumulate(Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8, Validation<E, T9> v9, Function9<T, T2, T3, T4, T5, T6, T7, T8, T9, R> f) {
        // TODO
        return null;
    }

    /**
     * Combines two {@code Validation}s to form a {@link Builder2}, which can then be used to perform further
     * combines, or apply a function to it in order to transform the {@link Builder2} into a {@code Validation}.
     *
     * @param <U>        type of the value contained in validation
     * @param validation the validation object to combine this with
     * @return an instance of Builder2
     */
    default <U> Builder2<E, T, U> combine(Validation<E, U> validation) {
        return new Builder2<>(this, validation);
    }

    /**
     * Combines three {@code Validation}s into a {@link Builder2 }.
     *
     * @param <E>         type of violation
     * @param <T1>        type of 1 valid value
     * @param <T2>        type of 2 valid value
     * @param validation1 1 validation
     * @param validation2 2 validation
     * @return an instance of Builder2&lt;E,T1,T2&gt;
     * @throws NullPointerException if validation1, validation2 is null
     */
    static <E, T1, T2> Builder2<E, T1, T2> combine(Validation<E, T1> validation1, Validation<E, T2> validation2) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        return new Builder2<>(validation1, validation2);
    }

    /**
     * Combines three {@code Validation}s into a {@link Builder3 }.
     *
     * @param <E>         type of violation
     * @param <T1>        type of 1 valid value
     * @param <T2>        type of 2 valid value
     * @param <T3>        type of 3 valid value
     * @param validation1 1 validation
     * @param validation2 2 validation
     * @param validation3 3 validation
     * @return an instance of Builder3&lt;E,T1,T2,T3&gt;
     * @throws NullPointerException if validation1, validation2, validation3 is null
     */
    static <E, T1, T2, T3> Builder3<E, T1, T2, T3> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        return new Builder3<>(validation1, validation2, validation3);
    }

    /**
     * Combines three {@code Validation}s into a {@link Builder4 }.
     *
     * @param <E>         type of violation
     * @param <T1>        type of 1 valid value
     * @param <T2>        type of 2 valid value
     * @param <T3>        type of 3 valid value
     * @param <T4>        type of 4 valid value
     * @param validation1 1 validation
     * @param validation2 2 validation
     * @param validation3 3 validation
     * @param validation4 4 validation
     * @return an instance of Builder4&lt;E,T1,T2,T3,T4&gt;
     * @throws NullPointerException if validation1, validation2, validation3, validation4 is null
     */
    static <E, T1, T2, T3, T4> Builder4<E, T1, T2, T3, T4> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        return new Builder4<>(validation1, validation2, validation3, validation4);
    }

    /**
     * Combines three {@code Validation}s into a {@link Builder5 }.
     *
     * @param <E>         type of violation
     * @param <T1>        type of 1 valid value
     * @param <T2>        type of 2 valid value
     * @param <T3>        type of 3 valid value
     * @param <T4>        type of 4 valid value
     * @param <T5>        type of 5 valid value
     * @param validation1 1 validation
     * @param validation2 2 validation
     * @param validation3 3 validation
     * @param validation4 4 validation
     * @param validation5 5 validation
     * @return an instance of Builder5&lt;E,T1,T2,T3,T4,T5&gt;
     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5 is null
     */
    static <E, T1, T2, T3, T4, T5> Builder5<E, T1, T2, T3, T4, T5> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        return new Builder5<>(validation1, validation2, validation3, validation4, validation5);
    }

    /**
     * Combines three {@code Validation}s into a {@link Builder6 }.
     *
     * @param <E>         type of violation
     * @param <T1>        type of 1 valid value
     * @param <T2>        type of 2 valid value
     * @param <T3>        type of 3 valid value
     * @param <T4>        type of 4 valid value
     * @param <T5>        type of 5 valid value
     * @param <T6>        type of 6 valid value
     * @param validation1 1 validation
     * @param validation2 2 validation
     * @param validation3 3 validation
     * @param validation4 4 validation
     * @param validation5 5 validation
     * @param validation6 6 validation
     * @return an instance of Builder6&lt;E,T1,T2,T3,T4,T5,T6&gt;
     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6 is null
     */
    static <E, T1, T2, T3, T4, T5, T6> Builder6<E, T1, T2, T3, T4, T5, T6> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5, Validation<E, T6> validation6) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        Objects.requireNonNull(validation6, "validation6 is null");
        return new Builder6<>(validation1, validation2, validation3, validation4, validation5, validation6);
    }

    /**
     * Combines three {@code Validation}s into a {@link Builder7 }.
     *
     * @param <E>         type of violation
     * @param <T1>        type of 1 valid value
     * @param <T2>        type of 2 valid value
     * @param <T3>        type of 3 valid value
     * @param <T4>        type of 4 valid value
     * @param <T5>        type of 5 valid value
     * @param <T6>        type of 6 valid value
     * @param <T7>        type of 7 valid value
     * @param validation1 1 validation
     * @param validation2 2 validation
     * @param validation3 3 validation
     * @param validation4 4 validation
     * @param validation5 5 validation
     * @param validation6 6 validation
     * @param validation7 7 validation
     * @return an instance of Builder7&lt;E,T1,T2,T3,T4,T5,T6,T7&gt;
     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7 is null
     */
    static <E, T1, T2, T3, T4, T5, T6, T7> Builder7<E, T1, T2, T3, T4, T5, T6, T7> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5, Validation<E, T6> validation6, Validation<E, T7> validation7) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        Objects.requireNonNull(validation6, "validation6 is null");
        Objects.requireNonNull(validation7, "validation7 is null");
        return new Builder7<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7);
    }

    /**
     * Combines three {@code Validation}s into a {@link Builder8 }.
     *
     * @param <E>         type of violation
     * @param <T1>        type of 1 valid value
     * @param <T2>        type of 2 valid value
     * @param <T3>        type of 3 valid value
     * @param <T4>        type of 4 valid value
     * @param <T5>        type of 5 valid value
     * @param <T6>        type of 6 valid value
     * @param <T7>        type of 7 valid value
     * @param <T8>        type of 8 valid value
     * @param validation1 1 validation
     * @param validation2 2 validation
     * @param validation3 3 validation
     * @param validation4 4 validation
     * @param validation5 5 validation
     * @param validation6 6 validation
     * @param validation7 7 validation
     * @param validation8 8 validation
     * @return an instance of Builder8&lt;E,T1,T2,T3,T4,T5,T6,T7,T8&gt;
     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8 is null
     */
    static <E, T1, T2, T3, T4, T5, T6, T7, T8> Builder8<E, T1, T2, T3, T4, T5, T6, T7, T8> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5, Validation<E, T6> validation6, Validation<E, T7> validation7, Validation<E, T8> validation8) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        Objects.requireNonNull(validation6, "validation6 is null");
        Objects.requireNonNull(validation7, "validation7 is null");
        Objects.requireNonNull(validation8, "validation8 is null");
        return new Builder8<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8);
    }

    /**
     * Combines three {@code Validation}s into a {@link Builder9 }.
     *
     * @param <E>         type of violation
     * @param <T1>        type of 1 valid value
     * @param <T2>        type of 2 valid value
     * @param <T3>        type of 3 valid value
     * @param <T4>        type of 4 valid value
     * @param <T5>        type of 5 valid value
     * @param <T6>        type of 6 valid value
     * @param <T7>        type of 7 valid value
     * @param <T8>        type of 8 valid value
     * @param <T9>        type of 9 valid value
     * @param validation1 1 validation
     * @param validation2 2 validation
     * @param validation3 3 validation
     * @param validation4 4 validation
     * @param validation5 5 validation
     * @param validation6 6 validation
     * @param validation7 7 validation
     * @param validation8 8 validation
     * @param validation9 9 validation
     * @return an instance of Builder9&lt;E,T1,T2,T3,T4,T5,T6,T7,T8,T9&gt;
     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9 is null
     */
    static <E, T1, T2, T3, T4, T5, T6, T7, T8, T9> Builder9<E, T1, T2, T3, T4, T5, T6, T7, T8, T9> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5, Validation<E, T6> validation6, Validation<E, T7> validation7, Validation<E, T8> validation8, Validation<E, T9> validation9) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        Objects.requireNonNull(validation6, "validation6 is null");
        Objects.requireNonNull(validation7, "validation7 is null");
        Objects.requireNonNull(validation8, "validation8 is null");
        Objects.requireNonNull(validation9, "validation9 is null");
        return new Builder9<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9);
    }

    /**
     * Combines three {@code Validation}s into a {@link Builder10 }.
     *
     * @param <E>         type of violation
     * @param <T1>        type of 1 valid value
     * @param <T2>        type of 2 valid value
     * @param <T3>        type of 3 valid value
     * @param <T4>        type of 4 valid value
     * @param <T5>        type of 5 valid value
     * @param <T6>        type of 6 valid value
     * @param <T7>        type of 7 valid value
     * @param <T8>        type of 8 valid value
     * @param <T9>        type of 9 valid value
     * @param <T10>        type of 10 valid value
     * @param validation1 1 validation
     * @param validation2 2 validation
     * @param validation3 3 validation
     * @param validation4 4 validation
     * @param validation5 5 validation
     * @param validation6 6 validation
     * @param validation7 7 validation
     * @param validation8 8 validation
     * @param validation9 9 validation
     * @param validation10 10 validation
     * @return an instance of Builder10&lt;E,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10&gt;
     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10 is null
     */
    static <E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Builder10<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5, Validation<E, T6> validation6, Validation<E, T7> validation7, Validation<E, T8> validation8, Validation<E, T9> validation9, Validation<E, T10> validation10) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        Objects.requireNonNull(validation6, "validation6 is null");
        Objects.requireNonNull(validation7, "validation7 is null");
        Objects.requireNonNull(validation8, "validation8 is null");
        Objects.requireNonNull(validation9, "validation9 is null");
        Objects.requireNonNull(validation10, "validation10 is null");
        return new Builder10<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10);
    }

    /**
     * Combines three {@code Validation}s into a {@link Builder11 }.
     *
     * @param <E>         type of violation
     * @param <T1>        type of 1 valid value
     * @param <T2>        type of 2 valid value
     * @param <T3>        type of 3 valid value
     * @param <T4>        type of 4 valid value
     * @param <T5>        type of 5 valid value
     * @param <T6>        type of 6 valid value
     * @param <T7>        type of 7 valid value
     * @param <T8>        type of 8 valid value
     * @param <T9>        type of 9 valid value
     * @param <T10>        type of 10 valid value
     * @param <T11>        type of 11 valid value
     * @param validation1 1 validation
     * @param validation2 2 validation
     * @param validation3 3 validation
     * @param validation4 4 validation
     * @param validation5 5 validation
     * @param validation6 6 validation
     * @param validation7 7 validation
     * @param validation8 8 validation
     * @param validation9 9 validation
     * @param validation10 10 validation
     * @param validation11 11 validation
     * @return an instance of Builder11&lt;E,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11&gt;
     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11 is null
     */
    static <E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Builder11<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5, Validation<E, T6> validation6, Validation<E, T7> validation7, Validation<E, T8> validation8, Validation<E, T9> validation9, Validation<E, T10> validation10, Validation<E, T11> validation11) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        Objects.requireNonNull(validation5, "validation5 is null");
        Objects.requireNonNull(validation6, "validation6 is null");
        Objects.requireNonNull(validation7, "validation7 is null");
        Objects.requireNonNull(validation8, "validation8 is null");
        Objects.requireNonNull(validation9, "validation9 is null");
        Objects.requireNonNull(validation10, "validation10 is null");
        Objects.requireNonNull(validation11, "validation11 is null");
        return new Builder11<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11);
    }
//
//    /**
//     * Combines three {@code Validation}s into a {@link Builder12 }.
//     *
//     * @param <E>         type of violation
//     * @param <T1>        type of 1 valid value
//     * @param <T2>        type of 2 valid value
//     * @param <T3>        type of 3 valid value
//     * @param <T4>        type of 4 valid value
//     * @param <T5>        type of 5 valid value
//     * @param <T6>        type of 6 valid value
//     * @param <T7>        type of 7 valid value
//     * @param <T8>        type of 8 valid value
//     * @param <T9>        type of 9 valid value
//     * @param <T10>        type of 10 valid value
//     * @param <T11>        type of 11 valid value
//     * @param <T12>        type of 12 valid value
//     * @param validation1 1 validation
//     * @param validation2 2 validation
//     * @param validation3 3 validation
//     * @param validation4 4 validation
//     * @param validation5 5 validation
//     * @param validation6 6 validation
//     * @param validation7 7 validation
//     * @param validation8 8 validation
//     * @param validation9 9 validation
//     * @param validation10 10 validation
//     * @param validation11 11 validation
//     * @param validation12 12 validation
//     * @return an instance of Builder12&lt;E,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12&gt;
//     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12 is null
//     */
//    static <E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Builder12<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5, Validation<E, T6> validation6, Validation<E, T7> validation7, Validation<E, T8> validation8, Validation<E, T9> validation9, Validation<E, T10> validation10, Validation<E, T11> validation11, Validation<E, T12> validation12) {
//        Objects.requireNonNull(validation1, "validation1 is null");
//        Objects.requireNonNull(validation2, "validation2 is null");
//        Objects.requireNonNull(validation3, "validation3 is null");
//        Objects.requireNonNull(validation4, "validation4 is null");
//        Objects.requireNonNull(validation5, "validation5 is null");
//        Objects.requireNonNull(validation6, "validation6 is null");
//        Objects.requireNonNull(validation7, "validation7 is null");
//        Objects.requireNonNull(validation8, "validation8 is null");
//        Objects.requireNonNull(validation9, "validation9 is null");
//        Objects.requireNonNull(validation10, "validation10 is null");
//        Objects.requireNonNull(validation11, "validation11 is null");
//        Objects.requireNonNull(validation12, "validation12 is null");
//        return new Builder12<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12);
//    }
//
//    /**
//     * Combines three {@code Validation}s into a {@link Builder13 }.
//     *
//     * @param <E>         type of violation
//     * @param <T1>        type of 1 valid value
//     * @param <T2>        type of 2 valid value
//     * @param <T3>        type of 3 valid value
//     * @param <T4>        type of 4 valid value
//     * @param <T5>        type of 5 valid value
//     * @param <T6>        type of 6 valid value
//     * @param <T7>        type of 7 valid value
//     * @param <T8>        type of 8 valid value
//     * @param <T9>        type of 9 valid value
//     * @param <T10>        type of 10 valid value
//     * @param <T11>        type of 11 valid value
//     * @param <T12>        type of 12 valid value
//     * @param <T13>        type of 13 valid value
//     * @param validation1 1 validation
//     * @param validation2 2 validation
//     * @param validation3 3 validation
//     * @param validation4 4 validation
//     * @param validation5 5 validation
//     * @param validation6 6 validation
//     * @param validation7 7 validation
//     * @param validation8 8 validation
//     * @param validation9 9 validation
//     * @param validation10 10 validation
//     * @param validation11 11 validation
//     * @param validation12 12 validation
//     * @param validation13 13 validation
//     * @return an instance of Builder13&lt;E,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13&gt;
//     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13 is null
//     */
//    static <E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Builder13<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5, Validation<E, T6> validation6, Validation<E, T7> validation7, Validation<E, T8> validation8, Validation<E, T9> validation9, Validation<E, T10> validation10, Validation<E, T11> validation11, Validation<E, T12> validation12, Validation<E, T13> validation13) {
//        Objects.requireNonNull(validation1, "validation1 is null");
//        Objects.requireNonNull(validation2, "validation2 is null");
//        Objects.requireNonNull(validation3, "validation3 is null");
//        Objects.requireNonNull(validation4, "validation4 is null");
//        Objects.requireNonNull(validation5, "validation5 is null");
//        Objects.requireNonNull(validation6, "validation6 is null");
//        Objects.requireNonNull(validation7, "validation7 is null");
//        Objects.requireNonNull(validation8, "validation8 is null");
//        Objects.requireNonNull(validation9, "validation9 is null");
//        Objects.requireNonNull(validation10, "validation10 is null");
//        Objects.requireNonNull(validation11, "validation11 is null");
//        Objects.requireNonNull(validation12, "validation12 is null");
//        Objects.requireNonNull(validation13, "validation13 is null");
//        return new Builder13<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13);
//    }
//
//    /**
//     * Combines three {@code Validation}s into a {@link Builder14 }.
//     *
//     * @param <E>         type of violation
//     * @param <T1>        type of 1 valid value
//     * @param <T2>        type of 2 valid value
//     * @param <T3>        type of 3 valid value
//     * @param <T4>        type of 4 valid value
//     * @param <T5>        type of 5 valid value
//     * @param <T6>        type of 6 valid value
//     * @param <T7>        type of 7 valid value
//     * @param <T8>        type of 8 valid value
//     * @param <T9>        type of 9 valid value
//     * @param <T10>        type of 10 valid value
//     * @param <T11>        type of 11 valid value
//     * @param <T12>        type of 12 valid value
//     * @param <T13>        type of 13 valid value
//     * @param <T14>        type of 14 valid value
//     * @param validation1 1 validation
//     * @param validation2 2 validation
//     * @param validation3 3 validation
//     * @param validation4 4 validation
//     * @param validation5 5 validation
//     * @param validation6 6 validation
//     * @param validation7 7 validation
//     * @param validation8 8 validation
//     * @param validation9 9 validation
//     * @param validation10 10 validation
//     * @param validation11 11 validation
//     * @param validation12 12 validation
//     * @param validation13 13 validation
//     * @param validation14 14 validation
//     * @return an instance of Builder14&lt;E,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14&gt;
//     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14 is null
//     */
//    static <E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Builder14<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5, Validation<E, T6> validation6, Validation<E, T7> validation7, Validation<E, T8> validation8, Validation<E, T9> validation9, Validation<E, T10> validation10, Validation<E, T11> validation11, Validation<E, T12> validation12, Validation<E, T13> validation13, Validation<E, T14> validation14) {
//        Objects.requireNonNull(validation1, "validation1 is null");
//        Objects.requireNonNull(validation2, "validation2 is null");
//        Objects.requireNonNull(validation3, "validation3 is null");
//        Objects.requireNonNull(validation4, "validation4 is null");
//        Objects.requireNonNull(validation5, "validation5 is null");
//        Objects.requireNonNull(validation6, "validation6 is null");
//        Objects.requireNonNull(validation7, "validation7 is null");
//        Objects.requireNonNull(validation8, "validation8 is null");
//        Objects.requireNonNull(validation9, "validation9 is null");
//        Objects.requireNonNull(validation10, "validation10 is null");
//        Objects.requireNonNull(validation11, "validation11 is null");
//        Objects.requireNonNull(validation12, "validation12 is null");
//        Objects.requireNonNull(validation13, "validation13 is null");
//        Objects.requireNonNull(validation14, "validation14 is null");
//        return new Builder14<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14);
//    }
//
//    /**
//     * Combines three {@code Validation}s into a {@link Builder15 }.
//     *
//     * @param <E>         type of violation
//     * @param <T1>        type of 1 valid value
//     * @param <T2>        type of 2 valid value
//     * @param <T3>        type of 3 valid value
//     * @param <T4>        type of 4 valid value
//     * @param <T5>        type of 5 valid value
//     * @param <T6>        type of 6 valid value
//     * @param <T7>        type of 7 valid value
//     * @param <T8>        type of 8 valid value
//     * @param <T9>        type of 9 valid value
//     * @param <T10>        type of 10 valid value
//     * @param <T11>        type of 11 valid value
//     * @param <T12>        type of 12 valid value
//     * @param <T13>        type of 13 valid value
//     * @param <T14>        type of 14 valid value
//     * @param <T15>        type of 15 valid value
//     * @param validation1 1 validation
//     * @param validation2 2 validation
//     * @param validation3 3 validation
//     * @param validation4 4 validation
//     * @param validation5 5 validation
//     * @param validation6 6 validation
//     * @param validation7 7 validation
//     * @param validation8 8 validation
//     * @param validation9 9 validation
//     * @param validation10 10 validation
//     * @param validation11 11 validation
//     * @param validation12 12 validation
//     * @param validation13 13 validation
//     * @param validation14 14 validation
//     * @param validation15 15 validation
//     * @return an instance of Builder15&lt;E,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15&gt;
//     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14, validation15 is null
//     */
//    static <E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Builder15<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5, Validation<E, T6> validation6, Validation<E, T7> validation7, Validation<E, T8> validation8, Validation<E, T9> validation9, Validation<E, T10> validation10, Validation<E, T11> validation11, Validation<E, T12> validation12, Validation<E, T13> validation13, Validation<E, T14> validation14, Validation<E, T15> validation15) {
//        Objects.requireNonNull(validation1, "validation1 is null");
//        Objects.requireNonNull(validation2, "validation2 is null");
//        Objects.requireNonNull(validation3, "validation3 is null");
//        Objects.requireNonNull(validation4, "validation4 is null");
//        Objects.requireNonNull(validation5, "validation5 is null");
//        Objects.requireNonNull(validation6, "validation6 is null");
//        Objects.requireNonNull(validation7, "validation7 is null");
//        Objects.requireNonNull(validation8, "validation8 is null");
//        Objects.requireNonNull(validation9, "validation9 is null");
//        Objects.requireNonNull(validation10, "validation10 is null");
//        Objects.requireNonNull(validation11, "validation11 is null");
//        Objects.requireNonNull(validation12, "validation12 is null");
//        Objects.requireNonNull(validation13, "validation13 is null");
//        Objects.requireNonNull(validation14, "validation14 is null");
//        Objects.requireNonNull(validation15, "validation15 is null");
//        return new Builder15<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14, validation15);
//    }
//
//    /**
//     * Combines three {@code Validation}s into a {@link Builder16 }.
//     *
//     * @param <E>         type of violation
//     * @param <T1>        type of 1 valid value
//     * @param <T2>        type of 2 valid value
//     * @param <T3>        type of 3 valid value
//     * @param <T4>        type of 4 valid value
//     * @param <T5>        type of 5 valid value
//     * @param <T6>        type of 6 valid value
//     * @param <T7>        type of 7 valid value
//     * @param <T8>        type of 8 valid value
//     * @param <T9>        type of 9 valid value
//     * @param <T10>        type of 10 valid value
//     * @param <T11>        type of 11 valid value
//     * @param <T12>        type of 12 valid value
//     * @param <T13>        type of 13 valid value
//     * @param <T14>        type of 14 valid value
//     * @param <T15>        type of 15 valid value
//     * @param <T16>        type of 16 valid value
//     * @param validation1 1 validation
//     * @param validation2 2 validation
//     * @param validation3 3 validation
//     * @param validation4 4 validation
//     * @param validation5 5 validation
//     * @param validation6 6 validation
//     * @param validation7 7 validation
//     * @param validation8 8 validation
//     * @param validation9 9 validation
//     * @param validation10 10 validation
//     * @param validation11 11 validation
//     * @param validation12 12 validation
//     * @param validation13 13 validation
//     * @param validation14 14 validation
//     * @param validation15 15 validation
//     * @param validation16 16 validation
//     * @return an instance of Builder16&lt;E,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16&gt;
//     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14, validation15, validation16 is null
//     */
//    static <E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Builder16<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5, Validation<E, T6> validation6, Validation<E, T7> validation7, Validation<E, T8> validation8, Validation<E, T9> validation9, Validation<E, T10> validation10, Validation<E, T11> validation11, Validation<E, T12> validation12, Validation<E, T13> validation13, Validation<E, T14> validation14, Validation<E, T15> validation15, Validation<E, T16> validation16) {
//        Objects.requireNonNull(validation1, "validation1 is null");
//        Objects.requireNonNull(validation2, "validation2 is null");
//        Objects.requireNonNull(validation3, "validation3 is null");
//        Objects.requireNonNull(validation4, "validation4 is null");
//        Objects.requireNonNull(validation5, "validation5 is null");
//        Objects.requireNonNull(validation6, "validation6 is null");
//        Objects.requireNonNull(validation7, "validation7 is null");
//        Objects.requireNonNull(validation8, "validation8 is null");
//        Objects.requireNonNull(validation9, "validation9 is null");
//        Objects.requireNonNull(validation10, "validation10 is null");
//        Objects.requireNonNull(validation11, "validation11 is null");
//        Objects.requireNonNull(validation12, "validation12 is null");
//        Objects.requireNonNull(validation13, "validation13 is null");
//        Objects.requireNonNull(validation14, "validation14 is null");
//        Objects.requireNonNull(validation15, "validation15 is null");
//        Objects.requireNonNull(validation16, "validation16 is null");
//        return new Builder16<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14, validation15, validation16);
//    }
//
//    /**
//     * Combines three {@code Validation}s into a {@link Builder17 }.
//     *
//     * @param <E>         type of violation
//     * @param <T1>        type of 1 valid value
//     * @param <T2>        type of 2 valid value
//     * @param <T3>        type of 3 valid value
//     * @param <T4>        type of 4 valid value
//     * @param <T5>        type of 5 valid value
//     * @param <T6>        type of 6 valid value
//     * @param <T7>        type of 7 valid value
//     * @param <T8>        type of 8 valid value
//     * @param <T9>        type of 9 valid value
//     * @param <T10>        type of 10 valid value
//     * @param <T11>        type of 11 valid value
//     * @param <T12>        type of 12 valid value
//     * @param <T13>        type of 13 valid value
//     * @param <T14>        type of 14 valid value
//     * @param <T15>        type of 15 valid value
//     * @param <T16>        type of 16 valid value
//     * @param <T17>        type of 17 valid value
//     * @param validation1 1 validation
//     * @param validation2 2 validation
//     * @param validation3 3 validation
//     * @param validation4 4 validation
//     * @param validation5 5 validation
//     * @param validation6 6 validation
//     * @param validation7 7 validation
//     * @param validation8 8 validation
//     * @param validation9 9 validation
//     * @param validation10 10 validation
//     * @param validation11 11 validation
//     * @param validation12 12 validation
//     * @param validation13 13 validation
//     * @param validation14 14 validation
//     * @param validation15 15 validation
//     * @param validation16 16 validation
//     * @param validation17 17 validation
//     * @return an instance of Builder17&lt;E,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17&gt;
//     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14, validation15, validation16, validation17 is null
//     */
//    static <E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Builder17<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5, Validation<E, T6> validation6, Validation<E, T7> validation7, Validation<E, T8> validation8, Validation<E, T9> validation9, Validation<E, T10> validation10, Validation<E, T11> validation11, Validation<E, T12> validation12, Validation<E, T13> validation13, Validation<E, T14> validation14, Validation<E, T15> validation15, Validation<E, T16> validation16, Validation<E, T17> validation17) {
//        Objects.requireNonNull(validation1, "validation1 is null");
//        Objects.requireNonNull(validation2, "validation2 is null");
//        Objects.requireNonNull(validation3, "validation3 is null");
//        Objects.requireNonNull(validation4, "validation4 is null");
//        Objects.requireNonNull(validation5, "validation5 is null");
//        Objects.requireNonNull(validation6, "validation6 is null");
//        Objects.requireNonNull(validation7, "validation7 is null");
//        Objects.requireNonNull(validation8, "validation8 is null");
//        Objects.requireNonNull(validation9, "validation9 is null");
//        Objects.requireNonNull(validation10, "validation10 is null");
//        Objects.requireNonNull(validation11, "validation11 is null");
//        Objects.requireNonNull(validation12, "validation12 is null");
//        Objects.requireNonNull(validation13, "validation13 is null");
//        Objects.requireNonNull(validation14, "validation14 is null");
//        Objects.requireNonNull(validation15, "validation15 is null");
//        Objects.requireNonNull(validation16, "validation16 is null");
//        Objects.requireNonNull(validation17, "validation17 is null");
//        return new Builder17<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14, validation15, validation16, validation17);
//    }
//
//    /**
//     * Combines three {@code Validation}s into a {@link Builder18 }.
//     *
//     * @param <E>         type of violation
//     * @param <T1>        type of 1 valid value
//     * @param <T2>        type of 2 valid value
//     * @param <T3>        type of 3 valid value
//     * @param <T4>        type of 4 valid value
//     * @param <T5>        type of 5 valid value
//     * @param <T6>        type of 6 valid value
//     * @param <T7>        type of 7 valid value
//     * @param <T8>        type of 8 valid value
//     * @param <T9>        type of 9 valid value
//     * @param <T10>        type of 10 valid value
//     * @param <T11>        type of 11 valid value
//     * @param <T12>        type of 12 valid value
//     * @param <T13>        type of 13 valid value
//     * @param <T14>        type of 14 valid value
//     * @param <T15>        type of 15 valid value
//     * @param <T16>        type of 16 valid value
//     * @param <T17>        type of 17 valid value
//     * @param <T18>        type of 18 valid value
//     * @param validation1 1 validation
//     * @param validation2 2 validation
//     * @param validation3 3 validation
//     * @param validation4 4 validation
//     * @param validation5 5 validation
//     * @param validation6 6 validation
//     * @param validation7 7 validation
//     * @param validation8 8 validation
//     * @param validation9 9 validation
//     * @param validation10 10 validation
//     * @param validation11 11 validation
//     * @param validation12 12 validation
//     * @param validation13 13 validation
//     * @param validation14 14 validation
//     * @param validation15 15 validation
//     * @param validation16 16 validation
//     * @param validation17 17 validation
//     * @param validation18 18 validation
//     * @return an instance of Builder18&lt;E,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18&gt;
//     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14, validation15, validation16, validation17, validation18 is null
//     */
//    static <E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Builder18<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5, Validation<E, T6> validation6, Validation<E, T7> validation7, Validation<E, T8> validation8, Validation<E, T9> validation9, Validation<E, T10> validation10, Validation<E, T11> validation11, Validation<E, T12> validation12, Validation<E, T13> validation13, Validation<E, T14> validation14, Validation<E, T15> validation15, Validation<E, T16> validation16, Validation<E, T17> validation17, Validation<E, T18> validation18) {
//        Objects.requireNonNull(validation1, "validation1 is null");
//        Objects.requireNonNull(validation2, "validation2 is null");
//        Objects.requireNonNull(validation3, "validation3 is null");
//        Objects.requireNonNull(validation4, "validation4 is null");
//        Objects.requireNonNull(validation5, "validation5 is null");
//        Objects.requireNonNull(validation6, "validation6 is null");
//        Objects.requireNonNull(validation7, "validation7 is null");
//        Objects.requireNonNull(validation8, "validation8 is null");
//        Objects.requireNonNull(validation9, "validation9 is null");
//        Objects.requireNonNull(validation10, "validation10 is null");
//        Objects.requireNonNull(validation11, "validation11 is null");
//        Objects.requireNonNull(validation12, "validation12 is null");
//        Objects.requireNonNull(validation13, "validation13 is null");
//        Objects.requireNonNull(validation14, "validation14 is null");
//        Objects.requireNonNull(validation15, "validation15 is null");
//        Objects.requireNonNull(validation16, "validation16 is null");
//        Objects.requireNonNull(validation17, "validation17 is null");
//        Objects.requireNonNull(validation18, "validation18 is null");
//        return new Builder18<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14, validation15, validation16, validation17, validation18);
//    }
//
//    /**
//     * Combines three {@code Validation}s into a {@link Builder19 }.
//     *
//     * @param <E>         type of violation
//     * @param <T1>        type of 1 valid value
//     * @param <T2>        type of 2 valid value
//     * @param <T3>        type of 3 valid value
//     * @param <T4>        type of 4 valid value
//     * @param <T5>        type of 5 valid value
//     * @param <T6>        type of 6 valid value
//     * @param <T7>        type of 7 valid value
//     * @param <T8>        type of 8 valid value
//     * @param <T9>        type of 9 valid value
//     * @param <T10>        type of 10 valid value
//     * @param <T11>        type of 11 valid value
//     * @param <T12>        type of 12 valid value
//     * @param <T13>        type of 13 valid value
//     * @param <T14>        type of 14 valid value
//     * @param <T15>        type of 15 valid value
//     * @param <T16>        type of 16 valid value
//     * @param <T17>        type of 17 valid value
//     * @param <T18>        type of 18 valid value
//     * @param <T19>        type of 19 valid value
//     * @param validation1 1 validation
//     * @param validation2 2 validation
//     * @param validation3 3 validation
//     * @param validation4 4 validation
//     * @param validation5 5 validation
//     * @param validation6 6 validation
//     * @param validation7 7 validation
//     * @param validation8 8 validation
//     * @param validation9 9 validation
//     * @param validation10 10 validation
//     * @param validation11 11 validation
//     * @param validation12 12 validation
//     * @param validation13 13 validation
//     * @param validation14 14 validation
//     * @param validation15 15 validation
//     * @param validation16 16 validation
//     * @param validation17 17 validation
//     * @param validation18 18 validation
//     * @param validation19 19 validation
//     * @return an instance of Builder19&lt;E,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19&gt;
//     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14, validation15, validation16, validation17, validation18, validation19 is null
//     */
//    static <E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Builder19<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5, Validation<E, T6> validation6, Validation<E, T7> validation7, Validation<E, T8> validation8, Validation<E, T9> validation9, Validation<E, T10> validation10, Validation<E, T11> validation11, Validation<E, T12> validation12, Validation<E, T13> validation13, Validation<E, T14> validation14, Validation<E, T15> validation15, Validation<E, T16> validation16, Validation<E, T17> validation17, Validation<E, T18> validation18, Validation<E, T19> validation19) {
//        Objects.requireNonNull(validation1, "validation1 is null");
//        Objects.requireNonNull(validation2, "validation2 is null");
//        Objects.requireNonNull(validation3, "validation3 is null");
//        Objects.requireNonNull(validation4, "validation4 is null");
//        Objects.requireNonNull(validation5, "validation5 is null");
//        Objects.requireNonNull(validation6, "validation6 is null");
//        Objects.requireNonNull(validation7, "validation7 is null");
//        Objects.requireNonNull(validation8, "validation8 is null");
//        Objects.requireNonNull(validation9, "validation9 is null");
//        Objects.requireNonNull(validation10, "validation10 is null");
//        Objects.requireNonNull(validation11, "validation11 is null");
//        Objects.requireNonNull(validation12, "validation12 is null");
//        Objects.requireNonNull(validation13, "validation13 is null");
//        Objects.requireNonNull(validation14, "validation14 is null");
//        Objects.requireNonNull(validation15, "validation15 is null");
//        Objects.requireNonNull(validation16, "validation16 is null");
//        Objects.requireNonNull(validation17, "validation17 is null");
//        Objects.requireNonNull(validation18, "validation18 is null");
//        Objects.requireNonNull(validation19, "validation19 is null");
//        return new Builder19<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14, validation15, validation16, validation17, validation18, validation19);
//    }
//
//    /**
//     * Combines three {@code Validation}s into a {@link Builder20 }.
//     *
//     * @param <E>         type of violation
//     * @param <T1>        type of 1 valid value
//     * @param <T2>        type of 2 valid value
//     * @param <T3>        type of 3 valid value
//     * @param <T4>        type of 4 valid value
//     * @param <T5>        type of 5 valid value
//     * @param <T6>        type of 6 valid value
//     * @param <T7>        type of 7 valid value
//     * @param <T8>        type of 8 valid value
//     * @param <T9>        type of 9 valid value
//     * @param <T10>        type of 10 valid value
//     * @param <T11>        type of 11 valid value
//     * @param <T12>        type of 12 valid value
//     * @param <T13>        type of 13 valid value
//     * @param <T14>        type of 14 valid value
//     * @param <T15>        type of 15 valid value
//     * @param <T16>        type of 16 valid value
//     * @param <T17>        type of 17 valid value
//     * @param <T18>        type of 18 valid value
//     * @param <T19>        type of 19 valid value
//     * @param <T20>        type of 20 valid value
//     * @param validation1 1 validation
//     * @param validation2 2 validation
//     * @param validation3 3 validation
//     * @param validation4 4 validation
//     * @param validation5 5 validation
//     * @param validation6 6 validation
//     * @param validation7 7 validation
//     * @param validation8 8 validation
//     * @param validation9 9 validation
//     * @param validation10 10 validation
//     * @param validation11 11 validation
//     * @param validation12 12 validation
//     * @param validation13 13 validation
//     * @param validation14 14 validation
//     * @param validation15 15 validation
//     * @param validation16 16 validation
//     * @param validation17 17 validation
//     * @param validation18 18 validation
//     * @param validation19 19 validation
//     * @param validation20 20 validation
//     * @return an instance of Builder20&lt;E,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20&gt;
//     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14, validation15, validation16, validation17, validation18, validation19, validation20 is null
//     */
//    static <E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Builder20<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5, Validation<E, T6> validation6, Validation<E, T7> validation7, Validation<E, T8> validation8, Validation<E, T9> validation9, Validation<E, T10> validation10, Validation<E, T11> validation11, Validation<E, T12> validation12, Validation<E, T13> validation13, Validation<E, T14> validation14, Validation<E, T15> validation15, Validation<E, T16> validation16, Validation<E, T17> validation17, Validation<E, T18> validation18, Validation<E, T19> validation19, Validation<E, T20> validation20) {
//        Objects.requireNonNull(validation1, "validation1 is null");
//        Objects.requireNonNull(validation2, "validation2 is null");
//        Objects.requireNonNull(validation3, "validation3 is null");
//        Objects.requireNonNull(validation4, "validation4 is null");
//        Objects.requireNonNull(validation5, "validation5 is null");
//        Objects.requireNonNull(validation6, "validation6 is null");
//        Objects.requireNonNull(validation7, "validation7 is null");
//        Objects.requireNonNull(validation8, "validation8 is null");
//        Objects.requireNonNull(validation9, "validation9 is null");
//        Objects.requireNonNull(validation10, "validation10 is null");
//        Objects.requireNonNull(validation11, "validation11 is null");
//        Objects.requireNonNull(validation12, "validation12 is null");
//        Objects.requireNonNull(validation13, "validation13 is null");
//        Objects.requireNonNull(validation14, "validation14 is null");
//        Objects.requireNonNull(validation15, "validation15 is null");
//        Objects.requireNonNull(validation16, "validation16 is null");
//        Objects.requireNonNull(validation17, "validation17 is null");
//        Objects.requireNonNull(validation18, "validation18 is null");
//        Objects.requireNonNull(validation19, "validation19 is null");
//        Objects.requireNonNull(validation20, "validation20 is null");
//        return new Builder20<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14, validation15, validation16, validation17, validation18, validation19, validation20);
//    }
//
//    /**
//     * Combines three {@code Validation}s into a {@link Builder21 }.
//     *
//     * @param <E>         type of violation
//     * @param <T1>        type of 1 valid value
//     * @param <T2>        type of 2 valid value
//     * @param <T3>        type of 3 valid value
//     * @param <T4>        type of 4 valid value
//     * @param <T5>        type of 5 valid value
//     * @param <T6>        type of 6 valid value
//     * @param <T7>        type of 7 valid value
//     * @param <T8>        type of 8 valid value
//     * @param <T9>        type of 9 valid value
//     * @param <T10>        type of 10 valid value
//     * @param <T11>        type of 11 valid value
//     * @param <T12>        type of 12 valid value
//     * @param <T13>        type of 13 valid value
//     * @param <T14>        type of 14 valid value
//     * @param <T15>        type of 15 valid value
//     * @param <T16>        type of 16 valid value
//     * @param <T17>        type of 17 valid value
//     * @param <T18>        type of 18 valid value
//     * @param <T19>        type of 19 valid value
//     * @param <T20>        type of 20 valid value
//     * @param <T21>        type of 21 valid value
//     * @param validation1 1 validation
//     * @param validation2 2 validation
//     * @param validation3 3 validation
//     * @param validation4 4 validation
//     * @param validation5 5 validation
//     * @param validation6 6 validation
//     * @param validation7 7 validation
//     * @param validation8 8 validation
//     * @param validation9 9 validation
//     * @param validation10 10 validation
//     * @param validation11 11 validation
//     * @param validation12 12 validation
//     * @param validation13 13 validation
//     * @param validation14 14 validation
//     * @param validation15 15 validation
//     * @param validation16 16 validation
//     * @param validation17 17 validation
//     * @param validation18 18 validation
//     * @param validation19 19 validation
//     * @param validation20 20 validation
//     * @param validation21 21 validation
//     * @return an instance of Builder21&lt;E,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21&gt;
//     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14, validation15, validation16, validation17, validation18, validation19, validation20, validation21 is null
//     */
//    static <E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> Builder21<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5, Validation<E, T6> validation6, Validation<E, T7> validation7, Validation<E, T8> validation8, Validation<E, T9> validation9, Validation<E, T10> validation10, Validation<E, T11> validation11, Validation<E, T12> validation12, Validation<E, T13> validation13, Validation<E, T14> validation14, Validation<E, T15> validation15, Validation<E, T16> validation16, Validation<E, T17> validation17, Validation<E, T18> validation18, Validation<E, T19> validation19, Validation<E, T20> validation20, Validation<E, T21> validation21) {
//        Objects.requireNonNull(validation1, "validation1 is null");
//        Objects.requireNonNull(validation2, "validation2 is null");
//        Objects.requireNonNull(validation3, "validation3 is null");
//        Objects.requireNonNull(validation4, "validation4 is null");
//        Objects.requireNonNull(validation5, "validation5 is null");
//        Objects.requireNonNull(validation6, "validation6 is null");
//        Objects.requireNonNull(validation7, "validation7 is null");
//        Objects.requireNonNull(validation8, "validation8 is null");
//        Objects.requireNonNull(validation9, "validation9 is null");
//        Objects.requireNonNull(validation10, "validation10 is null");
//        Objects.requireNonNull(validation11, "validation11 is null");
//        Objects.requireNonNull(validation12, "validation12 is null");
//        Objects.requireNonNull(validation13, "validation13 is null");
//        Objects.requireNonNull(validation14, "validation14 is null");
//        Objects.requireNonNull(validation15, "validation15 is null");
//        Objects.requireNonNull(validation16, "validation16 is null");
//        Objects.requireNonNull(validation17, "validation17 is null");
//        Objects.requireNonNull(validation18, "validation18 is null");
//        Objects.requireNonNull(validation19, "validation19 is null");
//        Objects.requireNonNull(validation20, "validation20 is null");
//        Objects.requireNonNull(validation21, "validation21 is null");
//        return new Builder21<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14, validation15, validation16, validation17, validation18, validation19, validation20, validation21);
//    }
//
//    /**
//     * Combines three {@code Validation}s into a {@link Builder22 }.
//     *
//     * @param <E>         type of violation
//     * @param <T1>        type of 1 valid value
//     * @param <T2>        type of 2 valid value
//     * @param <T3>        type of 3 valid value
//     * @param <T4>        type of 4 valid value
//     * @param <T5>        type of 5 valid value
//     * @param <T6>        type of 6 valid value
//     * @param <T7>        type of 7 valid value
//     * @param <T8>        type of 8 valid value
//     * @param <T9>        type of 9 valid value
//     * @param <T10>        type of 10 valid value
//     * @param <T11>        type of 11 valid value
//     * @param <T12>        type of 12 valid value
//     * @param <T13>        type of 13 valid value
//     * @param <T14>        type of 14 valid value
//     * @param <T15>        type of 15 valid value
//     * @param <T16>        type of 16 valid value
//     * @param <T17>        type of 17 valid value
//     * @param <T18>        type of 18 valid value
//     * @param <T19>        type of 19 valid value
//     * @param <T20>        type of 20 valid value
//     * @param <T21>        type of 21 valid value
//     * @param <T22>        type of 22 valid value
//     * @param validation1 1 validation
//     * @param validation2 2 validation
//     * @param validation3 3 validation
//     * @param validation4 4 validation
//     * @param validation5 5 validation
//     * @param validation6 6 validation
//     * @param validation7 7 validation
//     * @param validation8 8 validation
//     * @param validation9 9 validation
//     * @param validation10 10 validation
//     * @param validation11 11 validation
//     * @param validation12 12 validation
//     * @param validation13 13 validation
//     * @param validation14 14 validation
//     * @param validation15 15 validation
//     * @param validation16 16 validation
//     * @param validation17 17 validation
//     * @param validation18 18 validation
//     * @param validation19 19 validation
//     * @param validation20 20 validation
//     * @param validation21 21 validation
//     * @param validation22 22 validation
//     * @return an instance of Builder22&lt;E,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21,T22&gt;
//     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14, validation15, validation16, validation17, validation18, validation19, validation20, validation21, validation22 is null
//     */
//    static <E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Builder22<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4, Validation<E, T5> validation5, Validation<E, T6> validation6, Validation<E, T7> validation7, Validation<E, T8> validation8, Validation<E, T9> validation9, Validation<E, T10> validation10, Validation<E, T11> validation11, Validation<E, T12> validation12, Validation<E, T13> validation13, Validation<E, T14> validation14, Validation<E, T15> validation15, Validation<E, T16> validation16, Validation<E, T17> validation17, Validation<E, T18> validation18, Validation<E, T19> validation19, Validation<E, T20> validation20, Validation<E, T21> validation21, Validation<E, T22> validation22) {
//        Objects.requireNonNull(validation1, "validation1 is null");
//        Objects.requireNonNull(validation2, "validation2 is null");
//        Objects.requireNonNull(validation3, "validation3 is null");
//        Objects.requireNonNull(validation4, "validation4 is null");
//        Objects.requireNonNull(validation5, "validation5 is null");
//        Objects.requireNonNull(validation6, "validation6 is null");
//        Objects.requireNonNull(validation7, "validation7 is null");
//        Objects.requireNonNull(validation8, "validation8 is null");
//        Objects.requireNonNull(validation9, "validation9 is null");
//        Objects.requireNonNull(validation10, "validation10 is null");
//        Objects.requireNonNull(validation11, "validation11 is null");
//        Objects.requireNonNull(validation12, "validation12 is null");
//        Objects.requireNonNull(validation13, "validation13 is null");
//        Objects.requireNonNull(validation14, "validation14 is null");
//        Objects.requireNonNull(validation15, "validation15 is null");
//        Objects.requireNonNull(validation16, "validation16 is null");
//        Objects.requireNonNull(validation17, "validation17 is null");
//        Objects.requireNonNull(validation18, "validation18 is null");
//        Objects.requireNonNull(validation19, "validation19 is null");
//        Objects.requireNonNull(validation20, "validation20 is null");
//        Objects.requireNonNull(validation21, "validation21 is null");
//        Objects.requireNonNull(validation22, "validation22 is null");
//        return new Builder22<>(validation1, validation2, validation3, validation4, validation5, validation6, validation7, validation8, validation9, validation10, validation11, validation12, validation13, validation14, validation15, validation16, validation17, validation18, validation19, validation20, validation21, validation22);
//    }
}
