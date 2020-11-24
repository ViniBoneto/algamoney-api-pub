package com.example.algamoney.api.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class GeradorSenha {

	private static String senha = "admin";
	
	public static void main(String[] args) {
		if(args.length > 0)
			senha = args[0].trim();
			
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode(senha));
	}

}
