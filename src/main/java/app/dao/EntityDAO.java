package app.dao;

import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class EntityDAO<T> extends GenericDAO implements IEntityDAO
{
    public EntityDAO(EntityManagerFactory emf)
    {
        super(emf);
    }

    @Override
    public <T> T getEntityById(Integer id)
    {
        try
        {
            return super.getById((Class<T>) this.getClass(), id);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error getting entity by id", e);
        }
    }

    @Override
    public <T> T addEntity(Class<T> type, Class<T> secondType)
    {
        return null;
    }

    @Override
    public <T> T removeEntity(Class<T> type, Class<T> secondType)
    {
        return null;
    }

    @Override
    public <T> List<T> getEntitiesForEntity(Class<T> type)
    {
        return null;
    }
}
