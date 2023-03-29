package pl.lodz.p.it.tks.user.repository.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import pl.lodz.p.it.tks.user.model.users.UserEntity;
import pl.lodz.p.it.tks.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    EntityManager em;

    /**
     * Method which saves user with unique username to database.
     *
     * @param user user to be saved
     *
     * @return saved user
     */
    @Override
    public UserEntity add(UserEntity user) {
        em.persist(user);
        return user;
    }

    @Override
    public void remove(UserEntity user) {
        em.remove(em.merge(user));
    }

    @Override
    public Optional<UserEntity> getById(UUID id) {
        return Optional.ofNullable(em.find(UserEntity.class, id));
    }

    @Override
    public List<UserEntity> getAll() {
        return em.createNamedQuery("User.getAll", UserEntity.class).getResultList();
    }

    @Override
    public Optional<UserEntity> getUserByUsername(String username) {
        List<UserEntity> result = em
            .createNamedQuery("User.getByUsername", UserEntity.class)
            .setParameter("username", username)
            .getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(result.get(0));
    }

    /**
     * Returns users whose username contains given phrase.
     *
     * @param phrase phrase to be matched among usernames
     *
     * @return list of users, whose usernames contain given phrase
     */
    @Override
    public List<UserEntity> matchUserByUsername(String phrase) {
        return em.createNamedQuery("User.matchByUsername", UserEntity.class)
            .setParameter("username", '%' + phrase + '%')
            .getResultList();

    }

    @Override
    public List<UserEntity> getUsersByRole(String role) {
        return em.createNamedQuery("User.getByRole", UserEntity.class)
            .setParameter("role", '%' + role + '%')
            .getResultList();

    }

    @Override
    public List<UserEntity> getAllUsers() {
        return em.createNamedQuery("User.getAll", UserEntity.class).getResultList();
    }

    @Override
    public Optional<UserEntity> update(UserEntity user) {
        return Optional.ofNullable(em.merge(user));
    }

    @Override
    public List<UserEntity> getUsersByRoleAndMatchingUsername(String role, String username) {
        return em.createNamedQuery("User.getByRoleMatchingName", UserEntity.class)
            .setParameter("role", "%" + role + "%")
            .setParameter("username", "%" + username + "%")
            .getResultList();
    }
}
