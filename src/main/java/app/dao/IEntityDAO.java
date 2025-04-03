package app.dao;

import java.util.List;

public interface IEntityDAO
{
    <T> T getEntityById(Integer id);
    <T> T addEntity(Class<T> type, Class<T> secondType);
    <T> T removeEntity(Class<T> type, Class<T> secondType);
    <T> List<T> getEntitiesForEntity(Class<T> type);
}
