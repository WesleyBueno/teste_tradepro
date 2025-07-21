/**
 *
 */
package br.com.tradepro.provajava;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.logging.SessionLog;

/**
 * Fornece instâncias do {@link EntityManager} para a aplicação.
 * <p>
 * Esta classe utilitária é responsável por criar e gerenciar o
 * {@link EntityManagerFactory} como um singleton, garantindo que a conexão com
 * o banco de dados seja configurada apenas uma vez.
 * <p>
 * As configurações do banco de dados (um H2 em memória) são definidas
 * programaticamente, incluindo a geração automática do esquema (DDL).
 *
 * @version 1.0
 */
public class EntityManagerProvider {

	/**
	 * Instância única e estática do EntityManagerFactory, para garantir que seja
	 * criada apenas uma vez (padrão Singleton).
	 */
	private static EntityManagerFactory emf = null;

	/**
	 * Obtém uma nova instância de {@link EntityManager}.
	 * <p>
	 * Este é o método público que deve ser chamado pela aplicação sempre que uma
	 * nova operação com o banco de dados for necessária.
	 *
	 * @return Um novo {@link EntityManager} pronto para uso.
	 */
	public static EntityManager getEntityManager() {
		EntityManager entityManager = getEntityManagerFactory().createEntityManager();
		return entityManager;
	}

	/**
	 * Estabelece a conexão com o banco de dados.
	 *
	 * @return O {@link EntityManagerFactory} usado na aplicação.
	 */
	private static EntityManagerFactory getEntityManagerFactory() {
		if (emf == null || !emf.isOpen()) {
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put(PersistenceUnitProperties.JDBC_URL, "jdbc:h2:mem:test");
			parametros.put(PersistenceUnitProperties.JDBC_USER, "sa");
			parametros.put(PersistenceUnitProperties.JDBC_PASSWORD, "");
			parametros.put(PersistenceUnitProperties.JDBC_DRIVER, "org.h2.Driver");
			parametros.put(PersistenceUnitProperties.LOGGING_LEVEL, SessionLog.FINE_LABEL);
			parametros.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.DROP_AND_CREATE);
			parametros.put(PersistenceUnitProperties.DDL_GENERATION_MODE, PersistenceUnitProperties.DDL_DATABASE_GENERATION);
			emf = Persistence.createEntityManagerFactory("prod", parametros);
		}
		return emf;
	}
}
