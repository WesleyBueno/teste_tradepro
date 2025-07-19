package br.com.tradepro.provajava;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

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
	
	public void salvar() {
		EntityManager em = EntityManagerProvider.getEntityManager();
		
		try {
			em.getTransaction().begin();
			em.persist(this.pessoa);
			em.getTransaction().commit();
			
			this.pessoa = new Pessoa();
		} catch (Exception e) {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
		} finally {
			em.close();
		}
	}

}
