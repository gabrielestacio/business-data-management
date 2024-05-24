package application;

import model.entities.Department;

import java.util.logging.Logger;

public class Program {
    public static void main(String[] args) {
        Department dep = new Department(1, "Books");
        Logger logger = Logger.getLogger(Program.class.getName());

        logger.info(dep.toString());
    }
}
