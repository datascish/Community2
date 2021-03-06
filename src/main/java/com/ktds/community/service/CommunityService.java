package com.ktds.community.service;

import java.util.List;

import com.ktds.community.vo.CommunitySearchVO;
import com.ktds.community.vo.CommunityVO;

import io.github.seccoding.web.pager.explorer.PageExplorer;

public interface CommunityService {
	public PageExplorer getAll(CommunitySearchVO communitySearchVO);
	
	public CommunityVO getOne(int id);
	
	public int readMyCommunitiesCount(int userId);
	
	public List<CommunityVO> readMyCommunities(int userId);

	public boolean incrementRecommendCount(int id);
	
	public boolean incrementViewCount(int id);
	
	public boolean createCommunity(CommunityVO communityVO);
	
	public boolean removeCommunity(int id);
	
	public boolean updateCommunity(CommunityVO communityVO);
	
	public boolean deleteCommunities(List<Integer> ids, int userId);
}
