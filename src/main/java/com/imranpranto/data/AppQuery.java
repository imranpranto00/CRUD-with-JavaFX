package com.imranpranto.data;

import java.sql.ResultSet;
import java.sql.Statement;

import com.imranpranto.model.Student;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AppQuery {
    private DBConnection c = new DBConnection();

    public void addStudent(com.imranpranto.model.Student student) {
        try {
            c.getDBConn();
            java.sql.PreparedStatement ps = c.getCon()
                    .prepareStatement("insert into student(firstname,middlename,lastname)values(?,?,?)");
            ps.setString(1, student.getFirstname());
            ps.setString(2, student.getMiddlename());
            ps.setString(3, student.getLastname());
            ps.execute();
            ps.close();
            c.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ObservableList<com.imranpranto.model.Student> getStudentList() {
        ObservableList<com.imranpranto.model.Student> studentList = FXCollections.observableArrayList();
        try {
            String query = "select id, firstname, middlename, lastname from student order by lastname asc";
            c.getDBConn();
            Statement st = c.getCon().createStatement();
            ResultSet rs = st.executeQuery(query);
            com.imranpranto.model.Student s;
            while (rs.next()) {
                s = new com.imranpranto.model.Student(rs.getInt("id"), rs.getString("firstname"),
                        rs.getString("middlename"), rs.getString("lastname"));
                studentList.add(s);
            }
            rs.close();
            st.close();
            c.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentList;

    }

    public void updateStudent(Student student) {
        try {
            c.getDBConn();
            java.sql.PreparedStatement ps = c.getCon().prepareStatement("UPDATE `student`\n" +
                    "SET \n" +
                    "`firstname` = ?,\n" +
                    "`middlename` = ?,\n" +
                    "`lastname` = ?\n" +
                    "WHERE `id` = ? ");

            ps.setString(1, student.getFirstname());
            ps.setString(2, student.getMiddlename());
            ps.setString(3, student.getLastname());
            ps.setInt(4, student.getId());

            ps.execute();
            ps.close();
            c.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(Student student) {
        try {
            c.getDBConn();
            java.sql.PreparedStatement ps = c.getCon().prepareStatement("DELETE FROM `student`\n" +
                    "WHERE id = ?;");

            ps.setInt(1, student.getId());

            ps.execute();
            ps.close();
            c.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
