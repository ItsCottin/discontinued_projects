package br.com.pluri.eventor.view;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import lombok.Getter;
import lombok.Setter;
import br.com.etechoracio.common.view.BaseMB;
import br.com.pluri.eventor.business.AtividadeSB;
import br.com.pluri.eventor.model.Atividade;

@Getter
@Setter
@Scope("view")
@Controller
public class AtividadeMB extends BaseMB {

	@Autowired
	private AtividadeSB atividadeSB;
	private List<Atividade> resultadoAtividadeByEvento;
	private Atividade editAtividade = new Atividade();
	
	private Long idEvento;
	
	@PostConstruct
	public void PostConstruct(){
		//onEventoChange();
	}
	
	public void doSave(){
		atividadeSB.insert(editAtividade, idEvento);
		if(editAtividade.getId() == null){
			showInfoMessage("Atividade inserida com sucesso");
		}else{
			showInfoMessage("Atividade atualizada com sucesso");
		}
		onEventoChange();
	}
	
	public void onEventoChange(){
		resultadoAtividadeByEvento = atividadeSB.findByEventos(idEvento);
	}
	
	public void doRemove(Atividade exclui){
		//atividadeSB.delete(exclui);
	}
	
	public void doPrepareInsert(){
		this.editAtividade = new Atividade();
	}
	
	public void doEdit(Atividade edit){
		//this.editAtividade = atividadeSB.findById(edit.getId());
	}

}
