package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.UserMessage;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class UserMessageDao {

	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
	public UserMessageDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	//つぶやき表示用selectメソッド
	public List<UserMessage> select(Connection connection, Integer id, int num,String start, String end) {

		//ログの出力
		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			//表示したい物を取得したい
			//(つぶやき削除)すでにdeleteされてるのでここのSELECTで取得できない→表示されない
			//(つぶやき編集)すでにupdateされてるので更新後が取得される→更新後が表示される
			//sqlインスタンスに指示を1行ずつ追加
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ");
			sql.append("    messages.id as id, ");
			sql.append("    messages.text as text, ");
			sql.append("    messages.user_id as user_id, ");
			sql.append("    users.account as account, ");
			sql.append("    users.name as name, ");
			sql.append("    messages.created_date as created_date ");
			//内部結合の指示
			sql.append("FROM messages ");
			sql.append("INNER JOIN users ");
			//結合条件 messagesテーブルのuser_idとusersテーブルのidが等しい
			sql.append("ON messages.user_id = users.id ");
			//（絞り込み）
			sql.append("WHERE messages.created_date BETWEEN ? AND ? ");

			//(課題②)idがnullじゃない場合→id = suzuki みたいになってて、suzukiのつぶやきだけ表示してほしい
			if (id != null) {
				//取り出す条件指定
				sql.append("AND user_id = ? ");
			}

			//並び替え
			sql.append("ORDER BY created_date DESC limit " + num);
			ps = connection.prepareStatement(sql.toString());

			//（絞り込み）
			ps.setString(1, start);
			ps.setString(2, end);
			//(課題②)
			if (id != null) {
				ps.setInt(3, id);
			}

			ResultSet rs = ps.executeQuery();
			List<UserMessage> messages = toUserMessages(rs);
			//DBから抽出したものを各々でセットされた集合体を返す
			return messages;
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	//toUserMessagesメソッド→DBから抽出したものを各々でセットしてる
	private List<UserMessage> toUserMessages(ResultSet rs) throws SQLException {

		//ログの出力
		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		List<UserMessage> messages = new ArrayList<UserMessage>();
		try {
			while (rs.next()) {
				UserMessage message = new UserMessage();
				message.setId(rs.getInt("id"));
				message.setText(rs.getString("text"));
				message.setUserId(rs.getInt("user_id"));
				message.setAccount(rs.getString("account"));
				message.setName(rs.getString("name"));
				message.setCreatedDate(rs.getTimestamp("created_date"));
				messages.add(message);
			}
			return messages;
		} finally {
			close(rs);
		}
	}
}