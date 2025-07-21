package br.com.tradepro.provajava;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Test;

import br.com.tradepro.provajava.entity.Pessoa;

public class PessoaTest {
	
	@Test
	public void calcularIdade() {
		
		Pessoa pessoa = new Pessoa();
		
		LocalDate dataNasc = LocalDate.now().minusYears(20);
		Date dataNascimento = Date.from(dataNasc.atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		pessoa.setDataNascimento(dataNascimento);
		int idadeCalculada = pessoa.getIdade();
		
		assertEquals(20, idadeCalculada, "A idade deve ser 20.");
	}
	
	@Test public void DataNascNula() {
		
		Pessoa pessoa = new Pessoa();
		pessoa.setDataNascimento(null);
		
		int idadeCalculada = pessoa.getIdade();
		
		assertEquals(0, idadeCalculada, "A idade deve ser 0 caso a data de nascimento seja nula.");
	}

}
