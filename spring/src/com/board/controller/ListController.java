package com.board.controller;

import com.board.VO.BoardVO;
import com.board.dao.BoardDao;
import com.board.paging.Paging;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ListController {
	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(getClass());
	private int pageSize = 10;
	private int blockCount = 10;
		
	@Autowired
	private BoardDao boardDao;

	@RequestMapping({ "/board/list.do" })
	public ModelAndView process(
			@RequestParam(value = "pageNum", defaultValue = "1") int currentPage,
			@RequestParam(value = "keyField", defaultValue = "") String keyField,
			@RequestParam(value = "keyWord", defaultValue = "") String keyWord) {
		String pagingHtml = "";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		HashMap<String, Object> map = new HashMap();
		map.put("keyField", keyField);
		map.put("keyWord", keyWord);

		int count = this.boardDao.getCount(map);

		Paging page = new Paging(keyField, keyWord, currentPage, count,
				this.pageSize, this.blockCount, "list.do");

		pagingHtml = page.getPagingHtml().toString();

		map.put("start", Integer.valueOf(page.getStartCount()));
		map.put("end", Integer.valueOf(page.getEndCount()));

		List<BoardVO> list = null;
		if (count > 0) {
			list = this.boardDao.list(map);
		} else {
			list = Collections.emptyList();
		}
		int number = count - (currentPage - 1) * this.pageSize;

		ModelAndView mav = new ModelAndView();
		mav.setViewName("boardList");
		mav.addObject("count", Integer.valueOf(count));
		mav.addObject("currentPage", Integer.valueOf(currentPage));
		mav.addObject("list", list);
		mav.addObject("pagingHtml", pagingHtml);
		mav.addObject("number", Integer.valueOf(number));

		return mav;
	}
	
	
	@RequestMapping({ "/board/write.do" })
	public ModelAndView process(){
	System.out.println("asdasdsa");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("write");

		return mav;
	}
	
	
	
	
}
