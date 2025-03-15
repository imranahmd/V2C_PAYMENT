package com.rew.payment.utility;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rew.pg.db.DBConnectionHandler;

@WebServlet("/EmpServlet")
public class ProcedureController extends HttpServlet{

    private static final Logger logger = LoggerFactory.getLogger(ProcedureController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        logger.info("doGet method called");
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("doPost method called");

        Connection conn = null;
        CallableStatement callableStatement = null;
        ResultSet rs = null;

        try {
            logger.info("Getting database connection");
            conn = DBConnectionHandler.getConnection();
            final String sql = "Call pro_emp_procedure(?)";
            logger.info("SQL query: " + sql);

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            
            logger.info("Request parameter 'id': " + request.getParameter("id"));
            
            int actionId = Integer.parseInt(request.getParameter("id"));
            logger.info("Request parameter 'id': " + actionId);

            conn.setAutoCommit(false);
            logger.info("Set auto-commit to false");

            callableStatement = conn.prepareCall(sql);
            callableStatement.setInt(1, actionId);
            logger.info("Callable statement created and parameter set");

            boolean hasResultSet = callableStatement.execute();
            logger.info("Callable statement executed, hasResultSet: " + hasResultSet);

            if (hasResultSet) {
                logger.info("Processing result set");
                ResultSet resultSet = callableStatement.getResultSet();
                out.println("<table border='1'><tr><th>Emp ID</th><th>Name</th><th>Dept</th><th>Salary</th></tr>");
                while (resultSet.next()) {
                    logger.info("Result set row: emp_id=" + resultSet.getInt("emp_id")
                            + ", emp_name=" + resultSet.getString("emp_name")
                            + ", emp_dept=" + resultSet.getString("emp_dept")
                            + ", emp_salary=" + resultSet.getDouble("emp_salary"));
                    out.println("<tr><td>" + resultSet.getInt("emp_id") + "</td><td>" + resultSet.getString("emp_name")
                            + "</td><td>" + resultSet.getString("emp_dept") + "</td><td>"
                            + resultSet.getDouble("emp_salary") + "</td></tr>");
                }
                out.println("</table>");
            } else {
                int updateCount = callableStatement.getUpdateCount();
                logger.info("Update count: " + updateCount);
                out.println("<p>Update count: " + updateCount + "</p>");
            }

            conn.commit();
            logger.info("Transaction committed");

            rs = callableStatement.getResultSet();
            logger.info("Result set obtained after commit");

        } catch (Exception e) {
            logger.error("Exception occurred in executing procedure: " + e.getMessage(), e);
            if (conn != null) {
                try {
                    conn.rollback();
                    logger.info("Transaction rolled back");
                } catch (Exception rollbackEx) {
                    logger.error("Exception occurred during rollback: " + rollbackEx.getMessage(), rollbackEx);
                }
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    logger.info("Result set closed");
                } catch (Exception ex) {
                    logger.error("Exception occurred while closing result set: " + ex.getMessage(), ex);
                }
            }
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                    logger.info("Callable statement closed");
                } catch (Exception ex) {
                    logger.error("Exception occurred while closing callable statement: " + ex.getMessage(), ex);
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                    logger.info("Connection closed and auto-commit set to true");
                } catch (Exception ex) {
                    logger.error("Exception occurred while closing connection: " + ex.getMessage(), ex);
                }
            }
        }
    }
}