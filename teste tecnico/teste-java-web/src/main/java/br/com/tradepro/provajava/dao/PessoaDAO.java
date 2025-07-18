package br.com.tradepro.provajava.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.tradepro.provajava.entity.Pessoa;

public class PessoaDAO {

	private EntityManager entityManager;

	public PessoaDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<Pessoa> listAll() {
		return entityManager.createQuery("select p from pessoa p", Pessoa.class)
				.getResultList();
	}
}
