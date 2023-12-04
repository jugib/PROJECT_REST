package huy.java.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/students/*")
public class UserController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;
    private Gson gson;

    public void init() {
        userDAO = new UserDAO();
        gson = new Gson();
    }

    private void sendAsJSON(HttpServletResponse response, Object obj) throws ServletException, IOException {
        response.setContentType("application/json");
        String result = gson.toJson(obj);
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
    }
    
    
    
    

    // Get students
    // GET/RestAPI/students/
    // GET/RestAPI/students/id
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        res.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        // Return all students
        if (pathInfo == null || pathInfo.equals("/")) {
            List<User> students = userDAO.selectAllUsers();
            sendAsJSON(res, students);
            return;
        }

        String splits[] = pathInfo.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String studentID = splits[1];
        User student = userDAO.selectUserByID(studentID);
        if (student == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            sendAsJSON(res, student);
        }
    }

    // Post new student
    // POST/RestAPI/students/
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = req.getReader();

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String payload = buffer.toString();
            User student = gson.fromJson(payload, User.class);
            userDAO.insertUser(student);

            // Respond with success status
            res.setStatus(HttpServletResponse.SC_CREATED);
            res.getWriter().write("Student added successfully");
        } else {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // Delete student
    // DELETE/RestAPI/students/id
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        String[] splits = pathInfo.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        String studentID = splits[1];
        boolean deleted = userDAO.deleteUser(studentID);

        if (deleted) {
            res.getWriter().write("Student deleted successfully");
        } else {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Pre-flight request. Reply successfully:
        resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
