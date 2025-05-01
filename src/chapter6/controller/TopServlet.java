package chapter6.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.beans.User;
import chapter6.beans.UserMessage;
import chapter6.logging.InitApplication;
import chapter6.service.CommentService;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/index.jsp" })
public class TopServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
	public TopServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		//ログの出力
		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		boolean isShowMessageForm = false;
		User user = (User) request.getSession().getAttribute("loginUser");
		if (user != null) {
			isShowMessageForm = true;
		}
		//☆つぶやき表示用
		//（絞り込み）日付をString型で取得します
		String start = request.getParameter("start");
		String end = request.getParameter("end");

		//（追加課題③）
		String userId = request.getParameter("user_id");
		List<UserMessage> messages = new MessageService().select(userId, @@@);

		//messagesはDBから抽出したものを各々でセットした集合体
		request.setAttribute("messages", messages);

		//★返信表示用
		//返信は全て取得したいから引数なし→課題③実装前と同じ
		List<UserMessage> comments = new CommentService().select();
		//commentsはDBから抽出したものを各々でセットした集合体
		request.setAttribute("comments", comments);

		request.setAttribute("isShowMessageForm", isShowMessageForm);
		//top.jspにこれらの情報渡す
		request.getRequestDispatcher("/top.jsp").forward(request, response);
	}
}