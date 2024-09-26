package com.ssafy.board.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ssafy.board.model.BoardDto;
import com.ssafy.util.DBUtil;

public class ArticleDaoImpl implements ArticleDao{

	@Override
	public List<BoardDto> listAll() throws SQLException{
		List<BoardDto> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		String sql = "select * from board order by article_no desc"; // * 대신 컬럼명 사용하기
		
		try {
			conn = DBUtil.getConnection();
			stmt = conn.prepareStatement(sql);
			rset = stmt.executeQuery();

			BoardDto dto = null;
			while(rset.next()) {
				dto = new BoardDto();
				dto.setArticleNo(rset.getInt("article_no"));
				dto.setContent(rset.getString("content"));
				dto.setHit(rset.getInt("hit"));
				dto.setRegisterTime(rset.getString("register_time"));
				dto.setSubject(rset.getString("subject"));
				dto.setUserId(rset.getString("user_id"));
				list.add(dto);
			}
		}
		finally {
			DBUtil.close(rset, stmt, conn);
		}
		return list;
	}
	
	@Override
	public int write(BoardDto dto) throws SQLException{
		int cnt = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		String sql = "insert into board(user_id, content, subject) values (?,?,?);";
		
		try {
			conn = DBUtil.getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, dto.getUserId());
			stmt.setString(2, dto.getContent());
			stmt.setString(3, dto.getSubject());
			cnt = stmt.executeUpdate();
			System.out.println("글쓰기 성공");
		}
		finally {
			DBUtil.close(stmt, conn);
		}
		
		return cnt;
	}

	@Override
	public BoardDto view(int articleNo) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		String sql = "select * from board where article_no = ?"; // * 대신 컬럼명 사용하기
		
		BoardDto dto = new BoardDto();
		try {
			conn = DBUtil.getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, articleNo);
			rset = stmt.executeQuery();
			
			while(rset.next()) {
				dto.setArticleNo(rset.getInt("article_no"));
				dto.setContent(rset.getString("content"));
				dto.setHit(rset.getInt("hit"));
				dto.setRegisterTime(rset.getString("register_time"));
				dto.setSubject(rset.getString("subject"));
				dto.setUserId(rset.getString("user_id"));

			}
		}
		finally {
			DBUtil.close(rset, stmt, conn);
		}
		
		return dto;
	}
	
}
