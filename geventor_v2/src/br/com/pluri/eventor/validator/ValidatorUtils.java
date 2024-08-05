package br.com.pluri.eventor.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;

public class ValidatorUtils {

	/**
	 * @param component
	 * @return
	 */
	public static String getTitulo(UIComponent component) {

		if (component instanceof HtmlInputText) {
			return ((HtmlInputText) component).getLabel();
		}
		if (component instanceof HtmlInputTextarea) {
			return ((HtmlInputTextarea) component).getLabel();
		}

		return component.getId();
	}

	public static FacesMessage makeFacesMessage(String messageKey, Object... args) {
		return new FacesMessage(FacesMessage.SEVERITY_ERROR,
				MessageBundleLoader.getMessage(messageKey, args), null);
	}
}
