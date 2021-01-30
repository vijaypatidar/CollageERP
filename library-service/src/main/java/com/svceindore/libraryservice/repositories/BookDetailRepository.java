package com.svceindore.libraryservice.repositories;

import com.svceindore.libraryservice.models.BookDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Vijay Patidar
 * Date: 29/01/21
 * Time: 10:19 AM
 **/

@Repository
public interface BookDetailRepository extends MongoRepository<BookDetail,String> {

}
