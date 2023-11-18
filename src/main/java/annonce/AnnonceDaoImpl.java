package annonce;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnnonceDaoImpl implements AnnonceDao{
    protected Connection conn;
    public AnnonceDaoImpl(DaoFactory daoFactory) throws SQLException {
        try {
            this.conn = daoFactory.getConnection();
        } catch (SQLException e) {
            throw new SQLException("probleme driver manager ou acces bdd.");
        }

    }

    @Override
    public void add(Annonce annonce) {
        try {
            String sql = "INSERT INTO annonce (titre, description, prix, surface) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, annonce.getTitre());
            statement.setString(2, annonce.getDescription());
            statement.setDouble(3, annonce.getPrix());
            statement.setDouble(4, annonce.getSurface());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Annonce annonce) {
        try {
            String sql = "UPDATE annonce SET titre = ?, description = ?, prix = ?, surface = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, annonce.getTitre());
            statement.setString(2, annonce.getDescription());
            statement.setDouble(3, annonce.getPrix());
            statement.setDouble(4, annonce.getSurface());
            statement.setInt(5, annonce.getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            // Handle the exception
            e.printStackTrace();
        }
    }

    @Override
    public Annonce find(int id) {
        try {
            // Requete SQL
            String sql = "SELECT * FROM utlilisateur WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            // Je boucle sue le resultat
            if (resultSet.next()) {
                // J'instancie une annonce
                Annonce annonce = new Annonce();

                // Je lui assigne les valeurs de la base de données
                annonce.setId(resultSet.getInt("id"));
                annonce.setTitre(resultSet.getString("titre"));
                annonce.setDescription(resultSet.getString("description"));
                annonce.setPrix(resultSet.getDouble("prix"));
                annonce.setSurface(resultSet.getDouble("surface"));
                return annonce;
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(int id) {
        try {
            String sql = "DELETE FROM annonce WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No user with ID " + id + " found.");
            } else {
                System.out.println("User with ID " + id + " deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
