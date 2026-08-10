package org.unilab.uniplan.building;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.common.model.BaseService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuildingService implements BaseService<Building> {

    private final BuildingRepository buildingRepository;

    @Override
    public void save(final Building building) {
        buildingRepository.save(building);
    }

    @Override
    public List<Building> getAll() {
        return buildingRepository.findAll();
    }

    @Override
    public Optional<Building> getById(final UUID id) {
        return buildingRepository.findById(id);
    }

    @Override
    public void delete(Building building) {
        buildingRepository.delete(building);
    }
}
