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


@Named
@ViewScoped
public class PessoaBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Pessoa pessoa = new Pessoa();
	
	private List<Pessoa> pessoas;
	
	@PostConstruct
	public void init() {
		EntityManager em = EntityManagerProvider.getEntityManager();
		try {
			this.pessoas = em.createQuery("SELECT p FROM Pessoa p", Pessoa.class).getResultList();
		} finally {
			em.close();
		}
	}
	
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
	
	public void cancelar() {
		this.pessoa = new Pessoa();
	}
	
	public Date getHoje() {
		return new Date();
	}
	

}
