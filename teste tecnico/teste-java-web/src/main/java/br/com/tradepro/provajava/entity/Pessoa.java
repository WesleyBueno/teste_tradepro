/**
 *
 */
package br.com.tradepro.provajava.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Representa a entidade Pessoa no banco de dados.
 * <p>
 * Esta classe é mapeada para a tabela "pessoa" e contém os dados cadastrais
 * de um indivíduo. Também serve como o modelo de dados (Model) para a
 * interface de usuário JSF.
 *
 * @author Wesley Bueno
 * @version 1.0
 * @since 2025-07-18
 */
@Entity
@Table(name = "pessoa")
public class Pessoa implements Serializable {

	/**
	 * Controle de versão para a serialização do objeto.
	 */
	private static final long serialVersionUID = 1L;

	/** Identificador único da pessoa, gerado automaticamente pelo banco de dados. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true)
	private Long id;

	/** Nome da pessoa. */
	@Column(name = "nome")
	private String nome;
	

	/** Sobrenome da pessoa. */
	@Column(name = "sobrenome")
	private String sobrenome;
	
	
	/**
	 * gênero da pessoa.
	 * Armazena "M" para Masculino e "F" para Feminino.
	 */
	@Column(name = "sexo")
	private String sexo;

	/** Data de nascimento da pessoa. Armazenada no banco apenas como data DD-MM-YYYY. */
	@Temporal(TemporalType.DATE)
	@Column(name = "data_nascimento")
	private Date dataNascimento;
	
	/**
	 * CEP informado pelo usuário.
	 * Este campo é transiente e utilizado apenas para a consulta na API de endereço,
	 * não sendo persistido no banco de dados.
	 */
	@Transient
	private String cep;
	
	
	/** Endereço completo da pessoa, obtido através da consulta de CEP. */
	@Column(name = "endereco")
	private String endereco;

	/**
	 * Construtor padrão, exigido pelo JPA.
	 */
	public Pessoa() {
	}
	
	/**
	 * Construtor parametrizado para criar uma instância de Pessoa com dados iniciais.
	 *
	 * @param id O identificador único.
	 * @param nome O nome da pessoa.
	 * @param sobrenome O sobrenome da pessoa.
	 * @param sexo O sexo da pessoa ('M' ou 'F').
	 * @param dataNascimento A data de nascimento da pessoa.
	 */
	public Pessoa(Long id, String nome, String sobrenome, String sexo, Date dataNascimento) {
		super();
		this.id = id;
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.sexo = sexo;
		this.dataNascimento = dataNascimento;
	}
	
	// --- Getters e Setters ---

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	/**
	 * Calcula a idade da pessoa em anos completos com base na data de nascimento.
	 * <p>
	 * Este é um campo calculado e não é persistido no banco de dados, conforme
	 * indicado pela anotação {@code @Transient}.
	 *
	 * @return A idade atual da pessoa em anos. Retorna 0 se a data de nascimento for nula.
	 */
	@Transient
	public int getIdade() {
		if(this.dataNascimento == null) {
			return 0;
		}
		
		LocalDate dataNasc = this.dataNascimento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		LocalDate hoje = LocalDate.now();
		
		return Period.between(dataNasc, hoje).getYears();
		
	}
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}
	
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	// --- hashCode e equals ---

		/**
		 * Gera o código hash para o objeto, baseado unicamente no seu {@code id}.
		 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	/**
	 * Compara este objeto Pessoa com outro para verificar se são iguais.
	 * A comparação é baseada unicamente no {@code id}.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Pessoa)) {
			return false;
		}
		Pessoa other = (Pessoa) obj;
		return Objects.equals(id, other.id);
	}

}
