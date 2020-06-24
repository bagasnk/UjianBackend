package com.cimb.ujian.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cimb.ujian.entity.Movies;

public interface MoviesRepo extends JpaRepository <Movies,Integer> {

}
