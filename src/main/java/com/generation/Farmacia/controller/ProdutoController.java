package com.generation.Farmacia.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.generation.Farmacia.model.Produto;
import com.generation.Farmacia.repository.CategoriaRepository;
import com.generation.Farmacia.repository.ProdutoRepository;

import java.util.List;
import java.util.Optional;

public class ProdutoController {

	@RestController
	@RequestMapping("/produtos")
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	public class Produtocontroller {

		@Autowired
		private ProdutoRepository produtoRepository;

		@Autowired
		private CategoriaRepository categoriaRepository;

		@GetMapping
		public ResponseEntity<List<Produto>> getAll() {
			return ResponseEntity.ok(produtoRepository.findAll());
		}

		@GetMapping("/{id}")
		public ResponseEntity<Produto> getById(@PathVariable Long id) {
			return produtoRepository.findById(id).map(ResponseEntity::ok)
					.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		}

		@GetMapping("/nome/{nome}")
		public ResponseEntity<List<Produto>> getByNome(@PathVariable String nome) {
			return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
		}

		@PostMapping
		public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto) {
			if (categoriaRepository.existsById(produto.getCategoria().getId()))
				return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe!");
		}

		@PutMapping
		public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {
			if (produtoRepository.existsById(produto.getId())) {
				if (categoriaRepository.existsById(produto.getCategoria().getId()))
					return ResponseEntity.ok(produtoRepository.save(produto));

				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe!");
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		@ResponseStatus(HttpStatus.NO_CONTENT)
		@DeleteMapping("/{id}")
		public void delete(@PathVariable Long id) {
			Optional<Produto> produto = produtoRepository.findById(id);

			if (produto.isEmpty())
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);

			produtoRepository.deleteById(id);
		}
	}

}
