package com.ktds.member.service;

import com.ktds.community.dao.CommunityDao;
import com.ktds.member.dao.MemberDao;
import com.ktds.member.vo.MemberVO;
import com.ktds.util.SHA256Util;

public class MemberServiceImpl implements MemberService {
	private MemberDao memberDao;
	private CommunityDao communityDao;
	
	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}

	public void setCommunityDao(CommunityDao communityDao) {
		this.communityDao = communityDao;
	}

	@Override
	public boolean readCountMemberEmail(String email) {
		return memberDao.selectCountMemberEmail(email) > 0;
	}
	
	@Override
	public boolean readCountMemberNickname(String nickname) {
		return memberDao.selectCountMemberNickname(nickname) > 0;
	}
	
	@Override
	public boolean createMember(MemberVO memberVO) {
		String salt = SHA256Util.generateSalt();
		memberVO.setSalt(salt);
		
		String password = memberVO.getPassword();
		password = SHA256Util.getEncrypt(password, salt); // SHA256방식을 이용해 암호화
		memberVO.setPassword(password);
		
		return memberDao.insertMember(memberVO) > 0 ;
	}

	@Override
	public MemberVO readMember(MemberVO memberVO) {
		// TNS Listener Error
		// 1. 시작 - Oracle - Stop Database
		// 2. 시작 - Oracle - Start Database
		
		// SHA256를 이용해 다시 한 번 암호화해야 함
		// 1. 사용자의 ID로 SALT 가져오기
		String salt = memberDao.selectSalt(memberVO.getEmail());
		if (salt == null) {
			salt = "";
		}
		// 2. SALT로 암호화 하기
		String password = memberVO.getPassword();
		password = SHA256Util.getEncrypt(password, salt);
		memberVO.setPassword(password);
		
		return memberDao.selectMember(memberVO);
	}

	@Override
	public boolean removeMember(int id, String deleteFlag) {
		if (deleteFlag.equals("y")) {
				communityDao.deleteMyCommunities(id); // community와 member의 관계는 1:多이기 때문에 조건문 사용 x - null 주의
		}
		return memberDao.deleteMember(id) > 0;
	}
	
}
