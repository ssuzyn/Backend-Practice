package com.ssafy.board.controller;

import java.io.IOException;
import java.util.List;

import com.ssafy.board.model.BoardDto;
import com.ssafy.board.model.service.ArticleService;
import com.ssafy.board.model.service.ArticleServiceImpl;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/article")
public class ArticleServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("act");
		
		switch(action) {
		case "mvwrite": // 글 작성 화면으로 넘어가기
			domvWrite(request, response);
			break;
		case "list": // 글 목록 보기
			doList(request, response);
			break;
		case "write": // 실제 글 작성
			doWrite(request, response);
			break;
		case "view": // 특정 글 보기
			doView(request, response);
			break;
		default:
			break;
		}
	}



	private void domvWrite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * send 하는 경우
		 * 글쓰기 버튼 클릭 -> root/article?act=mvwrite -> /board/write.jsp
		 * 결국 클라이언트가 jsp파일 url을 보게 된다. 클라이언트는 servlet을 보게 만드는게 좋다.
		 */
//		String root = request.getContextPath();
//		response.sendRedirect(root + "/board/write.jsp");
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/board/write.jsp");
		dispatcher.forward(request, response);
		
	}
	
	private void doWrite(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ArticleService service = new ArticleServiceImpl();
		BoardDto dto = new BoardDto();
		dto.setUserId(request.getParameter("userid"));
		dto.setContent(request.getParameter("content"));
		dto.setSubject(request.getParameter("subject"));
		
		try {
			int cnt = service.write(dto);
			if(cnt == 0) { // insert 실패 -> 이미 실패했으니 메인화면으로 이동
				response.sendRedirect(request.getContextPath() + "/index.jsp");
			}
			else { // insert 성공 -> 목록 페이지로 이동 (sendRedirect)
				// 현재 article?act=write 주소이기 때문에 클라이언트한테 list 주소로 보여야 한다.
				// board/list.jsp 로 하면 안된다!!!!!!!!
				response.sendRedirect(request.getContextPath() + "/article?act=list");
			}
			
			
		} catch (Exception e) {
			// 이미 오류난 url이므로 send로 메인화면 이동
			response.sendRedirect(request.getContextPath() + "/index.jsp");
		}
	}
	
	private void doList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArticleService service = new ArticleServiceImpl();
		List<BoardDto> list = null;
		
		try {
			list = service.listAll();
			request.setAttribute("list", list); // jsp가 사용할 수 있도록
			RequestDispatcher dispatcher = request.getRequestDispatcher("/board/list.jsp");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			// 이미 오류난 url이므로 send로 메인화면 이동
			response.sendRedirect(request.getContextPath() + "/index.jsp");
		}
		
	}
	
	private void doView(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		ArticleService service = new ArticleServiceImpl();
		BoardDto dto = null;
		
		try {
			dto = service.view(Integer.parseInt(request.getParameter("articleNo")));
			request.setAttribute("dto", dto);
			RequestDispatcher dispatcher = request.getRequestDispatcher("board/view.jsp");
			dispatcher.forward(request, response);
		}catch (Exception e) {
			response.sendRedirect(request.getContextPath() + "/index.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
