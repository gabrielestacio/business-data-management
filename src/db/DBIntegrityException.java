package db;

import java.io.Serial;

public class DBIntegrityException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public DBIntegrityException(String message){
        super(STR."There's a problem with the database integrity: \{message}");
    }
}
