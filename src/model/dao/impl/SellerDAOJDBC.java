package model.dao.impl;

import db.DB;
import db.DBException;
import db.DBIntegrityException;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
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
    public void insert(Seller seller) {
        PreparedStatement statement = null;
        try{
            statement = connection.prepareStatement("INSERT INTO seller " +
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            statement.setString(1, seller.getName());
            statement.setString(2, seller.getName());
            statement.setDate(3, new Date(seller.getBirthDate().getTime()));
            statement.setDouble(4, seller.getBaseSalary());
            statement.setInt(5, seller.getDepartment().getId());

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected > 0){
                ResultSet result = statement.getGeneratedKeys();
                if(result.next()){
                    int id = result.getInt(1);
                    seller.setId(id);
                }
                DB.closeResultSet(result);
            } else{
                throw new DBException("Unexpected Error. No rows affected!");
            }
        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement statement = null;
        try{
            statement = connection.prepareStatement("UPDATE seller " +
                            "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                            "WHERE Id = ?"
            );

            statement.setString(1, seller.getName());
            statement.setString(2, seller.getName());
            statement.setDate(3, new Date(seller.getBirthDate().getTime()));
            statement.setDouble(4, seller.getBaseSalary());
            statement.setInt(5, seller.getDepartment().getId());
            statement.setInt(6, seller.getId());

            statement.executeUpdate();
        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement statement = null;
        try{
            statement = connection.prepareStatement("DELETE FROM seller " +
                    "WHERE Id = ?"
            );

            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected == 0){
                throw new DBIntegrityException(STR."There's no seller with id \{id} registered.");
            }
        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
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
