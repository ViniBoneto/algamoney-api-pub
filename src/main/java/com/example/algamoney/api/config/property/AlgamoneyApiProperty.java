package com.example.algamoney.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("algamoney")
public class AlgamoneyApiProperty {

	//Origem permitida poderia, opcionalmente, ter sido agrupada dentro de segurança
	private String origemPermitida = "http://localhost:8000"; 
	
	private final Security security = new Security();
	
	public Security getSecurity() {
		return security;
	}

	public String getOrigemPermitida() {
		return origemPermitida;
	}

	public void setOrigemPermitida(String origemPermitida) {
		this.origemPermitida = origemPermitida;
	}


	public static class Security { //Cls estáticas internas podem agupar props p/ tema 
		
		private boolean enableHttps; //P/ default boolean é iniciado c/ false

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}
		
	}
}
