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

	private static final String INVALID_PARAMETER = "不正なパラメータが入力されました";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		//更新したいレコードを特定できるのはid
		String checkId = request.getParameter("id");

		//エラーメッセージを表示したい条件はgetParameterしたidが
		//①空値になっている（スペース、改行含む）nullチェック先にやる！
		//②数字以外の文字
		//この２つだとintに変換できないから困る！
		HttpSession session = request.getSession();
		if(StringUtils.isBlank(checkId) || !checkId.matches("^[0-9]*$")) {
			String errorMessage =INVALID_PARAMETER;
			session.setAttribute("errorMessages", errorMessage);
			//エラーメッセージをトップ画面に表示させたい
			//request.getRequestDispatcher("top.jsp").forward(request, response);
			response.sendRedirect("./");
			return;
		}

		//無事にintにできました→Daoでid使いたいから運ぶ
		int id = Integer.parseInt(checkId);

		//messagesには更新対象のmessagesテーブルのid,user_id,textが入ってる
		Message edits = new MessageService().select(id);

		//エラーメッセージを表示したい条件はgetParameterしたidが
		//③存在しない
		if(edits == null) {
			String errorMessage =INVALID_PARAMETER;
			session.setAttribute("errorMessages", errorMessage);
			//エラーメッセージをトップ画面に表示させたい
			//request.getRequestDispatcher("top.jsp").forward(request, response);との違いは？
			response.sendRedirect("./");
			return;
		}

		request.setAttribute("edits",edits);
		//編集画面で更新前のテキストを表示したい
		request.getRequestDispatcher("edit.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		//更新に必要な情報を1つにまとめている→入力内容(afterText)と更新対象のid
		Message edit = getMessage(request);

		//バリデーション処理
		HttpSession session = request.getSession();
		List<String> errorMessages = new ArrayList<String>();
		if (!isValid(edit.getText(), errorMessages)) {
			session.setAttribute("errorMessages", errorMessages);
			//エラーだけど入力内容を表示させたい
			session.setAttribute("edit", edit);
			//エラーメッセージを編集画面に表示させたい
			request.getRequestDispatcher("edit.jsp").forward(request, response);
			return;
		}

		//Daoで使いたいから運ぶ＆更新だけしたいのでvoid
		new MessageService().update(edit);

		//ここまででDBの更新は終わっている
		//表示はtopサーブレットの仕事→「./」でtopサーブレットの@/index.jspを呼び出せる
		//→doGet呼び出しと同じ→違いは？
		response.sendRedirect("./");
	}

	public Message getMessage(HttpServletRequest request) {

		Message edit = new Message();
		edit.setId(Integer.parseInt(request.getParameter("id")));
		edit.setText(request.getParameter("text"));
		return edit ;

		//レビュー前はこの２つをそれぞれ用意してたけどbean使えば１つで済む
		//int id = Integer.parseInt(request.getParameter("id"));
		//String afterText =request.getParameter("text");
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

////修正前の考え方
//①
//＊beforeMessagesには更新対象のmessagesテーブルのid,user_id,textが入ってる
//Message beforeMessages = new MessageService().select(id);
//編集画面のテキストエリアに更新前のテキストを表示するためにJSPで${beforeMessages.text}してた
//②
//＊afterTextとして更新後(文字オーバー)のテキストを取得
//String afterText =request.getParameter("text");
//session.setAttribute("afterText", afterText);
//編集画面のテキストエリアにafterTextを表示するためにJSPで${afterText}してた
//①と②をJSPでchoose使って条件分岐して表示を切り替えてた


