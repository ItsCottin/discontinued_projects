package br.com.pluri.eventor.business;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.etechoracio.common.business.BaseSB;
import br.com.etechoracio.common.view.MessageBundleLoader;
import br.com.pluri.eventor.business.exception.CEPInvalidoException;
import br.com.pluri.eventor.dao.EnderecoDAO;
import br.com.pluri.eventor.model.Endereco;

@Service
public class EnderecoSB extends BaseSB {

	private EnderecoDAO enderecoDAO;
	
	@Override
	protected void postConstructImpl() {
		enderecoDAO = getDAO(EnderecoDAO.class);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Endereco findCidadeAndEstadoByCEP(String cep) throws CEPInvalidoException {
		if (validaCEP(cep)) {
			return enderecoDAO.findEnderecoByCEP(cep);
		} else  {
			throw new CEPInvalidoException(MessageBundleLoader.getMessage("critica.cpfinvalido", new Object[] {cep.replace("-", "").replace("_", "")}));
		}
	}
	
	public boolean validaCEP (String cep) {
		String regex = "\\d{5}-\\d{3}";
		return cep.matches(regex);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Endereco> findEnderecoByIdCidAndCEP(Long idCid, String cep){
		return enderecoDAO.findEnderecoByIdCidAndCEP(idCid, cep);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Endereco> findEnderecoByIdCid(Long idCid){
		return enderecoDAO.findEnderecoByIdCid(idCid);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public List<Integer> findDDDInnerJoinCity(Long idCid){
		//List<Object[]> obj = enderecoDAO.findDDDInnerJoinCity(idCid);
		//List<Integer> dddList = new ArrayList<Integer>();
		//for (Object[] o : obj) {
	    //    Integer ddd = (Integer) o[0];
	    //    dddList.add(ddd);
	    //}
		return enderecoDAO.findDDDInnerJoinCity(idCid);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public Endereco findEnderecoByCEP(String cep){
		return enderecoDAO.findEnderecoByCEP(cep);
	}
	
}
