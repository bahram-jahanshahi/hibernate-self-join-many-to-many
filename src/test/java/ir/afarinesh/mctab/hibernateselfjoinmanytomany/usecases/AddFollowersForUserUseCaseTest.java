package ir.afarinesh.mctab.hibernateselfjoinmanytomany.usecases;

import ir.afarinesh.mctab.hibernateselfjoinmanytomany.entities.User;
import ir.afarinesh.mctab.hibernateselfjoinmanytomany.repositories.UserJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AddFollowersForUserUseCaseTest {

    @Autowired
    private AddFollowersForUserUseCase addFollowersForUserUseCase;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("Test Add Follower For User")
    void addFollowers() throws Exception {
        // remove all users!
        userJpaRepository.deleteAll();

        // create user and save
        User user = new User(null, "User", "Main", new HashSet<>(), null);
        userJpaRepository.save(user);
        Long mainUserId = user.getId();

        // create three followers and save
        User followerOne = new User(null, "Follower", "One", null, null);
        User followerTwo = new User(null, "Follower", "Two", null, null);
        User followerThree = new User(null, "Follower", "Three", null, null);
        userJpaRepository.save(followerOne);
        userJpaRepository.save(followerTwo);
        userJpaRepository.save(followerThree);

        /*
            Add followers for user:
            now we are up to add followers for user
            we know user, followerOne, followerTwo and followerThree have been already persisted
         */
        addFollowersForUserUseCase.addFollowers(user, followerOne, followerTwo, followerThree);

        /*
            Here we are up to test whether followers are added or not
            So we need to find main user by id then verify whether user.getFollowers().size() equals 3 or not
         */
        Optional<User> mainUser = userJpaRepository.findById(mainUserId);
        // Main user is not empty
        assertNotNull(mainUser.get());
        // Count of followers equals 3
        assertEquals(3, mainUser.get().getFollowers().size());
    }
}
