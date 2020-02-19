package ie.ucd.lms.controller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReserveQueueRepository extends JpaRepository<ReserveQueue, Long> { }