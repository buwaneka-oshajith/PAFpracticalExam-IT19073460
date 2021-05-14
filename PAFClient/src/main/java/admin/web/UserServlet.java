package admin.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import admin.bean.User;
import admin.database.UserDao;





//Servlet
@WebServlet("/")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDao userdao;
       
    
  //Using database connection and  sql statements of userdao
	public void init() {
		userdao = new UserDao();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();

		try {
			switch (action) {
			//new form
			case "/new":
				showNewForm(request, response);
				break;
			//insert
			case "/insert":
				insertUser(request, response);
				break;
				//delete
			case "/delete":
				deleteUser(request, response);
				break;
				//Edit
			case "/edit":
				showEditForm(request, response);
				break;
				//Update
			case "/update":
				updateUser(request, response);
				break;
				//Default
			default:
				listUser(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	//user list
	private void listUser(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<User> listUser = userdao.selectAllUsers();
		request.setAttribute("listUser", listUser);
		RequestDispatcher dispatcher = request.getRequestDispatcher("listusers.jsp");
		dispatcher.forward(request, response);
	}
//Show new form
	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("userform.jsp");
		dispatcher.forward(request, response);
	}
	//Show Edit form

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		User existingUser = userdao.selectUser(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("userform.jsp");
		request.setAttribute("user", existingUser);
		dispatcher.forward(request, response);

	}
	//insert user function

	private void insertUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		User newUser = new User(name, email, country);
		userdao.insertUser(newUser);
		response.sendRedirect("list");
	}
	
	//Update user function

	private void updateUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");

		User book = new User(id, name, email, country);
		userdao.updateUser(book);
		response.sendRedirect("list");
	}
	

	//Delete user function
	private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		userdao.deleteUser(id);
		response.sendRedirect("list");

	}

}
