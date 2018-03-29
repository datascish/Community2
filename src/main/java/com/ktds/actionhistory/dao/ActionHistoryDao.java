package com.ktds.actionhistory.dao;

import java.util.List;

import com.ktds.actionhistory.vo.ActionHistorySearchVO;
import com.ktds.actionhistory.vo.ActionHistoryVO;

public interface ActionHistoryDao {
	
	public int insertActionHistory(ActionHistoryVO actionHistoryVO);
	
	public List<ActionHistoryVO> selectAllActionHistroy(ActionHistorySearchVO actionHistorySearchVO);
	
	public int selectAllActionHistoryCount(ActionHistorySearchVO actionHistorySearchVO);
}
