package com.ssafy.board.model.service;

import java.util.List;

import com.ssafy.board.model.BoardDto;
import com.ssafy.board.model.dao.ArticleDao;
import com.ssafy.board.model.dao.ArticleDaoImpl;

public class ArticleServiceImpl implements ArticleService{
	
	@Override
	public List<BoardDto> listAll() throws Exception{
		ArticleDao dao = new ArticleDaoImpl();
		return dao.listAll();
	}

	@Override
	public int write(BoardDto dto) throws Exception {
		ArticleDao dao = new ArticleDaoImpl();
		return dao.write(dto);
	}

	@Override
	public BoardDto view(int articleNo) throws Exception {
		ArticleDao dao = new ArticleDaoImpl();
		return dao.view(articleNo);
	}
}
