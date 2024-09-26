package com.ssafy.board.model.service;

import java.util.List;

import com.ssafy.board.model.BoardDto;

public interface ArticleService {
	List<BoardDto> listAll() throws Exception;
	int write(BoardDto dto) throws Exception;
	BoardDto view(int articleNo) throws Exception;
}
