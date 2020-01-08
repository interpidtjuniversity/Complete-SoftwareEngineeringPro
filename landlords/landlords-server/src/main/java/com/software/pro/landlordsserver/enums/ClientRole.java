package com.software.pro.landlordsserver.enums;

public enum ClientRole {

	PLAYER("玩家"),
	
	ROBOT("电脑"),

	;
	private String msg;

	private ClientRole(String msg) {
		this.msg = msg;
	}

	public final String getMsg() {
		return msg;
	}

	public final void setMsg(String msg) {
		this.msg = msg;
	}
}
