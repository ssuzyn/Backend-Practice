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
		} 
		else if ("view".equals(action)) {
			path = view(request, response);
			forward(request, response, path);
		} 
		else if ("mvwrite".equals(action)) {
			path = "/board/write.jsp";
			redirect(request, response, path);
		} 
		else if ("write".equals(action)) {
			path = write(request, response);
			redirect(request, response, path);
		} 
		else if ("mvmodify".equals(action)) {
			path = mvModify(request, response);
			forward(request, response, path);
		} 
		else if ("modify".equals(action)) {
			path = modify(request, response);
			forward(request, response, path);
		} 
		else if ("remove".equals(action)) {
			path = delete(request, response);
			redirect(request, response, path);
		} 
		else {
			redirect(request, response, path);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
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
				return "/index.jsp";
			}
		} else
			return "/user/login.jsp";
	}

	private String mvModify(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("userinfo");
		
		// TODO : 수정하고자하는 글의 글번호를 얻는다.
		int articleNo = Integer.parseInt(request.getParameter("articleno"));
		// TODO : 글번호에 해당하는 글정보를 얻고 글정보를 가지고 modify.jsp로 이동.
		BoardDto boardDto;
		try {
			boardDto = boardService.getArticle(articleNo);
			request.setAttribute("article", boardDto);
			return "board/modify.jsp";
		} catch (Exception e) {
			return "/article?action=list";
		}
	}

	private String modify(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("userinfo");
		
	    // 수정할 글 번호를 얻고 해당 글 정보를 다시 가져옵니다.
	    int articleNo = Integer.parseInt(request.getParameter("articleno"));
	    try {
	        BoardDto boardDto = boardService.getArticle(articleNo); // 글 정보를 가져옴

	        // 수정된 내용을 설정
	        boardDto.setSubject(request.getParameter("subject"));
	        boardDto.setContent(request.getParameter("content"));
	        System.out.println("수정할 내역 : " + boardDto.toString());

	        // 수정된 글 정보를 반영
	        boardService.modifyArticle(boardDto);

	        // 글 수정 후, 수정된 글을 보여주는 view로 이동
	        return "/article?action=view&articleno=" + boardDto.getArticleNo();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "/article?action=list"; // 오류 시 목록으로 이동
	    }
	}


	private String delete(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		MemberDto memberDto = (MemberDto) session.getAttribute("userinfo");
		
		// TODO : 삭제할 글 번호를 얻는다.
		int articleNo = Integer.parseInt(request.getParameter("articleno"));
		
		// TODO : 글번호를 파라미터로 service의 deleteArticle()을 호출.
		try {
			boardService.deleteArticle(articleNo);
			System.out.println("글 삭제 완료");
			// TODO : 글삭제 완료 후 list.jsp로 이동.(이후의 프로세스를 생각해 보세요.)
			return "/article?action=list";
		} catch (Exception e) {
			System.out.println("글 삭제 실패");
		}
		return "/article?action=list";
	}

}
