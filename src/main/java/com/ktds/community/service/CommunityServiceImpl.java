package com.ktds.community.service;

import java.util.ArrayList;
import java.util.List;

import com.ktds.community.dao.CommunityDao;
import com.ktds.community.vo.CommunitySearchVO;
import com.ktds.community.vo.CommunityVO;

import io.github.seccoding.web.pager.Pager;
import io.github.seccoding.web.pager.PagerFactory;
import io.github.seccoding.web.pager.explorer.ClassicPageExplorer;
import io.github.seccoding.web.pager.explorer.PageExplorer;

public class CommunityServiceImpl implements CommunityService{
	// 의존 - 멤버변수, 생성자 추가 : service에서 dao를 사용하겠다
	private CommunityDao communityDao;
	
	public void setCommunityDao(CommunityDao communityDao) {
		this.communityDao = communityDao;
	}
	
	@Override
	public PageExplorer getAll(CommunitySearchVO communitySearchVO) {
		// Oracle pagenation을 사용하겠다
		Pager pager = PagerFactory.getPager(Pager.ORACLE, 
							communitySearchVO.getPageNo() + "", 
							communityDao.selectCountAll(communitySearchVO)); 
		
		PageExplorer pageExplorer = pager.makePageExplorer(ClassicPageExplorer.class, communitySearchVO);
		pageExplorer.setList(communityDao.selectAll(communitySearchVO));
		
		return pageExplorer;
	}

	@Override
	public boolean createCommunity(CommunityVO communityVO) {
		// insertCount = 1
		String body = communityVO.getBody();
		body = body.replace("\n", "<br/>"); // \n --> <br/>
		body = filter(body);
		communityVO.setBody(body);

		int insertCount = communityDao.insertCommunity(communityVO);
		return insertCount > 0;
	}

	@Override
	public CommunityVO getOne(int id) {
		return communityDao.selectOne(id);
	}
	
	@Override
	public boolean incrementViewCount(int id) {
		return communityDao.incrementViewCount(id) > 0;
	}


	@Override
	public boolean incrementRecommendCount(int id) {
		return communityDao.incrementRecommendCount(id) > 0;
	}
	
	public String filter(String str) {
		
		List<String> blackList = new ArrayList<String>();
		blackList.add("욕");
		blackList.add("씨");
		blackList.add("발");
		blackList.add("1식");
		blackList.add("종간나세끼");
		blackList.add("2식");
		
		// str ==> 남편은 2식이에요.
		String[] splitString = str.split(" ");
		for (String word : splitString) {
			// word에 들어있는 글자가 blackList에 포함되어 있는지 확인
			for (String blackString : blackList) {
				if ( word.contains(blackString)) {
					return "뿅뿅";
				}
			}
		}
		return str;
	}

	@Override
	public boolean removeCommunity(int id) {
		return communityDao.deleteCommunity(id) > 0;
	}
	
	@Override
	public boolean updateCommunity(CommunityVO communityVO) {
		return communityDao.updateCommunity(communityVO) > 0;
	}
	
	@Override
	public int readMyCommunitiesCount(int userId) {
		return communityDao.selectMyCommunitiesCount(userId);
	}
	
	@Override
	public List<CommunityVO> readMyCommunities(int userId) {
		return communityDao.selectMyCommunities(userId);
	}
	
	@Override
	public boolean deleteCommunities(List<Integer> ids, int userId) {
		return communityDao.deleteCommunities(ids, userId) > 0;
	}
}
