package smarteye;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import smarteye.Communication;

class Base2Donnees {
	public int code_image;

	public String prenom;
	public String nom;
	public String tel;
	public int age;
	public String mail;

	public final String Base2Donnees_name = "smarteye";
	public final String Base2Donnees_user = "root";
	public final String Base2Donnees_pass = "";

	public Connection con;

	public boolean init() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try {
				this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Base2Donnees_name, Base2Donnees_user,
						Base2Donnees_pass);
			} catch (SQLException e) {

				System.out.println("Echec de la connexion à la base de données");

				return false;

			}

		} catch (ClassNotFoundException e) {

			e.printStackTrace();

			return false;
		}

		return true;
	}

	public void insert() {
		String sql = "INSERT INTO image_rec (code_image, prenom, nom, tel, age , mail) VALUES (?, ?, ?, ?,?,?)";

		PreparedStatement statement = null;
		try {
			statement = con.prepareStatement(sql);
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}

		try {

			statement.setInt(1, this.code_image);
			statement.setString(2, this.prenom);

			statement.setString(3, this.nom);
			statement.setString(4, this.tel);
			statement.setInt(5, this.age);
			statement.setString(6, this.mail);

			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("A new face data was inserted successfully!");
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

	}

	public ArrayList<String> getUser(int inCode) throws SQLException {

		ArrayList<String> user = new ArrayList<String>();

		try {

			//Base2Donnees app = new Base2Donnees();

			String sql = "select * from image_rec where code_image=" + inCode + " limit 1";

			Statement s = con.createStatement();

			ResultSet rs = s.executeQuery(sql);

			while (rs.next()) {



				user.add(0, Integer.toString(rs.getInt(2)));
				user.add(1, rs.getString(3));
				user.add(2, rs.getString(4));
				user.add(3, Integer.toString(rs.getInt(5)));
				user.add(4, Integer.toString(rs.getInt(6)));
				user.add(5, rs.getString(7));




			}

			con.close(); 
		} catch (Exception e) {
			e.getStackTrace();
		}
		return user;
	}

	public void db_close() throws SQLException
	{
		try {
			con.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}


	public int getCode_image() {
		return code_image;
	}

	public void setCode_image(int code_image) {
		this.code_image = code_image;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String GetNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

}
