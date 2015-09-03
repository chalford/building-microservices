package uk.co.bbc.vivo.tagpublisher;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class PosterConfiguration extends Configuration {
	
	@NotEmpty
	private String amqpUrl;
	
	@JsonProperty
	public String getAmqpUrl() {
		return amqpUrl;
	}
	
	@JsonProperty
	public void setAmqpUrl(String amqpUrl) {
		this.amqpUrl = amqpUrl;
	}

}
