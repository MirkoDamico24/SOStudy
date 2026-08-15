package it.uniroma2.dicii.ispw.sostudy.application;

import org.mindrot.jbcrypt.BCrypt;

public class PasswdHelper {

    private PasswdHelper() {}

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            return false;
        }
        return BCrypt.checkpw(password, hashedPassword);
    }

}