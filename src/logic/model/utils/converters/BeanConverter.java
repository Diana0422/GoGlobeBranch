package logic.model.utils.converters;

import java.util.List;

import logic.persistence.exceptions.DatabaseException;

public interface BeanConverter<T1,T2> {

	public T2 convertToBean(T1 o) throws DatabaseException;
	public T1 convertFromBean(T2 o) throws DatabaseException;
	public List<T2> convertToListBean(List<T1> list) throws DatabaseException;
	public List<T1> convertFromListBean(List<T2> list) throws DatabaseException;
	
}
