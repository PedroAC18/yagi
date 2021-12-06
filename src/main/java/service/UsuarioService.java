package service;

import java.math.BigInteger;
import java.security.MessageDigest;

import dao.UsuarioDAO;
import model.Usuario;
import spark.Request;
import spark.Response;

public class UsuarioService {
    private UsuarioDAO usuarioDAO;
    
    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    public Object add(Request request, Response response) throws Exception {
        usuarioDAO.connect();
        String cpf      = request.queryParams("cpf");
        String nome        = request.queryParams("nome"); 
        String sobrenome        = request.queryParams("sobrenome");
        String login       = request.queryParams("login");
        String senha      = request.queryParams("senha");
        String celular      = request.queryParams("celular");
        
        Usuario user = new Usuario(cpf, nome, sobrenome, login, senha, celular);
        
        
        if(((Usuario) usuarioDAO.get(login)) != null){
            usuarioDAO.close();
            return "Esse email ja existe";        
        }else if(usuarioDAO.getAll().contains(user)) {
            usuarioDAO.close();
            return "Esse cpf ja existe";        
        }
        
        usuarioDAO.add(user);
        
        response.status(201); // created
        //response.redirect("../index.html");
        
        usuarioDAO.close();
        
        return "Usuario cadastrado com sucesso!";
    }
    
    // Efetuar login pelo SENHA	
    public Object get(Request request, Response response) throws Exception {
        usuarioDAO.connect();
        
        String login = request.queryParams("login2");
        String senha = request.queryParams("senha2");
        
        Usuario usuario = (Usuario) usuarioDAO.get(login);
        
        
        usuarioDAO.close();
        
        response.header("Content-Type", "application/json");
        response.header("Content-Encoding", "UTF-8");
        
        MessageDigest m= MessageDigest.getInstance("MD5");
		m.update(senha.getBytes(),0,senha.length());
        
        boolean senhaCorreta = usuario.getSenha().compareTo(new BigInteger(1,m.digest()).toString(16)) == 0 ? true : false;
        
        if (usuario != null && senhaCorreta) {
        	System.out.println("Sucesso");
            return usuario.toJson();
        } else if(!senhaCorreta){
            response.status(403); // NOT FOUND
            System.out.println("Senha Incorreta");
            return "Senha Incorreta";
        }
        else {
            response.status(404); // NOT FOUND
            //response.redirect("404.html");
            System.out.println("Esse usuario não existe!");
            return "Esse usuario não existe!";
        }
    }

    
    
}