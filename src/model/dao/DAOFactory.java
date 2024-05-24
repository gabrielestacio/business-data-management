package model.dao;

import db.DB;
import model.dao.impl.SellerDAOJDBC;

public class DAOFactory {
    private DAOFactory() {
    }

    public static SellerDAO createSellerDAO(){
        return new SellerDAOJDBC(DB.getConnection());
    }
}
