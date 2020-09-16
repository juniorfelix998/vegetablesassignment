package servlets;

import com.google.gson.Gson;
import models.Response;
import models.Vegetable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;

import utils.DatabaseConnection;

public class VegetableServlet extends HttpServlet {

    /**
     * This will List all the Vegetables
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<Vegetable> vegetables = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement st = connection
                    .prepareStatement("SELECT * from vegetables");
            // Execute the insert command using executeUpdate()
            // This makes some changes in database
            ResultSet resultSet = st.executeQuery();

            while (resultSet.next()) {
                Vegetable vegetable = new Vegetable();
                vegetable.setId(resultSet.getInt("id"));
                vegetable.setName(resultSet.getString("name"));
                vegetable.setQuantity(resultSet.getInt("quantity"));
                vegetable.setPrice(resultSet.getFloat("price"));
                vegetables.add(vegetable);
            }

            st.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            Response r = new Response("The Request Cannot be Completed");
            PrintWriter out = resp.getWriter();
            String json = new Gson().toJson(r);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(400);
            out.print(json);
            out.flush();
        }

        String employeeJsonString = new Gson().toJson(vegetables);

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(employeeJsonString);
        out.flush();
    }

    /**
     * This is to add Vegetable
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            // Start the database
            Connection con = DatabaseConnection.initializeDatabase();

            // Create a SQL query to insert data into demo table
            // The table has of two columns, so two '?' is used
            PreparedStatement st = con
                    .prepareStatement("insert into vegetables(name,quantity,price) values(?, ?, ?)");

            // get the data using request object
            // sets the data to st pointer
            String name = req.getParameter("name");
            st.setString(1, name);
            st.setInt(2, Integer.valueOf(req.getParameter("quantity")));
            st.setFloat(3, Float.valueOf(req.getParameter("price")));

            // Execute the insert command using executeUpdate()
            // to make changes in database
            st.executeUpdate();

            // Close all the connections
            st.close();
            con.close();

            Response r = new Response(name + " added successfully");

            // Get a writer pointer and display the results that are sucessful
    
            PrintWriter out = resp.getWriter();
            String json = new Gson().toJson(r);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(json);
            out.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
            Response r = new Response("Sorry, we were unable to add the vegetable");
            PrintWriter out = resp.getWriter();
            String json = new Gson().toJson(r);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(400);
            out.print(json);
            out.flush();
        }
    }

    /**
     * Update of the Vegetable
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Enumeration<String> params = req.getParameterNames();
        StringBuilder res = new StringBuilder();
        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            res.append("Parameter Name - ").append(paramName).append(", Value - ").append(req.getParameter(paramName)).append(", ");
        }
        Response r = new Response(res.toString());
//        Response r = new Response(req.getAttribute("name") + " - " + retrieveId(req));
        PrintWriter out = resp.getWriter();
        String json = new Gson().toJson(r);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(400);
        out.print(json);
        out.flush();

    }

    private static int retrieveId(HttpServletRequest req) {
        String pathInfo = req.getRequestURI();
        String[] parts = pathInfo.split("/");
        if (parts.length > 0) {
            pathInfo = parts[parts.length - 1];
        }
        return Integer.parseInt(pathInfo);
    }

    /*
     * Deleting of the  Vegetable
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
