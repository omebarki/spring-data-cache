package omar.mebarki.repository;

import omar.mebarki.domain.Customer;

public interface CustomerRepository2Custom {
    void saveAndCache(Customer customer);

}
