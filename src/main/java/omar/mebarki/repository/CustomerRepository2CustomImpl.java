package omar.mebarki.repository;

import omar.mebarki.domain.Customer;
import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomerRepository2CustomImpl implements CustomerRepository2Custom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @CachePut(cacheNames = CustomerRepository.BY_LAST_NAME_CACHE, key = "#customer.id")
    public void saveAndCache(Customer customer) {
        entityManager.persist(customer);
    }
}
