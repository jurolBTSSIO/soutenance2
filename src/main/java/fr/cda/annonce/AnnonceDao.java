package fr.cda.annonce;

public interface AnnonceDao {
    void add(Annonce annonce);
    void update(Annonce annonce);
    Annonce find(int id);
    void delete(int id);
    void truncate();
}
