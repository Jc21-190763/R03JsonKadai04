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
        
        //--- SQL文作成
        String sql1 = "select POINT from point where TENPO_ID = ? AND USER_ID = ?";
        PreparedStatement st1 = con.prepareStatement(sql1);
        //TENPO_IDとUSER_IDのセット
        st1.setString(1, tenpoId);
        st1.setString(2, userId);
        ResultSet rs = st1.executeQuery();
        //該当するTENPO_IDとUSER_IDがあった場合POINTを返す
        while(rs.next() == true) {
        //--- pointを受け取る
        point = rs.getInt("POINT");
        }
        if (point == 0) {
            //該当するSQL文がない場合のSQL文作成
            String sql2 = "insert into point values(?, ?, 500)";
            st1 = con.prepareStatement(sql2);
            //TENPO_IDとUSER_IDのセット
            st1.setString(1, tenpoId);
            st1.setString(2, userId);
            st1.executeUpdate();
            st1 = con.prepareStatement(sql1);
            //TENPO_IDとUSER_IDのセット
            st1.setString(1, tenpoId);
            st1.setString(2, userId);
            ResultSet rs2 = st1.executeQuery();
            //該当するTENPO_IDとUSER_IDがあった場合POINTを返す
            while(rs2.next() == true) {
            //--- pointを受け取る
            point = rs.getInt("POINT");
            }
		}
        //--- オブジェクトを閉じる
        st1.close();
        con.close();
        return point;
    }
}
