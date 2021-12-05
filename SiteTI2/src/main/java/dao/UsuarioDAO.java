package dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import model.Usuario;

public class UsuarioDAO extends Banco implements DAO<Usuario> {
	
	// Generic get
	@Override
	public Usuario get(int id) {
		Usuario usuario = null;
		try {
			Statement st = connection.createStatement();
			String sql = ("SELECT *"
					    + "FROM  usuario"
					    + "WHERE usuario.id = " + id);
			ResultSet rs = st.executeQuery(sql);
			
			usuario = new Usuario(
								rs.getString("cpf"),
								rs.getString("nome"), 
							    rs.getString("sobrenome"),
							    rs.getString("login"),
							    rs.getString("senha"),
							    rs.getString("celular")
					        );
			System.out.println("Sucess! --- " + usuario.toString());
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return usuario;
	}
	
	/**
	 * Get a partir do email
	 * @param email
	 * @return
	 */
	public Usuario get(String login) {
		Usuario usuario = null;
		System.out.print(login);
		try {
			Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = (
							"SELECT * "
						    + "FROM usuario " + "WHERE login = '" + login + "';"
	
					     );
			ResultSet rs = st.executeQuery(sql);
			
			if(rs.next()) {
				rs.beforeFirst();
				rs.next();
				
				usuario = new Usuario(
									rs.getString("cpf"),
									rs.getString("nome"), 
								    rs.getString("sobrenome"),
								    rs.getString("login"),
								    rs.getString("senha"),
								    rs.getString("celular")
							    );
				
				System.out.println("Sucess! --- " + usuario.toString());
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return usuario;
	}
	
	@Override
	public void add(Usuario usuario) throws Exception {
		
		 MessageDigest m= MessageDigest.getInstance("MD5");
		 m.update(usuario.getSenha() .getBytes(),0,usuario.getSenha() .length());
		
		String sql = ("INSERT into usuario (cpf, nome, sobrenome, login, senha, celular) values ("
					    + " '" + usuario.getCpf() + "', "
					    + " '" + usuario.getNome() + "', "
					    + " '" + usuario.getSobrenome()  + "', "
					    + " '" + usuario.getLogin()  + "', "
			            + " '" + new BigInteger(1,m.digest()).toString(16) + "', "
			            + " '" + usuario.getCelular()     + "');"
			          );
		try {
			Statement st = connection.createStatement();
			st.executeUpdate(sql);
			System.out.println("Sucess! --- " + usuario.toString());
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println(sql);
		}
	}

	@Override
	public void update(Usuario usuario) {
		try {
			Statement st = connection.createStatement();
			String sql = ("UPDATE Usuario SET (cpf, nome, sobrenome, login, senha, celular, tipo) values ("
						    + " cpf = '"      + usuario.getCpf()      + "', "
						    + " nome = '" + usuario.getNome() + "', "
						    + " sobrenome = '"       + usuario.getSobrenome()       + "',  "
						    + " login = '"      + usuario.getLogin()       + "',  "
				            + " senha = '"     + usuario.getSenha()     + "', "
				            + " celular = '"     + usuario.getCelular()     + "', "
				            + "WHERE cpf = " + usuario.getCpf()
						  );
			st.executeUpdate(sql);	
			System.out.println("Sucess! --- " + usuario.toString());
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
	}
	
	@Override
	public void delete(Usuario usuario) {
		try {
			Statement st = connection.createStatement();
			String sql = ("DELETE "
					    + "FROM usuario "
					    + "WHERE usuario.cpf =" + usuario.getCpf());
			st.executeUpdate(sql);
			System.out.println("Sucess! --- " + usuario.toString());
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
	}

	public ArrayList<Usuario> getAll() {
		
		ArrayList<Usuario> users = null;  
		
		try {
			Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = ("SELECT *"
					    + "FROM usuario");
			ResultSet rs = st.executeQuery(sql);
			
			if(rs.next()) {
				rs.last();
				users = new ArrayList<Usuario>(rs.getRow());
				rs.beforeFirst();
				
				for(@SuppressWarnings("unused")
				int i = 0; rs.next(); i++) {
					users.add( new Usuario(
							rs.getString("cpf"),
							rs.getString("nome"), 
						    rs.getString("sobrenome"),
						    rs.getString("login"),
						    rs.getString("senha"),
						    rs.getString("celular")
					        )
					);
				}
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return users;
	}
		
}