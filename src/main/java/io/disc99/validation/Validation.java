package io.disc99.validation;

import io.disc99.function.*;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;

public interface Validation<E, T> {

    /**
     * Creates a {@link Valid} that contains the given {@code value}.
     *
     * @param <E>   type of the error
     * @param <T>   type of the given {@code value}
     * @param value A value
     * @return {@code Valid(value)}
     */
    static <E, T> Validation<E, T> valid(T value) {
        return new Valid<>(value);
    }

    /**
     * Creates an {@link Invalid} that contains the given {@code error}.
     *
     * @param <E>   type of the given {@code error}
     * @param <T>   type of the value
     * @param error An error
     * @return {@code Invalid(error)}
     * @throws NullPointerException if error is null
     */
    static <E, T> Validation<E, T> invalid(E error) {
        Objects.requireNonNull(error, "error is null");
        return new Invalid<>(error);
    }

    /**
     * Combines two {@code Validation}s to form a {@link Builder}, which can then be used to perform further
     * combines, or apply a function to it in order to transform the {@link Builder} into a {@code Validation}.
     *
     * @param <U>        type of the value contained in validation
     * @param validation the validation object to combine this with
     * @return an instance of Builder
     */
    default <U> Builder<E, T, U> combine(Validation<E, U> validation) {
        return new Builder<>(this, validation);
    }

    /**
     * Combines two {@code Validation}s into a {@link Builder}.
     *
     * @param <E>         type of error
     * @param <T1>        type of first valid value
     * @param <T2>        type of second valid value
     * @param validation1 first validation
     * @param validation2 second validation
     * @return an instance of Builder&lt;E,T1,T2&gt;
     * @throws NullPointerException if validation1 or validation2 is null
     */
    static <E, T1, T2> Builder<E, T1, T2> combine(Validation<E, T1> validation1, Validation<E, T2> validation2) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        return new Builder<>(validation1, validation2);
    }

    /**
     * Combines three {@code Validation}s into a {@link Builder3}.
     *
     * @param <E>         type of error
     * @param <T1>        type of first valid value
     * @param <T2>        type of second valid value
     * @param <T3>        type of third valid value
     * @param validation1 first validation
     * @param validation2 second validation
     * @param validation3 third validation
     * @return an instance of Builder3&lt;E,T1,T2,T3&gt;
     * @throws NullPointerException if validation1, validation2 or validation3 is null
     */
    static <E, T1, T2, T3> Builder3<E, T1, T2, T3> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        return new Builder3<>(validation1, validation2, validation3);
    }

    /**
     * Combines four {@code Validation}s into a {@link Builder4}.
     *
     * @param <E>         type of error
     * @param <T1>        type of first valid value
     * @param <T2>        type of second valid value
     * @param <T3>        type of third valid value
     * @param <T4>        type of fourth valid value
     * @param validation1 first validation
     * @param validation2 second validation
     * @param validation3 third validation
     * @param validation4 fourth validation
     * @return an instance of Builder3&lt;E,T1,T2,T3,T4&gt;
     * @throws NullPointerException if validation1, validation2, validation3 or validation4 is null
     */
    static <E, T1, T2, T3, T4> Builder4<E, T1, T2, T3, T4> combine(Validation<E, T1> validation1, Validation<E, T2> validation2, Validation<E, T3> validation3, Validation<E, T4> validation4) {
        Objects.requireNonNull(validation1, "validation1 is null");
        Objects.requireNonNull(validation2, "validation2 is null");
        Objects.requireNonNull(validation3, "validation3 is null");
        Objects.requireNonNull(validation4, "validation4 is null");
        return new Builder4<>(validation1, validation2, validation3, validation4);
    }

    /**
     * Combines five {@code Validation}s into a {@link Builder5}.
     *
     * @param <E>         type of error
     * @param <T1>        type of first valid value
     * @param <T2>        type of second valid value
     * @param <T3>        type of third valid value
     * @param <T4>        type of fourth valid value
     * @param <T5>        type of fifth valid value
     * @param validation1 first validation
     * @param validation2 second validation
     * @param validation3 third validation
     * @param validation4 fourth validation
     * @param validation5 fifth validation
     * @return an instance of Builder3&lt;E,T1,T2,T3,T4,T5&gt;
     * @throws NullPointerException if validation1, validation2, validation3, validation4 or validation5 is null
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
     * Combines six {@code Validation}s into a {@link Builder6}.
     *
     * @param <E>         type of error
     * @param <T1>        type of first valid value
     * @param <T2>        type of second valid value
     * @param <T3>        type of third valid value
     * @param <T4>        type of fourth valid value
     * @param <T5>        type of fifth valid value
     * @param <T6>        type of sixth valid value
     * @param validation1 first validation
     * @param validation2 second validation
     * @param validation3 third validation
     * @param validation4 fourth validation
     * @param validation5 fifth validation
     * @param validation6 sixth validation
     * @return an instance of Builder3&lt;E,T1,T2,T3,T4,T5,T6&gt;
     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5 or validation6 is null
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
     * Combines seven {@code Validation}s into a {@link Builder7}.
     *
     * @param <E>         type of error
     * @param <T1>        type of first valid value
     * @param <T2>        type of second valid value
     * @param <T3>        type of third valid value
     * @param <T4>        type of fourth valid value
     * @param <T5>        type of fifth valid value
     * @param <T6>        type of sixth valid value
     * @param <T7>        type of seventh valid value
     * @param validation1 first validation
     * @param validation2 second validation
     * @param validation3 third validation
     * @param validation4 fourth validation
     * @param validation5 fifth validation
     * @param validation6 sixth validation
     * @param validation7 seventh validation
     * @return an instance of Builder3&lt;E,T1,T2,T3,T4,T5,T6,T7&gt;
     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6 or validation7 is null
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
     * Combines eight {@code Validation}s into a {@link Builder8}.
     *
     * @param <E>         type of error
     * @param <T1>        type of first valid value
     * @param <T2>        type of second valid value
     * @param <T3>        type of third valid value
     * @param <T4>        type of fourth valid value
     * @param <T5>        type of fifth valid value
     * @param <T6>        type of sixth valid value
     * @param <T7>        type of seventh valid value
     * @param <T8>        type of eighth valid value
     * @param validation1 first validation
     * @param validation2 second validation
     * @param validation3 third validation
     * @param validation4 fourth validation
     * @param validation5 fifth validation
     * @param validation6 sixth validation
     * @param validation7 seventh validation
     * @param validation8 eighth validation
     * @return an instance of Builder3&lt;E,T1,T2,T3,T4,T5,T6,T7,T8&gt;
     * @throws NullPointerException if validation1, validation2, validation3, validation4, validation5, validation6, validation7 or validation8 is null
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

    // TODO
    default <U> Validation<List<E>, U> ap(Validation<List<E>, ? extends Function<? super T, ? extends U>> validation) {
        Objects.requireNonNull(validation, "validation is null");
        if (isValid()) {
            if (validation.isValid()) {
                Function<? super T, ? extends U> f = validation.get();
                U u = f.apply(this.get());
                return valid(u);
            } else {
                List<E> errors = validation.getError();
                return invalid(errors);
            }
        } else {
            if (validation.isValid()) {
                E error = this.getError();
                List<E> errors = new ArrayList<>();
                errors.add(error);
                return invalid(errors);
            } else {
                List<E> errors = validation.getError();
                E error = this.getError();
                errors.add(error);
                return invalid(errors);
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
     * @param f A mapper
     * @param <U>    The new component type
     * @return A new value
     */
    default <U> Validation<E, U> map(Function<? super T, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        if (isInvalid()) {
            return Validation.invalid(this.getError());
        } else {
            T value = this.get();
            return Validation.valid(f.apply(value));
        }
    }

    @SuppressWarnings("unchecked")
    default <U> Validation<E, U> flatMap(Function<? super T, ? extends Validation<E, ? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return isInvalid() ? (Validation<E, U>) this : (Validation<E, U>) mapper.apply(get());
    }

    /**
     * Whereas map only performs a mapping on a valid Validation, and mapError performs a mapping on an invalid
     * Validation, bimap allows you to provide mapping actions for both, and will give you the result based
     * on what type of Validation this is. Without this, you would have to do something like:
     *
     * validation.map(...).mapError(...);
     *
     * @param <E2>        type of the mapping result if this is an invalid
     * @param <T2>        type of the mapping result if this is a valid
     * @param errorMapper the invalid mapping operation
     * @param valueMapper the valid mapping operation
     * @return an instance of Validation&lt;U,R&gt;
     * @throws NullPointerException if invalidMapper or validMapper is null
     */
    default <E2, T2> Validation<E2, T2> bimap(Function<? super E, ? extends E2> errorMapper, Function<? super T, ? extends T2> valueMapper) {
        Objects.requireNonNull(errorMapper, "errorMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        if (isInvalid()) {
            E error = this.getError();
            return Validation.invalid(errorMapper.apply(error));
        } else {
            T value = this.get();
            return Validation.valid(valueMapper.apply(value));
        }
    }

    // TODO
    /**
     * Gets the value if it is a Valid or an value calculated from the error
     *
     * @param other a function which converts an error to an alternative value
     * @return the value, if the underlying Validation is a Valid, or else the alternative value
     * provided by {@code other} by applying the error.
     */
    default T getOrElseGet(Function<? super E, ? extends T> other) {
        Objects.requireNonNull(other, "other is null");
        if (isValid()) {
            return get();
        } else {
            return other.apply(getError());
        }
    }

    // TODO method naming
    default  <X extends Throwable> T getOrElseThrow(Function<? super E, ? extends X> exceptionMapper) throws X {
        if (isValid()) {
            return get();
        } else {
            throw exceptionMapper.apply(getError());
        }
    }

    // TODO method naming
    /**
     * Performs the given action for the value contained in {@code Valid}, or do nothing
     * if this is an Invalid.
     *
     * @param action the action to be performed on the contained value
     * @throws NullPointerException if action is null
     */
    default void ifPresent(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (isValid()) {
            action.accept(get());
        }
    }

    // TODO
    /**
     * Gets the error of this Validation if is an Invalid or throws if this is a Valid
     *
     * @return The error of this Invalid
     * @throws RuntimeException if this is a Valid
     */
    E getError();

    // TODO
    /**
     * Applies a function f to the error of this Validation if this is an Invalid. Otherwise does nothing
     * if this is a Valid.
     *
     * @param <U> type of the error resulting from the mapping
     * @param f   a function that maps the error in this Invalid
     * @return an instance of Validation&lt;U,T&gt;
     * @throws NullPointerException if mapping operation f is null
     */
    default <U> Validation<U, T> mapError(Function<? super E, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        if (isInvalid()) {
            E error = this.getError();
            return Validation.invalid(f.apply(error));
        } else {
            return Validation.valid(this.get());
        }
    }

    /**
     * A valid Validation
     *
     * @param <E> type of the error of this Validation
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
        public E getError() throws RuntimeException {
            throw new NoSuchElementException("error of 'valid' Validation");
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
     * @param <E> type of the error of this Validation
     * @param <T> type of the value of this Validation
     */
    final class Invalid<E, T> implements Validation<E, T>, Serializable {

        private static final long serialVersionUID = 1L;

        private final E error;

        /**
         * Construct an {@code Invalid}
         *
         * @param error The value of this error
         */
        private Invalid(E error) {
            this.error = error;
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
        public E getError() {
            return error;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Invalid && Objects.equals(error, ((Invalid<?, ?>) obj).error));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(error);
        }

        @Override
        public String toString() {
            return "Invalid(" + error + ")";
        }
    }

    final class Builder<E, T1, T2> {

        private Validation<E, T1> v1;
        private Validation<E, T2> v2;

        private Builder(Validation<E, T1> v1, Validation<E, T2> v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        public <R> Validation<List<E>, R> ap(BiFunction<T1, T2, R> f) {
            return v2.ap(v1.ap(Validation.valid(
                    t1 -> t2 -> f.apply(t1, t2)
            )));
        }

        public <T3> Builder3<E, T1, T2, T3> combine(Validation<E, T3> v3) {
            return new Builder3<>(v1, v2, v3);
        }

    }

    final class Builder3<E, T1, T2, T3> {

        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;

        private Builder3(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
        }

        public <R> Validation<List<E>, R> ap(Function3<T1, T2, T3, R> f) {
            return v3.ap(v2.ap(v1.ap(Validation.valid(
                    t1 -> t2 -> t3 -> f.apply(t1, t2, t3)
            ))));
        }

        public <T4> Builder4<E, T1, T2, T3, T4> combine(Validation<E, T4> v4) {
            return new Builder4<>(v1, v2, v3, v4);
        }

    }

    final class Builder4<E, T1, T2, T3, T4> {

        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;

        private Builder4(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
        }

        public <R> Validation<List<E>, R> ap(Function4<T1, T2, T3, T4, R> f) {
            return v4.ap(v3.ap(v2.ap(v1.ap(Validation.valid(
                    t1 -> t2 -> t3 -> t4 -> f.apply(t1, t2, t3, t4)
            )))));
        }

        public <T5> Builder5<E, T1, T2, T3, T4, T5> combine(Validation<E, T5> v5) {
            return new Builder5<>(v1, v2, v3, v4, v5);
        }

    }

    final class Builder5<E, T1, T2, T3, T4, T5> {

        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;

        private Builder5(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
        }

        public <R> Validation<List<E>, R> ap(Function5<T1, T2, T3, T4, T5, R> f) {
            return v5.ap(v4.ap(v3.ap(v2.ap(v1.ap(Validation.valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> f.apply(t1, t2, t3, t4, t5)
            ))))));
        }

        public <T6> Builder6<E, T1, T2, T3, T4, T5, T6> combine(Validation<E, T6> v6) {
            return new Builder6<>(v1, v2, v3, v4, v5, v6);
        }

    }

    final class Builder6<E, T1, T2, T3, T4, T5, T6> {

        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;

        private Builder6(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
        }

        public <R> Validation<List<E>, R> ap(Function6<T1, T2, T3, T4, T5, T6, R> f) {
            return v6.ap(v5.ap(v4.ap(v3.ap(v2.ap(v1.ap(Validation.valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> f.apply(t1, t2, t3, t4, t5, t6)
            )))))));
        }

        public <T7> Builder7<E, T1, T2, T3, T4, T5, T6, T7> combine(Validation<E, T7> v7) {
            return new Builder7<>(v1, v2, v3, v4, v5, v6, v7);
        }

    }

    final class Builder7<E, T1, T2, T3, T4, T5, T6, T7> {

        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;

        private Builder7(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
        }

        public <R> Validation<List<E>, R> ap(Function7<T1, T2, T3, T4, T5, T6, T7, R> f) {
            return v7.ap(v6.ap(v5.ap(v4.ap(v3.ap(v2.ap(v1.ap(Validation.valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> f.apply(t1, t2, t3, t4, t5, t6, t7)
            ))))))));
        }

        public <T8> Builder8<E, T1, T2, T3, T4, T5, T6, T7, T8> combine(Validation<E, T8> v8) {
            return new Builder8<>(v1, v2, v3, v4, v5, v6, v7, v8);
        }

    }

    final class Builder8<E, T1, T2, T3, T4, T5, T6, T7, T8> {

        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;
        private Validation<E, T8> v8;

        private Builder8(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
        }

        public <R> Validation<List<E>, R> ap(Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> f) {
            return v8.ap(v7.ap(v6.ap(v5.ap(v4.ap(v3.ap(v2.ap(v1.ap(Validation.valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8)
            )))))))));
        }
    }
}
