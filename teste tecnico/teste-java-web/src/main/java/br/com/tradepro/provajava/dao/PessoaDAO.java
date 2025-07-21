package br.com.tradepro.provajava.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.tradepro.provajava.entity.Pessoa;

/**
 * Data Access Object (DAO) para a entidade {@link Pessoa}.
 * <p>
 * Esta classe encapsula todas as operações de acesso ao banco de dados
 * (CRUD - Create, Read, Update, Delete) relacionadas à entidade Pessoa.
 * Ela utiliza um {@link EntityManager} para interagir com a camada de persistência.
 *
 * @version 1.0
 */
public class PessoaDAO {

	/**
	 * O EntityManager, contexto de persistência injetado para realizar as operações com o banco de dados.
	 */
	private EntityManager entityManager;

	/**
	 * Construtor que recebe uma instância do EntityManager.
	 *
	 * @param entityManager O {@link EntityManager} a ser utilizado pelo DAO.
	 */
	public PessoaDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	/**
	 * Busca e retorna todas as entidades {@link Pessoa} cadastradas no banco de dados.
	 *
	 * @return Uma {@link List} contendo todas as pessoas. A lista pode estar vazia se não houver registros.
	 */
	public List<Pessoa> listAll() {
		return entityManager.createQuery("select p from pessoa p", Pessoa.class)
				.getResultList();
	}
}
