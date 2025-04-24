package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.Message;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class MessageDao {

	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
	public MessageDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	//つぶやき登録用selectメソッド
 	public void insert(Connection connection, Message message) {

 		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
 		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

 		PreparedStatement ps = null;
 		try {
 			StringBuilder sql = new StringBuilder();
 			//sqlインスタンスにINSERTO文を1行ずつ文字列として追加
 			sql.append("INSERT INTO messages ( ");
 			sql.append("    user_id, ");
 			sql.append("    text, ");
 			sql.append("    created_date, ");
 			sql.append("    updated_date ");
 			sql.append(") VALUES ( ");
 			sql.append("    ?, "); // user_id
 			sql.append("    ?, "); // text
 			sql.append("    CURRENT_TIMESTAMP, "); // created_date
 			sql.append("    CURRENT_TIMESTAMP "); // updated_date
 			sql.append(")");

 			//合体させたINSERTO文を実行
 			ps = connection.prepareStatement(sql.toString());

 			//合体させたINSERTO文の穴埋め
 			//1個目の?に、右側のvalue入れる
 			ps.setInt(1, message.getUserId());
 			ps.setString(2, message.getText());
 			ps.executeUpdate();
 		} catch (SQLException e) {
 			log.log(Level.SEVERE, new Object() {
 			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
 			throw new SQLRuntimeException(e);
 		} finally {
 			close(ps);
 		}
 	}

	//つぶやき削除用deleteメソッド
	public void delete(Connection connection, int id) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			//SQLのDELETE文を文字列として代入
			String sql = "DELETE FROM messages WHERE id = ? ";

			//引数にあるSLELCT文で抽出したDBの情報をpsとして扱う
			ps = connection.prepareStatement(sql);
			//穴埋め
			ps.setInt(1, id);
			//穴埋めで完成したDELETE文を実行
			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	//つぶやき編集用selectメソッド
	public String select(Connection connection, int id) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			//SQLのDELETE文を文字列として代入
			String sql = "SELECT text FROM messages WHERE id = ? ";

			//引数にあるsql文で抽出したDBの情報をpsとして扱う
			ps = connection.prepareStatement(sql);
			//穴埋め
			ps.setInt(1, id);
			//穴埋めで完成したSLELCT文を実行→1レコードずつrsに入ってる
			ResultSet rs = ps.executeQuery();
			//1行下に行かないと取得できない
			rs.next();
			new Message().setText(rs.getString("text"));
			return new Message().getText();

		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
}