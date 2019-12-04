package omar.mebarki.controller;

import omar.mebarki.domain.Customer;
import omar.mebarki.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController

public class CustomerController {
    @Autowired
    CustomerRepository customerRepository;


    @RequestMapping(path = "/customers")
    @ResponseBody
    public List<Customer> getAll() {
        Iterable<Customer> customersIterator = customerRepository.findAll();
        List<Customer> customers = new ArrayList<>();
        customersIterator.forEach(customers::add);
        return customers;
    }
    @RequestMapping(path = "/customers/{name}")
    @ResponseBody
    public List<Customer> getByName(@PathVariable("name") String name) {
        Iterable<Customer> customersIterator = customerRepository.findByLastName(name);
        List<Customer> customers = new ArrayList<>();
        customersIterator.forEach(customers::add);
        return customers;
    }

    @RequestMapping("/customer/{id}")
    public void update(@PathVariable("id") Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            customer.get().setFirstName("Omar");
            customerRepository.save(customer.get());
        }
        ;
    }

    @RequestMapping(path = "/insert")
    @CacheEvict(value = "all", allEntries = true)
    public void insert() {
        customerRepository.save(new Customer("Omar", "MEBARKI"));
    }

}
