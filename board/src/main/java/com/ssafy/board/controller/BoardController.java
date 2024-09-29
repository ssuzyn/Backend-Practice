package com.ssafy.board.controller;

import java.io.IOException;
import java.util.List;

import com.ssafy.board.model.BoardDto;
import com.ssafy.board.model.service.BoardService;
import com.ssafy.board.model.service.BoardServiceImpl;
import com.ssafy.member.model.MemberDto;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/article")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BoardService boardService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		boardService = BoardServiceImpl.getBoardService();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		String path = "";
		if ("list".equals(action)) {
			path = list(request, response);
			forward(request, response, path);
		} else if ("view".equals(action)) {
			path = view(request, response);
			forward(request, response, path);
		} else if ("mvwrite".equals(action)) {
			path = "/board/write.jsp";
			redirect(request, response, path);
		} else if ("write".equals(action)) {
			path = write(request, response);
			redirect(request, response, path);
		} else if ("mvmodify".equals(action)) {
			path = mvModify(request, response);
			forward(request, response, path);
		} else if ("modify".equals(action)) {
			path = modify(request, response);
			forward(request, response, path);
		} else if ("delete".equals(action)) {
			path = delete(request, response);
			redirect(request, response, path);
		} else {
			redirect(request, response, path);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private void forward(HttpServletRequest request, HttpServletResponse response, String path)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(path);
		dispatcher.forward(request, response);
	}

	private void redirect(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
		response.sendRedirect(request.getContextPath() + path);
	}

	private String list(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("userinfo");
		if (memberDto != null) {
			try {
				List<BoardDto> list = boardService.listArticle();
				request.setAttribute("articles", list);

				return "/board/list.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				return "/index.jsp";
			}
		} else
			return "/user/login.jsp";
	}

	private String view(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("userinfo");
		if (memberDto != null) {
			int articleNo = Integer.parseInt(request.getParameter("articleno"));
			try {
				BoardDto boardDto = boardService.getArticle(articleNo);
				boardService.updateHit(articleNo);
				request.setAttribute("article", boardDto);

				return "/board/view.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				return "/index.jsp";
			}
		} else
			return "/user/login.jsp";
	}

	private String write(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("userinfo");
		if (memberDto != null) {
			BoardDto boardDto = new BoardDto();
			boardDto.setUserId(memberDto.getUserId());
			boardDto.setSubject(request.getParameter("subject"));
			boardDto.setContent(request.getParameter("content"));
			try {
				boardService.writeArticle(boardDto);
				return "/article?action=list";
			} catch (Exception e) {
				e.printStackTrace();
				return "/index.jsp";
			}
		} else
			return "/user/login.jsp";
	}

	private String mvModify(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		MemberDto member = (MemberDto) session.getAttribute("userinfo");
		if(member != null) {
			int articleNo = Integer.parseInt(request.getParameter("articleno"));
			
			try {
				BoardDto board = boardService.getArticle(articleNo);
				request.setAttribute("article", board);
				return "/board/modify.jsp";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "/index.jsp";
			}
		}
		else
			return "/user/login.jsp";
	}

	private String modify(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession();
		MemberDto member = (MemberDto) session.getAttribute("userinfo");
		int articleNo = Integer.parseInt(request.getParameter("articleno"));

		if(member != null) {
			try {
				BoardDto board = boardService.getArticle(articleNo);
				board.setSubject(request.getParameter("subject"));
				board.setContent(request.getParameter("content"));
				
				System.out.println("수정할 내역" + board.toString());
				boardService.modifyArticle(board);
				
				return "/article?action=view&articleno=" + board.getArticleNo();
			} catch (Exception e) {
				return "/article?action=list";
			}
		}
		else
			return "/user/login.jsp";
	}

	private String delete(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		MemberDto member = (MemberDto) session.getAttribute("userinfo");
		int articleNo = Integer.parseInt(request.getParameter("articleno"));
		
		if(member != null) {
			try {
				boardService.deleteArticle(articleNo);
				return "/article?action=list";
			} catch (Exception e) {
				System.out.println("글 삭제 실패");
				return "/article?action=list";
			}
		}
		else
			return "/user/login.jsp";
	}

}
