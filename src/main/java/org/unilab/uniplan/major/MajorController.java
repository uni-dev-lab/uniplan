package org.unilab.uniplan.major;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/majors")
public class MajorController {
    
    private final MajorService majorService;

    @Autowired
    public MajorController(final MajorService majorService) {
        this.majorService = majorService;
    }

    @PostMapping
    public ResponseEntity<MajorDTO> addMajor(@RequestBody final MajorDTO majorDTO) {
        return ResponseEntity.ok(majorService.createMajor(majorDTO));
    }
    @GetMapping("/{id}")
    public ResponseEntity<MajorDTO> getMajorById(@PathVariable final UUID id) {
        return ResponseEntity.ok(majorService.findMajorById(id)
                                             .orElseThrow(()->new RuntimeException("Major with id " + id + " not found")));
    }
    @GetMapping
    public ResponseEntity<List<MajorDTO>> getAllMajors() {
        return ResponseEntity.ok(majorService.findAll());
    }
    @PostMapping("/{id}")
    public ResponseEntity<MajorDTO> updateMajor(@PathVariable final UUID id, @RequestBody MajorDTO majorDTO) {
        return ResponseEntity.ok(majorService.updateMajor(id, majorDTO));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMajor(@PathVariable final UUID id) {
        if (majorService.deleteMajor(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
