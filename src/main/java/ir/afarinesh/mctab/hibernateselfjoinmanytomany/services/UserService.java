package ir.afarinesh.mctab.hibernateselfjoinmanytomany.services;

import ir.afarinesh.mctab.hibernateselfjoinmanytomany.entities.User;
import ir.afarinesh.mctab.hibernateselfjoinmanytomany.repositories.UserJpaRepository;
import ir.afarinesh.mctab.hibernateselfjoinmanytomany.usecases.AddFollowersForUserUseCase;
import org.springframework.stereotype.Service;

@Service
public class UserService implements AddFollowersForUserUseCase {

    final UserJpaRepository userJpaRepository;

    public UserService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Boolean addFollowers(User user, User... followers) throws Exception {
        for (int i = 0; i < followers.length; i++) {
            user.getFollowers().add(followers[i]);
        }
        userJpaRepository.save(user);
        return true;
    }
}
