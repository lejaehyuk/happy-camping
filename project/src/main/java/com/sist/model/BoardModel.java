package com.sist.model;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sist.controller.annotation.Controller;
import com.sist.controller.annotation.RequestMapping;
import com.sist.dao.BoardDAO;
import com.sist.util.Pagination;
import com.sist.vo.BoardReplyVO;
import com.sist.vo.BoardVO;

@Controller
public class BoardModel {

    private final BoardDAO boardDAO;

    public BoardModel() {
        this.boardDAO = new BoardDAO();
    }

    @RequestMapping("board/list.do")
    public String boardListPage(HttpServletRequest request, HttpServletResponse response) {
        String page = request.getParameter("page");
        if (page == null) {
            page = "1";
        }
        Pagination pagination = boardDAO.boardList(Integer.parseInt(page));
        request.setAttribute("page", pagination);
        return "/happy/board/board_list.jsp";
    }

    @RequestMapping("board/write.do")
    public String boardWritePage(HttpServletRequest request, HttpServletResponse response) {
        return "/happy/board/board_write.jsp";
    }

    @RequestMapping("board/detail.do")
    public String boardDetailPage(HttpServletRequest request, HttpServletResponse response) {
        int no = Integer.parseInt(request.getParameter("no"));
        BoardVO board = boardDAO.boardDetail(no);
        request.setAttribute("board", board);
        BoardDAO dao=new BoardDAO();
        BoardVO vo=new BoardVO();
        List<BoardReplyVO> list=dao.replyListData(no);
  	    request.setAttribute("list", list);
  	    request.setAttribute("count", list.size());
        return "/happy/board/board_detail.jsp";
    }

    @RequestMapping("board/update.do")
    public String boardUpdatePage(HttpServletRequest request, HttpServletResponse response) {
        int no = Integer.parseInt(request.getParameter("no"));
        BoardVO board = boardDAO.boardDetail(no);
        request.setAttribute("board", board);
        return "/happy/board/board_update.jsp";
    }

    @RequestMapping("boardWrite.do")
    public void boardWrite(HttpServletRequest request, HttpServletResponse response) {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String mid = (String) request.getSession().getAttribute("mid");
        String writer = (String) request.getSession().getAttribute("name");
        boardDAO.boardWrite(title, content, writer, mid);
    }

    @RequestMapping("boardUpdate.do")
    public void boardUpdate(HttpServletRequest request, HttpServletResponse response) {
        int no = Integer.parseInt(request.getParameter("no"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        boardDAO.boardUpdate(no, title, content);
    }

    @RequestMapping("boardDelete.do")
    public String boardDelete(HttpServletRequest request, HttpServletResponse response) {
        int no = Integer.parseInt(request.getParameter("no"));
        boardDAO.boardDelete(no);
        return "redirect:board/list.do";
    }
    //댓글
    @RequestMapping("board/replywrite.do")
    public String replyWrite(HttpServletRequest request,HttpServletResponse response)
    {
  	  try{
  		  request.setCharacterEncoding("UTF-8");
  	  }catch(Exception ex) {}
  	  HttpSession session=request.getSession();
  	  
  	  String bno=request.getParameter("bno");
  	  String writer=(String)session.getAttribute("name");
  	  String mid=(String)request.getSession().getAttribute("mid");
  	  String content=request.getParameter("content");
  	  BoardReplyVO vo=new BoardReplyVO();
  	  vo.setBno(Integer.parseInt(bno));
  	  vo.setMid(mid);
  	  vo.setWriter(writer);
  	  vo.setContent(content);
  	  BoardDAO dao=new BoardDAO();
  	  dao.replyWrite(vo);
  	  return "redirect:detail.do?no="+bno;
    }
    @RequestMapping("board/replyupdate.do")
    public String replyUpdate(HttpServletRequest request,HttpServletResponse response)
    {
  	  try{
            request.setCharacterEncoding("UTF-8");
  	  }catch(Exception ex) {}
  	  String bno=request.getParameter("bno");
  	  String brno=request.getParameter("brno");
  	  String content=request.getParameter("content");
  	  //DAO연결
  	  BoardDAO dao=new BoardDAO();
  	  dao.replyUpdate(Integer.parseInt(brno), content);
  	  return "redirect:detail.do?no="+bno;
    }
    @RequestMapping("board/replyReplyWrite.do")
    public String reply_replyWrite(HttpServletRequest request,HttpServletResponse response)
    {
  	  try{
            request.setCharacterEncoding("UTF-8");
  	  }catch(Exception ex) {}
  	  String bno=request.getParameter("bno");
  	  String pno=request.getParameter("pno");
  	  String content=request.getParameter("content");

  	  String mid=(String)request.getSession().getAttribute("mid");
  	  String writer=(String)request.getSession().getAttribute("writer");

  	  BoardReplyVO vo=new BoardReplyVO();
  	  vo.setBno(Integer.parseInt(bno));
  	  vo.setMid(mid);
  	  vo.setWriter(writer);
  	  vo.setContent(content);
  	  BoardDAO dao=new BoardDAO();
  	  dao.replyReplyWrite(Integer.parseInt(pno), vo);
  	  return "redirect:detail.do?no="+bno;
    }
    @RequestMapping("board/replyDelete.do")
    public String replyDelete(HttpServletRequest request,HttpServletResponse response)
    {
    	String bno=request.getParameter("bno");
  	  String brno=request.getParameter("brno");
  	  BoardDAO dao=new BoardDAO();
  	  dao.replyDelete(Integer.parseInt(brno));
  	  return "redirect:detail.do?no="+bno;
    }
  }