//package chapter6.dao;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.logging.Logger;
//
//import chapter6.beans.UserMessage;
//import chapter6.exception.SQLRuntimeException;
//import chapter6.logging.InitApplication;
//
//public class UserCommentDao {
//
//	/**
//	 * ロガーインスタンスの生成
//	 */
//	Logger log = Logger.getLogger("twitter");
//
//	/**
//	 * デフォルトコンストラクタ
//	 * アプリケーションの初期化を実施する。
//	 */
//	public UserCommentDao() {
//		InitApplication application = InitApplication.getInstance();
//		application.init();
//	}
//
//	//返信表示用selectメソッド
//	public List<UserMessage> select(Connection connection, int num) {
//
//		//ログの出力
//		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
//		" : " + new Object() {}.getClass().getEnclosingMethod().getName());
//
//		PreparedStatement ps = null;
//		try {
//			StringBuilder sql = new StringBuilder();
//			sql.append("SELECT ");
//			sql.append("    comments.id as id, ");
//			sql.append("    comments.text as text, ");
//			sql.append("    comments.user_id as user_id, ");
//			sql.append("    comments.message_id as message_id, ");
//			sql.append("    users.name as name, ");
//			sql.append("    users.account as account, ");
//			sql.append("    comments.created_date as created_date ");
//			sql.append("FROM  comments");
//			sql.append("INNER JOIN users ");
//			sql.append("ON comments.user_id = users.id ");
//			sql.append("ORDER BY created_date DESC limit " + num);
//
//			ps = connection.prepareStatement(sql.toString());
//			ResultSet rs = ps.executeQuery();
//
//			List<UserMessage> comments = toUserComments(rs);
//			return comments;
//		} catch (SQLException e) {
//			log.log(Level.SEVERE, new Object() {
//			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
//			throw new SQLRuntimeException(e);
//		} finally {
//			close(ps);
//		}
//	}
//
//	private List<UserMessage> toUserComments(ResultSet rs) throws SQLException {
//
//		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
//		" : " + new Object() {}.getClass().getEnclosingMethod().getName());
//		//messagesと同じクラスだけど別インスタンスなので上書きされません
//		List<UserMessage> comments = new ArrayList<UserMessage>();
//		try {
//			while (rs.next()) {
//				UserMessage comment = new UserMessage();
//				comment.setId(rs.getInt("id"));
//				comment.setText(rs.getString("text"));
//				comment.setUserId(rs.getInt("user_id"));
//				comment.setAccount(rs.getString("account"));
//				comment.setName(rs.getString("name"));
//				comment.setCreatedDate(rs.getTimestamp("created_date"));
//				comments.add(comment);
//			}
//			return comments;
//		} finally {
//			close(rs);
//		}
//	}
//}