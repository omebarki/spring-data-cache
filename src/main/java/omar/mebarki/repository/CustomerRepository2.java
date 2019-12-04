package omar.mebarki.repository;

import omar.mebarki.domain.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public interface CustomerRepository2 extends CrudRepository<Customer, Long>, CustomerRepository2Custom {
}