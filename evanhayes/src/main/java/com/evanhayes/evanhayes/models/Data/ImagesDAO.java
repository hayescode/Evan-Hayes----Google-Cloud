package com.evanhayes.evanhayes.models.Data;

import com.evanhayes.evanhayes.models.Images.Images;
import org.springframework.data.repository.CrudRepository;

public interface ImagesDAO extends CrudRepository<Images, Integer> {
    Images[] findByCategoryId(int category_id);
}
