package com.ktds.community.dao;

import java.util.List;

import com.ktds.community.vo.CommunityVO;

public interface CommunityDao {
	// 전체 게시글의 목록 가져와서 보여주기
	public List<CommunityVO> selectAll();
	
	public CommunityVO selectOne(int id);
	
	public int selectMyCommunitiesCount(int userId);
	
	public List<CommunityVO> selectMyCommunities(int userId);
	
	public int incrementViewCount(int id); // 조회수 증가
	
	public int incrementRecommendCount(int id); // 추천수 증가

	public int insertCommunity(CommunityVO communityVO);
	
	public int deleteCommunity(int id);
	
	public int deleteCommunities(List<Integer> ids, int userId);
	
	public int deleteMyCommunities(int userId);
	
	public int updateCommunity(CommunityVO communityVO);
}
