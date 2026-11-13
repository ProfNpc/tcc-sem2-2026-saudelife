package br.com.belval.saudelife.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.belval.saudelife.model.Usuario;
import br.com.belval.saudelife.repository.UsuarioRepository;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    //curl -X POST http://localhost:8080/usuarios -H "Content-Type: application/json; Charset=utf-8" -d @usuario-pao.json
    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
    	
		usuario.setId(null);
		//Se houver algum campo que seja de uso interno 
		//esse eh o ponto para esse campo ser inicializado
		usuario.setSenhaHash("");
		
		
		System.out.println("Chegou : " + usuario.toString());
		
		Usuario salvo = usuarioRepository.save(usuario);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(salvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obterUsuario(@PathVariable UUID id) {
        Optional<Usuario> optional = usuarioRepository.findById(id);
        if (optional.isPresent()) {
            return ResponseEntity
            		.status(HttpStatus.OK)
            		.body(optional.get());
        }
        return ResponseEntity
        		.status(HttpStatus.NOT_FOUND)
        		.body("Usuario não encontrado!");
    }
    
	@GetMapping
	public ResponseEntity<Iterable<Usuario>> listarUsuarios() {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(usuarioRepository.findAll());
	}
	
	//curl -X PUT http://localhost:8080/usuarios/1 -H "Content-Type: application/json; Charset=utf-8" -d @usuario-pao2.json
	@PutMapping("/{id}")
	public ResponseEntity<Object> atualizarUsuario(
			@PathVariable UUID id,
			@RequestBody Usuario usuarioNovo) {
		
		Optional<Usuario> optional = usuarioRepository.findById(id);
		
		if (optional.isEmpty()) {

			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body("Usuario não encontrado!");
		}
		
		Usuario usuarioAtual = optional.get();
		
		//São sobreescritos todos os atributos menos 
		//id e dataCriacao, estes são mantidos.
		//Atualiza apenas os campos mutáveis.
		usuarioAtual.setNome(usuarioNovo.getNome());
		usuarioAtual.setEmail(usuarioNovo.getEmail());
		usuarioAtual.setTipo(usuarioNovo.getTipo());
		usuarioAtual.setObjetivo(usuarioNovo.getObjetivo());
		usuarioAtual.setDiasSemana(usuarioNovo.getDiasSemana());
		usuarioAtual.setPlano(usuarioNovo.getPlano());
		
		usuarioRepository.save(usuarioAtual);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body("Usuario atualizado com sucesso!");
	}

	//curl -X DELETE http://localhost:8080/usuarios/1
	@DeleteMapping("/{id}")
	public ResponseEntity<String> apagarUsuario(@PathVariable UUID id) {
		
		Optional<Usuario> optional = usuarioRepository.findById(id);
		
		if (optional.isEmpty()) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body("Usuario não encontrado!");
		}

		usuarioRepository.deleteById(id);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body("Usuario apagado com sucesso!");
	}
}
