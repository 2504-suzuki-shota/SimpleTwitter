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
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet {

	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
	public EditServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		//更新したいレコードを特定できるのはid
		String checkId = request.getParameter("id");

		//エラーメッセージを表示したい条件はgetParameterしたidが
		//①数字以外の文字
		//②空値になっている
		//この２つだとintに変換できないから困る！
		HttpSession session = request.getSession();
		if(!checkId.matches("^[0-9]*$") || StringUtils.isEmpty(checkId)) {
			String errorMessage ="不正なパラメータが入力されました";
			session.setAttribute("errorMessages", errorMessage);
			//エラーメッセージをトップ画面に表示させたい
			request.getRequestDispatcher("top.jsp").forward(request, response);
			return;
		}

		//無事にintにできました→Daoでid使いたいから運ぶ
		int id = Integer.parseInt(checkId);

		//before_messagesにはmessagesテーブルのid,user_id,textが入ってる
		Message beforeMessages = new MessageService().select(id);

		//エラーメッセージを表示したい条件はgetParameterしたidが
		//③存在しない
		if(beforeMessages == null) {
			String errorMessage ="不正なパラメータが入力されました";
			session.setAttribute("errorMessages", errorMessage);
			//エラーメッセージをトップ画面に表示させたい
			request.getRequestDispatcher("top.jsp").forward(request, response);
			return;
		}
		request.setAttribute("beforeMessages",beforeMessages);
		//edit.jspにrequest, responseを返す
		request.getRequestDispatcher("edit.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		//更新したいのはテキスト
		String afterText = request.getParameter("text");

		//バリデーション処理
		HttpSession session = request.getSession();
		List<String> errorMessages = new ArrayList<String>();
		if (!isValid(afterText, errorMessages)) {
			session.setAttribute("errorMessages", errorMessages);
			//エラーメッセージを編集画面に表示させたい
			request.getRequestDispatcher("edit.jsp").forward(request, response);
			return;
		}

		//つぶやきのレコードを特定できるのはid
		int id = Integer.parseInt(request.getParameter("id"));
		//Daoで使いたいから運ぶ＆更新だけしたいのでvoid
		new MessageService().update(id, afterText);

		//ここまででDBの更新は終わっている
		//表示はtopサーブレットの仕事
		new TopServlet().doGet(request, response);
	}

	private boolean isValid(String text, List<String> errorMessages) {
		log.info(new Object() {}.getClass().getEnclosingClass().getName()
		+" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		//テキストが何も入力されてない場合（シフト、改行のみもNG）
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
