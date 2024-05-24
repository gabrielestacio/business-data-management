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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDAOJDBC implements SellerDAO {
    private final Connection connection;

    private static final String DEPARTMENT_ID = "DepartmentId";

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
                return instantiateSeller(result, instantiateDepartment(result));
            }
            return null;
        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(result);
        }
    }

    private Seller instantiateSeller(ResultSet result, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(result.getInt("Id"));
        seller.setName(result.getString("Name"));
        seller.setEmail(result.getString("Email"));
        seller.setBaseSalary(result.getDouble("BaseSalary"));
        seller.setBirthDate(result.getDate("BirthDate"));
        seller.setDepartment(department);

        return seller;
    }

    private Department instantiateDepartment(ResultSet result) throws SQLException {
        Department department = new Department();
        department.setId(result.getInt(DEPARTMENT_ID));
        department.setName(result.getString("DepName"));

        return department;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement statement = null;
        ResultSet result = null;
        try{
            statement = connection.prepareStatement("SELECT seller.*,department.Name as DepName FROM seller " +
                    "INNER JOIN department ON seller.DepartmentId = department.Id " +
                    "ORDER BY Name"
            );

            result = statement.executeQuery();
            if(!result.next()){
                return null;
            }

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (result.next()){
                Department temp = map.get(result.getInt(DEPARTMENT_ID));

                if(temp == null){
                    temp = instantiateDepartment(result);
                    map.put(result.getInt(DEPARTMENT_ID), temp);
                }
                sellers.add(instantiateSeller(result, temp));
            }

            return sellers;
        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(result);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement statement = null;
        ResultSet result = null;
        try{
            statement = connection.prepareStatement("SELECT seller.*,department.Name as DepName FROM seller " +
                    "INNER JOIN department ON seller.DepartmentId = department.Id WHERE DepartmentId = ? " +
                    "ORDER BY Name"
            );

            statement.setInt(1, department.getId());

            result = statement.executeQuery();
            if(!result.next()){
                return null;
            }

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (result.next()){
                Department temp = map.get(result.getInt(DEPARTMENT_ID));

                if(temp == null){
                    temp = instantiateDepartment(result);
                    map.put(result.getInt(DEPARTMENT_ID), temp);
                }
                sellers.add(instantiateSeller(result, temp));
            }

            return sellers;
        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(result);
        }
    }
}
