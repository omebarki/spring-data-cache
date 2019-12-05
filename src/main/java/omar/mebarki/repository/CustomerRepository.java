package omar.mebarki.repository;

import omar.mebarki.domain.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerRepository extends CrudRepository<Customer, Long> {
    List<Customer> findByLastName(String lastName);

    @Query("from Customer where lastName = :lastName")
    List<Customer> findByLastNameNotCached(String lastName);


}