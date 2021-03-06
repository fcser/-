package cn.java.rlmj.servlet;

/**
 * 可能的错误：客户端并没有设置门禁的id，，，所以我可能返回去的id是空的
 */
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.json.JSONException;
import org.json.JSONObject;

import cn.java.rlmj.dao.EquipmentMapper;
import cn.java.rlmj.dao.SqlSessionFactoryUtils;
import cn.java.rlmj.pojo.Equipment;
import cn.java.rlmj.utils.KeyValue;

/**
 * Servlet implementation class Registerdevice
 */
// @WebServlet("/Registerdevice")
public class Registerdevice extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Registerdevice() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String key = request.getParameter("key");
		String device = request.getParameter("device");
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		boolean isKey = KeyValue.checkKey(key);
		if (isKey) {
			SqlSession sqlSession = null;
			JSONObject js = new JSONObject();
			try {
				JSONObject json = new JSONObject(device);
				int id = json.getInt("id");
				int houseid = json.getInt("houseid");
				String time = json.getString("time");
				Equipment equipment = new Equipment();
				equipment.setId( id);
				equipment.setHouse_id(houseid);
				equipment.setTime(time);
				sqlSession = SqlSessionFactoryUtils.openSqlSession();
				EquipmentMapper equipmentMapper = sqlSession.getMapper(EquipmentMapper.class);
				equipmentMapper.insertEquipment(equipment);
				sqlSession.commit();
				js.put("id", id);
				js.put("res", 0);
				js.put("err", "");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				sqlSession.rollback();
				e.printStackTrace();
				try {
					js.put("id", 0);
					js.put("res", -1);
					js.put("err", "设备注册失败");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} finally {
				sqlSession.close();
				writer.write(js.toString());
			}
		}
	}

}
