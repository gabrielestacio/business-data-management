package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDAOJDBC implements SellerDAO {
    private final Connection connection;

    public SellerDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Seller department) {
        // TODO document why this method is empty
    }

    @Override
    public void update(Seller department) {
        // TODO document why this method is empty
    }

    @Override
    public void deleteById(Integer id) {
        // TODO document why this method is empty
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement statement = null;
        ResultSet result = null;
        try{
            statement = connection.prepareStatement("SELECT seller.*,department.Name as DepName FROM seller " +
                    "INNER JOIN department ON seller.DepartmentId = department.Id WHERE seller.Id = ?"
            );

            statement.setInt(1, id);

            result = statement.executeQuery();
            if(result.next()){
                Department department = new Department();
                department.setId(result.getInt("DepartmentId"));
                department.setName(result.getString("DepName"));

                Seller seller = new Seller();
                seller.setId(result.getInt("Id"));
                seller.setName(result.getString("Name"));
                seller.setEmail(result.getString("Email"));
                seller.setBaseSalary(result.getDouble("BaseSalary"));
                seller.setBirthDate(result.getDate("BirthDate"));
                seller.setDepartment(department);

                return seller;
            }
            return null;
        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(result);
        }
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }
}
