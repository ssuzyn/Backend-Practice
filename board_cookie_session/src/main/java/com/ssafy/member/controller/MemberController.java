package com.ssafy.member.controller;

import java.io.IOException;

import com.ssafy.member.model.MemberDto;
import com.ssafy.member.model.service.MemberService;
import com.ssafy.member.model.service.MemberServiceImpl;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/user")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MemberService memberService;
	
	public void init() {
		memberService = MemberServiceImpl.getMemberService();
	}
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		
		String path = "";
		if("mvjoin".equals(action)) {
			path = "/user/join.jsp";
			redirect(request, response, path);
		} else if("join".equals(action)) {
			path = join(request, response);
			redirect(request, response, path);
		} else if("mvlogin".equals(action)) {
			path = "/user/login.jsp";
			redirect(request, response, path);
		} else if("login".equals(action)) {
			path = login(request, response);
			forward(request, response, path);
		} else if("logout".equals(action)) {
			path = logout(request, response);
			redirect(request, response, path);
		} else {
			redirect(request, response, path);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		request.setCharacterEncoding("utf-8");
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
	
	private String join(HttpServletRequest request, HttpServletResponse response) {
		// TODO : 이름, 아이디, 비밀번호, 이메일등 회원정보를 받아 MemberDto로 setting.
		// TODO : 위의 데이터를 이용하여 service의 joinMember() 호출.
		// TODO : 정상 등록 후 로그인 페이지로 이동.
		return null;
	}
	
	private String login(HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("userid");
		String userPwd = request.getParameter("userpwd");
		
		try {
			MemberDto memberDto = memberService.loginMember(userId, userPwd); // dto 만들어서 넘기자
			if(memberDto != null) { // 로그인 성공
//				request.setAttribute("userinfo", memberDto); 
				// 계속해서 로그인 상태를 관리하고, 로그아웃도 해야하니까 request보단 session으로 관리하는 것이 좋겠다.
				// 그러면 jsp가 아닌 컨트롤러(서블릿)에서 세션 관리를 계속 해주면 된다.
				
				HttpSession session = request.getSession();
				session.setAttribute("userinfo", memberDto);
				
				String saveId = request.getParameter("saveid");
				
				if("ok".equals(saveId)) { // 아이디 저장 체크했다면
					// Cookie 생성
					Cookie cookie = new Cookie("SSAFY_ID", memberDto.getUserId());
					cookie.setMaxAge(60*60*24*365*5);
					cookie.setPath(request.getContextPath());
					response.addCookie(cookie); // 필수
				}
				else { // 아이디를 지우자
					Cookie[] cookies = request.getCookies();
					
					if(cookies != null){
						for(Cookie cookie : cookies){
							if(cookie.getName().equals("SSAFY_ID")){
								cookie.setMaxAge(0);
								cookie.setPath(request.getContextPath());
								response.addCookie(cookie); // 필수
								break;
							}
						}
					}
				}
				return "index.jsp";
			}
			else { // 로그인 실패
				return "/user/login.jsp";
			}
		}catch(Exception e) {
			return "/index.jsp";
		}
	}
	
	private String logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.removeAttribute("userinfo");
		session.invalidate();
		return "";
	}

}
