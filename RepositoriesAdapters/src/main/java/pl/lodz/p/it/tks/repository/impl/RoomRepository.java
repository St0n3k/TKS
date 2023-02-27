package pl.lodz.p.it.tks.repository.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import pl.lodz.p.it.tks.model.RoomEntity;
import pl.lodz.p.it.tks.repository.Repository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class RoomRepository implements Repository<RoomEntity> {

    @PersistenceContext
    private EntityManager em;


    /**
     * Method which saved room to database, room number has to be unique, otherwise method will throw exception
     *
     * @param room room to be saved
     * @return saved room
     */
    @Override
    public RoomEntity add(RoomEntity room) {
        em.persist(room);
        return room;
    }

    @Override
    public void remove(RoomEntity room) {
        em.remove(em.merge(room));
    }

    @Override
    public Optional<RoomEntity> getById(Long id) {
        return Optional.ofNullable(em.find(RoomEntity.class, id));
    }

    @Override
    public Optional<RoomEntity> update(RoomEntity room) {
        return Optional.ofNullable(em.merge(room));
    }

    @Override
    public List<RoomEntity> getAll() {
        return em.createNamedQuery("Room.getAll", RoomEntity.class).getResultList();
    }

    public Optional<RoomEntity> getByRoomNumber(int roomNumber) {
        List<RoomEntity> result = em.createNamedQuery("Room.getByRoomNumber", RoomEntity.class)
                              .setParameter("roomNumber", roomNumber)
                              .getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(result.get(0));
    }

    /**
     * @param id id to be checked among rooms
     * @return true if a room with given id exists
     */
    public boolean existsById(Long id) {
        return em.createNamedQuery("Room.existsById", Boolean.class)
                 .setParameter("id", id)
                 .getSingleResult();
    }
}
