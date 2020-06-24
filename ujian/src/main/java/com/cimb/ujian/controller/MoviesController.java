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
@RequestMapping("/movies")
public class MoviesController {

	@Autowired
	private MoviesRepo moviesRepo;
	
	@Autowired
	private CategoriesRepo categoriesRepo;
	
	@PostMapping
	public Movies addMovies(@RequestBody Movies movies) {
		return moviesRepo.save(movies);
	}
	 
	@GetMapping
	public Iterable<Movies> getMovies() {
		return moviesRepo.findAll();
	}	
	
	@DeleteMapping("/{moviesId}")
	public void deleteMovies(@PathVariable int moviesId) {
		Movies findMovies = moviesRepo.findById(moviesId).get();
		
		findMovies.getCategories().forEach(categories -> {
			List<Movies> categoriesMovies = categories.getMovies();
			categoriesMovies.remove(findMovies);
			categoriesRepo.save(categories);
		});
		findMovies.setCategories(null);
		moviesRepo.deleteById(moviesId);
	}
	
	@DeleteMapping("/{moviesId}/categories/{categoriesId}")
	public Movies deleteMoviesCategory(@PathVariable int moviesId,@PathVariable int categoriesId) {
		Movies findMovies = moviesRepo.findById(moviesId).get();
		Categories findCategories = categoriesRepo.findById(categoriesId).get();
		
		findMovies.getCategories().remove(findCategories);
		return moviesRepo.save(findMovies);
	}
	
	
	@PostMapping("/{moviesId}/categories/{categoriesId}")
	public Movies addCategoryToMovies(@PathVariable int moviesId, @PathVariable int categoriesId) {
		Movies findMovies = moviesRepo.findById(moviesId).get();
		Categories findCategories = categoriesRepo.findById(categoriesId).get();
		findMovies.getCategories().add(findCategories);
		return moviesRepo.save(findMovies);
	}
	
	@PutMapping("/edit")
	public Movies editMovies(@RequestBody Movies movies) {
		Movies findMovies= moviesRepo.findById(movies.getId()).get();
		movies.setCategories(findMovies.getCategories());
		return moviesRepo.save(movies);
	}
	
	
}

            