package dao;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DAO {
    static DataSource ds;
    
    //--------------------------------------------------------------
    //--- データベースへのコネクションを貼るメソッド
    //--------------------------------------------------------------
    public Connection getConnection() throws Exception {
        if (ds == null) {
            InitialContext ic = new InitialContext();
            ds = (DataSource)ic.lookup("java:/comp/env/jdbc/jsonkadai04");
        }
        return ds.getConnection();
    }
    
    //--------------------------------------------------------------
    //--- pointテーブルからTENPO_IDとUSER_IDに合致したPointがある場合Pointを返却する
    //--------------------------------------------------------------
    public int searchAndInsert(String tenpoId,String userId) throws Exception {
        //pointを格納する変数を宣言
    	int point = 0;
    	
    	//--- データベース接続
        Connection con = getConnection();
        
        //--- 該当するtenpoIdとuserIdが存在するかどうかで判断してSQL文を作る
        String sql1 = "select POINT from point where TENPO_ID = ? AND USER_ID = ?";
        PreparedStatement st1 = con.prepareStatement(sql1);
        st1.setString(1, tenpoId);
        st1.setString(2, userId);
        //クエリの結果を取得
        ResultSet rs = st1.executeQuery();
        
        //tenpoIdとuserIdが存在するか？
        while(rs.next() == true) {
            //--- pointを受け取る
            point = rs.getInt("POINT");
            //--- オブジェクトを閉じる
            st1.close();
            con.close();
        }
        //存在しなかった
        String sql2 = "insert into point values(?, ?, 500)";
        PreparedStatement st2 = con.prepareStatement(sql2);
        st2.setString(1, tenpoId);
        st2.setString(2, userId);
        st2.executeUpdate();
        //--- オブジェクトを閉じる
        st2.close();
        con.close();
        
        return point;
    }
}
