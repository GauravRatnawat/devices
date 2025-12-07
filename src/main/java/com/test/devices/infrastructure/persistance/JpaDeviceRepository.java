package com.test.devices.infrastructure.persistance;

import com.test.devices.domain.model.Device;
import com.test.devices.domain.model.DeviceState;
import com.test.devices.domain.repository.DeviceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class JpaDeviceRepository implements DeviceRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Device save(Device device) {
        DeviceEntity entity = DeviceEntity.fromDomain(device);

        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entity = entityManager.merge(entity);
        }

        return entity.toDomain();
    }

    @Override
    public Optional<Device> findById(Long id) {
        DeviceEntity entity = entityManager.find(DeviceEntity.class, id);
        return Optional.ofNullable(entity).map(DeviceEntity::toDomain);
    }

    @Override
    public List<Device> findAll() {
        return entityManager.createQuery("SELECT d FROM DeviceEntity d", DeviceEntity.class)
            .getResultList()
            .stream()
            .map(DeviceEntity::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Device> findByBrand(String brand) {
        return entityManager.createQuery(
                "SELECT d FROM DeviceEntity d WHERE d.brand = :brand",
                DeviceEntity.class)
            .setParameter("brand", brand)
            .getResultList()
            .stream()
            .map(DeviceEntity::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Device> findByState(DeviceState state) {
        return entityManager.createQuery(
                "SELECT d FROM DeviceEntity d WHERE d.state = :state",
                DeviceEntity.class)
            .setParameter("state", state)
            .getResultList()
            .stream()
            .map(DeviceEntity::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(Device device) {
        DeviceEntity entity = entityManager.find(DeviceEntity.class, device.getId());
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

}
