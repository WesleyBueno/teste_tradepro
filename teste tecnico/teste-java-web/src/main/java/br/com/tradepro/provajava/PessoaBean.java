package br.com.tradepro.provajava;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParsingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.tradepro.provajava.entity.Pessoa;

/**
 * Backing bean (controller) para a página de cadastro de pessoas (cadastro.xhtml).
 * <p>
 * Esta classe gerencia o estado da visão, incluindo o objeto {@link Pessoa} sendo
 * criado ou editado no formulário e a lista de pessoas já cadastradas exibida na tabela.
 * <p>
 * Utiliza o escopo {@code @ViewScoped} para manter o estado da página durante
 * as interações AJAX do usuário, como a busca de CEP e o salvamento de dados.
 *
 * @author Wesley Bueno
 * @version 1.0
 * @since 2025-07-18
 */
@Named
@ViewScoped
public class PessoaBean implements Serializable {

	/**
	 * Controle de versão para a serialização do objeto.
	 */
	private static final long serialVersionUID = 1L;
	
	/** Objeto que armazena os dados da nova pessoa preenchida no formulário. */
	private Pessoa pessoa = new Pessoa();
	
	/** Lista de todas as pessoas cadastradas, usada para popular a p:dataTable na view. */
	private List<Pessoa> pessoas;
	
	/**
	 * Método de inicialização executado uma vez após a criação do bean, com o {@code @PostConstruct}.
	 * Carrega a lista de todas as pessoas a partir do banco de dados para popular a tabela na view.
	 */
	@PostConstruct
	public void init() {
		EntityManager em = EntityManagerProvider.getEntityManager();
		try {
			this.pessoas = em.createQuery("SELECT p FROM Pessoa p", Pessoa.class).getResultList();
		} finally {
			em.close();
		}
	}
	
	// --- Getters e Setters ---
	
	public List<Pessoa> getPessoas(){
		return pessoas;
	}

	public PessoaBean() {

	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	public Date getHoje() {
		return new Date();
	}
	
	
	
	/**
	 * Action listener para o evento AJAX 'blur' do campo CEP.
	 * <p>
	 * Consulta a API externa BrasilAPI com o CEP digitado pelo usuário, processa o
	 * retorno JSON e preenche automaticamente o campo de endereço do objeto {@code pessoa}.
	 */
	public void buscarCep() {
		String cep = this.pessoa.getCep();
		
		if(cep != null) {
			cep = cep.replaceAll("[^0-9]", "");
		}
		
		if(cep == null || cep.length() !=8) {
			return;
		}
		
		try {
			URL url = new URL("https://brasilapi.com.br/api/cep/v1/" + cep);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			
			if(conn.getResponseCode() != 200) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "CEP não encontrado ou inválido"));
				return;
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuilder response = new StringBuilder();
			String output;
			while ((output = br.readLine()) != null) {
				response.append(output);
			}
			conn.disconnect();
			
			 JsonReader jsonReader = (JsonReader) Json.createReader(new StringReader(response.toString()));
	         JsonObject jsonObject = jsonReader.readObject();
	            
	         String rua = jsonObject.getString("street", "");
	         String bairro = jsonObject.getString("neighborhood", "");
	         String cidade = jsonObject.getString("city", "");
	         String estado = jsonObject.getString("state", "");
	         
	         String enderecoCompleto = String.format("%s, %s, %s - %s", rua, bairro, cidade, estado).trim();
	         this.pessoa.setEndereco(enderecoCompleto);
	         
	         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Endereço encontrado!"));
		} catch(JsonParsingException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Formato inválido"));
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro Crítico", "Não foi possível encontrar a API"));
		}
	}
	
	/**
	 * Action method para o botão "Salvar".
	 * <p>
	 * Valida pelo nome e sobrenome, se a pessoa já existe no banco de dados. Se não
	 * existir, persiste o novo objeto {@link Pessoa}, atualiza a lista em memória
	 * para exibição na tabela e limpa o formulário para um novo cadastro.
	 * <p>
	 * Adiciona mensagens de sucesso ou erro ao {@link FacesContext}.
	 */
	public void salvar() {
		EntityManager em = EntityManagerProvider.getEntityManager();
		
		try {
			
			//valida se já existe o nome e sobrenome no banco dedados
			TypedQuery<Pessoa> query = em.createQuery("SELECT p FROM Pessoa p WHERE p.nome = :nome AND p.sobrenome = :sobrenome", Pessoa.class);
			
			//pega os valores do formulario para a query
			query.setParameter("nome", this.pessoa.getNome());
			query.setParameter("sobrenome", this.pessoa.getSobrenome());
			
			try {
				query.getSingleResult();
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Este nome já existe"));
				return;
			} catch (NoResultException e) {}
			
			em.getTransaction().begin();
			em.persist(this.pessoa);
			em.getTransaction().commit();
			
			this.pessoas.add(this.pessoa);
			this.pessoa = new Pessoa();
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Cadastro realizado!"));
			
		} catch (Exception e) {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar", "Ocorreu um erro no salvamento dos dados"));
				e.printStackTrace();
			}
		} finally {
			em.close();
		}
	}
	
	/**
	 * Action method para o botão "Cancelar".
	 * Limpa os campos do formulário reiniciando o objeto {@code pessoa} com uma nova instância vazia.
	 */
	public void cancelar() {
		this.pessoa = new Pessoa();
	}
	

}
