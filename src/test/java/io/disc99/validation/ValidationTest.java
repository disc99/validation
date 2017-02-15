package io.disc99.validation;

import org.junit.Test;

import java.util.*;
import java.util.function.Function;

import static io.disc99.validation.Validation.invalid;
import static io.disc99.validation.Validation.valid;
import static io.disc99.validation.Validations.isNotEmpty;
import static io.disc99.validation.Validations.pattern;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import static org.assertj.core.api.Assertions.assertThat;

public class ValidationTest {

    @Test
    public void shouldBuildUpForSuccessCombine() {
        Validation<String, String> v1 = valid("John Doe");
        Validation<String, Integer> v2 = valid(39);
        Validation<String, Optional<String>> v3 = valid(Optional.of("address"));
        Validation<String, Optional<String>> v4 = valid(Optional.empty());
        Validation<String, String> v5 = valid("111-111-1111");
        Validation<String, String> v6 = valid("alt1");
        Validation<String, String> v7 = valid("alt2");
        Validation<String, String> v8 = valid("alt3");
        Validation<String, String> v9 = valid("alt4");

        Validation<String, TestValidation> result = v1.combine(v2).ap(TestValidation::new);

        Validation<String, TestValidation> result2 = v1.combine(v2).combine(v3).ap(TestValidation::new);
        Validation<String, TestValidation> result3 = v1.combine(v2).combine(v4).ap(TestValidation::new);

        Validation<String, TestValidation> result4 = v1.combine(v2).combine(v3).combine(v5).ap(TestValidation::new);
        Validation<String, TestValidation> result5 = v1.combine(v2).combine(v3).combine(v5).combine(v6).ap(TestValidation::new);
        Validation<String, TestValidation> result6 = v1.combine(v2).combine(v3).combine(v5).combine(v6).combine(v7).ap(TestValidation::new);
        Validation<String, TestValidation> result7 = v1.combine(v2).combine(v3).combine(v5).combine(v6).combine(v7).combine(v8).ap(TestValidation::new);
        Validation<String, TestValidation> result8 = v1.combine(v2).combine(v3).combine(v5).combine(v6).combine(v7).combine(v8).combine(v9).ap(TestValidation::new);

        Validation<String, String> result9 = v1.combine(v2).combine(v3).ap((p1, p2, p3) -> p1 + ":" + p2 + ":" + p3.orElse("none"));

        assertThat(result.isValid()).isTrue();
        assertThat(result2.isValid()).isTrue();
        assertThat(result3.isValid()).isTrue();
        assertThat(result4.isValid()).isTrue();
        assertThat(result5.isValid()).isTrue();
        assertThat(result6.isValid()).isTrue();
        assertThat(result7.isValid()).isTrue();
        assertThat(result8.isValid()).isTrue();
        assertThat(result9.isValid()).isTrue();

        assertThat(result.get() instanceof TestValidation).isTrue();
        assertThat(result9.get() instanceof String).isTrue();
    }

    @Test
    public void shouldBuildUpForSuccessMapN() {
        Validation<String, String> v1 = valid("John Doe");
        Validation<String, Integer> v2 = valid(39);
        Validation<String, Optional<String>> v3 = valid(Optional.of("address"));
        Validation<String, Optional<String>> v4 = valid(Optional.empty());
        Validation<String, String> v5 = valid("111-111-1111");
        Validation<String, String> v6 = valid("alt1");
        Validation<String, String> v7 = valid("alt2");
        Validation<String, String> v8 = valid("alt3");
        Validation<String, String> v9 = valid("alt4");

        // Alternative map(n) functions to the 'combine' function
        Validation<String, TestValidation> result = Validation.combine(v1, v2).ap(TestValidation::new);
        Validation<String, TestValidation> result2 = Validation.combine(v1, v2, v3).ap(TestValidation::new);
        Validation<String, TestValidation> result3 = Validation.combine(v1, v2, v4).ap(TestValidation::new);
        Validation<String, TestValidation> result4 = Validation.combine(v1, v2, v3, v5).ap(TestValidation::new);
        Validation<String, TestValidation> result5 = Validation.combine(v1, v2, v3, v5, v6).ap(TestValidation::new);
        Validation<String, TestValidation> result6 = Validation.combine(v1, v2, v3, v5, v6, v7).ap(TestValidation::new);
        Validation<String, TestValidation> result7 = Validation.combine(v1, v2, v3, v5, v6, v7, v8).ap(TestValidation::new);
        Validation<String, TestValidation> result8 = Validation.combine(v1, v2, v3, v5, v6, v7, v8, v9).ap(TestValidation::new);

        Validation<String, String> result9 = Validation.combine(v1, v2, v3).ap((p1, p2, p3) -> p1 + ":" + p2 + ":" + p3.orElse("none"));

        assertThat(result.isValid()).isTrue();
        assertThat(result2.isValid()).isTrue();
        assertThat(result3.isValid()).isTrue();
        assertThat(result4.isValid()).isTrue();
        assertThat(result5.isValid()).isTrue();
        assertThat(result6.isValid()).isTrue();
        assertThat(result7.isValid()).isTrue();
        assertThat(result8.isValid()).isTrue();
        assertThat(result9.isValid()).isTrue();

        assertThat(result.get() instanceof TestValidation).isTrue();
        assertThat(result9.get() instanceof String).isTrue();
    }

    @Test
    public void shouldValidateValidPerson() {
        final String name = "John Doe";
        final int age = 30;
        final Validation<String, Person> actual = new PersonValidator().validatePerson(name, age);
        final Validation<String, Person> expected = valid(new Person(name, age));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldValidateInvalidPerson() {
        final String name = "John? Doe!4";
        final int age = -1;
        final Validation<String, Person> actual = new PersonValidator().validatePerson(name, age);
        final Validation<String, Person> expected = invalid(Arrays.asList(
                "Name contains invalid characters: '[!, 4, ?]'",
                "Age must be greater than 0"
        ));
        assertThat(actual).isEqualTo(expected);
    }

    public static class TestValidation {
        public String name;
        public Integer age;
        public Optional<String> address;
        public String phone;
        public String alt1;
        public String alt2;
        public String alt3;
        public String alt4;

        public TestValidation(String name, Integer age) {
            this.name = name;
            this.age = age;
            address = Optional.empty();
        }

        public TestValidation(String name, Integer age, Optional<String> address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }

        public TestValidation(String name, Integer age, Optional<String> address, String phone) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.phone = phone;
        }

        public TestValidation(String name, Integer age, Optional<String> address, String phone, String alt1) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.phone = phone;
            this.alt1 = alt1;
        }

        public TestValidation(String name, Integer age, Optional<String> address, String phone, String alt1, String alt2) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.phone = phone;
            this.alt1 = alt1;
            this.alt2 = alt2;
        }

        public TestValidation(String name, Integer age, Optional<String> address, String phone, String alt1, String alt2, String alt3) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.phone = phone;
            this.alt1 = alt1;
            this.alt2 = alt2;
            this.alt3 = alt3;
        }

        public TestValidation(String name, Integer age, Optional<String> address, String phone, String alt1, String alt2, String alt3, String alt4) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.phone = phone;
            this.alt1 = alt1;
            this.alt2 = alt2;
            this.alt3 = alt3;
            this.alt4 = alt4;
        }

        @Override
        public String toString() {
            return "TestValidation(" + name + "," + age + "," + address.orElse("none") + phone + "," + ")";
        }
    }

    static class PersonValidator {

        private final String validNameChars = "[a-zA-Z ]";
        private final int minAge = 0;

        public Validation<String, Person> validatePerson(String name, int age) {
            return Validation.combine(validateName(name), validateAge(age)).ap(Person::new);
        }

        private Validation<String, String> validateName(String name) {
            return Arrays.stream(name.split(""))
                    .filter(n -> !n.replaceAll(validNameChars, "").isEmpty())
                    .collect(collectingAndThen(toCollection(TreeSet::new),
                            invalid -> invalid.isEmpty()
                                    ? valid(name)
                                    : invalid("Name contains invalid characters: '" + invalid + "'")));
        }

        private Validation<String, Integer> validateAge(int age) {
            return (age < minAge) ? invalid("Age must be greater than 0") : valid(age);
        }
    }

    static class Person {

        public final String name;
        public final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Person) {
                final Person person = (Person) o;
                return Objects.equals(name, person.name) && age == person.age;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }

        @Override
        public String toString() {
            return "Person(" + name + ", " + age + ")";
        }
    }

    static class SignUpForm {
        String name;
        String pass;
        String confirmPass;
        SignUpForm(String name, String pass, String confirmPass) {
            this.name = name;
            this.pass = pass;
            this.confirmPass = confirmPass;
        }
    }
    static class User {
        String name;
        String pass;
        User(String name, String pass) {
            this.name = name;
            this.pass = pass;
        }
    }

    @Test
    public void shouldValidateInvalidPersonWithOrder() throws Exception {
        SignUpForm f1 = new SignUpForm("@Tom", "pass",null);
        SignUpForm f2 = new SignUpForm("@Tom", "pass","pass");
        SignUpForm f3 = new SignUpForm("@Tom", "ok_pass","ng_pass");
        SignUpForm f4 = new SignUpForm("Tom", "ok_pass","ok_pass");

        FormValidator validator = new FormValidator();

        Validation<String, User> v1 = validator.validFrom(f1);
        Validation<String, User> v2 = validator.validFrom(f2);
        Validation<String, User> v3 = validator.validFrom(f3);
        Validation<String, User> v4 = validator.validFrom(f4);
    }

    static class FormValidator {
        Validation<String, User> validFrom(SignUpForm form) {
            return Validation.combine(
                    validName(form.name),
                    validPassword(form.pass, form.confirmPass)
            ).ap(User::new);
        }

        Validation<String, String> validName(String name) {
            return isNotEmpty(name);
        }

        Function<String, Validation<String, String>> passPattern = p -> pattern(p, "[A-Za-z_]");

        Validation<String, String> validPassword(String pass, String confirmPass) {
            return Validation.zip(
                    isNotEmpty(pass).flatMap(passPattern),
                    isNotEmpty(confirmPass).flatMap(passPattern),
                    (p1, p2) -> p1.equals(p2) ? valid(p1) : invalid("not match the confirmation password")
            );
        }
    }
}
//
//
//interface V<E, T> {
//    static <E, T> V<E, T> valid(T value) {
//        return new Vd<>(value);
//    }
//    static <E, T> V<E, T> invalid(E... errors) {
//        return new Ivd<>(Arrays.asList(errors));
//    }
//
//    T getValid();
//    List<E> getInvalid();
//
//    static class Vd<E, T> implements V<E, T> {
//        T value;
//        Vd(T value) {
//            this.value = value;
//        }
//
//        @Override
//        public T getValid() {
//            return value;
//        }
//
//        @Override
//        public List<E> getInvalid() {
//            throw new NoSuchElementException();
//        }
//    }
//    static class Ivd<E, T> implements V<E, T> {
//        List<E> errors;
//        Ivd(List<E> errors) {
//            this.errors = errors;
//        }
//
//        @Override
//        public T getValid() {
//            throw new NoSuchElementException();
//        }
//
//        @Override
//        public List<E> getInvalid() {
//            return null;
//        }
//    }
//}