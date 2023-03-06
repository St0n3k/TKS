package pl.lodz.p.it.tks.repository.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import pl.lodz.p.it.tks.model.RentEntity;
import pl.lodz.p.it.tks.model.RoomEntity;
import pl.lodz.p.it.tks.repository.RentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class RentRepositoryImpl implements RentRepository {

    @PersistenceContext
    EntityManager em;

    /**
     * Synchronized method which saves Rent object to database
     * if the dates do not collide with other rents for the same room
     *
     * @param rent
     * @return
     */
    @Override
    public synchronized RentEntity add(RentEntity rent) {

        Optional<RoomEntity> room = Optional.ofNullable(em.find(RoomEntity.class, rent.getRoom().getId()));

        if (room.isEmpty()) {
            throw new RuntimeException("Room not found");
            //FIXME
        }

        em.lock(room.get(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        boolean isColliding = isColliding(rent.getBeginTime(),
                                          rent.getEndTime(),
                                          rent.getRoom().getRoomNumber());

        if (isColliding) {
            return null;
        }

        em.persist(rent);

        return rent;
    }

    @Override
    public void remove(RentEntity rent) {
        em.remove(em.merge(rent));
    }

    @Override
    public Optional<RentEntity> getById(UUID id) {
        return Optional.ofNullable(em.find(RentEntity.class, id));
    }

    @Override
    public List<RentEntity> getAll() {
        return em.createNamedQuery("Rent.getAll", RentEntity.class).getResultList();
    }

    @Override
    public List<RentEntity> getByRoomId(UUID roomId) {
        return em.createNamedQuery("Rent.getByRoomId", RentEntity.class)
                 .setParameter("roomId", roomId)
                 .getResultList();
    }

    @Override
    public List<RentEntity> getByClientUsername(String username) {
        return em.createNamedQuery("Rent.getByClientUsername", RentEntity.class)
                 .setParameter("username", username)
                 .getResultList();
    }

    @Override
    public List<RentEntity> getByClientId(UUID clientId) {
        return em.createNamedQuery("Rent.getByClientId", RentEntity.class)
                 .setParameter("id", clientId)
                 .getResultList();
    }

    @Override
    public Optional<RentEntity> update(RentEntity rent) {
        return Optional.ofNullable(em.merge(rent));
    }

    /**
     * Method to check if given period of time is colliding with periods of existing rents for given room
     *
     * @param beginDate begin date of currently created rent
     * @param endDate end date of currently created rent
     * @param roomNumber number of the room
     * @return false if new rent can be created for given period of time
     */
    private boolean isColliding(LocalDateTime beginDate, LocalDateTime endDate, int roomNumber) {
        List<RentEntity> rentsColliding = em.createNamedQuery("Rent.getRentsColliding", RentEntity.class)
                                      .setParameter("beginDate", beginDate)
                                      .setParameter("endDate", endDate)
                                      .setParameter("roomNumber", roomNumber)
                                      .getResultList();

        return !rentsColliding.isEmpty();
    }

    /**
     * Removes Rent with given ID.
     *
     * @param rentId
     * @return true if rent existed and was removed, false otherwise.
     */
    @Override
    public boolean removeById(UUID rentId) {
        Optional<RentEntity> rent = getById(rentId);

        if (rent.isEmpty()) {
            return false;
        }

        em.remove(rent.get());
        return true;
    }

    /**
     * @param roomId ID of the room.
     * @param past Flag indicating, whether past or active rents are returned.
     * @return Past rents if past is false, active (future) rents otherwise.
     */
    @Override
    public List<RentEntity> findByRoomAndStatus(UUID roomId, boolean past) {
        TypedQuery<RentEntity> query;
        if (past) {
            query = em.createNamedQuery("Rent.getPastRentsByRoom", RentEntity.class);
        } else {
            query = em.createNamedQuery("Rent.getActiveRentsByRoom", RentEntity.class);
        }

        return query.setParameter("id", roomId)
                    .getResultList();
    }

    /**
     * @param clientId ID of the client.
     * @param past Flag indicating, whether past or active rents are returned.
     * @return Past rents if past is false, active (future) rents otherwise.
     */
    @Override
    public List<RentEntity> findByClientAndStatus(UUID clientId, boolean past) {
        TypedQuery<RentEntity> query;
        if (past) {
            query = em.createNamedQuery("Rent.getPastRentsByClient", RentEntity.class);
        } else {
            query = em.createNamedQuery("Rent.getActiveRentsByClient", RentEntity.class);
        }

        return query.setParameter("id", clientId)
                    .getResultList();
    }
}
