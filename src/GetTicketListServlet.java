import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetPointServlet
 */
@WebServlet("/getTicketList")
public class GetTicketListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetTicketListServlet() {
		super();

// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/getTicketList.jsp");
		rd.forward(request, response);

		final String driverName = "com.mysql.jdbc.Driver";
		final String url = "jdbc:mysql://192.168.54.190:3306/jsonkadai04";
		final String id = "jsonkadi04";
		final String pass = "JsonKadai04";

		try {

			Class.forName(driverName);
			Connection connection = DriverManager.getConnection(url, id, pass);

			PreparedStatement st = connection
					.prepareStatement("select point from ticket_table where ticketid=? AND ticketname=?");
//select文変更した

			String ticket = request.getParameter("ticketid");
			String namae = request.getParameter("ticketname");
			st.setString(1, ticket);
			st.setString(2, namae);
			ResultSet result = st.executeQuery();

			List<String[]> list = new ArrayList<>();

			if (result.next() == true) {
				String[] s = new String[3];
				s[0] = result.getString("ticketid");
				s[1] = result.getString("ticketname");
				s[2] = result.getString("point");
				list.add(s);

			} else {
				PreparedStatement st2 = connection
						.prepareStatement("insert into point (ticketid,ticketname,ticket_table) value(?,?,500)");
				st2.setString(1, ticket);
				st2.setString(2, namae);

				int x = st2.executeUpdate();

				if (x == 1) {
					System.out.println("新規追加成功");
					st.setString(1, ticket);
					st.setString(2, namae);
					result = st.executeQuery();
					if (result.next() == true) {
						String[] s = new String[3];
						s[0] = result.getString("ticketid");
						s[1] = result.getString("ticketname");
						s[2] = result.getString("point");
						list.add(s);
					}
				} else {
					System.out.println("新規追加失敗");
				}
			}

			request.setAttribute("list", list);
			RequestDispatcher gt = request.getRequestDispatcher("/WEB-INF/JSP/getTicket.jsp");
			gt.forward(request, response);
		} catch (ClassNotFoundException e) {
// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (SQLException e) {
// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}