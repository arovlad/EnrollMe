package repository;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

public interface Repository<E> {

    /**
     *
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     */
    E find(Long id) throws SQLException;

    /**
     *
     * @param entity entity must be not null
     * @return the entity with the specified id or null - if there is no entity with the given id
     */
    E add(E entity) throws AlreadyInListException, FileNotFoundException, UnsupportedEncodingException, SQLException;

    /**
     * @return all entities
     */
    Iterable<E> getAll() throws SQLException;

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     */
    E remove(Long id) throws FileNotFoundException, UnsupportedEncodingException, SQLException;

    /**
     * @param entity entity must not be null
     * @return null - if the entity is updated, otherwise returns the entity - (e.g id does not exist).
     */
    E update(E entity) throws FileNotFoundException, UnsupportedEncodingException, SQLException;
}
