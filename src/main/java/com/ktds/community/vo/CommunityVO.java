package com.ktds.community.vo;

import java.io.File;
import java.io.IOException;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import com.ktds.member.vo.MemberVO;

public class CommunityVO {
	// id, 제목, 내용, 작성자(닉네임+아이디), 작성일, 조회수 + 댓글
	private int id;
	@NotEmpty(message="제목은 필수 입력입니다.")
	private String title;
	@NotEmpty(message="내용은 필수 입력입니다.")
	private String body;
	
	private int userId;
	
	private String writeDate;
	
	private int viewCount;
	private int recommendCount;
	private String requestIp;
	private MultipartFile file;
	private String filename;
	private String displayFilename;
	// 한 명의 회원은 하나의 게시글을 작성할 수 있다 (1:1 관계)
	// 이를 확인하기 위해 회원 정보를 가지고 있는 MemberVO를 import
	private MemberVO memberVO;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public int getRecommendCount() {
		return recommendCount;
	}

	public void setRecommendCount(int recommendCount) {
		this.recommendCount = recommendCount;
	}
	
	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}
	
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDisplayFilename() {
		if (displayFilename == null) {
			displayFilename = "";
		}
		return displayFilename;
	}

	public void setDisplayFilename(String displayFilename) {
		this.displayFilename = displayFilename;
	}
	
	public MemberVO getMemberVO() {
		return memberVO;
	}

	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}

	public String save() {
		
		if (file != null && !file.isEmpty()) {
			// file이 전송되고 그 파일이 비어있지 않다면
			displayFilename = file.getOriginalFilename();
			File newFile = new File("d:/uploadFiles/" + file.getOriginalFilename()); // upload하면 이 위치에 file을 써라
			try {
				file.transferTo(newFile);
				return newFile.getAbsolutePath(); // 전송시킨 파일의 절대경로를 반환
			} catch (IllegalStateException e) {
				throw new RuntimeException(e.getMessage(), e);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		
		return null;
	}
}
