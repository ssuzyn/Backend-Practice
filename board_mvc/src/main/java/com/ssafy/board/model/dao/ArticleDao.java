package com.ssafy.board.model.dao;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.board.model.BoardDto;

public interface ArticleDao {
	List<BoardDto> listAll() throws SQLException;
	int write(BoardDto dto) throws SQLException;
	BoardDto view(int articleNo) throws SQLException;
//	BoardDto view(BoardDto dto) throws SQLException; // 확장성이 좋다.
}
