package annonce;

/**
 * Classe Annonce
 * @author cda
 */
public class Annonce {
    private int id;
    private String titre;
    private String description;
    private double prix;
    private double surface;
    private int id_ville;
    private int id_type;

    /**
     * Contructeur
     * @param titre
     * @param description
     * @param prix
     * @param surface
     * @param id_ville
     * @param id_type
     */
    public Annonce(String titre, String description, double prix, double surface, int id_ville, int id_type) {
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.surface = surface;
        this.id_ville = id_ville;
        this.id_type = id_type;
    }

    /**
     * Constructeur de la classe Annnonce
     * @param titre
     * @param description
     * @param prix
     * @param surface
     */
    public Annonce( String titre, String description, double prix, double surface) {
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.surface = surface;
    }

    /**
     * Constructeur vide
     */
    public Annonce() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public int getId_ville() {
        return id_ville;
    }

    public void setId_ville(int id_ville) {
        this.id_ville = id_ville;
    }

    public int getId_type() {
        return id_type;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    /**
     * Methode toString()
     * @return
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Annonce{");
        sb.append("id=").append(id);
        sb.append(", titre='").append(titre).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", prix=").append(prix);
        sb.append(", surface=").append(surface);
        sb.append('}');
        return sb.toString();
    }
}
