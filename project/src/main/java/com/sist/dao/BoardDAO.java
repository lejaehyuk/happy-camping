package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sist.util.DBConn;
import com.sist.util.Pagination;
import com.sist.vo.BoardReplyVO;
import com.sist.vo.BoardVO;

public class BoardDAO {

    private Connection conn;

    private PreparedStatement ps;

    private final DBConn dbConn;

    public BoardDAO() {
        this.dbConn = DBConn.getInstance();
    }

    public Pagination boardList(int curPage) {
        List<BoardVO> items = new ArrayList<>();
        String pagingQuery = "SELECT bno, title, regdate, writer, hit, mid , num " +
                "FROM (SELECT bno, title, regdate, writer, hit, mid , ROWNUM AS num " +
                "FROM (SELECT /*+ INDEX_DESC(HC_BOARD_2 HC_BOARD_BNO_PK) */ bno, title, regdate, writer, hit , mid FROM HC_BOARD_2)) " +
                "WHERE num BETWEEN ? AND ?";
        String totalBoardCntQuery = "SELECT COUNT(*) FROM HC_BOARD_2";
        int totalBoardCnt = 0;
        int rowSize = 10;
        int start = (rowSize * curPage) - (rowSize - 1);
        int end = rowSize * curPage;
        try {
            conn = dbConn.createConnection();
            ps = conn.prepareStatement(totalBoardCntQuery);
            ResultSet rs = ps.executeQuery();
            rs.next();
            totalBoardCnt = rs.getInt(1);
            ps = conn.prepareStatement(pagingQuery);
            ps.setInt(1 , start);
            ps.setInt(2, end);
            rs = ps.executeQuery();
            while (rs.next()) {
                BoardVO vo = new BoardVO();
                vo.setBno(rs.getInt(1));
                vo.setTitle(rs.getString(2));
                vo.setRegDate(rs.getDate(3));
                vo.setWriter(rs.getString(4));
                vo.setHit(rs.getInt(5));
                vo.setMid(rs.getString(6));
                items.add(vo);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConn.closeConnection(ps, conn);
        }
        return new Pagination(items, curPage, totalBoardCnt , 10);
    }

    public void boardWrite(String title, String content, String writer, String mid) {
        String boardWriteSQL = "INSERT INTO HC_BOARD_2 (bno, title, content, writer, mid) VALUES (HC_BOARD_SEQ.NEXTVAL, ?, ?, ? ,?)";
        try {
            conn = dbConn.createConnection();
            ps = conn.prepareStatement(boardWriteSQL);
            ps.setString(1, title);
            ps.setString(2, content);
            ps.setString(3, writer);
            ps.setString(4, mid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConn.closeConnection(ps, conn);
        }
    }
    public BoardVO boardDetail(int no) {
        String boardFindQuery = "SELECT bno, title, writer, content, regdate, mid FROM HC_BOARD_2 WHERE bno =?";
        String boardUpdateQuery = "UPDATE HC_BOARD_2 SET hit = hit + 1 WHERE bno = ?";
        BoardVO vo = new BoardVO();
        try {
            conn = dbConn.createConnection();
            ps = conn.prepareStatement(boardFindQuery);
            ps.setInt(1, no);
            ResultSet rs = ps.executeQuery();
            rs.next();
            vo.setBno(rs.getInt(1));
            vo.setTitle(rs.getString(2));
            vo.setWriter(rs.getString(3));
            vo.setContent(rs.getString(4));
            vo.setRegDate(rs.getDate(5));
            vo.setMid(rs.getString(6));
            ps = conn.prepareStatement(boardUpdateQuery);
            ps.setInt(1, no);
            ps.executeUpdate();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConn.closeConnection(ps, conn);
        }
        return vo;
    }

    public void boardUpdate(int no, String title, String content) {
        String boardUpdateQuery = "UPDATE HC_BOARD_2 SET title = ?, content = ? WHERE bno = ?";
        try {
            conn = dbConn.createConnection();
            ps = conn.prepareStatement(boardUpdateQuery);
            ps.setString(1, title);
            ps.setString(2, content);
            ps.setInt(3, no);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConn.closeConnection(ps, conn);
        }
    }

    public void boardDelete(int no) {
        String boardDeleteQuery = "DELETE FROM HC_BOARD_2 WHERE bno = ?";
        try {
            conn = dbConn.createConnection();
            ps = conn.prepareStatement(boardDeleteQuery);
            ps.setInt(1, no);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConn.closeConnection(ps, conn);
        }
    }
    //댓글
    public void replyWrite(BoardReplyVO vo)
    {
  	  try
  	  {
  		conn = dbConn.createConnection();
  		  String sql="INSERT INTO HC_BOARD_REPLY_2(brno,bno,mid,writer,content,group_id) "
  				    +"VALUES(hc_board_reply_seq.nextval,?,?,?,?,(SELECT NVL(MAX(group_id)+1,1) FROM HC_BOARD_REPLY_2))";
  		  ps=conn.prepareStatement(sql);
  		  ps.setInt(1, vo.getBno());
  		  ps.setString(2, vo.getMid());
  		  ps.setString(3, vo.getWriter());
  		  ps.setString(4, vo.getContent());
  		  ps.executeUpdate();
  	  }catch(SQLException e)
  	  {
  		 e.printStackTrace();
  	  }
  	  finally
  	  {
  		dbConn.closeConnection(ps, conn);
  	  }
    }
    // 댓글 읽기
    public List<BoardReplyVO> replyListData(int bno)
    {
  	  List<BoardReplyVO> list=new ArrayList<>();
  	  try
  	  {
  		conn = dbConn.createConnection();
  		  String sql="SELECT brno,bno,mid,writer,content,group_tab,TO_CHAR(regdate,'YYYY-MM-DD HH24:MI:SS') "
  				    +"FROM HC_BOARD_REPLY_2 "
  				    +"WHERE bno=? "
  				    +"ORDER BY group_id DESC,group_step ASC";
  		  ps=conn.prepareStatement(sql);
  		  ps.setInt(1, bno);
  		  ResultSet rs=ps.executeQuery();
  		  while(rs.next())
  		  {
  			  BoardReplyVO vo=new BoardReplyVO();
  			  vo.setBrno(rs.getInt(1));
  			  vo.setBno(rs.getInt(2));
  			  vo.setMid(rs.getString(3));
  			  vo.setWriter(rs.getString(4));
  			  vo.setContent(rs.getString(5));
  			  vo.setGroup_tab(rs.getInt(6));
  			  vo.setDbday(rs.getString(7));
  			  list.add(vo);
  		  }
  		  rs.close();
  	  }catch(SQLException e){
  		e.printStackTrace();
  	  }
  	  finally{
  		dbConn.closeConnection(ps, conn);
  	  }
  	  return list;
    }

    public void replyUpdate(int brno,String msg) 
    {
  	  try
  	  {
  	    conn = dbConn.createConnection();
  		  String sql="UPDATE project_reply SET "
  				    +"msg=? "
  				    +"WHERE brno=?";
  		  ps=conn.prepareStatement(sql);
  		  ps.setString(1, msg);
  		  ps.setInt(2, brno);
  		  ps.executeUpdate();
  	  }catch(SQLException e){
  		e.printStackTrace();
  	  }
  	  finally{
  		 dbConn.closeConnection(ps, conn);
  	  }
    }
    public void replyReplyWrite(int root,BoardReplyVO vo)
    {
  	  try
  	  {
  		conn = dbConn.createConnection();
  		  conn.setAutoCommit(false);
  		  String sql="SELECT group_id,group_step,group_tab "
  				    +"FROM HC_BOARD_REPLY_2 "
  				    +"WHERE brno=?";
  		  ps=conn.prepareStatement(sql);
  		  ps.setInt(1, root);
  		  ResultSet rs=ps.executeQuery();
  		  rs.next();
  		  int gi=rs.getInt(1);
  		  int gs=rs.getInt(2);
  		  int gt=rs.getInt(3);
  		  rs.close();
  		  sql="UPDATE HC_BOARD_REPLY_2 SET "
  		     +"group_step=group_step+1 "
  		     +"WHERE group_id=? AND group_step>?";
  		  ps=conn.prepareStatement(sql);
  		  ps.setInt(1, gi);
  		  ps.setInt(2, gs);
  		  ps.executeUpdate();
  		  sql="INSERT INTO project_reply(brno,bno,mid,writer,content,group_id,group_step,group_tab,root) "
  		     +"VALUES(hc_brno_seq.nextval,?,?,?,?,?,?,?,?)";
  		  ps=conn.prepareStatement(sql);
  		  ps.setInt(1, vo.getBno());
  		  ps.setString(2, vo.getMid());
  		  ps.setString(3, vo.getWriter());
  		  ps.setString(4, vo.getContent());
  		  ps.setInt(5, gi);
  		  ps.setInt(6, gs+1);
  		  ps.setInt(7, gt+1);
  		  ps.setInt(8, root);
  		  ps.executeUpdate();
  		  sql="UPDATE HC_BOARD_REPLY_2 SET "
  			 +"depth=depth+1 "
  			 +"WHERE brno=?";
  		  ps=conn.prepareStatement(sql);
  		  ps.setInt(1, root);
  		  ps.executeUpdate();
  		  conn.commit();
  	  }catch(Exception ex){
  		  ex.printStackTrace();
  		  try{
  			  conn.rollback();
  		  }catch(Exception e) {}
  	  }
  	  finally
  	  {
  		  try
  		  {
  			  conn.setAutoCommit(true);
  		  }catch(Exception ex) {}
  		 dbConn.closeConnection(ps, conn);
  	  }
    }

    public void replyDelete(int brno)
    {
  	  try
  	  {
  		conn = dbConn.createConnection();
  		  conn.setAutoCommit(false);
  		  String sql="SELECT root,depth FROM HC_BOARD_REPLY_2 "
  				    +"WHERE brno=?";
  		  ps=conn.prepareStatement(sql);
  		  ps.setInt(1, brno);
  		  ResultSet rs=ps.executeQuery();
  		  rs.next();
  		  int root=rs.getInt(1);
  		  int depth=rs.getInt(2);
  		  rs.close();
  		  if(depth==0)
  		  {
  			  sql="DELETE FROM HC_BOARD_REPLY_2 "
  				 +"WHERE brno=?";
  			  ps=conn.prepareStatement(sql);
  			  ps.setInt(1, brno);
  			  ps.executeUpdate();
  			  sql="UPDATE HC_BOARD_REPLY_2 SET "
  				 +"depth=depth-1 "
  				 +"WHERE brno=?";
  			  ps=conn.prepareStatement(sql);
  			  ps.setInt(1, root);
  			  ps.executeUpdate();
  			  }
  		  else
  		  {
  			  String msg="관리자가 삭제한 댓글입니다";
  			  sql="UPDATE HC_BOARD_REPLY_2 SET "
  				 +"msg=? "
  			     +"WHERE brno=?";
  			  ps=conn.prepareStatement(sql);
  			  ps.setString(1, msg);
  			  ps.setInt(2, brno);
  			  ps.executeUpdate();
  		  }
  		  conn.commit();
  	  }catch(Exception ex)
  	  {
  		  ex.printStackTrace();
  		  try
  		  {
  			  conn.rollback();
  		  }catch(Exception e) {}
  	  }
  	  finally
  	  {
  		  try
  		  {
  			  conn.setAutoCommit(true);
  			dbConn.closeConnection(ps, conn);
  		  }catch(Exception ex){}
  	  }
    }


	

  }
























