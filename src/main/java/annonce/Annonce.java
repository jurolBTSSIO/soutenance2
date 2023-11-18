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

    /**
     * Constructeur de la classe Annnonce
     * @param id
     * @param titre
     * @param description
     * @param prix
     * @param surface
     */
    public Annonce(int id, String titre, String description, double prix, double surface) {
        this.id = id;
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
