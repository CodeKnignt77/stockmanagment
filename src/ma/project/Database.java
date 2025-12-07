// xof whatsapp dyalk atl9a fih xi updates 9alomlk patron lah yrda 3lih

/*
package ma.project;


public class Database {

    private Connection conn;

    public Database() {
        try {
            // This creates/opens db.sqlite file automatically
            conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
            System.out.println("Connexion à SQLite réussie !");

            creerTables();
        } catch (SQLException e) {
            System.out.println("ERREUR connexion : " + e.getMessage());
        }
    }

    }

    private void creerTables() throws SQLException {
        String sqlClients = """
            CREATE TABLE IF NOT EXISTS clients (
                id INTEGER PRIMARY KEY,
                nom TEXT,
                telephone TEXT,
                adresse TEXT,
                ice TEXT,
                email TEXT
            )""";

        String sqlProduits = """
            CREATE TABLE IF NOT EXISTS produits (
                id INTEGER PRIMARY KEY,
                nom TEXT,
                prixAchatHT REAL,
                prixVenteTTC REAL,
                quantite INTEGER
            )""";

        String sqlFactures = """
            CREATE TABLE IF NOT EXISTS factures (
                id INTEGER PRIMARY KEY,
                numero TEXT,
                date TEXT,
                clientId INTEGER,
                totalHT REAL,
                totalTVA REAL,
                totalTTC REAL
            )""";

        Statement stmt = conn.createStatement();
        stmt.execute(sqlClients);
        stmt.execute(sqlProduits);
        stmt.execute(sqlFactures);
        System.out.println("Tables créées avec succès !");
    }

    public void sauvegarderClient(Client c) {
        String sql = "INSERT OR REPLACE INTO clients(id,nom,telephone,adresse,ice,email) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, c.getId());
            p.setString(2, c.getNomComplet());
            p.setString(3, c.getTelephone());
            p.setString(4, c.getAdresse());
            p.setString(5, c.getIce());
            p.setString(6, c.getEmail());
            p.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur client : " + e.getMessage());
        }
    }

    public void sauvegarderProduit(Produit p) {
        String sql = "INSERT OR REPLACE INTO produits(id,nom,prixAchatHT,prixVenteTTC,quantite) VALUES(?,?,?,?,?)";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, p.getId());
            p.setString(2, p.getNom());
            p.setDouble(3, p.getPrixAchatHT());
            p.setDouble(4, p.getPrixVenteTTC());
            p.setInt(5, p.getQuantite());
            p.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur produit : " + e.getMessage());
        }
    }

    public void sauvegarderFacture(Facture f) {
        String sql = "INSERT OR REPLACE INTO factures(id,numero,date,clientId,totalHT,totalTVA,totalTTC) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, f.getId());
            p.setString(2, f.getNumero());
            p.setString(3, f.getDate());
            p.setInt(4, f.getClient().getId());
            p.setDouble(5, f.getTotalHT());
            p.setDouble(6, f.getTotalTVA());
            p.setDouble(7, f.getTotalTTC());
            p.executeUpdate();
            System.out.println("Facture sauvegardée → " + f.getNumero());
        } catch (SQLException e) {
            System.out.println("Erreur facture : " + e.getMessage());
        }
    }
}*/



