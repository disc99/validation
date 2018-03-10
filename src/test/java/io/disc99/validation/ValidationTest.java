package io.disc99.validation;

import org.junit.Test;

import java.util.*;

import static io.disc99.validation.Validation.*;
import static java.util.function.Function.identity;
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

        Validation<String, TestValidation> result = v1.combine(v2).apply(TestValidation::new);

        Validation<String, TestValidation> result2 = v1.combine(v2).combine(v3).apply(TestValidation::new);
        Validation<String, TestValidation> result3 = v1.combine(v2).combine(v4).apply(TestValidation::new);

        Validation<String, TestValidation> result4 = v1.combine(v2).combine(v3).combine(v5).apply(TestValidation::new);
        Validation<String, TestValidation> result5 = v1.combine(v2).combine(v3).combine(v5).combine(v6).apply(TestValidation::new);
        Validation<String, TestValidation> result6 = v1.combine(v2).combine(v3).combine(v5).combine(v6).combine(v7).apply(TestValidation::new);
        Validation<String, TestValidation> result7 = v1.combine(v2).combine(v3).combine(v5).combine(v6).combine(v7).combine(v8).apply(TestValidation::new);
        Validation<String, TestValidation> result8 = v1.combine(v2).combine(v3).combine(v5).combine(v6).combine(v7).combine(v8).combine(v9).apply(TestValidation::new);

        Validation<String, String> result9 = v1.combine(v2).combine(v3).apply((p1, p2, p3) -> p1 + ":" + p2 + ":" + p3.orElse("none"));

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
        Validation<String, TestValidation> result = combine(v1, v2).apply(TestValidation::new);
        Validation<String, TestValidation> result2 = combine(v1, v2, v3).apply(TestValidation::new);
        Validation<String, TestValidation> result3 = combine(v1, v2, v4).apply(TestValidation::new);
        Validation<String, TestValidation> result4 = combine(v1, v2, v3, v5).apply(TestValidation::new);
        Validation<String, TestValidation> result5 = combine(v1, v2, v3, v5, v6).apply(TestValidation::new);
        Validation<String, TestValidation> result6 = combine(v1, v2, v3, v5, v6, v7).apply(TestValidation::new);
        Validation<String, TestValidation> result7 = combine(v1, v2, v3, v5, v6, v7, v8).apply(TestValidation::new);
        Validation<String, TestValidation> result8 = combine(v1, v2, v3, v5, v6, v7, v8, v9).apply(TestValidation::new);

        Validation<String, String> result9 = combine(v1, v2, v3).apply((p1, p2, p3) -> p1 + ":" + p2 + ":" + p3.orElse("none"));

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
    public void shouldBuildUpForSuccessAccumulate() {
        Validation<String, String> v1 = valid("John Doe");
        Validation<String, Integer> v2 = valid(39);
        Validation<String, Optional<String>> v3 = valid(Optional.of("address"));
        Validation<String, Optional<String>> v4 = valid(Optional.empty());
        Validation<String, String> v5 = valid("111-111-1111");
        Validation<String, String> v6 = valid("alt1");
        Validation<String, String> v7 = valid("alt2");
        Validation<String, String> v8 = valid("alt3");
        Validation<String, String> v9 = valid("alt4");

        Validation<String, TestValidation> result =  v1.accumulate(v2, TestValidation::new);
        Validation<String, TestValidation> result2 = v1.accumulate(v2, v3, TestValidation::new);
        Validation<String, TestValidation> result3 = v1.accumulate(v2, v4, TestValidation::new);
        Validation<String, TestValidation> result4 = v1.accumulate(v2, v3, v5, TestValidation::new);
        Validation<String, TestValidation> result5 = v1.accumulate(v2, v3, v5, v6, TestValidation::new);
        Validation<String, TestValidation> result6 = v1.accumulate(v2, v3, v5, v6, v7, TestValidation::new);
        Validation<String, TestValidation> result7 = v1.accumulate(v2, v3, v5, v6, v7, v8, TestValidation::new);
        Validation<String, TestValidation> result8 = v1.accumulate(v2, v3, v5, v6, v7, v8, v9, TestValidation::new);

        Validation<String, String> result9 = v1.accumulate(v2, v3, (p1, p2, p3) -> p1 + ":" + p2 + ":" + p3.orElse("none"));

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
            return combine(validateName(name), validateAge(age)).apply(Person::new);
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
        UserName name;
        UserPassword pass;
        User(UserName name, UserPassword pass) {
            this.name = name;
            this.pass = pass;
        }
    }

    static class UserName {
        String name;

        public UserName(String name) {
            this.name = name;
        }
    }
    static class UserPassword {
        String pass;

        public UserPassword(String pass) {
            this.pass = pass;
        }
    }

    @Test
    public void shouldValidateInvalidPersonWithOrder() throws Exception {
        SignUpForm f1 = new SignUpForm("", "pass",null);
        SignUpForm f2 = new SignUpForm("", "pass","pass");
        SignUpForm f3 = new SignUpForm("", "ok_pass","ng_pass");
        SignUpForm f4 = new SignUpForm("Tom", "ok_pass","ok_pass");

        FormValidator validator = new FormValidator();

        Validation<String, User> v1 = validator.validFrom(f1);
        Validation<String, User> v2 = validator.validFrom(f2);
        Validation<String, User> v3 = validator.validFrom(f3);
        Validation<String, User> v4 = validator.validFrom(f4);

        assertThat(v1.isInvalid()).isTrue();
        assertThat(v1.getViolations()).hasSize(3);
        assertThat(v2.isInvalid()).isTrue();
        assertThat(v2.getViolations()).hasSize(3);
        assertThat(v3.isInvalid()).isTrue();
        assertThat(v3.getViolations()).hasSize(2);
        assertThat(v4.isValid()).isTrue();
    }

    static class FormValidator implements Validator {
        Validation<String, User> validFrom(SignUpForm form) {
            return combine(
                    validName(form.name),
                    validPassword(form.pass, form.confirmPass)
            ).apply(User::new);
        }

        Validation<String, User> validFrom2(SignUpForm form) {
            return combine(
                    required(form.name),
                    required(form.pass),
                    length(form.pass, 6, 20),
                    pattern(form.pass, "[a-zA-Z_]*"),
                    required(form.confirmPass),
                    equal(form.pass, form.confirmPass)
            ).apply((name, pass, _1, _2, _3, _4) -> new User(new UserName(name), new UserPassword(pass)));
        }

        Validation<String, User> validFrom3(SignUpForm form) {
            return combine(
                    required(form.name).map(UserName::new),
                    combine(
                            required(form.pass).flatMap(pass -> combine(
                                    length(pass, 6, 20),
                                    pattern(pass, "[a-zA-Z_]*")
                            ).apply((_1, _2) -> _1)),
                            required(form.confirmPass)
                    ).apply(this::equal).flatMap(identity()).map(UserPassword::new)
            ).apply(User::new);
        }

        Validation<String, User> validFrom4(SignUpForm form) {
            return combine(
                    required(form.name).map(UserName::new),
                    combine(
                            required(form.pass)
                                    .flatMap(pass -> length(pass, 6, 20))
                                    .flatMap(pass -> pattern(pass, "[a-zA-Z_]*")),
                            required(form.confirmPass)
                    ).apply(this::equal).flatMap(identity()).map(UserPassword::new)
            ).apply(User::new);
        }

        Validation<String, User> validFrom5(SignUpForm form) {
            return compose(
                    validName(form.name),
                    validPassword(form.pass, form.confirmPass),
                    User::new
            );
        }

        Validation<String, User> validFrom6(SignUpForm form) {
            return compose(
                    required(form.name).map(UserName::new),
                    compose(
                            required(form.pass).flatMap(pass -> length(pass, 6, 20)).flatMap(pass -> pattern(pass, "[a-zA-Z_]*")),
                            required(form.confirmPass),
                            this::equal
                    ).flatMap(identity()).map(UserPassword::new),
                    User::new
            );
        }

        Validation<String, UserName> validName(String name) {
            return notEmpty(name).map(UserName::new);
        }

        Validation<String, UserPassword> validPassword(String pass, String confirmPass) {
            return combine(
                    notEmpty(pass).flatMap(p -> length(p, 6, 20)).flatMap(p -> pattern(p, "[a-zA-Z_]*")),
                    notEmpty(confirmPass)
            ).apply(this::equal).flatMap(identity()).map(UserPassword::new);
        }
    }
}
