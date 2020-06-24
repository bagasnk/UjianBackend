package com.cimb.ujian.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cimb.ujian.dao.CategoriesRepo;
import com.cimb.ujian.dao.MoviesRepo;
import com.cimb.ujian.entity.Categories;
import com.cimb.ujian.entity.Movies;

@RestController
@RequestMapping("/categories")
public class CategoriesController {

	@Autowired
	private MoviesRepo moviesRepo;
	
	@Autowired
	private CategoriesRepo categoriesRepo;
	
	@PostMapping
	public Categories addCategories(@RequestBody Categories categories) {
		return categoriesRepo.save(categories);
	}
	
	@GetMapping
	public Iterable<Categories> getCategories() {
		return categoriesRepo.findAll();
	}	
	
	@GetMapping("/{categoriesId}/movies")
	public List<Movies> getMoviesOfCategories(@PathVariable int categoriesId) {
		Categories findCategories = categoriesRepo.findById(categoriesId).get();
		
		return findCategories.getMovies();
	}
	
	@DeleteMapping("/{categoriesId}")
	public void deleteCategories(@PathVariable int categoriesId) {
		Categories findCategories = categoriesRepo.findById(categoriesId).get();
		
		findCategories.getMovies().forEach(movies -> {
			List<Categories> moviesCategories = movies.getCategories();
			moviesCategories.remove(findCategories);
			moviesRepo.save(movies);
		});
		findCategories.setMovies(null);
		categoriesRepo.deleteById(categoriesId);
	}
	
	@PutMapping("/edit")
	public Categories editCategories(@RequestBody Categories categories) {
		Categories findCategories = categoriesRepo.findById(categories.getId()).get();
		categories.setMovies(findCategories.getMovies());
		return categoriesRepo.save(categories);
	}
}
