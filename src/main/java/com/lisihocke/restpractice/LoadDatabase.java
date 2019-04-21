package com.lisihocke.restpractice;

import com.lisihocke.restpractice.employee.Employee;
import com.lisihocke.restpractice.employee.EmployeeRepository;
import com.lisihocke.restpractice.order.Order;
import com.lisihocke.restpractice.order.OrderRepository;
import com.lisihocke.restpractice.order.OrderStatus;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository, OrderRepository orderRepository) {
        return args -> {
            log.info("Preloading " + repository.save(new Employee("Bilbo",  "Baggins", "Burglar")));
            log.info("Preloading " + repository.save(new Employee("Frodo", "Baggins", "Thief")));

            orderRepository.save(new Order("MacBook Pro", OrderStatus.COMPLETED));
            orderRepository.save(new Order("iPhone", OrderStatus.IN_PROGRESS));

            orderRepository.findAll().forEach(order -> log.info("Preloaded " + order));
        };
    }
}
