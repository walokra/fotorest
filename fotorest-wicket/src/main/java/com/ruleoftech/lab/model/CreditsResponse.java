package com.ruleoftech.lab.model;

import java.io.Serializable;

public class CreditsResponse implements Serializable {
	private static final long serialVersionUID = 1L;

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
