package org.unilab.uniplan;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class PostConstructMethodService {

    // This method populates the database with sample data
    @PostConstruct
    public void populateDatabase() {
        // Data
    }
}
