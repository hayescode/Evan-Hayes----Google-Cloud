package com.evanhayes.evanhayes.models.Data;

import com.evanhayes.evanhayes.models.Category.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CategoryDAO extends CrudRepository<Category, Integer> {
}
