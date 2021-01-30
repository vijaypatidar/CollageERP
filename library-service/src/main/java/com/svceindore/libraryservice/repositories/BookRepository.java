package com.svceindore.libraryservice.repositories;

import com.svceindore.libraryservice.models.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Vijay Patidar
 * Date: 29/01/21
 * Time: 10:19 AM
 **/

@Repository
public interface BookRepository extends MongoRepository<Book,String> {
}
