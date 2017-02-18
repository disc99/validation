package io.disc99.validation;

import io.disc99.function.*;
import static io.disc99.validation.Validation.valid;

import java.util.function.BiFunction;

final class Builders {

    static final class Builder2<E, T1, T2> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;

        Builder2(Validation<E, T1> v1, Validation<E, T2> v2) {
            this.v1 = v1;
            this.v2 = v2;

        }

        public <R> Validation<E, R> apply(BiFunction<T1, T2, R> f) {
            return v2.apply(v1.apply(valid(
                    t1 -> t2 -> f.apply(t1, t2)
            )));
        }

        public <T3> Builder3<E, T1, T2, T3> combine(Validation<E, T3> v3) {
            return new Builder3<>(v1 ,v2 ,v3);
        }
    }

    static final class Builder3<E, T1, T2, T3> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;

        Builder3(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;

        }

        public <R> Validation<E, R> apply(Function3<T1, T2, T3, R> f) {
            return v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> f.apply(t1, t2, t3)
            ))));
        }

        public <T4> Builder4<E, T1, T2, T3, T4> combine(Validation<E, T4> v4) {
            return new Builder4<>(v1 ,v2 ,v3 ,v4);
        }
    }

    static final class Builder4<E, T1, T2, T3, T4> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;

        Builder4(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;

        }

        public <R> Validation<E, R> apply(Function4<T1, T2, T3, T4, R> f) {
            return v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> f.apply(t1, t2, t3, t4)
            )))));
        }

        public <T5> Builder5<E, T1, T2, T3, T4, T5> combine(Validation<E, T5> v5) {
            return new Builder5<>(v1 ,v2 ,v3 ,v4 ,v5);
        }
    }

    static final class Builder5<E, T1, T2, T3, T4, T5> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;

        Builder5(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;

        }

        public <R> Validation<E, R> apply(Function5<T1, T2, T3, T4, T5, R> f) {
            return v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> f.apply(t1, t2, t3, t4, t5)
            ))))));
        }

        public <T6> Builder6<E, T1, T2, T3, T4, T5, T6> combine(Validation<E, T6> v6) {
            return new Builder6<>(v1 ,v2 ,v3 ,v4 ,v5 ,v6);
        }
    }

    static final class Builder6<E, T1, T2, T3, T4, T5, T6> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;

        Builder6(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;

        }

        public <R> Validation<E, R> apply(Function6<T1, T2, T3, T4, T5, T6, R> f) {
            return v6.apply(v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> f.apply(t1, t2, t3, t4, t5, t6)
            )))))));
        }

        public <T7> Builder7<E, T1, T2, T3, T4, T5, T6, T7> combine(Validation<E, T7> v7) {
            return new Builder7<>(v1 ,v2 ,v3 ,v4 ,v5 ,v6 ,v7);
        }
    }

    static final class Builder7<E, T1, T2, T3, T4, T5, T6, T7> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;

        Builder7(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;

        }

        public <R> Validation<E, R> apply(Function7<T1, T2, T3, T4, T5, T6, T7, R> f) {
            return v7.apply(v6.apply(v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> f.apply(t1, t2, t3, t4, t5, t6, t7)
            ))))))));
        }

        public <T8> Builder8<E, T1, T2, T3, T4, T5, T6, T7, T8> combine(Validation<E, T8> v8) {
            return new Builder8<>(v1 ,v2 ,v3 ,v4 ,v5 ,v6 ,v7 ,v8);
        }
    }

    static final class Builder8<E, T1, T2, T3, T4, T5, T6, T7, T8> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;
        private Validation<E, T8> v8;

        Builder8(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;

        }

        public <R> Validation<E, R> apply(Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> f) {
            return v8.apply(v7.apply(v6.apply(v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8)
            )))))))));
        }

        public <T9> Builder9<E, T1, T2, T3, T4, T5, T6, T7, T8, T9> combine(Validation<E, T9> v9) {
            return new Builder9<>(v1 ,v2 ,v3 ,v4 ,v5 ,v6 ,v7 ,v8 ,v9);
        }
    }

    static final class Builder9<E, T1, T2, T3, T4, T5, T6, T7, T8, T9> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;
        private Validation<E, T8> v8;
        private Validation<E, T9> v9;

        Builder9(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8, Validation<E, T9> v9) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
            this.v9 = v9;

        }

        public <R> Validation<E, R> apply(Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> f) {
            return v9.apply(v8.apply(v7.apply(v6.apply(v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9)
            ))))))))));
        }

        public <T10> Builder10<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> combine(Validation<E, T10> v10) {
            return new Builder10<>(v1 ,v2 ,v3 ,v4 ,v5 ,v6 ,v7 ,v8 ,v9 ,v10);
        }
    }

    static final class Builder10<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;
        private Validation<E, T8> v8;
        private Validation<E, T9> v9;
        private Validation<E, T10> v10;

        Builder10(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8, Validation<E, T9> v9, Validation<E, T10> v10) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
            this.v9 = v9;
            this.v10 = v10;

        }

        public <R> Validation<E, R> apply(Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> f) {
            return v10.apply(v9.apply(v8.apply(v7.apply(v6.apply(v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10)
            )))))))))));
        }

        public <T11> Builder11<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> combine(Validation<E, T11> v11) {
            return new Builder11<>(v1 ,v2 ,v3 ,v4 ,v5 ,v6 ,v7 ,v8 ,v9 ,v10 ,v11);
        }
    }

    static final class Builder11<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;
        private Validation<E, T8> v8;
        private Validation<E, T9> v9;
        private Validation<E, T10> v10;
        private Validation<E, T11> v11;

        Builder11(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8, Validation<E, T9> v9, Validation<E, T10> v10, Validation<E, T11> v11) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
            this.v9 = v9;
            this.v10 = v10;
            this.v11 = v11;

        }

        public <R> Validation<E, R> apply(Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> f) {
            return v11.apply(v10.apply(v9.apply(v8.apply(v7.apply(v6.apply(v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11)
            ))))))))))));
        }

        public <T12> Builder12<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> combine(Validation<E, T12> v12) {
            return new Builder12<>(v1 ,v2 ,v3 ,v4 ,v5 ,v6 ,v7 ,v8 ,v9 ,v10 ,v11 ,v12);
        }
    }

    static final class Builder12<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;
        private Validation<E, T8> v8;
        private Validation<E, T9> v9;
        private Validation<E, T10> v10;
        private Validation<E, T11> v11;
        private Validation<E, T12> v12;

        Builder12(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8, Validation<E, T9> v9, Validation<E, T10> v10, Validation<E, T11> v11, Validation<E, T12> v12) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
            this.v9 = v9;
            this.v10 = v10;
            this.v11 = v11;
            this.v12 = v12;

        }

        public <R> Validation<E, R> apply(Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> f) {
            return v12.apply(v11.apply(v10.apply(v9.apply(v8.apply(v7.apply(v6.apply(v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12)
            )))))))))))));
        }

        public <T13> Builder13<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> combine(Validation<E, T13> v13) {
            return new Builder13<>(v1 ,v2 ,v3 ,v4 ,v5 ,v6 ,v7 ,v8 ,v9 ,v10 ,v11 ,v12 ,v13);
        }
    }

    static final class Builder13<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;
        private Validation<E, T8> v8;
        private Validation<E, T9> v9;
        private Validation<E, T10> v10;
        private Validation<E, T11> v11;
        private Validation<E, T12> v12;
        private Validation<E, T13> v13;

        Builder13(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8, Validation<E, T9> v9, Validation<E, T10> v10, Validation<E, T11> v11, Validation<E, T12> v12, Validation<E, T13> v13) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
            this.v9 = v9;
            this.v10 = v10;
            this.v11 = v11;
            this.v12 = v12;
            this.v13 = v13;

        }

        public <R> Validation<E, R> apply(Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> f) {
            return v13.apply(v12.apply(v11.apply(v10.apply(v9.apply(v8.apply(v7.apply(v6.apply(v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> t13 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13)
            ))))))))))))));
        }

        public <T14> Builder14<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> combine(Validation<E, T14> v14) {
            return new Builder14<>(v1 ,v2 ,v3 ,v4 ,v5 ,v6 ,v7 ,v8 ,v9 ,v10 ,v11 ,v12 ,v13 ,v14);
        }
    }

    static final class Builder14<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;
        private Validation<E, T8> v8;
        private Validation<E, T9> v9;
        private Validation<E, T10> v10;
        private Validation<E, T11> v11;
        private Validation<E, T12> v12;
        private Validation<E, T13> v13;
        private Validation<E, T14> v14;

        Builder14(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8, Validation<E, T9> v9, Validation<E, T10> v10, Validation<E, T11> v11, Validation<E, T12> v12, Validation<E, T13> v13, Validation<E, T14> v14) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
            this.v9 = v9;
            this.v10 = v10;
            this.v11 = v11;
            this.v12 = v12;
            this.v13 = v13;
            this.v14 = v14;

        }

        public <R> Validation<E, R> apply(Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R> f) {
            return v14.apply(v13.apply(v12.apply(v11.apply(v10.apply(v9.apply(v8.apply(v7.apply(v6.apply(v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> t13 -> t14 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14)
            )))))))))))))));
        }

        public <T15> Builder15<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> combine(Validation<E, T15> v15) {
            return new Builder15<>(v1 ,v2 ,v3 ,v4 ,v5 ,v6 ,v7 ,v8 ,v9 ,v10 ,v11 ,v12 ,v13 ,v14 ,v15);
        }
    }

    static final class Builder15<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;
        private Validation<E, T8> v8;
        private Validation<E, T9> v9;
        private Validation<E, T10> v10;
        private Validation<E, T11> v11;
        private Validation<E, T12> v12;
        private Validation<E, T13> v13;
        private Validation<E, T14> v14;
        private Validation<E, T15> v15;

        Builder15(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8, Validation<E, T9> v9, Validation<E, T10> v10, Validation<E, T11> v11, Validation<E, T12> v12, Validation<E, T13> v13, Validation<E, T14> v14, Validation<E, T15> v15) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
            this.v9 = v9;
            this.v10 = v10;
            this.v11 = v11;
            this.v12 = v12;
            this.v13 = v13;
            this.v14 = v14;
            this.v15 = v15;

        }

        public <R> Validation<E, R> apply(Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R> f) {
            return v15.apply(v14.apply(v13.apply(v12.apply(v11.apply(v10.apply(v9.apply(v8.apply(v7.apply(v6.apply(v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> t13 -> t14 -> t15 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15)
            ))))))))))))))));
        }

        public <T16> Builder16<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> combine(Validation<E, T16> v16) {
            return new Builder16<>(v1 ,v2 ,v3 ,v4 ,v5 ,v6 ,v7 ,v8 ,v9 ,v10 ,v11 ,v12 ,v13 ,v14 ,v15 ,v16);
        }
    }

    static final class Builder16<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;
        private Validation<E, T8> v8;
        private Validation<E, T9> v9;
        private Validation<E, T10> v10;
        private Validation<E, T11> v11;
        private Validation<E, T12> v12;
        private Validation<E, T13> v13;
        private Validation<E, T14> v14;
        private Validation<E, T15> v15;
        private Validation<E, T16> v16;

        Builder16(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8, Validation<E, T9> v9, Validation<E, T10> v10, Validation<E, T11> v11, Validation<E, T12> v12, Validation<E, T13> v13, Validation<E, T14> v14, Validation<E, T15> v15, Validation<E, T16> v16) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
            this.v9 = v9;
            this.v10 = v10;
            this.v11 = v11;
            this.v12 = v12;
            this.v13 = v13;
            this.v14 = v14;
            this.v15 = v15;
            this.v16 = v16;

        }

        public <R> Validation<E, R> apply(Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> f) {
            return v16.apply(v15.apply(v14.apply(v13.apply(v12.apply(v11.apply(v10.apply(v9.apply(v8.apply(v7.apply(v6.apply(v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> t13 -> t14 -> t15 -> t16 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16)
            )))))))))))))))));
        }

        public <T17> Builder17<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> combine(Validation<E, T17> v17) {
            return new Builder17<>(v1 ,v2 ,v3 ,v4 ,v5 ,v6 ,v7 ,v8 ,v9 ,v10 ,v11 ,v12 ,v13 ,v14 ,v15 ,v16 ,v17);
        }
    }

    static final class Builder17<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;
        private Validation<E, T8> v8;
        private Validation<E, T9> v9;
        private Validation<E, T10> v10;
        private Validation<E, T11> v11;
        private Validation<E, T12> v12;
        private Validation<E, T13> v13;
        private Validation<E, T14> v14;
        private Validation<E, T15> v15;
        private Validation<E, T16> v16;
        private Validation<E, T17> v17;

        Builder17(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8, Validation<E, T9> v9, Validation<E, T10> v10, Validation<E, T11> v11, Validation<E, T12> v12, Validation<E, T13> v13, Validation<E, T14> v14, Validation<E, T15> v15, Validation<E, T16> v16, Validation<E, T17> v17) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
            this.v9 = v9;
            this.v10 = v10;
            this.v11 = v11;
            this.v12 = v12;
            this.v13 = v13;
            this.v14 = v14;
            this.v15 = v15;
            this.v16 = v16;
            this.v17 = v17;

        }

        public <R> Validation<E, R> apply(Function17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R> f) {
            return v17.apply(v16.apply(v15.apply(v14.apply(v13.apply(v12.apply(v11.apply(v10.apply(v9.apply(v8.apply(v7.apply(v6.apply(v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> t13 -> t14 -> t15 -> t16 -> t17 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17)
            ))))))))))))))))));
        }

        public <T18> Builder18<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> combine(Validation<E, T18> v18) {
            return new Builder18<>(v1 ,v2 ,v3 ,v4 ,v5 ,v6 ,v7 ,v8 ,v9 ,v10 ,v11 ,v12 ,v13 ,v14 ,v15 ,v16 ,v17 ,v18);
        }
    }

    static final class Builder18<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;
        private Validation<E, T8> v8;
        private Validation<E, T9> v9;
        private Validation<E, T10> v10;
        private Validation<E, T11> v11;
        private Validation<E, T12> v12;
        private Validation<E, T13> v13;
        private Validation<E, T14> v14;
        private Validation<E, T15> v15;
        private Validation<E, T16> v16;
        private Validation<E, T17> v17;
        private Validation<E, T18> v18;

        Builder18(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8, Validation<E, T9> v9, Validation<E, T10> v10, Validation<E, T11> v11, Validation<E, T12> v12, Validation<E, T13> v13, Validation<E, T14> v14, Validation<E, T15> v15, Validation<E, T16> v16, Validation<E, T17> v17, Validation<E, T18> v18) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
            this.v9 = v9;
            this.v10 = v10;
            this.v11 = v11;
            this.v12 = v12;
            this.v13 = v13;
            this.v14 = v14;
            this.v15 = v15;
            this.v16 = v16;
            this.v17 = v17;
            this.v18 = v18;

        }

        public <R> Validation<E, R> apply(Function18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R> f) {
            return v18.apply(v17.apply(v16.apply(v15.apply(v14.apply(v13.apply(v12.apply(v11.apply(v10.apply(v9.apply(v8.apply(v7.apply(v6.apply(v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> t13 -> t14 -> t15 -> t16 -> t17 -> t18 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18)
            )))))))))))))))))));
        }

        public <T19> Builder19<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> combine(Validation<E, T19> v19) {
            return new Builder19<>(v1 ,v2 ,v3 ,v4 ,v5 ,v6 ,v7 ,v8 ,v9 ,v10 ,v11 ,v12 ,v13 ,v14 ,v15 ,v16 ,v17 ,v18 ,v19);
        }
    }

    static final class Builder19<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;
        private Validation<E, T8> v8;
        private Validation<E, T9> v9;
        private Validation<E, T10> v10;
        private Validation<E, T11> v11;
        private Validation<E, T12> v12;
        private Validation<E, T13> v13;
        private Validation<E, T14> v14;
        private Validation<E, T15> v15;
        private Validation<E, T16> v16;
        private Validation<E, T17> v17;
        private Validation<E, T18> v18;
        private Validation<E, T19> v19;

        Builder19(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8, Validation<E, T9> v9, Validation<E, T10> v10, Validation<E, T11> v11, Validation<E, T12> v12, Validation<E, T13> v13, Validation<E, T14> v14, Validation<E, T15> v15, Validation<E, T16> v16, Validation<E, T17> v17, Validation<E, T18> v18, Validation<E, T19> v19) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
            this.v9 = v9;
            this.v10 = v10;
            this.v11 = v11;
            this.v12 = v12;
            this.v13 = v13;
            this.v14 = v14;
            this.v15 = v15;
            this.v16 = v16;
            this.v17 = v17;
            this.v18 = v18;
            this.v19 = v19;

        }

        public <R> Validation<E, R> apply(Function19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R> f) {
            return v19.apply(v18.apply(v17.apply(v16.apply(v15.apply(v14.apply(v13.apply(v12.apply(v11.apply(v10.apply(v9.apply(v8.apply(v7.apply(v6.apply(v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> t13 -> t14 -> t15 -> t16 -> t17 -> t18 -> t19 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19)
            ))))))))))))))))))));
        }

        public <T20> Builder20<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> combine(Validation<E, T20> v20) {
            return new Builder20<>(v1 ,v2 ,v3 ,v4 ,v5 ,v6 ,v7 ,v8 ,v9 ,v10 ,v11 ,v12 ,v13 ,v14 ,v15 ,v16 ,v17 ,v18 ,v19 ,v20);
        }
    }

    static final class Builder20<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;
        private Validation<E, T8> v8;
        private Validation<E, T9> v9;
        private Validation<E, T10> v10;
        private Validation<E, T11> v11;
        private Validation<E, T12> v12;
        private Validation<E, T13> v13;
        private Validation<E, T14> v14;
        private Validation<E, T15> v15;
        private Validation<E, T16> v16;
        private Validation<E, T17> v17;
        private Validation<E, T18> v18;
        private Validation<E, T19> v19;
        private Validation<E, T20> v20;

        Builder20(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8, Validation<E, T9> v9, Validation<E, T10> v10, Validation<E, T11> v11, Validation<E, T12> v12, Validation<E, T13> v13, Validation<E, T14> v14, Validation<E, T15> v15, Validation<E, T16> v16, Validation<E, T17> v17, Validation<E, T18> v18, Validation<E, T19> v19, Validation<E, T20> v20) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
            this.v9 = v9;
            this.v10 = v10;
            this.v11 = v11;
            this.v12 = v12;
            this.v13 = v13;
            this.v14 = v14;
            this.v15 = v15;
            this.v16 = v16;
            this.v17 = v17;
            this.v18 = v18;
            this.v19 = v19;
            this.v20 = v20;

        }

        public <R> Validation<E, R> apply(Function20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R> f) {
            return v20.apply(v19.apply(v18.apply(v17.apply(v16.apply(v15.apply(v14.apply(v13.apply(v12.apply(v11.apply(v10.apply(v9.apply(v8.apply(v7.apply(v6.apply(v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> t13 -> t14 -> t15 -> t16 -> t17 -> t18 -> t19 -> t20 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20)
            )))))))))))))))))))));
        }

        public <T21> Builder21<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> combine(Validation<E, T21> v21) {
            return new Builder21<>(v1 ,v2 ,v3 ,v4 ,v5 ,v6 ,v7 ,v8 ,v9 ,v10 ,v11 ,v12 ,v13 ,v14 ,v15 ,v16 ,v17 ,v18 ,v19 ,v20 ,v21);
        }
    }

    static final class Builder21<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;
        private Validation<E, T8> v8;
        private Validation<E, T9> v9;
        private Validation<E, T10> v10;
        private Validation<E, T11> v11;
        private Validation<E, T12> v12;
        private Validation<E, T13> v13;
        private Validation<E, T14> v14;
        private Validation<E, T15> v15;
        private Validation<E, T16> v16;
        private Validation<E, T17> v17;
        private Validation<E, T18> v18;
        private Validation<E, T19> v19;
        private Validation<E, T20> v20;
        private Validation<E, T21> v21;

        Builder21(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8, Validation<E, T9> v9, Validation<E, T10> v10, Validation<E, T11> v11, Validation<E, T12> v12, Validation<E, T13> v13, Validation<E, T14> v14, Validation<E, T15> v15, Validation<E, T16> v16, Validation<E, T17> v17, Validation<E, T18> v18, Validation<E, T19> v19, Validation<E, T20> v20, Validation<E, T21> v21) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
            this.v9 = v9;
            this.v10 = v10;
            this.v11 = v11;
            this.v12 = v12;
            this.v13 = v13;
            this.v14 = v14;
            this.v15 = v15;
            this.v16 = v16;
            this.v17 = v17;
            this.v18 = v18;
            this.v19 = v19;
            this.v20 = v20;
            this.v21 = v21;

        }

        public <R> Validation<E, R> apply(Function21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R> f) {
            return v21.apply(v20.apply(v19.apply(v18.apply(v17.apply(v16.apply(v15.apply(v14.apply(v13.apply(v12.apply(v11.apply(v10.apply(v9.apply(v8.apply(v7.apply(v6.apply(v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> t13 -> t14 -> t15 -> t16 -> t17 -> t18 -> t19 -> t20 -> t21 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21)
            ))))))))))))))))))))));
        }

        public <T22> Builder22<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> combine(Validation<E, T22> v22) {
            return new Builder22<>(v1 ,v2 ,v3 ,v4 ,v5 ,v6 ,v7 ,v8 ,v9 ,v10 ,v11 ,v12 ,v13 ,v14 ,v15 ,v16 ,v17 ,v18 ,v19 ,v20 ,v21 ,v22);
        }
    }

    static final class Builder22<E, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> {
        private Validation<E, T1> v1;
        private Validation<E, T2> v2;
        private Validation<E, T3> v3;
        private Validation<E, T4> v4;
        private Validation<E, T5> v5;
        private Validation<E, T6> v6;
        private Validation<E, T7> v7;
        private Validation<E, T8> v8;
        private Validation<E, T9> v9;
        private Validation<E, T10> v10;
        private Validation<E, T11> v11;
        private Validation<E, T12> v12;
        private Validation<E, T13> v13;
        private Validation<E, T14> v14;
        private Validation<E, T15> v15;
        private Validation<E, T16> v16;
        private Validation<E, T17> v17;
        private Validation<E, T18> v18;
        private Validation<E, T19> v19;
        private Validation<E, T20> v20;
        private Validation<E, T21> v21;
        private Validation<E, T22> v22;

        Builder22(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3, Validation<E, T4> v4, Validation<E, T5> v5, Validation<E, T6> v6, Validation<E, T7> v7, Validation<E, T8> v8, Validation<E, T9> v9, Validation<E, T10> v10, Validation<E, T11> v11, Validation<E, T12> v12, Validation<E, T13> v13, Validation<E, T14> v14, Validation<E, T15> v15, Validation<E, T16> v16, Validation<E, T17> v17, Validation<E, T18> v18, Validation<E, T19> v19, Validation<E, T20> v20, Validation<E, T21> v21, Validation<E, T22> v22) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
            this.v5 = v5;
            this.v6 = v6;
            this.v7 = v7;
            this.v8 = v8;
            this.v9 = v9;
            this.v10 = v10;
            this.v11 = v11;
            this.v12 = v12;
            this.v13 = v13;
            this.v14 = v14;
            this.v15 = v15;
            this.v16 = v16;
            this.v17 = v17;
            this.v18 = v18;
            this.v19 = v19;
            this.v20 = v20;
            this.v21 = v21;
            this.v22 = v22;

        }

        public <R> Validation<E, R> apply(Function22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R> f) {
            return v22.apply(v21.apply(v20.apply(v19.apply(v18.apply(v17.apply(v16.apply(v15.apply(v14.apply(v13.apply(v12.apply(v11.apply(v10.apply(v9.apply(v8.apply(v7.apply(v6.apply(v5.apply(v4.apply(v3.apply(v2.apply(v1.apply(valid(
                    t1 -> t2 -> t3 -> t4 -> t5 -> t6 -> t7 -> t8 -> t9 -> t10 -> t11 -> t12 -> t13 -> t14 -> t15 -> t16 -> t17 -> t18 -> t19 -> t20 -> t21 -> t22 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22)
            )))))))))))))))))))))));
        }
    }
}
