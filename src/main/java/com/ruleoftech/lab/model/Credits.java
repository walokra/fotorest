package com.ruleoftech.lab.model;

public class Credits {
	private Integer UserLimit;
	private Integer UserRemaining;
	private Long UserReset;
	private Integer ClientLimit;
	private Integer ClientRemaining;

	public Integer getUserLimit() {
		return UserLimit;
	}

	public void setUserLimit(Integer userLimit) {
		UserLimit = userLimit;
	}

	public Integer getUserRemaining() {
		return UserRemaining;
	}

	public void setUserRemaining(Integer userRemaining) {
		UserRemaining = userRemaining;
	}

	public Long getUserReset() {
		return UserReset;
	}

	public void setUserReset(Long userReset) {
		UserReset = userReset;
	}

	public Integer getClientLimit() {
		return ClientLimit;
	}

	public void setClientLimit(Integer clientLimit) {
		ClientLimit = clientLimit;
	}

	public Integer getClientRemaining() {
		return ClientRemaining;
	}

	public void setClientRemaining(Integer clientRemaining) {
		ClientRemaining = clientRemaining;
	}

}
