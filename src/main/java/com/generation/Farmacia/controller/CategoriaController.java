package com.generation.Farmacia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.generation.Farmacia.model.Categoria;
import com.generation.Farmacia.repository.CategoriaRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@PostMapping
	public Categoria criarCategoria(@RequestBody Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	@GetMapping
	public List<Categoria> listarCategorias() {
		return categoriaRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Categoria> buscarCategoriaPorId(@PathVariable Long id) {
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		return categoria.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Categoria>> getByTitulo(@PathVariable String titulo) {
		return ResponseEntity.ok(categoriaRepository.findAllByTituloContainingIgnoreCase(titulo));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Categoria> atualizarCategoria(@PathVariable Long id,
			@RequestBody Categoria categoriaAtualizada) {
		return categoriaRepository.findById(id).map(categoria -> {
			categoria.setNome(categoriaAtualizada.getNome());
			categoria.setDescricao(categoriaAtualizada.getDescricao());
			return ResponseEntity.ok(categoriaRepository.save(categoria));
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deletarCategoria(@PathVariable Long id) {
		return categoriaRepository.findById(id).map(categoria -> {
			categoriaRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		}).orElse(ResponseEntity.notFound().build());
	}
}