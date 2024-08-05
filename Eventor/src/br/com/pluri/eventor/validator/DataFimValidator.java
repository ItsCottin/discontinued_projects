package br.com.pluri.eventor.validator;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


@FacesValidator(value = "dataFimValidator")
public class DataFimValidator implements Validator {

	private static final String DATA_FINAL_MAIOR_QUE_DATA_INICIAL = "data.final.maior.que.data.inicial";
	private static final String ATTRIBUTE_NOT_SUPORTED = "com.sun.faces.ATTRIBUTE_NOT_SUPORTED";
	private static final String MESSAGE_PAST_DATE_VALIDATOR = "PastDateValidator.invalido";
	private static final String DATE_REQUIRED = "datas.intervalo.obrigatorias";
	
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		String fieldInitialDate = (String) component.getAttributes().get("fieldInitialDate");
		UIInput inputDataInicial = (UIInput) context.getViewRoot().findComponent(fieldInitialDate);
		
		if (fieldInitialDate == null) {
			throw new ValidatorException(ValidatorUtils.makeFacesMessage(ATTRIBUTE_NOT_SUPORTED, "fieldInitialDate", "UIInput['Data Inicial']"));
		}
		
		String labelDataInicio = ValidatorUtils.getTitulo(inputDataInicial);
		String labelDataFim = ValidatorUtils.getTitulo(component);
		String strDateInterval = (String) component.getAttributes().get("dateInterval");
		
		if (strDateInterval == null) {
			throw new ValidatorException(ValidatorUtils.makeFacesMessage(ATTRIBUTE_NOT_SUPORTED, "dateInterval", "UIInput['" + labelDataFim + "']"));
		}
				
		Date dataInicial = (Date) inputDataInicial.getValue();
		Date dataFinal = (Date) value;
		Date today = new Date();
		
		if (dataInicial != null && dataFinal != null) {
			if (dataFinal.before(dataInicial)) {
				inputDataInicial.setValid(false);
				throw new ValidatorException(ValidatorUtils.makeFacesMessage(DATA_FINAL_MAIOR_QUE_DATA_INICIAL, labelDataFim, labelDataInicio));
			}
		} else if (dataInicial != null || dataFinal != null) {
			inputDataInicial.setValid(false);
			throw new ValidatorException(ValidatorUtils.makeFacesMessage(DATE_REQUIRED, labelDataInicio, labelDataFim));
		}
		
		if (value != null) {
			if (dataInicial.after(today)) {
				inputDataInicial.setValid(false);
				((UIInput) component).setValid(true);
				throw new ValidatorException(ValidatorUtils.makeFacesMessage(MESSAGE_PAST_DATE_VALIDATOR, labelDataInicio));
			}
			
			if (dataFinal.after(today)) {
				throw new ValidatorException(ValidatorUtils.makeFacesMessage(MESSAGE_PAST_DATE_VALIDATOR, labelDataFim));
			}
			
		}
	}
}
