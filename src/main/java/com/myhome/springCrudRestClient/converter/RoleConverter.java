package com.myhome.springCrudRestClient.converter;

import com.myhome.springCrudRestClient.model.Role;
import com.myhome.springCrudRestClient.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A converter class used in views to map id's to actual role objects.
 */
@Component
public class RoleConverter implements Converter<Object, Role> {

	private final RoleService roleService;

	@Autowired
	public RoleConverter(RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * Gets Role by Id
	 * @see Converter#convert(Object)
	 */
	@Override
	public Role convert(Object element) {

		Integer id = Integer.parseInt((String)element);

		return roleService.get(id).orElseThrow(IllegalArgumentException::new);
	}
}