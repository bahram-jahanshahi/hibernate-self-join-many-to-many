# Hibernate Self-Join and Many to Many Relation
The main purpose of this sample project is just showing 
how we can easily design and implement a `self-join` and `many-to-many` relation
in hibernate.
## How to run
In order to test how it works run `AddFollowersForUserUseCaseTest.java` file.

## Design database considerations
In this project we have one entity named `User.java` which has `many-to-many`
relation with itself.
```java
package ir.afarinesh.mctab.hibernateselfjoinmanytomany.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String firstName;

    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_follower",
    joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "follower_id")}
    )
    private Set<User> followers = new HashSet<>();

    @ManyToMany(mappedBy = "followers")
    private Set<User> followings = new HashSet<>();
}
```
> consider if we define `@ManyToMany( ... Set<User> followers ...` in reverse we need define 
> `@ManyToMany(mappedBy = "followers") private Set<User> followings ...`. 
> If we loose `mappedBy = "followers"` then process doesn't work.  
  
## Use Case: Add followers for user
In order to add a bunch of followers for particular user we implement use case by this way:
```java
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
```
## Test: Add followers for user
And finally in order to test how use case works run this test:
```java
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
```
## Conclusion
In order to draw a brief conclusion first of all you should notice 
how `self-join many-to-many` is implemented: consider both side of relation I mentioned.
