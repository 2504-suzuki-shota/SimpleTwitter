package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Comment;
import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.CommentService;

@WebServlet(urlPatterns = { "/comment" })

public class CommentServlet extends HttpServlet {

	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
	public CommentServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	@Override //method が post だからdoPost呼び出し
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		//ログの出力
		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		HttpSession session = request.getSession();

		Comment comment = getComment(request, session);

		//バリデーション処理
		List<String> errorMessages = new ArrayList<String>();
		if (!isValid(comment.getText(), errorMessages)) {
			session.setAttribute("errorMessages", errorMessages);
			//エラーメッセージをトップ画面に表示させたい
			response.sendRedirect("./");
			return;
		}

		new CommentService().insert(comment);

		response.sendRedirect("./");
	}

	public Comment getComment(HttpServletRequest request, HttpSession session) {
		Comment comment = new Comment();
		comment.setMessageId(Integer.parseInt(request.getParameter("id")));
		comment.setText(request.getParameter("comment"));
		User user = (User) session.getAttribute("loginUser");
		comment.setUserId(user.getId());
		return comment;
	}

	private boolean isValid(String text, List<String> errorMessages) {

		//ログの出力
		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		//返信が何も入力されてない場合（シフト、改行もNG）
		if (StringUtils.isBlank(text)) {
			errorMessages.add("メッセージを入力してください");
			//返信が141文字以上場合
		} else if (140 < text.length()) {
			errorMessages.add("140文字以下で入力してください");
		}

		//errorMessagesリストの要素数が0じゃない場合→上記どちらかのエラーがあった場合
		if (errorMessages.size() != 0) {
			return false;
		}
		return true;
	}
}
