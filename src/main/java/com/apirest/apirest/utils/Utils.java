package com.apirest.apirest.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.UUID;

public class Utils {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d.*\\d)[A-Za-z\\d]{4,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    public static boolean validarPassword(String password) {

        boolean validacion = false;

        try {
            Matcher matcher = pattern.matcher(password);
            validacion = matcher.matches();

        } catch (java.lang.NumberFormatException e) {
        } catch (Exception e) {
        }
        return validacion;
    }

        public static UUID crearUUID() {
            UUID uuid = UUID.randomUUID();
            return uuid;

        }
}
