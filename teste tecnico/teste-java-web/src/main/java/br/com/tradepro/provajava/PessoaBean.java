package br.com.tradepro.provajava;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import br.com.tradepro.provajava.entity.Pessoa;

@Named
@RequestScoped
public class PessoaBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Pessoa pessoa = new Pessoa();

	public PessoaBean() {

	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

}
