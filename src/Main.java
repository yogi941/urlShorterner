package src;

/**
 * Entry point for the URL Shortener application.
 *
 * How to compile and run:
 * ─────────────────────────────────────────────────────────────────────
 *   javac -cp .;mysql-connector-j.jar src/*.java
 *   java  -cp .;mysql-connector-j.jar src.Main
 * ─────────────────────────────────────────────────────────────────────
 *
 * On Linux/Mac, replace ; with : in the classpath.
 *
 * Make sure MySQL is running and the database is created.
 * See DBConnection.java for the SQL schema.
 */
public class Main {

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.start();
    }
}
