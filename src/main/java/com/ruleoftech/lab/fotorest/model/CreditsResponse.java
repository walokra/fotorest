package com.ruleoftech.lab.fotorest.model;

public class CreditsResponse {
	private Credits data;
	private Boolean success;
	private Integer status;

	public Credits getData() {
		return data;
	}

	public void setData(Credits data) {
		this.data = data;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
