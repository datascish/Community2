package com.ktds.actionhistory.vo;

public interface ActionHistory {
	
	static public interface ReqType {
		public static final String VIEW = "view"; // page 보기
		public static final String MEMBER = "mbr"; 
		public static final String COMMUNITY = "com";
		public static final String UPLOAD = "upl";
		public static final String DOWNLOAD = "dwn";
		
	}
	
	static public interface LOG {
		public static final String VIEW = "View : %s, Method : %s";
		public static final String LOGIN = "Login : %s";
		public static final String LOGOUT = "Logout : %s"; // email
		public static final String REGIST = "Regist : Email(%s), Nickname(%s), Result(%s)";
		
		public static final String WRITE = "Write : Subject(%s), Body(%s)";
		public static final String UPDATE = "Update : Subject(%s), Body(%s)";
		public static final String DELETE = "Delete : ID(%d), Subject(%s), Body(%s)";
		public static final String READ = "Read : ID(%d), Subject(%s), Body(%s)";
		public static final String RECOMMEND = "Recommend : ID(%d)";
		public static final String WRITE_REPLY = "Write Reply : CommunityId(%d), Body(%s)";
		
		public static final String DOWNLOAD = "Download : CommunityId(%d), Filename(%s)";
		public static final String UPLOAD = "Upload : Filename(%s)"; 
	}
}
