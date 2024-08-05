package br.com.etechoracio.common.view;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.etechoracio.common.model.BaseORM;

/**
 * Conversor para entidades.
 * 
 * <pre>
 * Last Modified  $Date: 2013/09/17 16:48:31 $
 * Last Modified by $Author: Rogério de Morais $
 * </pre>
 * 
 * @author Rogério de Morais
 * @version $Revision: 1.0 $
 */
@FacesConverter(value = "entityConverter")
public class EntityConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (value != null) {
			return this.getAttributesFrom(component).get(value);
		}
		return null;
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value != null && !"".equals(value)) {

			BaseORM entity = (BaseORM) value;

			if (entity.getId() == null) {
				return "";
			}

			this.addAttribute(component, entity);

			Long codigo = (Long) entity.getId();
			if (codigo != null) {
				return String.valueOf(codigo);
			}
		}
		return (String) value;
	}

	protected void addAttribute(UIComponent component, BaseORM o) {
		String key = o.getId().toString();
		this.getAttributesFrom(component).put(key, o);
	}

	protected Map<String, Object> getAttributesFrom(UIComponent component) {
		return component.getAttributes();
	}

}
