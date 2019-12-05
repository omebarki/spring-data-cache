package omar.mebarki.cache.repository;

import omar.mebarki.config.AppConfig;
import omar.mebarki.domain.Customer;
import omar.mebarki.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {AppConfig.class})
@EnableAutoConfiguration
@AutoConfigureTestDatabase

public class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository repository;
    @Autowired
    CacheManager cacheManager;


    private final int INITIAL_LINES_COUNT = 5;

    private static final Customer CUSTOMERS[] = new Customer[]{
            new Customer("Jack", "Bauer"),
            new Customer("Chloe", "O'Brian"),
            new Customer("Kim", "Bauer"),
            new Customer("David", "Palmer"),
            new Customer("Michelle", "Dessler")
    };

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
        repository.saveAll(Arrays.asList(CUSTOMERS));
    }

    private List<Customer> getCustomers() {
        Iterable<Customer> customers = repository.findAll();
        List<Customer> consumersList = new ArrayList<>();
        customers.forEach(consumersList::add);
        return consumersList;
    }


    public void insertOne() {
        createCustomer("Omar", "MEBARKI");
    }

    @Test
    public void testCacheInsertBeforeSelect() {
        insertOne();
        insertOne();
        System.out.println("--------------------------");
        Optional<Customer> customer = repository.findById(6L);
        assertThat(customer.isPresent()).isTrue();
        System.out.println("2--------------------------");
        customer = repository.findById(6L);
        System.out.println("--------------------------");

    }

    @Test
    public void testCacheInsertAfterSelect_WhenFirstSelectReturnsEmpty() {
        List<Customer> customersCached = repository.findByLastName("MEBARKI");
        insertOne();
        assertThat(customersCached.size()).isEqualTo(0);

    }

    @Test
    public void testCacheInsertAfterSelect_WhenFirstSelectReturnsNonEmpty() {
        insertOne();
        List<Customer> customersCached = repository.findByLastName("MEBARKI");
        assertThat(customersCached.size()).isEqualTo(1);
        insertOne();
        assertThat(repository.findByLastNameNotCached("MEBARKI").size()).isEqualTo(2);
        assertThat(customersCached.size()).isEqualTo(1);//Le cache n'est pas invalidé
    }

    @Test
    public void testUpdateNonCached() {
        ;
        final String firstName = "Omar2";
        insertOne();
        updateCustomer(6L, firstName);
        List<Customer> mebarki = repository.findByLastName("MEBARKI");
        assertThat(mebarki.get(0).getFirstName()).isEqualTo(firstName);
    }

    @Test
    public void testUpdateCached() {
        ;
        final String firstName = "Omar2";
        insertOne();
        List<Customer> mebarki = repository.findByLastName("MEBARKI");
        assertThat(mebarki.get(0).getFirstName()).isEqualTo("Omar");
        updateCustomer(6L, firstName);
        assertThat(mebarki.get(0).getFirstName()).isEqualTo("Omar");//la mise à jour n'est pas prise dans le cache
    }

    @Test
    public void testUpdateCachedWithCachePut() {
        final String firstName = "Omar2";
        insertOne();
        List<Customer> mebarki = repository.findByLastName("MEBARKI");
        assertThat(mebarki.get(0).getFirstName()).isEqualTo("Omar");
        updateCustomerWithCachePut(6L, firstName);
        mebarki = repository.findByLastName("MEBARKI");
        assertThat(mebarki.get(0).getFirstName()).isEqualTo(firstName);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createCustomer(String firstName, String lastName) {
        repository.save(new Customer(firstName, lastName));
    }

    public void updateCustomer(Long id, String firstName) {
        repository.findById(id).ifPresent(customer -> {
            customer.setFirstName(firstName);
            repository.save(customer);
        });

    }

    public void updateCustomerWithCachePut(Long id, String firstName) {
        repository.findById(id).ifPresent(customer -> {
            customer.setFirstName(firstName);
            repository.save(customer);
        });
    }

}
