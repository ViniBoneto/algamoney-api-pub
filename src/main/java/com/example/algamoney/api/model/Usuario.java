package com.example.algamoney.api.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "usuario")
public class Usuario {

	/* Ñ preciso usar a anotação @GeneratedValue, pois não serão registrados novos usuários	
	 * pela aplicação (ñ se precisa de coluna identidade auto incrementável). */
	@Id
	private Long codigo;
	
	@Column
	@NotNull
	@Size(min = 1, max = 50)
	private String nome;
	
	@Column
	@NotNull
	@Size(min = 1, max = 50)
	private String email;
	
	@Column
	@NotNull
	@Size(min = 1, max = 50)
	private String senha;
	
	@NotNull
	@ManyToMany(fetch = FetchType.EAGER) // FetchType.EAGER define q qdo o usuário for buscado, suas permissões tb serão
	@JoinTable( name = "usuario_permissao",
				joinColumns=@JoinColumn(name="codigo_usuario", referencedColumnName = "codigo"),
				inverseJoinColumns=@JoinColumn(name="codigo_permissao", referencedColumnName = "codigo") )
	private List<Permissao> permissoes;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
