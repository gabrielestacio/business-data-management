package model.dao.impl;

import db.DB;
import db.DBException;
import db.DBIntegrityException;
import model.dao.DepartmentDAO;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAOJDBC implements DepartmentDAO {
    private final Connection connection;

    public DepartmentDAOJDBC(Connection connection){
        this.connection = connection;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement statement = null;
        try{
            statement = connection.prepareStatement("INSERT INTO department " +
                            "(Name) " +
                            "VALUES " +
                            "(?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            statement.setString(1, department.getName());

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected > 0){
                ResultSet result = statement.getGeneratedKeys();
                if(result.next()){
                    int id = result.getInt(1);
                    department.setId(id);
                }
                DB.closeResultSet(result);
            }
        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void update(Department department) {
        PreparedStatement statement = null;
        try{
            statement = connection.prepareStatement("UPDATE department " +
                            "SET " +
                            "Name = ? " +
                            "WHERE Id = ?"
            );

            statement.setString(1, department.getName());
            statement.setInt(2, department.getId());

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
            statement = connection.prepareStatement("DELETE FROM department " +
                    "WHERE Id = ?"
            );

            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected == 0){
                throw new DBIntegrityException(STR."There's no department with id \{id} registered.");
            }
        } catch (SQLException e){
            throw new DBIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement statement = null;
        ResultSet result = null;
        try{
            statement = connection.prepareStatement("SELECT * FROM department " +
                    "WHERE department.Id = ?"
            );

            statement.setInt(1, id);

            result = statement.executeQuery();
            if(result.next()){
                return instantiateDepartment(result);
            }
            return null;
        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(result);
        }
    }

    private Department instantiateDepartment(ResultSet result) throws SQLException {
        Department department = new Department();
        department.setId(result.getInt("Id"));
        department.setName(result.getString("Name"));

        return department;
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement statement = null;
        ResultSet result = null;
        try{
            statement = connection.prepareStatement("SELECT * FROM department " +
                    "ORDER BY Name"
            );

            result = statement.executeQuery();

            List<Department> departments = new ArrayList<>();

            while (result.next()){
                departments.add(instantiateDepartment(result));
            }

            return departments;
        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(result);
        }
    }
}
