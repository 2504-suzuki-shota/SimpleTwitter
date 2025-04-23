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

import chapter6.beans.Message;
import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/message" })
public class MessageServlet extends HttpServlet {

	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
	public MessageServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		//ログの出力
		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		//セッションを作る
		HttpSession session = request.getSession();
		List<String> errorMessages = new ArrayList<String>();

		//クライアント側が入力したテキストを変数textとして扱う
		String text = request.getParameter("text");

		//入力の仕方でエラーがあった場合
		if (!isValid(text, errorMessages)) {
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}
		//textをセッターに保存して使えるようにする
		Message message = new Message();
		message.setText(text);

		//sessionの中にloginUserとして保存していたものを取り出します
		User user = (User) session.getAttribute("loginUser");
		message.setUserId(user.getId());

		new MessageService().insert(message);
		response.sendRedirect("./");
	}

	private boolean isValid(String text, List<String> errorMessages) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		//テキストが何も入力されてない場合（シフト、改行もNG）
		if (StringUtils.isBlank(text)) {
			errorMessages.add("メッセージを入力してください");
			//テキストが141文字以上場合
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