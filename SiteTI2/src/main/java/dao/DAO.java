package dao;

public interface DAO<T>{
    
    public T get(int key);
    public void add(T p) throws Exception;
    public void update(T p);
    public void delete(T p);
    
}