package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    private Validator validator;
    private UserController uc;
    private User user;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    uc = new UserController();
        user = new User();
        user.setId(0);
        user.setEmail("user@mail.ru");
        user.setName("Пользователь");
        user.setLogin("user");
        user.setBirthday(LocalDate.of(1985, Month.JANUARY, 1));
    }

    @Test
    public void testCreateUserWithEmailDuplicate() {
        uc.create(user);
        User newUser = new User();
        newUser.setEmail("user@mail.ru");
        newUser.setLogin("new-user");
        newUser.setName("Новый пользователь");
        newUser.setBirthday(LocalDate.of(1985, Month.JANUARY, 1));

        assertThrows(DuplicatedDataException.class, () -> uc.create(newUser));
    }

    @Test
    public void testCreateUserWithLoginBlank() {
        user.setLogin("  ");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Логин не может быть пустым и содержать пробелы", violations.iterator().next().getMessage());
    }


    @Test
    public void testCreateUserWithLoginNull() {
        user.setLogin(null);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Логин не может быть пустым и содержать пробелы", violations.iterator().next().getMessage());
    }

    @Test
    public void testCreateUserLoginWithWhitespace() {
        user.setLogin("us er");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Логин не может быть пустым и содержать пробелы", violations.iterator().next().getMessage());
    }

    @Test
    public void testSetLoginInsteadOfNameIfBlank() {
        user.setLogin("user");
        user.setName("   ");

        uc.create(user);

        List<User> users = uc.getAll();
        assertEquals(1, users.size());
        assertEquals("user", users.getFirst().getName());
    }

    @Test
    public void testSetLoginInsteadOfNameIfNull() {
        user.setLogin("user");
        user.setName(null);

        uc.create(user);

        List<User> users = uc.getAll();
        assertEquals(1, users.size());
        assertEquals("user", users.getFirst().getName());
    }

    @Test
    public void testCreateUserBeforeToday() {
        LocalDate date = LocalDate.of(2025, Month.JANUARY, 1);
        user.setBirthday(date);

        uc.create(user);

        List<User> users = uc.getAll();
        assertEquals(1, users.size());
        assertEquals(date, users.getFirst().getBirthday());
    }

    @Test
    public void testCreateUserAfterToday() {
        user.setBirthday(LocalDate.of(2050, Month.JANUARY, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertEquals("Дата рождения не может быть в будущем", violations.iterator().next().getMessage());
    }

    @Test
    public void testUpdateUserNewEmail() throws IllegalAccessException {
        uc.create(user);
        user.setEmail("new@mail.ru");

        uc.update(user);

        List<User> users = uc.getAll();
        assertEquals(1, users.size());
        assertEquals("new@mail.ru", users.getFirst().getEmail());
    }

    @Test
    public void testUpdateUserNewLogin() throws IllegalAccessException {
        uc.create(user);
        user.setLogin("new-user");

        uc.update(user);

        List<User> users = uc.getAll();
        assertEquals(1, users.size());
        assertEquals("new-user", users.getFirst().getLogin());
    }

    @Test
    public void testUpdateUserNewName() throws IllegalAccessException {
        uc.create(user);
        user.setName("Новый пользователь");

        uc.update(user);

        List<User> users = uc.getAll();
        assertEquals(1, users.size());
        assertEquals("Новый пользователь", users.getFirst().getName());
    }

    @Test
    public void testUpdateUserNewBirthday() throws IllegalAccessException {
        uc.create(user);
        LocalDate newDate = LocalDate.of(2025, Month.JANUARY, 1);
        user.setBirthday(newDate);

        uc.update(user);

        List<User> users = uc.getAll();
        assertEquals(1, users.size());
        assertEquals(newDate, users.getFirst().getBirthday());
    }
}
